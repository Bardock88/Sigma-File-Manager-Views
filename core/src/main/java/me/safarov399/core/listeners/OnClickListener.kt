package me.safarov399.core.listeners

import me.safarov399.domain.models.adapter.FileFolderModel
import me.safarov399.domain.models.adapter.OnHoldModel

interface OnClickListener {
    fun onClickFileFolder(position: Int, model: FileFolderModel){}
    fun onClickBottomSheetItem(position: Int, model: OnHoldModel){}
}