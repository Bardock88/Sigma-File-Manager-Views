package me.safarov399.uikit.custom_views.dialogs

import androidx.fragment.app.Fragment

object DialogProvider {

    fun deleteConfirmationDialog(fragment: Fragment, type: String, name: String, onClick: (GenericDialog) -> Unit) {
        GenericDialog(fragment.requireActivity()).apply {
            setTitle(fragment.getString(me.safarov399.common.R.string.are_you_sure))
            setDescription(fragment.getString(me.safarov399.common.R.string.are_you_sure_desc_full, fragment.getString(me.safarov399.common.R.string.are_you_sure_desc_partial), type, name ))
            setConfirmButtonText(fragment.getString(me.safarov399.common.R.string.yes))
            setCancelButtonText(fragment.getString(me.safarov399.common.R.string.cancel))
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
            setTitle(fragment.getString(me.safarov399.common.R.string.cannot_copy_here))
            setDescription(fragment.getString(me.safarov399.common.R.string.destination_already_exists))
            setConfirmButtonText(fragment.getString(me.safarov399.common.R.string.yes))
            setCancelButtonText(fragment.getString(me.safarov399.common.R.string.no))
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

    fun renameDialog(fragment: Fragment, oldText: String, onConfirm: (EditTextDialog) -> Unit) {
        EditTextDialog(fragment.requireActivity()).apply {
            setTitle("Rename")
            setHint("New name")
            setText(oldText)
            setConfirmAction {
                onConfirm.invoke(this)
                dismiss()
            }
            setCancelAction {
                dismiss()
            }
            show()
        }
    }
}