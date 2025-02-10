package me.safarov399.domain.sorting

interface AbstractSortingPreferenceRepository {
    fun saveSortTypePreference(sortType: Int)
    fun getSortTypePreference(): Int
    fun saveSortOrderPreference(sortOrder: Int)
    fun getSortOrderPreference(): Int
}