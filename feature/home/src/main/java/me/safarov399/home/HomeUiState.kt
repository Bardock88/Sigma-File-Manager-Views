package me.safarov399.home

import me.safarov399.domain.models.adapter.FileFolderModel

data class HomeUiState(
    var currentPath: String = "/storage/emulated/0/",
    var currentFileFolders: List<FileFolderModel> = emptyList()
)