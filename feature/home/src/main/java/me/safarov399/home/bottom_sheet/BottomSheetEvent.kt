package me.safarov399.home.bottom_sheet

sealed class BottomSheetEvent {
    data class Open(val path: String): BottomSheetEvent()
    data class OpenWith(val path: String): BottomSheetEvent()

    data class View(val path: String): BottomSheetEvent()

    data class Install(val path: String): BottomSheetEvent()
    data class VerifySignature(val path: String): BottomSheetEvent()
    data class CheckDependencies(val path: String): BottomSheetEvent()

    data class Properties(val path: String): BottomSheetEvent()
    data class Share(val path: String): BottomSheetEvent()
    data class Compress(val paths: List<String>): BottomSheetEvent()
    data class Delete(val paths: List<String>): BottomSheetEvent()
    data class Shred(val paths: List<String>, val overWriteCount: Int): BottomSheetEvent()
    data class Rename(val path: String, val oldName: String, val newName: String): BottomSheetEvent()
    data class Copy(val items:List<String>, val oldPath: String, val newPath: String): BottomSheetEvent()
    data class Move(val items:List<String>,val oldPath: String, val newPath: String): BottomSheetEvent()
    data class AddToFavorites(val path: String): BottomSheetEvent()
    data class Extract(val path: String, val extractionPath: String): BottomSheetEvent()
    data class CreateShortcut(val path: String): BottomSheetEvent()
    data class Checksum(val path: String): BottomSheetEvent()

    data class ChangePermissions(val path: String): BottomSheetEvent()    // For root users. Full implementation and function parameters will be added later on.
}