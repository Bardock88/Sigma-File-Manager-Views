package me.safarov399.core.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.safarov399.domain.models.adapter.FileFolderModel
import me.safarov399.domain.models.adapter.FileModel
import me.safarov399.domain.models.adapter.FolderModel
import me.safarov399.uikit.databinding.FileTileBinding
import me.safarov399.uikit.databinding.FolderTileBinding
import java.util.Locale

class FileFolderAdapter : ListAdapter<FileFolderModel, RecyclerView.ViewHolder>(FileFolderDiffUtilCallback()) {

    private var onFolderClickListener: OnClickListener? = null
    private var onFileClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            FILE_VIEW -> {
                FileViewHolder(
                    binding = FileTileBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ), parent.context
                )
            }

            FOLDER_VIEW -> {
                FolderViewHolder(
                    binding = FolderTileBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ), parent.context
                )
            }

            else -> throw IllegalArgumentException("Invalid type of view type $viewType at")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is FileModel -> {
                (holder as FileViewHolder).bind(item)
                holder.itemView.setOnClickListener {
                    onFileClickListener?.onClick(position, item)
                }
            }

            is FolderModel -> {
                (holder as FolderViewHolder).bind(item)
                holder.itemView.setOnClickListener {
                    onFolderClickListener?.onClick(position, item)
                }
            }

            else -> throw IllegalArgumentException("Invalid holder type for position $position")
        }
    }

    fun setOnClickListener(folderListener: OnClickListener?, fileListener: OnClickListener) {
        this.onFolderClickListener = folderListener
        this.onFileClickListener = fileListener
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is FileModel -> FILE_VIEW
            is FolderModel -> FOLDER_VIEW
            else -> throw IllegalArgumentException("Invalid data type at position $position")
        }
    }

    internal class FileViewHolder(private val binding: FileTileBinding, private val ctx: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bind(fileModel: FileModel) {
            binding.fileTileTitleTv.text = fileModel.name
            val size = String.format(Locale.getDefault(), "%,d", fileModel.size)
            val sizeText = ctx.getString(me.safarov399.common.R.string.file_size, size, ctx.getString(me.safarov399.common.R.string.size))
            binding.fileSizeTv.text = sizeText
        }
    }

    internal class FolderViewHolder(private val binding: FolderTileBinding, private val ctx: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bind(folderModel: FolderModel) {
            val itemCountText = when (folderModel.itemCount) {
                0L -> {
                    ctx.getString(me.safarov399.common.R.string.empty)
                }

                -1L -> {
                    "<DIR>"
                }

                else -> {
                    val itemCount = String.format(Locale.getDefault(), "%,d", folderModel.itemCount)
                    ctx.getString(me.safarov399.common.R.string.folder_items, itemCount, ctx.getString(me.safarov399.common.R.string.items))
                }
            }
            binding.folderTileTitleTv.text = folderModel.name
            binding.folderItemCountTv.text = itemCountText
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

        /**
         * Due to FileFolderModel being an interface, equals() methods of the implementing data classes are called. However, compiler still complains about it not being implemented in the interface level. Which is why, using
         * ```kotlin
         * @SuppressLint("DiffUtilEquals")
         * ```
         * here is perfectly acceptable.
         */
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