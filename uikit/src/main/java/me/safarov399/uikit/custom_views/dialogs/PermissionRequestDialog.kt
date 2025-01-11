package me.safarov399.uikit.custom_views.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import me.safarov399.uikit.databinding.PermissionRequestDialogBinding

class PermissionRequestDialog(ctx: Context): Dialog(ctx) {
    private val binding: PermissionRequestDialogBinding

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
//        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = PermissionRequestDialogBinding.inflate(LayoutInflater.from(context))
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

}