package me.safarov399.home.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import me.safarov399.core.file.FileFolderOperationsConstants.APK_OPERATIONS_LIST
import me.safarov399.core.file.FileFolderOperationsConstants.ARCHIVE_OPERATIONS
import me.safarov399.core.file.FileFolderOperationsConstants.FILE_OPERATIONS_LIST
import me.safarov399.core.file.FileFolderOperationsConstants.FOLDER_OPERATIONS_LIST
import me.safarov399.core.adapter.BottomSheetAdapter
import me.safarov399.core.base.BaseFragment
import me.safarov399.core.navigation.NavigationDestinations.APK_OPERATIONS_CODE
import me.safarov399.core.navigation.NavigationDestinations.ARCHIVE_OPERATIONS_CODE
import me.safarov399.core.navigation.NavigationDestinations.FILE_OPERATIONS_CODE
import me.safarov399.core.navigation.NavigationDestinations.FOLDER_OPERATIONS_CODE
import me.safarov399.home.databinding.FragmentOnHoldBottomSheetBinding

@AndroidEntryPoint
class BottomSheetFragment : BaseFragment<FragmentOnHoldBottomSheetBinding, BottomSheetViewModel, BottomSheetState, BottomSheetEffect, BottomSheetEvent>() {

    private var operationsType: Int = 0
    private var adapter: BottomSheetAdapter? = null
    private var fileName: String? = null
    private var filePath: String? = null

    fun setFileName(name: String) {
        this.fileName = name
    }

    fun setFilePath(path: String) {
        this.filePath = path
    }

    fun setOperationsType(type: Int) {
        this.operationsType = type
    }

    private fun setRecyclerViewList() {
        when (operationsType) {
            FILE_OPERATIONS_CODE -> {
                adapter?.submitList(FILE_OPERATIONS_LIST)
            }

            APK_OPERATIONS_CODE -> {
                adapter?.submitList(APK_OPERATIONS_LIST)
            }

            ARCHIVE_OPERATIONS_CODE -> {
                adapter?.submitList(ARCHIVE_OPERATIONS)
            }

            FOLDER_OPERATIONS_CODE -> {
                adapter?.submitList(FOLDER_OPERATIONS_LIST)
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = BottomSheetAdapter()
        setRecyclerViewList()
        binding.bottomRv.adapter = adapter
        binding.bottomSheetTitleTv.text = fileName
    }

    override val getViewBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOnHoldBottomSheetBinding = { inflater, viewGroup, value ->
        FragmentOnHoldBottomSheetBinding.inflate(inflater, viewGroup, value)
    }

    override fun getViewModelClass(): Class<BottomSheetViewModel> = BottomSheetViewModel::class.java
}