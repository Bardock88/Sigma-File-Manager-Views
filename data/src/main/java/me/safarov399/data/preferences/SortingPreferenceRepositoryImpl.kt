package me.safarov399.data.preferences

import me.safarov399.domain.sorting.AbstractSortingPreferenceRepository
import javax.inject.Inject


class SortingPreferenceRepositoryImpl @Inject constructor (
    private val encryptedPreferencesManager: EncryptedSharedPreferencesManager
) : AbstractSortingPreferenceRepository {

    override fun saveSortingPreference(sortType: Int) {
        encryptedPreferencesManager.saveSortingPreference(sortType)
    }

    override fun getSortingPreference(): Int {
        return encryptedPreferencesManager.getSortingPreference()
    }
}