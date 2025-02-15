package me.safarov399.home.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import me.safarov399.common.file.FileConstants.FILE_TYPE
import me.safarov399.core.adapter.BottomSheetAdapter
import me.safarov399.core.base.BaseFragment
import me.safarov399.core.file.FileFolderOperationsConstants.APK_OPERATIONS_LIST
import me.safarov399.core.file.FileFolderOperationsConstants.ARCHIVE_OPERATIONS
import me.safarov399.core.file.FileFolderOperationsConstants.FILE_OPERATIONS_LIST
import me.safarov399.core.file.FileFolderOperationsConstants.FOLDER_OPERATIONS_LIST
import me.safarov399.core.file.FileHandler
import me.safarov399.core.file.fileAndPathMerger
import me.safarov399.core.listeners.OnClickListener
import me.safarov399.core.navigation.NavigationDestinations.APK_OPERATIONS_CODE
import me.safarov399.core.navigation.NavigationDestinations.ARCHIVE_OPERATIONS_CODE
import me.safarov399.core.navigation.NavigationDestinations.FILE_OPERATIONS_CODE
import me.safarov399.core.navigation.NavigationDestinations.FOLDER_OPERATIONS_CODE
import me.safarov399.domain.models.adapter.OnHoldModel
import me.safarov399.home.databinding.FragmentOnHoldBottomSheetBinding

@AndroidEntryPoint
class BottomSheetFragment : BaseFragment<FragmentOnHoldBottomSheetBinding, BottomSheetViewModel, BottomSheetState, BottomSheetEffect, BottomSheetEvent>() {

    private var operationsType: Int = 0
    private var adapter: BottomSheetAdapter? = null
    private var fileName: String? = null
    private var filePath: String? = null
    private var type: Int? = null

    fun setFileName(name: String) {
        this.fileName = name
    }

    fun setFilePath(path: String) {
        this.filePath = path
    }

    fun setOperationsType(type: Int) {
        this.operationsType = type
    }

    fun setType(type: Int) {
        this.type = type
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
        binding.typeIv.setImageResource(
            if (type == FILE_TYPE) {
                me.safarov399.uikit.R.drawable.file
            } else me.safarov399.uikit.R.drawable.folder
        )

        adapter?.setOnClickListener(object : OnClickListener {
            override fun onClickBottomSheetItem(position: Int, model: OnHoldModel) {
                when (model.title) {
                    me.safarov399.common.R.string.open -> FileHandler.openFile(filePath!!, fileName!!, this@BottomSheetFragment, false)
                    me.safarov399.common.R.string.open_with -> FileHandler.openFile(filePath!!, fileName!!, this@BottomSheetFragment, true)
                    me.safarov399.common.R.string.view -> postEvent(BottomSheetEvent.View(fileAndPathMerger(fileName!!, filePath!!)))
                    me.safarov399.common.R.string.install -> FileHandler.installApk(filePath!!, fileName!!, this@BottomSheetFragment)
                    me.safarov399.common.R.string.verify_signature -> postEvent(BottomSheetEvent.VerifySignature(fileAndPathMerger(fileName!!, filePath!!)))
                    me.safarov399.common.R.string.check_dependencies -> postEvent(BottomSheetEvent.CheckDependencies(fileAndPathMerger(fileName!!, filePath!!)))
                    me.safarov399.common.R.string.properties -> postEvent(BottomSheetEvent.Properties(fileAndPathMerger(fileName!!, filePath!!)))
                    me.safarov399.common.R.string.share -> postEvent(BottomSheetEvent.Share(fileAndPathMerger(fileName!!, filePath!!)))
                    me.safarov399.common.R.string.compress -> postEvent(BottomSheetEvent.Compress(listOf(fileAndPathMerger(fileName!!, filePath!!))))
                    me.safarov399.common.R.string.delete -> {
                        postEvent(BottomSheetEvent.Delete(listOf(fileAndPathMerger(fileName!!, filePath!!))))
                        (requireParentFragment() as? BottomSheetDialogFragment)?.dismiss()
                    }

                    me.safarov399.common.R.string.shred -> postEvent(BottomSheetEvent.Shred(listOf(fileAndPathMerger(fileName!!, filePath!!)), 0))
                    me.safarov399.common.R.string.rename -> postEvent(BottomSheetEvent.Rename(filePath!!, fileName!!, ""))
                    me.safarov399.common.R.string.copy -> postEvent(BottomSheetEvent.Copy(listOf(fileAndPathMerger(fileName!!, filePath!!)), filePath!!, ""))
                    me.safarov399.common.R.string.move_to -> postEvent(BottomSheetEvent.Move(listOf(fileAndPathMerger(fileName!!, filePath!!)), filePath!!, ""))
                    me.safarov399.common.R.string.add_to_favorites -> postEvent(BottomSheetEvent.AddToFavorites(listOf(fileAndPathMerger(fileName!!, filePath!!))))
                    me.safarov399.common.R.string.extract -> postEvent(BottomSheetEvent.Extract(fileAndPathMerger(fileName!!, filePath!!), ""))
                    me.safarov399.common.R.string.create_shortcut -> postEvent(BottomSheetEvent.CreateShortcut(fileAndPathMerger(fileName!!, filePath!!)))
                    me.safarov399.common.R.string.checksum -> postEvent(BottomSheetEvent.Checksum(fileAndPathMerger(fileName!!, filePath!!)))
                    me.safarov399.common.R.string.change_permissions -> postEvent(BottomSheetEvent.ChangePermissions(fileAndPathMerger(fileName!!, filePath!!)))
                }
            }
        })
    }


    override fun onEffectUpdate(effect: BottomSheetEffect) {
        when (effect) {
            BottomSheetEffect.DeletionFailed -> Toast.makeText(requireActivity(), getString(me.safarov399.common.R.string.deletion_failure), Toast.LENGTH_SHORT).show()
        }
    }

    override val getViewBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOnHoldBottomSheetBinding = { inflater, viewGroup, value ->
        FragmentOnHoldBottomSheetBinding.inflate(inflater, viewGroup, value)
    }

    override fun getViewModelClass(): Class<BottomSheetViewModel> = BottomSheetViewModel::class.java
}