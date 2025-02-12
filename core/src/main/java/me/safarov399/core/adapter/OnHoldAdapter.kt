package me.safarov399.core.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.safarov399.core.listeners.OnClickListener
import me.safarov399.domain.models.adapter.OnHoldModel

class OnHoldAdapter : ListAdapter<OnHoldModel, OnHoldAdapter.OnHoldViewHolder>(OnHoldDiffCallback()) {

    private var onClickListener: OnClickListener? = null

    class OnHoldViewHolder(itemView: View, private val ctx: Context) : RecyclerView.ViewHolder(itemView) {
        fun bind(model: OnHoldModel) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnHoldViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(me.safarov399.uikit.R.layout.on_hold_bottom_sheet_tile, parent, false)
        return OnHoldViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: OnHoldViewHolder, position: Int) {
        val onHoldModel = getItem(position)
        holder.bind(onHoldModel)

        holder.itemView.setOnClickListener {
            onClickListener?.onClickBottomSheetItem(position, onHoldModel)
        }
    }

    class OnHoldDiffCallback : DiffUtil.ItemCallback<OnHoldModel>() {
        override fun areItemsTheSame(oldItem: OnHoldModel, newItem: OnHoldModel): Boolean {
            return oldItem.title == newItem.title && oldItem.iconId == newItem.iconId
        }

        override fun areContentsTheSame(oldItem: OnHoldModel, newItem: OnHoldModel): Boolean {
            return oldItem == newItem
        }

    }
}