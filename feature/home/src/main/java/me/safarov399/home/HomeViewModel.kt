package me.safarov399.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.safarov399.common.file.FileConstants.ASCENDING_ORDER
import me.safarov399.common.file.FileConstants.DATE_SORTING_TYPE
import me.safarov399.common.file.FileConstants.FILE_TYPE
import me.safarov399.common.file.FileConstants.NAME_SORTING_TYPE
import me.safarov399.common.file.FileConstants.SIZE_SORTING_TYPE
import me.safarov399.common.file.FileConstants.TYPE_SORTING_TYPE
import me.safarov399.core.base.BaseViewModel
import me.safarov399.core.file.fileAndPathMerger
import me.safarov399.core.storage.StorageConstants.DANGEROUS_DIRECTORIES
import me.safarov399.core.storage.StorageConstants.DATA_DIRECTORY
import me.safarov399.core.storage.StorageConstants.OBB_DIRECTORY
import me.safarov399.domain.models.adapter.FileFolderModel
import me.safarov399.domain.models.adapter.FileModel
import me.safarov399.domain.models.adapter.FolderModel
import me.safarov399.domain.repo.AbstractSortingPreferenceRepository
import java.io.File
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sortingPreferenceRepository: AbstractSortingPreferenceRepository
) : BaseViewModel<HomeUiState, HomeEffect, HomeEvent>() {

    override fun onEventUpdate(event: HomeEvent) {
        when (event) {
            is HomeEvent.ChangePath -> viewModelScope.launch(Dispatchers.IO) {
                setState(
                    getCurrentState().copy(
                        currentPath = event.newPath,
                        currentFileFolders = readStorage(event.newPath),
                        sortType = getSortType(),
                        isAscending = getSortOrder() == ASCENDING_ORDER
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

            is HomeEvent.ChangeSortType -> {
                saveSortType(event.sortBy)
                setState(
                    getCurrentState().copy(
                        sortType = event.sortBy
                    )
                )
            }

            is HomeEvent.ChangeSortOrder -> {
                saveSortOrder(event.sortOrder)
                setState(
                    getCurrentState().copy(
                        isAscending = event.sortOrder == ASCENDING_ORDER,
                    )
                )
            }

            is HomeEvent.SwitchOperationMode -> {
                setState(
                    getCurrentState().copy(
                        operationModel = event.operationModel
                    )
                )
            }

            is HomeEvent.Copy -> {
                copy(event.filePaths, event.newPath, event.overwrite)
            }

            is HomeEvent.Rename -> {
                rename(event.oldName, event.newName)
            }
        }
    }

    private fun saveSortType(sortType: Int) {
        sortingPreferenceRepository.saveSortTypePreference(sortType)
    }

    private fun getSortType(): Int {
        return sortingPreferenceRepository.getSortTypePreference()
    }

    private fun saveSortOrder(sortOrder: Int) {
        sortingPreferenceRepository.saveSortOrderPreference(sortOrder)
    }

    private fun getSortOrder(): Int {
        return sortingPreferenceRepository.getSortOrderPreference()
    }

    private fun copy(filePaths: List<String>, newPath: String, overwrite: Boolean = false) {
        var problemWithCopying = false
        for (filePath in filePaths) {
            try {
                if (!isCopyingIntoItself(filePath, newPath)) {
                    val file = File(filePath)
                    val destination = File(newPath, file.name)
                    file.copyRecursively(destination, overwrite)   // this function writes the contents of a directory instead of itself, which is why a new variable called destination has been created.
                } else {
                    problemWithCopying = true
                    postEffect(HomeEffect.CopyingIntoItself)
                }

            } catch (e: FileAlreadyExistsException) {
                problemWithCopying = true
                postEffect(HomeEffect.CopiedFileAlreadyExist(e.file.name, operationFiles = filePaths, newPath))
            }
        }
        if (!problemWithCopying) {
            postEffect(HomeEffect.CopySuccessful)
        }
    }

    private fun isCopyingIntoItself(sourcePath: String, destinationPath: String): Boolean {
        return destinationPath.startsWith(sourcePath) // Prevents self-copy and subfolder copy
    }

    private fun rename(oldName: String, newName: String) {
        val oldFile = File(oldName)
        val newPath = fileAndPathMerger(newName, oldName.substringBeforeLast("/"))
        val newFile = File(newPath)
        if (oldFile.exists()) {
            if (!newFile.exists()) {
                oldFile.renameTo(newFile)
            } else {
                if (newFile.isFile) {
                    postEffect(HomeEffect.FileAlreadyExists)
                } else {
                    postEffect(HomeEffect.FolderAlreadyExists)
                }
            }

        } else {
            postEffect(HomeEffect.DoesNotExist)
        }
    }

    private fun createFileFolder(name: String, path: String, type: Int) {
        /**
         * Trimming a file or folder name by default may not be suitable for some power users (for whatever reason). That is why, this line is going to be commented out for the time being. I will create a toggle in settings screen to control this behaviour (users will still be able to achieve the original behaviour). For the time being, the user is free to add as many spaces and newlines without them being removed from the file or folder name.
         */
//        name = name.trim()
        val file = File(path, name)
        if (type == FILE_TYPE) {
            if ("/" in name) {
                val parentPath = name.substringBeforeLast("/")
                val parentDirectory = File(path, parentPath)
                parentDirectory.mkdirs()    // Creates the parent directory before trying to create the file
            }
            val fileCreated = file.createNewFile()
            if (!fileCreated) {
                postEffect(HomeEffect.FileAlreadyExists)
            } else {
                postEffect(HomeEffect.FileCreated(name, path))
            }
        } else {
            val folderCreated = file.mkdirs()
            if (!folderCreated) {
                postEffect(HomeEffect.FolderAlreadyExists)
            } else {
                postEffect(HomeEffect.FolderCreated(name, path))
            }
        }
    }

    private fun readStorage(path: String): List<FileFolderModel> {
        val sortType = getSortType()
        val sortOrder = getSortOrder()
        val externalStorageDirectory = File(path)
        val fileAndFolders = externalStorageDirectory.listFiles()
        val onlyFiles = mutableListOf<FileModel>()
        val onlyFolders = mutableListOf<FolderModel>()
        for (file in fileAndFolders!!) {
            if (file.isFile) {
                onlyFiles.add(FileModel(name = file.name, size = file.length(), lastModified = file.lastModified()))
            } else {
                if (path in DANGEROUS_DIRECTORIES) {
                    if (file.name.equals(DATA_DIRECTORY) || file.name.equals(OBB_DIRECTORY)) {
                        onlyFolders.add(FolderModel(name = file.name, itemCount = -1L, lastModified = file.lastModified()))
                    } else {
                        onlyFolders.add(FolderModel(name = file.name, itemCount = file?.listFiles()?.size!!.toLong(), lastModified = file.lastModified()))
                    }
                } else {
                    onlyFolders.add(FolderModel(name = file.name, itemCount = file?.listFiles()?.size!!.toLong(), lastModified = file.lastModified()))
                }
            }
        }

        if (sortOrder == ASCENDING_ORDER) {
            when (sortType) {
                NAME_SORTING_TYPE -> {
                    onlyFiles.sortBy { it.name.lowercase() }
                    onlyFolders.sortBy { it.name.lowercase() }
                }

                DATE_SORTING_TYPE -> {
                    onlyFiles.sortBy { it.lastModified }
                    onlyFolders.sortBy { it.lastModified }
                }

                SIZE_SORTING_TYPE -> {
                    onlyFiles.sortBy { it.size }
                    onlyFolders.sortBy { it.name.lowercase() }
                }

                TYPE_SORTING_TYPE -> {
                    onlyFiles.sortBy {
                        if (it.name.contains(".")) {
                            it.name.substringAfterLast(".").lowercase()
                        } else it.name.lowercase()
                    }
                    onlyFolders.sortBy { it.name.lowercase() }
                }
            }
        } else {
            when (sortType) {
                NAME_SORTING_TYPE -> {
                    onlyFiles.sortByDescending { it.name.lowercase() }
                    onlyFolders.sortByDescending { it.name.lowercase() }
                }

                DATE_SORTING_TYPE -> {
                    onlyFiles.sortByDescending { it.lastModified }
                    onlyFolders.sortByDescending { it.lastModified }
                }

                SIZE_SORTING_TYPE -> {
                    onlyFiles.sortByDescending { it.size }
                    onlyFolders.sortBy { it.name.lowercase() }
                }

                TYPE_SORTING_TYPE -> {
                    onlyFiles.sortByDescending {
                        if (it.name.contains(".")) {
                            it.name.substringAfterLast(".").lowercase()
                        } else it.name.lowercase()
                    }
                    onlyFolders.sortBy { it.name.lowercase() }
                }
            }
        }
        return (onlyFolders + onlyFiles)
    }

    override fun getInitialState(): HomeUiState = HomeUiState()
}