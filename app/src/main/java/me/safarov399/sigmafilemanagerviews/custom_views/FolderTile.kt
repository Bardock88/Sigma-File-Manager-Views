package me.safarov399.sigmafilemanagerviews.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import me.safarov399.sigmafilemanagerviews.R
import me.safarov399.sigmafilemanagerviews.databinding.FolderTileBinding

class FolderTile @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding = FolderTileBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.FolderTile, 0, 0).apply {
            binding.folderTileTitleTv.text = getString(R.styleable.FolderTile_titleText)
            binding.folderItemCountTv.text = getString(R.styleable.FolderTile_itemCount) + " items"
        }
    }
}