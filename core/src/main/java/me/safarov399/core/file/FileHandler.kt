package me.safarov399.core.file

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import me.safarov399.common.file.FileExtensions.APK_FILE
import java.io.File

object FileHandler {

    fun openFile(path: String, name: String, fragment: Fragment, generic: Boolean) {
        val file = File(path, name)
        val fileExtension = file.extension
        val uri = FileProvider.getUriForFile(fragment.requireContext(), fragment.requireActivity().packageName + ".fileprovider", file)

        if (fileExtension == APK_FILE) {
            if (fragment.requireActivity().packageManager.canRequestPackageInstalls()) {
                installApk(path, name, fragment)
            } else {
                requestApkInstallPermission(fragment)
            }
        }

        var mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension) ?: "*/*"
        if (generic) mimeType = "*/*"
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, mimeType)
            flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        }
        fragment.startActivity(intent)
    }


    fun installApk(path: String, name: String, fragment: Fragment) {
        val absolutePath = fileAndPathMerger(name, path)
        val apkFile = File(absolutePath)
        if (!apkFile.exists()) {
            Log.e("APKInstall", "APK file does not exist: $absolutePath")
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