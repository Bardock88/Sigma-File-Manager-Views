package me.safarov399.uikit.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import me.safarov399.uikit.R
import me.safarov399.uikit.databinding.FileTileBinding


class FileTile @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding = FileTileBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.FileTile, 0, 0).apply {
            binding.fileTileTitleTv.text = getString(R.styleable.FileTile_titleText)
            binding.fileSizeTv.text = getString(R.styleable.FileTile_fileSize) + " items"
        }
    }

    fun setFileSizeText(text: String) {
        binding.fileSizeTv.text = text
    }
    fun setFileTitleText(text: String) {
        binding.fileTileTitleTv.text = text
    }
}