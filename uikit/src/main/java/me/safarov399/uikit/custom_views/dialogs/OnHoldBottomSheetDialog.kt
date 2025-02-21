package me.safarov399.uikit.custom_views.dialogs

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import me.safarov399.uikit.databinding.OnHoldBottomSheetDialogBinding

class OnHoldBottomSheetDialog(
    private val fragmentFactory: (() -> Fragment)? = null,
    private val dismissListener: (() -> Unit)? = null
) : BottomSheetDialogFragment() {
    private var binding: OnHoldBottomSheetDialogBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = OnHoldBottomSheetDialogBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (fragmentFactory == null) {
            dismiss()

        } else {
            binding?.apply {
                childFragmentManager.beginTransaction().apply {
                    add(onHoldBottomSheetFragment.id, fragmentFactory.invoke())
                }.commit()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissListener?.invoke()
    }
}