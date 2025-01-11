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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import me.safarov399.home.databinding.FragmentHomeBinding
import me.safarov399.uikit.custom_views.dialogs.PermissionDialog


class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private val requiredPermissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private val requestAndroid10AndBelowPermissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        for (permission in permissions) {
            if (!permission.value) {
                break
            }
        }

        if (checkStoragePermissions()) {
//            Read files
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
            // Permission not granted, finish the activity
            showPermissionRequestDialog()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val hasStoragePermission = checkStoragePermissions()
        if (!hasStoragePermission) {
            showPermissionRequestDialog()
        } else {
//            Read files
        }
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
            requestAndroid10AndBelowPermissionsLauncher.launch(requiredPermissions)
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

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}