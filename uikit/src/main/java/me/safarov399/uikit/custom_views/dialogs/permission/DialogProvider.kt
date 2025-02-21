package me.safarov399.uikit.custom_views.dialogs.permission

import androidx.fragment.app.Fragment

object DialogProvider {

    fun deleteConfirmationDialog(fragment: Fragment, type: String, name: String, onClick: (GenericDialog) -> Unit) {
        GenericDialog(fragment.requireActivity()).apply {
            setTitle("Are you sure?")
            setDescription("Are you sure you want to delete the $type $name?")
            setConfirmButtonText("Yes")
            setCancelButtonText("Cancel")
            setConfirmationOnClickListener {
                onClick.invoke(this)
                dismiss()
            }
            setCancelButtonOnClickListener {
                dismiss()
            }
            show()
        }
    }

    fun goToSettingsDialog(fragment: Fragment, onClick: (GenericDialog) -> Unit) {
        GenericDialog(fragment.requireActivity()).apply {
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

    fun showPermissionRequestDialog(fragment: Fragment, onClick: (GenericDialog) -> Unit) {
        GenericDialog(fragment.requireActivity()).apply {
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

    fun confirmCopyOverwriteDialog(fragment: Fragment, onConfirm: (GenericDialog) -> Unit, onCancel: (GenericDialog) -> Unit) {
        GenericDialog(fragment.requireActivity()).apply{
            setTitle("Cannot copy here")
            setDescription("The destination already exists. Do you want to overwrite?")
            setConfirmButtonText("Yes")
            setCancelButtonText("No")
            setConfirmationOnClickListener {
                onConfirm.invoke(this)
                dismiss()
            }
            setCancelButtonOnClickListener {
                onCancel.invoke(this)
                dismiss()
            }
            show()
        }
    }
}