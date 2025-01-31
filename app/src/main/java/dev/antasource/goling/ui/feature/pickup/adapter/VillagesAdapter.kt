package dev.antasource.goling.ui.feature.pickup.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.antasource.goling.data.model.country.Villages
import dev.antasource.goling.databinding.ItemRegionListBinding

class VillagesAdapter(private val villages: List<Villages>, private val onVillageClick:(Villages)-> Unit): RecyclerView.Adapter<VillagesAdapter.VillagesViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VillagesViewHolder {
       val binding = ItemRegionListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VillagesViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: VillagesViewHolder,
        position: Int
    ) {
        holder.bind(villages[position])
        holder.itemView.setOnClickListener{ v->
            onVillageClick(Villages(villages[position].id, villages[position].district_id, villages[position].name))
        }
    }

    override fun getItemCount(): Int = villages.size

    inner class VillagesViewHolder(val binding: ItemRegionListBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(get: Villages){
            binding.txtLocationItem.text = get.name
        }
    }
}