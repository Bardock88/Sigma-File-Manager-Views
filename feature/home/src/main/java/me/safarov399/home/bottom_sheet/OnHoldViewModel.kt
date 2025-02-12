package me.safarov399.home.bottom_sheet

import me.safarov399.core.base.BaseViewModel

class OnHoldViewModel : BaseViewModel<OnHoldState, OnHoldEffect, OnHoldEvent>() {
    override fun getInitialState(): OnHoldState = OnHoldState()

    override fun onEventUpdate(event: OnHoldEvent) {
        when (event) {
            is OnHoldEvent.Compress -> {

            }

            is OnHoldEvent.Properties -> {

            }

            is OnHoldEvent.Open -> {

            }

            is OnHoldEvent.Share -> {

            }

            is OnHoldEvent.AddToFavorites -> {

            }

            is OnHoldEvent.ChangePermissions -> {

            }

            is OnHoldEvent.CheckDependencies -> {

            }

            is OnHoldEvent.Checksum -> {

            }

            is OnHoldEvent.Copy -> {

            }

            is OnHoldEvent.CreateShortcut -> {

            }

            is OnHoldEvent.Delete -> {

            }

            is OnHoldEvent.Duplicate -> {

            }

            is OnHoldEvent.Extract -> {

            }

            is OnHoldEvent.Install -> {

            }

            is OnHoldEvent.Move -> {

            }

            is OnHoldEvent.OpenWith -> {

            }

            is OnHoldEvent.Rename -> {

            }

            is OnHoldEvent.Shred -> {

            }

            is OnHoldEvent.VerifySignature -> {

            }
        }
    }


}