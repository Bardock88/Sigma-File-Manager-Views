package me.safarov399.domain.sorting

interface AbstractSortingPreferenceRepository {
    fun saveSortingPreference(sortType: Int)
    fun getSortingPreference(): Int
}