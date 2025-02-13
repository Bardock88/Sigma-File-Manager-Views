package me.safarov399.core.file

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File

object FileHandler {
    fun installApk(apkUri: String, fragment: Fragment) {
        val apkFile = File(apkUri)
        if (!apkFile.exists()) {
            Log.e("APKInstall", "APK file does not exist: $apkUri")
            return
        }

        val fileUri: Uri = FileProvider.getUriForFile(
            fragment.requireActivity(),
            "${fragment.requireActivity().packageName}.fileprovider",
            apkFile
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(fileUri, "application/vnd.android.package-archive")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        try {
            fragment.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Log.e("APKInstall", "No activity found to handle APK installation", e)
        }
    }

    fun requestApkInstallPermission(fragment: Fragment) {
        val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
            data = Uri.parse("package:${fragment.requireActivity().packageName}")
        }
        fragment.startActivity(intent)
    }
}