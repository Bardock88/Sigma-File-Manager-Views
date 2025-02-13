package me.safarov399.core.listeners

import me.safarov399.domain.models.adapter.FileFolderModel
import me.safarov399.domain.models.adapter.OnHoldModel

interface OnHoldListener {
    fun onHoldFileFolder(position: Int, model: FileFolderModel) {}
    fun onHoldBottomSheetItem(position: Int, model: OnHoldModel) {}
}