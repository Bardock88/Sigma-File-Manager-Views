package me.safarov399.uikit.custom_views.dialogs.permission

import androidx.fragment.app.Fragment

object DialogProvider {
    fun goToSettingsDialog(fragment: Fragment, onClick: (PermissionDialog) -> Unit) {
        PermissionDialog(fragment.requireActivity()).apply {
            setTitle(fragment.getString(me.safarov399.common.R.string.not_granted_title))
            setDescription(fragment.getString(me.safarov399.common.R.string.not_granted_description))
            setConfirmButtonText(fragment.getString(me.safarov399.common.R.string.not_granted_confirm))
            setCancelButtonText(fragment.getString(me.safarov399.common.R.string.not_granted_cancel))
            setConfirmationOnClickListener {
                onClick.invoke(this)
            }
            show()
        }
    }

    fun showPermissionRequestDialog(fragment: Fragment, onClick: (PermissionDialog) -> Unit) {
        PermissionDialog(fragment.requireActivity()).apply {
            setTitle(fragment.getString(me.safarov399.common.R.string.permission_dialog_title))
            setDescription(fragment.getString(me.safarov399.common.R.string.permission_dialog_description))
            setConfirmButtonText(fragment.getString(me.safarov399.common.R.string.ok))
            setCancelButtonText(fragment.getString(me.safarov399.common.R.string.cancel))
            setConfirmationOnClickListener {
                onClick.invoke(this)
            }
            show()
        }
    }
}