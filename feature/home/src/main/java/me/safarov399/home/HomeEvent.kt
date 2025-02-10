package me.safarov399.home

import me.safarov399.common.FileConstants.ASCENDING_ORDER
import me.safarov399.common.FileConstants.NAME_SORTING_TYPE

sealed class HomeEvent {
    data class ChangePath(val newPath: String) : HomeEvent()
    data class CreateObject(val name: String, val path: String, val type: Int): HomeEvent()
    data class ChangeSortType(val sortBy: Int = NAME_SORTING_TYPE): HomeEvent()
    data class ChangeSortOrder(val sortOrder: Int = ASCENDING_ORDER): HomeEvent()
}