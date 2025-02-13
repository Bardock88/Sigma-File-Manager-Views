package me.safarov399.home.bottom_sheet

import me.safarov399.core.base.BaseViewModel

class BottomSheetViewModel : BaseViewModel<BottomSheetState, BottomSheetEffect, BottomSheetEvent>() {
    override fun getInitialState(): BottomSheetState = BottomSheetState()

    override fun onEventUpdate(event: BottomSheetEvent) {
        when (event) {
            is BottomSheetEvent.Compress -> {

            }

            is BottomSheetEvent.Properties -> {

            }

            is BottomSheetEvent.Open -> {

            }

            is BottomSheetEvent.Share -> {

            }

            is BottomSheetEvent.AddToFavorites -> {

            }

            is BottomSheetEvent.ChangePermissions -> {

            }

            is BottomSheetEvent.CheckDependencies -> {

            }

            is BottomSheetEvent.Checksum -> {

            }

            is BottomSheetEvent.Copy -> {

            }

            is BottomSheetEvent.CreateShortcut -> {

            }

            is BottomSheetEvent.Delete -> {

            }

            is BottomSheetEvent.Duplicate -> {

            }

            is BottomSheetEvent.Extract -> {

            }

            is BottomSheetEvent.Install -> {

            }

            is BottomSheetEvent.Move -> {

            }

            is BottomSheetEvent.OpenWith -> {

            }

            is BottomSheetEvent.Rename -> {

            }

            is BottomSheetEvent.Shred -> {

            }

            is BottomSheetEvent.VerifySignature -> {

            }
        }
    }


}