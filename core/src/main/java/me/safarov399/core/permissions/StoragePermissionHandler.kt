package me.safarov399.core.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import androidx.core.content.ContextCompat

class StoragePermissionHandler {
    companion object {
        fun checkStoragePermissions(ctx: Context): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                //Android is 11 (R) or above
                return Environment.isExternalStorageManager()
            } else {
                //Below android 11
                val write = ContextCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                val read = ContextCompat.checkSelfPermission(ctx, Manifest.permission.READ_EXTERNAL_STORAGE)

                return read == PackageManager.PERMISSION_GRANTED && write == PackageManager.PERMISSION_GRANTED
            }
        }
    }
}