package me.safarov399.home.bottom_sheet

sealed class BottomSheetEffect {
    data object DeletionFailed: BottomSheetEffect()
}