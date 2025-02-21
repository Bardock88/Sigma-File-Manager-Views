package me.safarov399.home

import me.safarov399.common.file.FileConstants.ASCENDING_ORDER
import me.safarov399.common.file.FileConstants.NAME_SORTING_TYPE
import me.safarov399.core.file.OperationModes.NORMAL

sealed class HomeEvent {
    data class ChangePath(val newPath: String) : HomeEvent()
    data class CreateObject(val name: String, val path: String, val type: Int): HomeEvent()
    data class ChangeSortType(val sortBy: Int = NAME_SORTING_TYPE): HomeEvent()
    data class ChangeSortOrder(val sortOrder: Int = ASCENDING_ORDER): HomeEvent()
    data class SwitchOperationMode(val operationMode: Int = NORMAL): HomeEvent()
}