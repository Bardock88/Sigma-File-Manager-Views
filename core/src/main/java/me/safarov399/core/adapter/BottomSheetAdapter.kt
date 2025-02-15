package me.safarov399.core.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.safarov399.core.listeners.OnClickListener
import me.safarov399.domain.models.adapter.OnHoldModel

class BottomSheetAdapter : ListAdapter<OnHoldModel, BottomSheetAdapter.OnHoldViewHolder>(OnHoldDiffCallback()) {

    private var onClickListener: OnClickListener? = null

    class OnHoldViewHolder(itemView: View, private val ctx: Context) : RecyclerView.ViewHolder(itemView) {
        private val onHoldIcon = itemView.findViewById<ImageView>(me.safarov399.uikit.R.id.ohbst_icon)
        private val titleTv = itemView.findViewById<TextView>(me.safarov399.uikit.R.id.ohbst_title)
        fun bind(model: OnHoldModel) {
            titleTv.text = ctx.getString(model.title)
            onHoldIcon.setImageResource(model.iconId)
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

    fun setOnClickListener(listener: OnClickListener) {
        this.onClickListener = listener
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