package me.safarov399.home

import me.safarov399.common.FileConstants

sealed class HomeEvent {
    data class ChangePath(val newPath: String) : HomeEvent()
    data class CreateObject(val name: String, val path: String, val type: Int): HomeEvent()
    data class ChangeSortingType(val sortBy: Int = FileConstants.NAME_SORTING_TYPE): HomeEvent()
}