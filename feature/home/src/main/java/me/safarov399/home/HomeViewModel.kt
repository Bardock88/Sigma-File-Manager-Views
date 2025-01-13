package me.safarov399.home

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.safarov399.core.base.BaseViewModel
import me.safarov399.core.storage.StorageConstants
import me.safarov399.domain.models.adapter.FileFolderModel
import me.safarov399.domain.models.adapter.FileModel
import me.safarov399.domain.models.adapter.FolderModel
import java.io.File


class HomeViewModel : BaseViewModel<HomeUiState, HomeEffect, HomeEvent>() {

    override fun onEventUpdate(event: HomeEvent) {
        when (event) {
            is HomeEvent.ChangePath -> viewModelScope.launch(Dispatchers.IO) {
                setState(
                    getCurrentState().copy(
                        currentPath = event.newPath,
                        currentFileFolders = readStorage(event.newPath)
                    )
                )
            }
        }
    }

    private fun readStorage(path: String): List<FileFolderModel> {
        val externalStorageDirectory = File(path)
        val fileAndFolders = externalStorageDirectory.listFiles()
        val onlyFiles = mutableListOf<FileModel>()
        val onlyFolders = mutableListOf<FolderModel>()
        for (file in fileAndFolders!!) {
            if (file.isFile) {
                onlyFiles.add(FileModel(name = file.name, size = file.length()))
            } else {
                if (path in StorageConstants.DANGEROUS_DIRECTORIES) {
                    if (file.name.equals("data") || file.name.equals("obb")) {
                        onlyFolders.add(FolderModel(name = file.name, itemCount = -1L))
                    } else {
                        onlyFolders.add(FolderModel(name = file.name, itemCount = file?.listFiles()?.size!!.toLong()))
                    }
                } else {
                    onlyFolders.add(FolderModel(name = file.name, itemCount = file?.listFiles()?.size!!.toLong()))
                }
            }
        }

        onlyFiles.sortBy { it.name }
        onlyFolders.sortBy { it.name }
        return (onlyFolders + onlyFiles)
    }

    override fun getInitialState(): HomeUiState = HomeUiState()
}