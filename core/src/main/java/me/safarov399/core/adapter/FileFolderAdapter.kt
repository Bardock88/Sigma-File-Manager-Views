package me.safarov399.core.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.safarov399.domain.models.adapter.FileFolderModel
import me.safarov399.uikit.databinding.FileTileBinding
import me.safarov399.uikit.databinding.FolderTileBinding
import me.safarov399.domain.models.adapter.FileModel
import me.safarov399.domain.models.adapter.FolderModel

class FileFolderAdapter : ListAdapter<FileFolderModel, RecyclerView.ViewHolder>(FileFolderDiffUtilCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            FILE_VIEW -> {
                FileViewHolder(
                    binding = FileTileBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
            FOLDER_VIEW -> {
                FolderViewHolder(
                    binding = FolderTileBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
            else -> throw IllegalArgumentException("Invalid type of view type $viewType at")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(val item = getItem(position)) {
            is FileModel -> (holder as FileViewHolder).bind(item)
            is FolderModel -> (holder as FolderViewHolder).bind(item)
            else -> throw IllegalArgumentException("Invalid holder type for position $position")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is FileModel -> FILE_VIEW
            is FolderModel -> FOLDER_VIEW
            else -> throw IllegalArgumentException("Invalid data type at position $position")
        }
    }

    internal class FileViewHolder(private val binding: FileTileBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(fileModel: FileModel) {
            binding.fileTileTitleTv.text = fileModel.name
            binding.fileSizeTv.text = fileModel.size.toString() + " bytes"
        }
    }

    internal class FolderViewHolder(private val binding: FolderTileBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(folderModel: FolderModel) {
            binding.folderTileTitleTv.text = folderModel.name
            binding.folderItemCountTv.text = folderModel.itemCount.toString() + " items"
        }
    }

    class FileFolderDiffUtilCallback : DiffUtil.ItemCallback<FileFolderModel>() {
        override fun areItemsTheSame(oldItem: FileFolderModel, newItem: FileFolderModel): Boolean {
            return when {
                oldItem is FileModel && newItem is FileModel -> oldItem.name == newItem.name
                oldItem is FolderModel && newItem is FolderModel -> oldItem.name == newItem.name
                else -> false
            }
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: FileFolderModel, newItem: FileFolderModel): Boolean {
            return when {
                oldItem is FileModel && newItem is FileModel -> oldItem == newItem
                oldItem is FolderModel && newItem is FolderModel -> oldItem == newItem
                else -> false
            }
        }

    }

    private companion object {
        private const val FILE_VIEW = 0
        private const val FOLDER_VIEW = 1
    }
}