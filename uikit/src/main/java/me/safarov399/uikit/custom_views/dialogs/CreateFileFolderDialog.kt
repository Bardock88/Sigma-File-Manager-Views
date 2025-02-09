package me.safarov399.uikit.custom_views.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import me.safarov399.uikit.databinding.CreateFileFolderDialogBinding

class CreateFileFolderDialog(ctx: Context): Dialog(ctx) {
    private val binding: CreateFileFolderDialogBinding

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = CreateFileFolderDialogBinding.inflate(LayoutInflater.from(ctx))
        setContentView(binding.root)
    }
    fun setTitle(title: String) {
        binding.cffTitleTv.text = title
    }
    fun setHint(hint: String) {
        binding.cffNameTiet.hint = hint
    }
    fun setConfirmAction(onClick: () -> Unit) {
        binding.cffOkBtn.setOnClickListener {
            onClick.invoke()
        }
    }
    fun setCancelAction(onClick: () -> Unit) {
        binding.cffCancelBtn.setOnClickListener {
            onClick.invoke()
        }
    }
}