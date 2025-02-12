package me.safarov399.home.bottom_sheet

sealed class OnHoldEvent {
    data class Open(val path: String): OnHoldEvent()
    data class OpenWith(val path: String): OnHoldEvent()

    data class Install(val path: String): OnHoldEvent()
    data class VerifySignature(val path: String): OnHoldEvent()
    data class CheckDependencies(val path: String): OnHoldEvent()

    data class Properties(val path: String): OnHoldEvent()
    data class Share(val path: String): OnHoldEvent()
    data class Compress(val paths: List<String>): OnHoldEvent()
    data class Delete(val paths: List<String>): OnHoldEvent()
    data class Shred(val paths: List<String>, val overWriteCount: Int): OnHoldEvent()
    data class Rename(val oldName: String, val newName: String): OnHoldEvent()
    data class Copy(val oldPath: String, val newPath: String): OnHoldEvent()
    data class Move(val oldPath: String, val newPath: String): OnHoldEvent()
    data class Duplicate(val path: String): OnHoldEvent()
    data class AddToFavorites(val path: String): OnHoldEvent()
    data class Extract(val path: String, val extractionPath: String): OnHoldEvent()
    data class CreateShortcut(val path: String): OnHoldEvent()
    data class Checksum(val path: String): OnHoldEvent()

    data class ChangePermissions(val path: String): OnHoldEvent()    // For root users. Full implementation and function parameters will be added later on.
}