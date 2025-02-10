package me.safarov399.data.preferences

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class EncryptedSharedPreferencesManager(ctx: Context) {
    private val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val sharedPreferences = EncryptedSharedPreferences.create(
        "secure_prefs",
        masterKey,
        ctx,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveSortingPreference(sortType: Int) {
        sharedPreferences.edit().putInt(SORT_TYPE, sortType).apply()
    }

    fun getSortingPreference(): Int {
        return sharedPreferences.getInt(SORT_TYPE, 0) // Default to 0 (e.g., name sorting)
    }

    companion object {
        const val SORT_TYPE = "sort_type"
    }
}