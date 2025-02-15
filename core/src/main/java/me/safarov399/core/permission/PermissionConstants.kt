package me.safarov399.core.permission

import android.Manifest

object PermissionConstants {
    val requiredPermissionsAndroid10AndBelow = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
}