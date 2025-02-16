package dev.antasource.goling.ui.feature.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.antasource.goling.data.model.pickup.response.History
import dev.antasource.goling.databinding.ItemOrderHistoryProgressBinding
import dev.antasource.goling.util.Util.convertIsoToDateTime

class HistoryOrderAdapter(private val history: List<History>): RecyclerView.Adapter<HistoryOrderAdapter.HistoryOrderViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryOrderViewHolder {
        val binding = ItemOrderHistoryProgressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryOrderViewHolder(binding)
    }
    override fun onBindViewHolder(
        holder: HistoryOrderViewHolder,
        position: Int
    ) {
        val itemData = history[position]
        holder.getHistoryOrderList(itemData)
        if(position == history.size - 1){
            holder.binding.lineIndicator.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = history.size

    class HistoryOrderViewHolder(val binding: ItemOrderHistoryProgressBinding): RecyclerView.ViewHolder(binding.root){

        fun getHistoryOrderList(history: History){
            binding.tvDateTime.text = convertIsoToDateTime(history.createdAt)
            binding.txtOrderStatus.text = history.status
        }

    }
}