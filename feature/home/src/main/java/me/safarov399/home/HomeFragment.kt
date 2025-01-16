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
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import me.safarov399.core.PermissionConstants
import me.safarov399.core.adapter.FileFolderAdapter
import me.safarov399.core.adapter.OnClickListener
import me.safarov399.core.base.BaseFragment
import me.safarov399.core.storage.StorageConstants
import me.safarov399.domain.models.adapter.FileFolderModel
import me.safarov399.domain.models.adapter.FolderModel
import me.safarov399.home.databinding.FragmentHomeBinding
import me.safarov399.uikit.custom_views.dialogs.PermissionDialog

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel, HomeUiState, HomeEffect, HomeEvent>() {

    private var fileFolderAdapter: FileFolderAdapter? = null
    private var rv: RecyclerView? = null

    private lateinit var backPressCallback: OnBackPressedCallback
    private val defaultPath = Environment.getExternalStorageDirectory().toString()
    private var currentPath = defaultPath


    private val requestAndroid10AndBelowPermissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        for (permission in permissions) {
            if (!permission.value) {
                break
            }
        }

        if (checkStoragePermissions()) {
            postEvent(HomeEvent.ChangePath(defaultPath))
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
            postEvent(HomeEvent.ChangePath(defaultPath))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureViews(view)
        handlePermissionAndStorageReading()
        handleBackPress()
    }

    override fun onStateUpdate(state: HomeUiState) {

        currentPath = state.currentPath
        backPressCallback.isEnabled = currentPath != defaultPath

        fileFolderAdapter?.submitList(state.currentFileFolders)
        fileFolderAdapter?.setOnClickListener(object : OnClickListener {
            override fun onClick(position: Int, model: FileFolderModel) {
                val folderName = (model as FolderModel).name
                val temporaryCurrentPath = if (state.currentPath.endsWith("/")) {
                    state.currentPath + folderName
                } else {
                    "${state.currentPath}/$folderName"
                }

                if (temporaryCurrentPath !in StorageConstants.RESTRICTED_DIRECTORIES) {
                    state.currentPath = temporaryCurrentPath
                    postEvent(HomeEvent.ChangePath(state.currentPath))
                }
                binding.homeToolbarCl.findViewById<TextView>(R.id.path_tv).text = state.currentPath

            }
        })

        binding.homeNavUp.setOnClickListener {
            if (state.currentPath != defaultPath) {
                val nextPath = state.currentPath.substringBeforeLast("/")
                postEvent(
                    HomeEvent.ChangePath(
                        nextPath
                    )
                )
                binding.homeToolbarCl.findViewById<TextView>(R.id.path_tv).text = nextPath

            }

        }
    }

    private fun configureViews(view: View) {
        rv = binding.homeRv
        fileFolderAdapter = FileFolderAdapter()
        rv?.adapter = fileFolderAdapter
        view.post {
            val insets = ViewCompat.getRootWindowInsets(view)
            if (insets != null) {
                binding.homeToolbarCl.setPadding(32, insets.getInsets(WindowInsetsCompat.Type.systemBars()).top, 0, 32)
            }
        }
    }

    private fun handlePermissionAndStorageReading() {
        val hasStoragePermission = checkStoragePermissions()
        if (!hasStoragePermission) {
            showPermissionRequestDialog()
        } else {
            postEvent(HomeEvent.ChangePath(defaultPath))
        }
    }

    private fun handleBackPress() {
        backPressCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (currentPath != defaultPath) {
                    postEvent(
                        HomeEvent.ChangePath(
                            currentPath.substringBeforeLast("/")
                        )
                    )
                    binding.homeToolbarCl.findViewById<TextView>(R.id.path_tv).text = currentPath.substringBeforeLast("/")
                } else {
                    isEnabled = false // Allow the system to handle back press
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backPressCallback)
    }

    private fun showPermissionRequestDialog() {
        val dialog = PermissionDialog(requireActivity())
        dialog.setConfirmationOnClickListener {
            dialog.dismiss()
            requestStoragePermission()
        }
        dialog.show()
    }

    private fun goToSettingsDialog() {
        val dialog = PermissionDialog(requireActivity())
        dialog.setTitle("Permission not granted")
        dialog.setDescription("Please go to settings to give storage permission.")
        dialog.setConfirmButtonText("Go to settings")
        dialog.setCancelButtonText("Exit the app")
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
    }
}