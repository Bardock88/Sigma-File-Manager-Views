package me.safarov399.core

import android.Manifest

object PermissionConstants {
    val requiredPermissionsAndroid10AndBelow = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
}