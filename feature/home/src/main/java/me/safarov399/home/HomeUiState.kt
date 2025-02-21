package me.safarov399.home

import me.safarov399.common.file.FileConstants
import me.safarov399.core.file.OperationModes
import me.safarov399.core.storage.StorageConstants.DEFAULT_DIRECTORY
import me.safarov399.domain.models.adapter.FileFolderModel

data class HomeUiState(
    var currentPath: String = DEFAULT_DIRECTORY,
    var currentFileFolders: List<FileFolderModel> = emptyList(),
    var isAscending: Boolean = true,
    var sortType: Int = FileConstants.NAME_SORTING_TYPE,
    var operationMode: Int = OperationModes.NORMAL
)