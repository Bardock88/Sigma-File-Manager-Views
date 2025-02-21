package me.safarov399.home.bottom_sheet

import dagger.hilt.android.lifecycle.HiltViewModel
import me.safarov399.core.base.BaseViewModel
import me.safarov399.domain.repo.AbstractFileFolderOperationRepository
import javax.inject.Inject

@HiltViewModel
class BottomSheetViewModel @Inject constructor(
    private val repository: AbstractFileFolderOperationRepository
) : BaseViewModel<BottomSheetState, BottomSheetEffect, BottomSheetEvent>() {
    override fun getInitialState(): BottomSheetState = BottomSheetState()

    override fun onEventUpdate(event: BottomSheetEvent) {
        when (event) {
            is BottomSheetEvent.Compress -> {
                repository.compress(event.paths)
            }

            is BottomSheetEvent.Properties -> {
                repository.properties(event.path)
            }

            is BottomSheetEvent.Open -> {

            }

            is BottomSheetEvent.Share -> {

            }

            is BottomSheetEvent.AddToFavorites -> {

            }

            is BottomSheetEvent.ChangePermissions -> {
                repository.changePermissions(event.path)
            }

            is BottomSheetEvent.CheckDependencies -> {
                repository.checkDependencies(event.path)
            }

            is BottomSheetEvent.Checksum -> {
                repository.checksum(event.path)
            }


            is BottomSheetEvent.CreateShortcut -> {

            }

            is BottomSheetEvent.Delete -> {
                val success = repository.delete(event.paths)
                if(!success) {
                    postEffect(BottomSheetEffect.DeletionFailed)
                }
            }

            is BottomSheetEvent.Extract -> {
                repository.extract(event.path)
            }

            is BottomSheetEvent.Install -> {

            }

            is BottomSheetEvent.Move -> {
                repository.move(event.items, event.oldPath, event.newPath)
            }

            is BottomSheetEvent.OpenWith -> {

            }

            is BottomSheetEvent.Rename -> {
                repository.rename(event.path, event.newName)
            }

            is BottomSheetEvent.Shred -> {
                repository.shred(event.paths)
            }

            is BottomSheetEvent.VerifySignature -> {
                repository.verifySignature(event.path)
            }

            is BottomSheetEvent.View -> {

            }
        }
    }
}