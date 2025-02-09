package me.safarov399.home

import me.safarov399.core.storage.StorageConstants.DEFAULT_DIRECTORY
import me.safarov399.domain.models.adapter.FileFolderModel

data class HomeUiState(
    var currentPath: String = DEFAULT_DIRECTORY,
    var currentFileFolders: List<FileFolderModel> = emptyList()
)