package me.safarov399.home

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.safarov399.common.MiscellaneousConstants.FILE_TYPE
import me.safarov399.core.base.BaseViewModel
import me.safarov399.core.storage.StorageConstants.DANGEROUS_DIRECTORIES
import me.safarov399.core.storage.StorageConstants.DATA_DIRECTORY
import me.safarov399.core.storage.StorageConstants.OBB_DIRECTORY
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

            is HomeEvent.CreateObject -> {
                viewModelScope.launch(Dispatchers.IO) {
                    createFileFolder(event.name, event.path, event.type)
                    setState(
                        getCurrentState().copy(
                            currentFileFolders = readStorage(event.path)
                        )
                    )
                }
            }
        }
    }

    private fun createFileFolder(name: String, path: String, type: Int) {
        val file = File(path, name)
        if(type == FILE_TYPE) {
            if ("/" in name) {
                val parentPath = name.substringBeforeLast("/")
                val parentDirectory = File(path, parentPath)
                parentDirectory.mkdirs()    // Creates the parent directory before trying to create the file
            }
            file.createNewFile()
        } else {
            file.mkdirs()
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
                if (path in DANGEROUS_DIRECTORIES) {
                    if (file.name.equals(DATA_DIRECTORY) || file.name.equals(OBB_DIRECTORY)) {
                        onlyFolders.add(FolderModel(name = file.name, itemCount = -1L))
                    } else {
                        onlyFolders.add(FolderModel(name = file.name, itemCount = file?.listFiles()?.size!!.toLong()))
                    }
                } else {
                    onlyFolders.add(FolderModel(name = file.name, itemCount = file?.listFiles()?.size!!.toLong()))
                }
            }
        }

        onlyFiles.sortBy { it.name.lowercase() }
        onlyFolders.sortBy { it.name.lowercase() }
        return (onlyFolders + onlyFiles)
    }

    override fun getInitialState(): HomeUiState = HomeUiState()
}