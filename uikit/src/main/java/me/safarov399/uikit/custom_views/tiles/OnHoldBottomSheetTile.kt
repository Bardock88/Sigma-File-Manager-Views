package me.safarov399.uikit.custom_views.tiles

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import me.safarov399.uikit.databinding.OnHoldBottomSheetTileBinding

class OnHoldBottomSheetTile @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    /**
     * `fragment_home.xml` is unable to see this view without binding. That is why,
     * ```kotlin
     * @Suppress("unused")
     * ```
     * is used here.
     */
    @Suppress("unused")
    private val binding = OnHoldBottomSheetTileBinding.inflate(LayoutInflater.from(context), this, true)
}