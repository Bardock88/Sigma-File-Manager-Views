package me.safarov399.home

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import me.safarov399.common.file.FileConstants.ASCENDING_ORDER
import me.safarov399.common.file.FileConstants.DATE_SORTING_TYPE
import me.safarov399.common.file.FileConstants.DESCENDING_ORDER
import me.safarov399.common.file.FileConstants.FILE_TYPE
import me.safarov399.common.file.FileConstants.FOLDER_TYPE
import me.safarov399.common.file.FileConstants.NAME_SORTING_TYPE
import me.safarov399.common.file.FileConstants.SIZE_SORTING_TYPE
import me.safarov399.common.file.FileConstants.TYPE_SORTING_TYPE
import me.safarov399.common.file.FileExtensions.APK_FILE
import me.safarov399.common.file.FileExtensions.ARCHIVING_ALGORITHMS
import me.safarov399.common.file.FileExtensions.COMPRESSION_ALGORITHMS
import me.safarov399.common.file.FileExtensions.COMPRESSION_AND_ARCHIVE
import me.safarov399.core.adapter.FileFolderAdapter
import me.safarov399.core.base.BaseFragment
import me.safarov399.core.file.FileHandler
import me.safarov399.core.file.OperationModel
import me.safarov399.core.file.OperationTypes.COPY
import me.safarov399.core.file.OperationTypes.NORMAL
import me.safarov399.core.file.OperationTypes.RENAME
import me.safarov399.core.listeners.BottomSheetResultListener
import me.safarov399.core.listeners.OnClickListener
import me.safarov399.core.listeners.OnHoldListener
import me.safarov399.core.navigation.NavigationDestinations.APK_OPERATIONS_CODE
import me.safarov399.core.navigation.NavigationDestinations.ARCHIVE_OPERATIONS_CODE
import me.safarov399.core.navigation.NavigationDestinations.FILE_OPERATIONS_CODE
import me.safarov399.core.navigation.NavigationDestinations.FOLDER_OPERATIONS_CODE
import me.safarov399.core.permission.PermissionConstants
import me.safarov399.core.permission.PermissionManager
import me.safarov399.core.storage.StorageConstants.DEFAULT_DIRECTORY
import me.safarov399.core.storage.StorageConstants.OPERATION_ACCESS_DENIED_DIRECTORIES
import me.safarov399.core.storage.StorageConstants.RESTRICTED_DIRECTORIES
import me.safarov399.domain.models.adapter.FileFolderModel
import me.safarov399.domain.models.adapter.FileModel
import me.safarov399.domain.models.adapter.FolderModel
import me.safarov399.home.bottom_sheet.BottomSheetFragment
import me.safarov399.home.databinding.FragmentHomeBinding
import me.safarov399.uikit.custom_views.dialogs.DialogProvider
import me.safarov399.uikit.custom_views.dialogs.EditTextDialog
import me.safarov399.uikit.custom_views.dialogs.OnHoldBottomSheetDialog


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel, HomeUiState, HomeEffect, HomeEvent>(), BottomSheetResultListener {

    private var fileFolderAdapter: FileFolderAdapter? = null
    private var rv: RecyclerView? = null

    private var backPressCallback: OnBackPressedCallback? = null
    private var currentPath = DEFAULT_DIRECTORY

    private var isClickable = true
    private var areAllFabVisible = false
    private var isAscending = true
    private var sortType: Int = NAME_SORTING_TYPE
    private var operationMode: Int = NORMAL
    private var operationFiles: List<String>? = null


    private val requestAndroid10AndBelowPermissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        for (permission in permissions) {
            if (!permission.value) {
                break
            }
        }

        if (!PermissionManager.checkStoragePermissions(this)) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) || !shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                DialogProvider.goToSettingsDialog(this) { dialog ->
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + requireActivity().packageName)
                    )
                    startActivity(intent)
                    dialog.dismiss()
                    activity?.finish()
                }
            } else {
                DialogProvider.showPermissionRequestDialog(this) { dialog ->
                    dialog.dismiss()
                    requestStoragePermission()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private val requestAndroid11AndHigherPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        if (!Environment.isExternalStorageManager()) {
            DialogProvider.showPermissionRequestDialog(this) { dialog ->
                dialog.dismiss()
                requestStoragePermission()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureViews()
        if (!PermissionManager.checkStoragePermissions(this)) {
            DialogProvider.showPermissionRequestDialog(this) { dialog ->
                dialog.dismiss()
                requestStoragePermission()
            }
        }
        handleBackPress()
    }


    override fun onResume() {
        super.onResume()
        if (PermissionManager.checkStoragePermissions(this)) {
            postEvent(HomeEvent.ChangePath(currentPath))
        }
    }


    override fun onEffectUpdate(effect: HomeEffect) {
        when (effect) {
            HomeEffect.FileAlreadyExists -> Toast.makeText(requireActivity(), getString(me.safarov399.common.R.string.file_already_exists), Toast.LENGTH_SHORT).show()
            is HomeEffect.FileCreated -> Toast.makeText(requireActivity(), getString(me.safarov399.common.R.string.file_created, effect.name, effect.path), Toast.LENGTH_LONG).show()
            HomeEffect.FolderAlreadyExists -> Toast.makeText(requireActivity(), getString(me.safarov399.common.R.string.folder_already_exists), Toast.LENGTH_SHORT).show()
            is HomeEffect.FolderCreated -> Toast.makeText(requireActivity(), getString(me.safarov399.common.R.string.folder_created, effect.name, effect.path), Toast.LENGTH_LONG).show()
            is HomeEffect.CopiedFileAlreadyExist -> {
                DialogProvider.confirmCopyOverwriteDialog(this, onConfirm = {
                    postEvent(HomeEvent.Copy(effect.operationFiles, effect.newPath, true))
                }, onCancel = {
                    postEvent(HomeEvent.SwitchOperationMode())
                })
            }

            HomeEffect.CopySuccessful -> {
                postEvent(HomeEvent.ChangePath(currentPath))
                switchNormalMode()
                Toast.makeText(requireActivity(), getString(me.safarov399.common.R.string.copied_to_path, getString(me.safarov399.common.R.string.copied_to), currentPath), Toast.LENGTH_SHORT).show()
            }

            HomeEffect.CopyingIntoItself -> Toast.makeText(requireActivity(), getString(me.safarov399.common.R.string.cannot_copy_into_itself), Toast.LENGTH_SHORT).show()
            HomeEffect.DoesNotExist -> Toast.makeText(requireActivity(), getString(me.safarov399.common.R.string.file_does_not_exist), Toast.LENGTH_SHORT).show()
        }
    }


    override fun onStateUpdate(state: HomeUiState) {
        if (operationMode == COPY) {
            switchCopyMode()
        }
        operationFiles = state.operationModel.files
        currentPath = state.currentPath
        sortType = state.sortType
        backPressCallback?.isEnabled = currentPath != DEFAULT_DIRECTORY
        fileFolderAdapter?.submitList(state.currentFileFolders) {
            rv?.post {
                rv?.scrollToPosition(0)
            }
        }
        isAscending = state.isAscending

        when (this.operationMode) {
            COPY -> {
                fileFolderAdapter?.setOnClickListener(object : OnClickListener {
                    override fun onClickFileFolder(position: Int, model: FileFolderModel) {
                        folderClick(model, state)
                    }
                }, object : OnClickListener {
                    override fun onClickFileFolder(position: Int, model: FileFolderModel) {}
                })

                fileFolderAdapter?.setOnHoldListener(
                    object : OnHoldListener {
                        override fun onHoldFileFolder(position: Int, model: FileFolderModel) {}
                    }, object : OnHoldListener {
                        override fun onHoldFileFolder(position: Int, model: FileFolderModel) {}
                    }
                )
            }

            NORMAL -> {
                fileFolderAdapter?.setOnClickListener(object : OnClickListener {
                    override fun onClickFileFolder(position: Int, model: FileFolderModel) {
                        folderClick(model, state)
                    }
                }, object : OnClickListener {
                    override fun onClickFileFolder(position: Int, model: FileFolderModel) {
                        fileClick(model, state)
                    }
                })

                fileFolderAdapter?.setOnHoldListener(
                    object : OnHoldListener {
                        override fun onHoldFileFolder(position: Int, model: FileFolderModel) {
                            folderLongPress(model, state)
                        }
                    }, object : OnHoldListener {
                        override fun onHoldFileFolder(position: Int, model: FileFolderModel) {
                            fileLongPress(model, state)
                        }
                    }
                )
            }
        }

        binding.homeNavUp.setOnClickListener {
            navigateUp(state)
        }

        binding.homeCancelIv.setOnClickListener {
            switchNormalMode()
        }

        binding.homePasteIv.setOnClickListener {
            postEvent(HomeEvent.Copy(operationFiles!!, state.currentPath, false))
        }
    }

    private fun fileLongPress(model: FileFolderModel, state: HomeUiState) {
        val fragment = BottomSheetFragment()
        val fileExtension = if ((model as FileModel).name.contains(".")) {
            (model).name.substringAfterLast(".")
        } else ""
        when (fileExtension) {
            APK_FILE -> fragment.setOperationsType(APK_OPERATIONS_CODE)
            in ARCHIVING_ALGORITHMS -> fragment.setOperationsType(ARCHIVE_OPERATIONS_CODE)
            in COMPRESSION_ALGORITHMS -> fragment.setOperationsType(ARCHIVE_OPERATIONS_CODE)
            in COMPRESSION_AND_ARCHIVE -> fragment.setOperationsType(ARCHIVE_OPERATIONS_CODE)
            else -> fragment.setOperationsType(FILE_OPERATIONS_CODE)
        }
        fragment.apply {
            setFileName(model.name)
            setFilePath(currentPath)
            setType(FILE_TYPE)
            setResultListener(this@HomeFragment)
        }
        val bottomSheet = OnHoldBottomSheetDialog(fragmentFactory = { fragment }, dismissListener = { postEvent(HomeEvent.ChangePath(state.currentPath)) })


        if (!bottomSheet.isAdded) {
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        }
    }

    private fun folderLongPress(model: FileFolderModel, state: HomeUiState) {
        val fragment = BottomSheetFragment()
        fragment.apply {
            setFileName((model as FolderModel).name)
            setOperationsType(FOLDER_OPERATIONS_CODE)
            setFilePath(currentPath)
            setType(FOLDER_TYPE)
            setResultListener(this@HomeFragment)
        }
        val bottomSheet = OnHoldBottomSheetDialog(
            fragmentFactory = { fragment },
            dismissListener = {
                postEvent(HomeEvent.ChangePath(state.currentPath))
            }
        )

        if (!bottomSheet.isAdded) {
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        }
    }

    private fun fileClick(model: FileFolderModel, state: HomeUiState) {
        hideFab()

        if (!isClickable) return // Ignore clicks if interaction is disabled
        isClickable = false
        FileHandler.openFile(state.currentPath, (model as FileModel).name, this@HomeFragment, false)
        isClickable = true // Reset the flag after handling the intent
    }

    private fun folderClick(model: FileFolderModel, state: HomeUiState) {
        hideFab()

        if (!isClickable) return // Ignore clicks if interaction is disabled
        isClickable = false

        val folderName = (model as FolderModel).name
        val temporaryCurrentPath = if (state.currentPath.endsWith("/")) {
            state.currentPath + folderName
        } else {
            "${state.currentPath}/$folderName"
        }

        if (temporaryCurrentPath !in RESTRICTED_DIRECTORIES) {
            postEvent(HomeEvent.ChangePath(temporaryCurrentPath))
            state.currentPath = temporaryCurrentPath
        }
        binding.pathTv.text = state.currentPath

        currentPath = state.currentPath

        // Re-enable interaction after a short delay
        binding.root.postDelayed({ isClickable = true }, 500)
    }

    private fun switchCopyMode() {
        binding.apply {
            homeCancelIv.visibility = VISIBLE
            homePasteIv.visibility = VISIBLE
        }
    }

    private fun switchNormalMode() {
        this.operationMode = NORMAL
        binding.apply {
            homeCancelIv.visibility = GONE
            homePasteIv.visibility = GONE
        }
        postEvent(HomeEvent.SwitchOperationMode())
    }

    private fun navigateUp(state: HomeUiState) {
        if (state.currentPath != DEFAULT_DIRECTORY) {
            hideFab()
            val nextPath = state.currentPath.substringBeforeLast("/")
            postEvent(
                HomeEvent.ChangePath(
                    nextPath
                )
            )
            binding.pathTv.text = nextPath
        }
    }


    private fun configureViews() {
        rv = binding.homeRv
        fileFolderAdapter = FileFolderAdapter()
        rv?.adapter = fileFolderAdapter
        hideFab()

        binding.apply {
            homeCreateFileFab.imageTintList = null
            homeCreateFolderFab.imageTintList = null

            pathTv.text = DEFAULT_DIRECTORY

            homeCreateEfab.setOnClickListener {
                if (areAllFabVisible) {
                    hideFab()
                } else {
                    showFab()
                }
            }

            homeCreateFolderFab.setOnClickListener {
                showCreateFileFolderDialog(FOLDER_TYPE)
            }

            homeCreateFileFab.setOnClickListener {
                showCreateFileFolderDialog(FILE_TYPE)
            }
            homeThreeDotsIv.setOnClickListener {
                showSortingPopup(it)
            }
        }
    }


    private fun configureSortType(sortType: Int, popup: PopupMenu) {
        when (sortType) {
            NAME_SORTING_TYPE -> {
                popup.menu.apply {
                    findItem(me.safarov399.common.R.id.sort_name).isChecked = true
                    findItem(me.safarov399.common.R.id.sort_date).isChecked = false
                    findItem(me.safarov399.common.R.id.sort_size).isChecked = false
                    findItem(me.safarov399.common.R.id.sort_type).isChecked = false
                }
            }

            DATE_SORTING_TYPE -> {
                popup.menu.apply {
                    findItem(me.safarov399.common.R.id.sort_name).isChecked = false
                    findItem(me.safarov399.common.R.id.sort_date).isChecked = true
                    findItem(me.safarov399.common.R.id.sort_size).isChecked = false
                    findItem(me.safarov399.common.R.id.sort_type).isChecked = false
                }
            }

            SIZE_SORTING_TYPE -> {
                popup.menu.apply {
                    findItem(me.safarov399.common.R.id.sort_name).isChecked = false
                    findItem(me.safarov399.common.R.id.sort_date).isChecked = false
                    findItem(me.safarov399.common.R.id.sort_size).isChecked = true
                    findItem(me.safarov399.common.R.id.sort_type).isChecked = false
                }
            }

            TYPE_SORTING_TYPE -> {
                popup.menu.apply {
                    findItem(me.safarov399.common.R.id.sort_name).isChecked = false
                    findItem(me.safarov399.common.R.id.sort_date).isChecked = false
                    findItem(me.safarov399.common.R.id.sort_size).isChecked = false
                    findItem(me.safarov399.common.R.id.sort_type).isChecked = true
                }
            }
        }
    }


    private fun configureSortOrder(isAscending: Boolean, popup: PopupMenu) {
        popup.menu.apply {
            findItem(me.safarov399.common.R.id.sort_ascending).isChecked = isAscending
            findItem(me.safarov399.common.R.id.sort_descending).isChecked = !isAscending
        }
    }


    private fun showSortingPopup(view: View) {
        val popup = PopupMenu(requireActivity(), view)
        val popupMenuInflater = popup.menuInflater
        popupMenuInflater.inflate(me.safarov399.common.R.menu.sort_menu, popup.menu)

        val headerItem = popup.menu.findItem(me.safarov399.common.R.id.sort_header)
        val headerTitle = SpannableString(getString(me.safarov399.common.R.string.sorting_options))
        headerTitle.setSpan(StyleSpan(Typeface.BOLD), 0, headerTitle.length, 0)
        headerTitle.setSpan(ForegroundColorSpan(Color.WHITE), 0, headerTitle.length, 0)
        headerItem.title = headerTitle
        headerItem.isEnabled = false  // Make it non-clickable

        configureSortOrder(isAscending, popup)
        configureSortType(sortType, popup)

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                me.safarov399.common.R.id.sort_name -> {
                    sortType = NAME_SORTING_TYPE
                    configureSortType(sortType, popup)
                    postEvent(HomeEvent.ChangeSortType(sortType))
                    postEvent(HomeEvent.ChangePath(currentPath))
                }

                me.safarov399.common.R.id.sort_date -> {
                    sortType = DATE_SORTING_TYPE
                    configureSortType(sortType, popup)
                    postEvent(HomeEvent.ChangeSortType(sortType))
                    postEvent(HomeEvent.ChangePath(currentPath))
                }

                me.safarov399.common.R.id.sort_size -> {
                    sortType = SIZE_SORTING_TYPE
                    configureSortType(sortType, popup)
                    postEvent(HomeEvent.ChangeSortType(sortType))
                    postEvent(HomeEvent.ChangePath(currentPath))
                }

                me.safarov399.common.R.id.sort_type -> {
                    sortType = TYPE_SORTING_TYPE
                    configureSortType(sortType, popup)
                    postEvent(HomeEvent.ChangeSortType(sortType))
                    postEvent(HomeEvent.ChangePath(currentPath))
                }

                me.safarov399.common.R.id.sort_ascending -> {
                    isAscending = true
                    configureSortOrder(true, popup)
                    postEvent(HomeEvent.ChangeSortOrder(ASCENDING_ORDER))
                    postEvent(HomeEvent.ChangePath(currentPath))
                }

                me.safarov399.common.R.id.sort_descending -> {
                    isAscending = false
                    configureSortOrder(false, popup)
                    postEvent(HomeEvent.ChangeSortOrder(DESCENDING_ORDER))
                    postEvent(HomeEvent.ChangePath(currentPath))
                }
            }
            true
        }
        popup.show()
    }


    private fun hideFab() {
        binding.apply {
            homeCreateFileFab.hide()
            homeCreateFolderFab.hide()
            homeCreateFolderTv.visibility = GONE
            homeCreateFileTv.visibility = GONE
            homeCreateEfab.shrink()
        }
        areAllFabVisible = false
    }


    private fun showFab() {
        binding.apply {
            homeCreateFileFab.show()
            homeCreateFolderFab.show()
            homeCreateFolderTv.visibility = VISIBLE
            homeCreateFileTv.visibility = VISIBLE
            homeCreateEfab.extend()
        }
        areAllFabVisible = true
    }


    private fun handleBackPress() {
        backPressCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (currentPath != DEFAULT_DIRECTORY) {
                    postEvent(
                        HomeEvent.ChangePath(
                            currentPath.substringBeforeLast("/")
                        )
                    )
                    binding.pathTv.text = currentPath.substringBeforeLast("/")
                } else {
                    isEnabled = false // Allow the system to handle back press
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backPressCallback as OnBackPressedCallback)
    }


    private fun showCreateFileFolderDialog(itemType: Int) {
        EditTextDialog(requireActivity()).apply {
            if (itemType == FILE_TYPE) {
                setTitle(getString(me.safarov399.common.R.string.create_new_file))
                setHint(getString(me.safarov399.common.R.string.enter_file_name))
                setConfirmAction {
                    val fileToCreate = findViewById<TextInputEditText>(me.safarov399.uikit.R.id.cff_name_tiet).text.toString()
                    postEvent(HomeEvent.CreateObject(fileToCreate, currentPath, FILE_TYPE))
                    dismiss()

                    /**
                     * The commented out code below is a safe-guard mechanism to prevent users from creating files with only spaces or newlines. However, since it has a possibility of not being optimal for some power users (for whatever reason), I am not going to implement it at the moment. Yet, for the sake of someone who may not like such behaviour and want to turn it on but does not want to construct the logic to do so, just comment out the last two lines and uncomment the code below. I will create a toggle in settings screen to switch this behaviour on and off later on.
                     */
//                    if (fileToCreate.isNotBlank()) {
//                        postEvent(HomeEvent.CreateObject(fileToCreate, currentPath, FILE_TYPE))
//                        dismiss()
//                    } else {
//                        Toast.makeText(requireActivity(), getString(me.safarov399.common.R.string.file_name_must_not_empty), Toast.LENGTH_SHORT).show()
//                    }
                }
            } else {
                setTitle(getString(me.safarov399.common.R.string.create_new_folder))
                setHint(getString(me.safarov399.common.R.string.enter_folder_name))
                setConfirmAction {
                    val fileToCreate = findViewById<TextInputEditText>(me.safarov399.uikit.R.id.cff_name_tiet).text.toString()
                    postEvent(HomeEvent.CreateObject(fileToCreate, currentPath, FOLDER_TYPE))
                    dismiss()

                    /**
                     * The commented out code below is a safe-guard mechanism to prevent users from creating folders with only spaces or newlines. However, since it has a possibility of not being optimal for some power users (for whatever reason), I am not going to implement it at the moment. Yet, for the sake of someone who may not like such behaviour and want to turn it on but does not want to construct the logic to do so, just comment out the last two lines and uncomment the code below. I will create a toggle in settings screen to switch this behaviour on and off later on.
                     */
//                    if (fileToCreate.isNotBlank()) {
//                        postEvent(HomeEvent.CreateObject(fileToCreate, currentPath, FOLDER_TYPE))
//                        dismiss()
//                    } else {
//                        Toast.makeText(requireActivity(), getString(me.safarov399.common.R.string.folder_name_must_not_empty), Toast.LENGTH_SHORT).show()
//                    }
                }
            }
            setCancelAction {
                dismiss()
            }
            show()
        }
    }


    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requestStoragePermissionAndroid11AndHigher()
        } else {
            requestAndroid10AndBelowPermissionsLauncher.launch(PermissionConstants.requiredPermissionsAndroid10AndBelow)
        }
    }


    @RequiresApi(Build.VERSION_CODES.R)
    private fun requestStoragePermissionAndroid11AndHigher() {
        val uri = Uri.fromParts("package", requireActivity().packageName, null)
        val intent = Intent(
            Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri
        )
        requestAndroid11AndHigherPermissionLauncher.launch(intent)
    }


    override fun getViewModelClass(): Class<HomeViewModel> = HomeViewModel::class.java

    override val getViewBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding = { inflater, viewGroup, value ->
        FragmentHomeBinding.inflate(inflater, viewGroup, value)
    }

    override fun onDestroy() {
        super.onDestroy()
        rv = null
        fileFolderAdapter = null
        backPressCallback = null
    }

    override fun onBottomSheetResult(operationModel: OperationModel) {
        when (operationModel.operationType) {
            COPY -> {
                for (file in operationModel.files!!) {
                    if (file in OPERATION_ACCESS_DENIED_DIRECTORIES) {
                        Toast.makeText(requireActivity(), getString(me.safarov399.common.R.string.cannot_copy_requires_root), Toast.LENGTH_SHORT).show()
                        break
                    } else {
                        this.operationMode = COPY
                        this.operationFiles = operationModel.files
                        postEvent(HomeEvent.SwitchOperationMode(operationModel))
                        Toast.makeText(requireActivity(), me.safarov399.common.R.string.select_destination_folder, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            RENAME -> {

                if (operationModel.file!! in OPERATION_ACCESS_DENIED_DIRECTORIES) {
                    Toast.makeText(requireActivity(), me.safarov399.common.R.string.cannot_rename_requires_root, Toast.LENGTH_SHORT).show()
                } else {
                    DialogProvider.renameDialog(this, operationModel.file!!.substringAfterLast("/")) {
                        postEvent(HomeEvent.Rename(operationModel.file!!, it.getText()))
                        postEvent(HomeEvent.ChangePath(currentPath))
                    }

                }
            }
        }
    }
}