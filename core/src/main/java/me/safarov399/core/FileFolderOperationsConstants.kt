package me.safarov399.core

import me.safarov399.domain.models.adapter.OnHoldModel

class FileFolderOperationsConstants {
    private val open = OnHoldModel(0, "Open")
    private val openWith = OnHoldModel(0, "Open with")

    private val view = OnHoldModel(0, "View")

    private val install = OnHoldModel(0,"Install")
    private val verifySignature = OnHoldModel(0,"Verify signature")
    private val checkDependencies = OnHoldModel(0,"Check dependencies")

    private val properties = OnHoldModel(0,"Properties")
    private val share = OnHoldModel(0,"Share")
    private val compress = OnHoldModel(0,"Compress")
    private val delete = OnHoldModel(0,"Delete")
    private val shred = OnHoldModel(0,"Shred")
    private val rename = OnHoldModel(0,"Rename")
    private val copy = OnHoldModel(0,"Copy")
    private val move = OnHoldModel(0,"Move to ...")
    private val addToFavorites = OnHoldModel(0,"Add to Favorites")
    private val extract = OnHoldModel(0,"Extract")
    private val createShortcut = OnHoldModel(0,"Create shortcut")
    private val checksum = OnHoldModel(0,"Checksum")

    private val changePermissions = OnHoldModel(0,"Change Permissions")

    val FILE_OPERATIONS_LIST = listOf(open, openWith, properties, share, compress, delete, shred, rename, copy, move, addToFavorites, createShortcut, checksum)
    val APK_OPERATIONS_LIST = listOf(open, openWith, install, verifySignature, checkDependencies, properties, share, compress, delete, shred, rename, copy, move, addToFavorites, createShortcut, checksum, changePermissions)
    val ARCHIVE_OPERATIONS = listOf(open, openWith, view, properties, share, compress, delete, shred, rename, copy, move, addToFavorites, extract, createShortcut, checksum, changePermissions)
    val FOLDER_OPERATIONS_LIST = listOf(properties, share, compress, delete, shred, rename, copy, move, addToFavorites, createShortcut, changePermissions)
}