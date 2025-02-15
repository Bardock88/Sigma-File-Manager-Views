package me.safarov399.domain.models.adapter

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class OnHoldModel(
    @DrawableRes val iconId: Int,
    @StringRes val title: Int
)