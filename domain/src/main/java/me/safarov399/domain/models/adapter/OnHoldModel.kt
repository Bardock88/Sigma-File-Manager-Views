package me.safarov399.domain.models.adapter

import androidx.annotation.DrawableRes

data class OnHoldModel(
    @DrawableRes val iconId: Int,
    val title: String
)