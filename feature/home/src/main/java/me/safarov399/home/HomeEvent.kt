package me.safarov399.home

import me.safarov399.common.file.FileConstants.ASCENDING_ORDER
import me.safarov399.common.file.FileConstants.NAME_SORTING_TYPE
import me.safarov399.core.file.OperationModel
import me.safarov399.core.file.OperationTypes

sealed class HomeEvent {
    data class ChangePath(val newPath: String) : HomeEvent()
    data class CreateObject(val name: String, val path: String, val type: Int) : HomeEvent()
    data class ChangeSortType(val sortBy: Int = NAME_SORTING_TYPE) : HomeEvent()
    data class ChangeSortOrder(val sortOrder: Int = ASCENDING_ORDER) : HomeEvent()
    data class SwitchOperationMode(val operationModel: OperationModel = OperationModel(OperationTypes.NORMAL, emptyList())) : HomeEvent()
    data class Copy(val filePaths: List<String>, val newPath: String, val overwrite: Boolean = false) : HomeEvent()
    data class Rename(val oldName: String, val newName: String) : HomeEvent()
}