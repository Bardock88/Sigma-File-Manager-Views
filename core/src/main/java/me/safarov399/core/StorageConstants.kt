package me.safarov399.core

import android.annotation.SuppressLint

object StorageConstants {
    private const val ANDROID_DIRECTORY = "/storage/emulated/0/Android"
    @SuppressLint("SdCardPath")
    private const val SDCARD_ANDROID_DIRECTORY = "/sdcard/Android"
    val RESTRICTED_DIRECTORIES = listOf(ANDROID_DIRECTORY, SDCARD_ANDROID_DIRECTORY)
}