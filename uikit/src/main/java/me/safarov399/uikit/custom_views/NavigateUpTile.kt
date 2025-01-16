package me.safarov399.uikit.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import me.safarov399.uikit.databinding.NavigateUpTileBinding

class NavigateUpTile @JvmOverloads constructor(
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
    private val binding = NavigateUpTileBinding.inflate(LayoutInflater.from(context), this, true)
}