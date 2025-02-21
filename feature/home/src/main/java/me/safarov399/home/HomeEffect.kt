package me.safarov399.home

sealed class HomeEffect {
    data object FileAlreadyExists: HomeEffect()
    data class FileCreated(val name: String, val path: String): HomeEffect()
    data object FolderAlreadyExists: HomeEffect()
    data class FolderCreated(val name: String, val path: String): HomeEffect()
    data class CopiedFileAlreadyExist(val name: String, val operationFiles: List<String>, val newPath: String): HomeEffect()
    data object CopySuccessful: HomeEffect()
    data object CopyingIntoItself: HomeEffect()
}