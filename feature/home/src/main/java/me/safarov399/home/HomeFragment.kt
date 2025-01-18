package me.safarov399.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import me.safarov399.core.PermissionConstants
import me.safarov399.core.adapter.FileFolderAdapter
import me.safarov399.core.adapter.OnClickListener
import me.safarov399.core.base.BaseFragment
import me.safarov399.core.storage.StorageConstants
import me.safarov399.core.storage.StorageConstants.DEFAULT_DIRECTORY
import me.safarov399.domain.models.adapter.FileFolderModel
import me.safarov399.domain.models.adapter.FolderModel
import me.safarov399.home.databinding.FragmentHomeBinding
import me.safarov399.uikit.custom_views.dialogs.SigmaDialog

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel, HomeUiState, HomeEffect, HomeEvent>() {

    private var fileFolderAdapter: FileFolderAdapter? = null
    private var rv: RecyclerView? = null

    private var backPressCallback: OnBackPressedCallback? = null
    private var currentPath = DEFAULT_DIRECTORY

    private var isClickable = true


    private val requestAndroid10AndBelowPermissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        for (permission in permissions) {
            if (!permission.value) {
                break
            }
        }

        if (checkStoragePermissions()) {
            postEvent(HomeEvent.ChangePath(DEFAULT_DIRECTORY))
        } else {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) || !shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                goToSettingsDialog()
            } else {
                showPermissionRequestDialog()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private val requestAndroid11AndHigherPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        val isPermissionGranted = Environment.isExternalStorageManager()
        if (!isPermissionGranted) {
            showPermissionRequestDialog()
        } else {
            postEvent(HomeEvent.ChangePath(DEFAULT_DIRECTORY))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureViews()
        handlePermissionAndStorageReading()
        handleBackPress()
    }

    override fun onResume() {
        super.onResume()
        if (checkStoragePermissions()) {
            postEvent(HomeEvent.ChangePath(currentPath))
        }
    }

    override fun onStateUpdate(state: HomeUiState) {
        currentPath = state.currentPath
        backPressCallback?.isEnabled = currentPath != DEFAULT_DIRECTORY
        fileFolderAdapter?.submitList(state.currentFileFolders)

        fileFolderAdapter?.setOnClickListener(object : OnClickListener {
            override fun onClick(position: Int, model: FileFolderModel) {
                if (!isClickable) return // Ignore clicks if interaction is disabled
                isClickable = false

                val folderName = (model as FolderModel).name
                val temporaryCurrentPath = if (state.currentPath.endsWith("/")) {
                    state.currentPath + folderName
                } else {
                    "${state.currentPath}/$folderName"
                }

                if (temporaryCurrentPath !in StorageConstants.RESTRICTED_DIRECTORIES) {
                    postEvent(HomeEvent.ChangePath(temporaryCurrentPath))
                    state.currentPath = temporaryCurrentPath
                }
                binding.pathTv.text = state.currentPath

                // Re-enable interaction after a short delay
                binding.root.postDelayed({ isClickable = true }, 500)
            }
        })

        binding.homeNavUp.setOnClickListener {
            if (state.currentPath != DEFAULT_DIRECTORY) {
                val nextPath = state.currentPath.substringBeforeLast("/")
                postEvent(
                    HomeEvent.ChangePath(
                        nextPath
                    )
                )
                binding.pathTv.text = nextPath
            }
        }
    }

    private fun configureViews() {
        rv = binding.homeRv
        fileFolderAdapter = FileFolderAdapter()
        rv?.adapter = fileFolderAdapter
        binding.pathTv.text = DEFAULT_DIRECTORY
    }

    private fun handlePermissionAndStorageReading() {
        val hasStoragePermission = checkStoragePermissions()
        if (!hasStoragePermission) {
            showPermissionRequestDialog()
        } else {
            postEvent(HomeEvent.ChangePath(DEFAULT_DIRECTORY))
        }
    }

    private fun handleBackPress() {
        backPressCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (currentPath != DEFAULT_DIRECTORY) {
                    postEvent(
                        HomeEvent.ChangePath(
                            currentPath.substringBeforeLast("/")
                        )
                    )
                    binding.pathTv.text = currentPath.substringBeforeLast("/")
                } else {
                    isEnabled = false // Allow the system to handle back press
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backPressCallback as OnBackPressedCallback)
    }

    private fun showPermissionRequestDialog() {
        val dialog = SigmaDialog(requireActivity())
        dialog.setTitle(getString(me.safarov399.common.R.string.permission_dialog_title))
        dialog.setDescription(getString(me.safarov399.common.R.string.permission_dialog_description))
        dialog.setConfirmButtonText(getString(me.safarov399.common.R.string.ok))
        dialog.setCancelButtonText(getString(me.safarov399.common.R.string.cancel))
        dialog.setConfirmationOnClickListener {
            dialog.dismiss()
            requestStoragePermission()
        }
        dialog.show()
    }

    private fun goToSettingsDialog() {
        val dialog = SigmaDialog(requireActivity())
        dialog.setTitle(getString(me.safarov399.common.R.string.not_granted_title))
        dialog.setDescription(getString(me.safarov399.common.R.string.not_granted_description))
        dialog.setConfirmButtonText(getString(me.safarov399.common.R.string.not_granted_confirm))
        dialog.setCancelButtonText(getString(me.safarov399.common.R.string.not_granted_cancel))
        dialog.setConfirmationOnClickListener {
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + requireActivity().packageName)
            )
            startActivity(intent)
            dialog.dismiss()
            activity?.finish()
        }
        dialog.show()
    }

    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requestStoragePermissionAndroid11AndHigher()
        } else {
            requestAndroid10AndBelowPermissionsLauncher.launch(PermissionConstants.requiredPermissionsAndroid10AndBelow)
        }
    }

    private fun checkStoragePermissions(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //Android is 11 (R) or above
            return Environment.isExternalStorageManager()
        } else {
            //Below android 11
            val write = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val read = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)

            return read == PackageManager.PERMISSION_GRANTED && write == PackageManager.PERMISSION_GRANTED
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun requestStoragePermissionAndroid11AndHigher() {
        val uri = Uri.fromParts("package", requireActivity().packageName, null)
        val intent = Intent(
            Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri
        )
        requestAndroid11AndHigherPermissionLauncher.launch(intent)
    }

    override fun getViewModelClass(): Class<HomeViewModel> = HomeViewModel::class.java

    override val getViewBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding = { inflater, viewGroup, value ->
        FragmentHomeBinding.inflate(inflater, viewGroup, value)
    }

    override fun onDestroy() {
        super.onDestroy()
        rv = null
        fileFolderAdapter = null
        backPressCallback = null
    }
}