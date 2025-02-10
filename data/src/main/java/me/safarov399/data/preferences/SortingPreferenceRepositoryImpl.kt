package me.safarov399.data.preferences

import me.safarov399.domain.sorting.AbstractSortingPreferenceRepository
import javax.inject.Inject


class SortingPreferenceRepositoryImpl @Inject constructor (
    private val encryptedPreferencesManager: EncryptedSharedPreferencesManager
) : AbstractSortingPreferenceRepository {

    override fun saveSortTypePreference(sortType: Int) {
        encryptedPreferencesManager.saveSortTypePreference(sortType)
    }

    override fun getSortTypePreference(): Int {
        return encryptedPreferencesManager.getSortTypePreference()
    }

    override fun saveSortOrderPreference(sortOrder: Int) {
        encryptedPreferencesManager.saveSortOrderPreference(sortOrder)
    }

    override fun getSortOrderPreference(): Int {
        return encryptedPreferencesManager.getSortOrderPreference()
    }

}