package me.safarov399.uikit.custom_views.dialogs.permission

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import me.safarov399.uikit.databinding.PermissionDialogBinding

class PermissionDialog(ctx: Context) : Dialog(ctx) {
    private val binding: PermissionDialogBinding

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = PermissionDialogBinding.inflate(LayoutInflater.from(context))
        binding.permissionDialogCancelBtn.setOnClickListener {
            (ctx as Activity).finish()
        }
        setContentView(binding.root)
    }

    fun setConfirmationOnClickListener(onClick: () -> Unit) {
        binding.permissionDialogConfirmationBtn.setOnClickListener {
            onClick.invoke()
        }
    }

    fun setConfirmButtonText(text: String) {
        binding.permissionDialogConfirmationBtn.text = text
    }

    fun setCancelButtonText(text: String) {
        binding.permissionDialogCancelBtn.text = text
    }

    fun setTitle(text: String) {
        binding.permissionDialogTitleTv.text = text
    }

    fun setDescription(text: String) {
        binding.permissionDialogDescriptionTv.text = text
    }

    fun setCancelButtonOnClickListener(onClick: () -> Unit) {
        binding.permissionDialogCancelBtn.setOnClickListener {
            onClick.invoke()
        }
    }
}