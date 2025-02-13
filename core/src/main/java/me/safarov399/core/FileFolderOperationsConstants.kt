package me.safarov399.core

import me.safarov399.domain.models.adapter.OnHoldModel

object FileFolderOperationsConstants {
    private val open = OnHoldModel(me.safarov399.uikit.R.drawable.open, "Open")
    private val openWith = OnHoldModel(me.safarov399.uikit.R.drawable.open_with, "Open with")

    private val view = OnHoldModel(me.safarov399.uikit.R.drawable.view, "View")

    private val install = OnHoldModel(me.safarov399.uikit.R.drawable.open,"Install")
    private val verifySignature = OnHoldModel(me.safarov399.uikit.R.drawable.verify,"Verify signature")
    private val checkDependencies = OnHoldModel(me.safarov399.uikit.R.drawable.check_dependencies,"Check dependencies")

    private val properties = OnHoldModel(me.safarov399.uikit.R.drawable.info,"Properties")
    private val share = OnHoldModel(me.safarov399.uikit.R.drawable.share,"Share")
    private val compress = OnHoldModel(me.safarov399.uikit.R.drawable.compress,"Compress")
    private val delete = OnHoldModel(me.safarov399.uikit.R.drawable.delete,"Delete")
    private val shred = OnHoldModel(me.safarov399.uikit.R.drawable.shred,"Shred")
    private val rename = OnHoldModel(me.safarov399.uikit.R.drawable.edit,"Rename")
    private val copy = OnHoldModel(me.safarov399.uikit.R.drawable.copy,"Copy")
    private val move = OnHoldModel(me.safarov399.uikit.R.drawable.cut,"Move to ...")
    private val addToFavorites = OnHoldModel(me.safarov399.uikit.R.drawable.star_outline,"Add to Favorites")
    private val extract = OnHoldModel(me.safarov399.uikit.R.drawable.extract,"Extract")
    private val createShortcut = OnHoldModel(me.safarov399.uikit.R.drawable.shortcut,"Create shortcut")
    private val checksum = OnHoldModel(me.safarov399.uikit.R.drawable.checksum,"Checksum")

    private val changePermissions = OnHoldModel(me.safarov399.uikit.R.drawable.permission,"Change Permissions")

    val FILE_OPERATIONS_LIST = listOf(open, openWith, properties, share, compress, delete, shred, rename, copy, move, addToFavorites, createShortcut, checksum)
    val APK_OPERATIONS_LIST = listOf(open, openWith, install, verifySignature, checkDependencies, properties, share, compress, delete, shred, rename, copy, move, addToFavorites, createShortcut, checksum, changePermissions)
    val ARCHIVE_OPERATIONS = listOf(open, openWith, view, properties, share, compress, delete, shred, rename, copy, move, addToFavorites, extract, createShortcut, checksum, changePermissions)
    val FOLDER_OPERATIONS_LIST = listOf(properties, share, compress, delete, shred, rename, copy, move, addToFavorites, createShortcut, changePermissions)
}