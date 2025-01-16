package me.safarov399.core.storage

import android.annotation.SuppressLint

@SuppressLint("SdCardPath")
object StorageConstants {
    private const val ANDROID_DATA_DIRECTORY = "/storage/emulated/0/Android/data"
    private const val ANDROID_OBB_DIRECTORY="/storage/emulated/0/Android/obb"

    private const val SDCARD_ANDROID_DATA_DIRECTORY = "/sdcard/Android/data"
    private const val SDCARD_ANDROID_OBB_DIRECTORY = "/sdcard/Android/obb"

    private const val ANDROID_DIRECTORY = "/storage/emulated/0/Android"
    private const val SDCARD_ANDROID_DIRECTORY="/sdcard/Android"

    const val DEFAULT_DIRECTORY = "/storage/emulated/0"

    val RESTRICTED_DIRECTORIES = listOf(ANDROID_DATA_DIRECTORY, ANDROID_OBB_DIRECTORY, SDCARD_ANDROID_DATA_DIRECTORY, SDCARD_ANDROID_OBB_DIRECTORY)
    val DANGEROUS_DIRECTORIES = listOf(ANDROID_DIRECTORY, SDCARD_ANDROID_DIRECTORY)
}