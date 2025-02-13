package me.safarov399.data.preferences

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import me.safarov399.common.file.FileConstants.ASCENDING_ORDER
import me.safarov399.common.file.FileConstants.NAME_SORTING_TYPE

class EncryptedSharedPreferencesManager(ctx: Context) {
    private val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val sharedPreferences = EncryptedSharedPreferences.create(
        "secure_prefs",
        masterKey,
        ctx,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveSortTypePreference(sortType: Int) {
        sharedPreferences.edit().putInt(SORT_TYPE, sortType).apply()
    }

    fun getSortTypePreference(): Int {
        return sharedPreferences.getInt(SORT_TYPE, NAME_SORTING_TYPE)
    }

    fun saveSortOrderPreference(sortOrder: Int) {
        sharedPreferences.edit().putInt(SORT_ORDER, sortOrder).apply()
    }

    fun getSortOrderPreference(): Int {
        return sharedPreferences.getInt(SORT_ORDER, ASCENDING_ORDER)
    }

    companion object {
        const val SORT_TYPE = "sort_type"
        const val SORT_ORDER = "sort_order"
    }
}