package me.safarov399.core.adapter

import me.safarov399.domain.models.adapter.FileFolderModel

interface OnClickListener {
    fun onClick(position: Int, model: FileFolderModel)
}