package me.safarov399.core.file

import me.safarov399.domain.models.adapter.OnHoldModel

object FileFolderOperationsConstants {
    private val open = OnHoldModel(me.safarov399.uikit.R.drawable.open, me.safarov399.common.R.string.open)
    private val openWith = OnHoldModel(me.safarov399.uikit.R.drawable.open_with, me.safarov399.common.R.string.open_with)

    private val view = OnHoldModel(me.safarov399.uikit.R.drawable.view, me.safarov399.common.R.string.view)

    private val install = OnHoldModel(me.safarov399.uikit.R.drawable.open,me.safarov399.common.R.string.install)
    private val verifySignature = OnHoldModel(me.safarov399.uikit.R.drawable.verify,me.safarov399.common.R.string.verify_signature)
    private val checkDependencies = OnHoldModel(me.safarov399.uikit.R.drawable.check_dependencies,me.safarov399.common.R.string.check_dependencies)

    private val properties = OnHoldModel(me.safarov399.uikit.R.drawable.info,me.safarov399.common.R.string.properties)
    private val share = OnHoldModel(me.safarov399.uikit.R.drawable.share,me.safarov399.common.R.string.share)
    private val compress = OnHoldModel(me.safarov399.uikit.R.drawable.compress,me.safarov399.common.R.string.compress)
    private val delete = OnHoldModel(me.safarov399.uikit.R.drawable.delete,me.safarov399.common.R.string.delete)
    private val shred = OnHoldModel(me.safarov399.uikit.R.drawable.shred,me.safarov399.common.R.string.shred)
    private val rename = OnHoldModel(me.safarov399.uikit.R.drawable.edit,me.safarov399.common.R.string.rename)
    private val copy = OnHoldModel(me.safarov399.uikit.R.drawable.copy,me.safarov399.common.R.string.copy)
    private val move = OnHoldModel(me.safarov399.uikit.R.drawable.cut,me.safarov399.common.R.string.move_to)
    private val addToFavorites = OnHoldModel(me.safarov399.uikit.R.drawable.star_outline,me.safarov399.common.R.string.add_to_favorites)
    private val extract = OnHoldModel(me.safarov399.uikit.R.drawable.extract,me.safarov399.common.R.string.extract)
    private val createShortcut = OnHoldModel(me.safarov399.uikit.R.drawable.shortcut,me.safarov399.common.R.string.create_shortcut)
    private val checksum = OnHoldModel(me.safarov399.uikit.R.drawable.checksum,me.safarov399.common.R.string.checksum)

    private val changePermissions = OnHoldModel(me.safarov399.uikit.R.drawable.permission,me.safarov399.common.R.string.change_permissions)

    val FILE_OPERATIONS_LIST = listOf(open, openWith, properties, share, compress, delete, shred, rename, copy, move, addToFavorites, createShortcut, checksum)
    val APK_OPERATIONS_LIST = listOf(install, verifySignature, checkDependencies, properties, share, compress, delete, shred, rename, copy, move, addToFavorites, createShortcut, checksum, changePermissions)
    val ARCHIVE_OPERATIONS = listOf(open, openWith, view, properties, share, compress, delete, shred, rename, copy, move, addToFavorites, extract, createShortcut, checksum, changePermissions)
    val FOLDER_OPERATIONS_LIST = listOf(properties, share, compress, delete, shred, rename, copy, move, addToFavorites, createShortcut, changePermissions)
}