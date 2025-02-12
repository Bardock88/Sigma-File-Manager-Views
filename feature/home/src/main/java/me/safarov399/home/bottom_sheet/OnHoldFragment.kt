package me.safarov399.home.bottom_sheet

import android.view.LayoutInflater
import android.view.ViewGroup
import me.safarov399.core.base.BaseFragment
import me.safarov399.home.databinding.FragmentOnHoldBottomSheetBinding

class OnHoldFragment : BaseFragment<FragmentOnHoldBottomSheetBinding, OnHoldViewModel, OnHoldState, OnHoldEffect, OnHoldEvent>() {

    override val getViewBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOnHoldBottomSheetBinding = { inflater, viewGroup, value ->
        FragmentOnHoldBottomSheetBinding.inflate(inflater, viewGroup, value)
    }

    override fun getViewModelClass(): Class<OnHoldViewModel> = OnHoldViewModel::class.java
}