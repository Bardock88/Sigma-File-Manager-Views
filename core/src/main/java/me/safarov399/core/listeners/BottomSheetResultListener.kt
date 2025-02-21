package me.safarov399.core.listeners

import me.safarov399.core.file.OperationModel

interface BottomSheetResultListener {
    fun onBottomSheetResult(operationModel: OperationModel)
}