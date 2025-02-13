package me.safarov399.home.bottom_sheet

import android.view.LayoutInflater
import android.view.ViewGroup
import me.safarov399.core.base.BaseFragment
import me.safarov399.home.databinding.FragmentOnHoldBottomSheetBinding

class BottomSheetFragment : BaseFragment<FragmentOnHoldBottomSheetBinding, BottomSheetViewModel, BottomSheetState, BottomSheetEffect, BottomSheetEvent>() {

    override val getViewBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOnHoldBottomSheetBinding = { inflater, viewGroup, value ->
        FragmentOnHoldBottomSheetBinding.inflate(inflater, viewGroup, value)
    }

    override fun getViewModelClass(): Class<BottomSheetViewModel> = BottomSheetViewModel::class.java
}