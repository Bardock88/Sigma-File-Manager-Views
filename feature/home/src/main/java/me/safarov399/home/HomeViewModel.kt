package me.safarov399.home

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
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
                readStorage(event.newPath).onEach {
                    setState(
                        getCurrentState().copy(
                            currentPath = event.newPath,
                            currentFileFolders = it
                        )
                    )
                }.collect()

            }
        }
    }

    private fun readStorage(path: String): Flow<List<FileFolderModel>> {
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
        val sortedFileFolders = onlyFolders + onlyFiles

        return flow {
            emit(sortedFileFolders)
        }


    }

    override fun getInitialState(): HomeUiState = HomeUiState()

}