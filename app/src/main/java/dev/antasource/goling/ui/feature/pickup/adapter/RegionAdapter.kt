package dev.antasource.goling.ui.feature.pickup.adapter

import android.util.Printer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import dev.antasource.goling.data.model.country.Region
import dev.antasource.goling.databinding.ItemRegionListBinding

class RegionAdapter(
    private val region: List<Region>,
    private val onRegionClick: (Region) -> Unit
): RecyclerView.Adapter<RegionAdapter.RegionViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RegionViewHolder {
        val binding = ItemRegionListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RegionViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RegionViewHolder,
        position: Int
    ) {
        holder.bind(region[position])
        holder.itemView.setOnClickListener{ v: View ->
            onRegionClick(Region(region[position].id, region[position].name))
        }
    }

    override fun getItemCount(): Int = region.size


    class RegionViewHolder(val binding: ItemRegionListBinding): ViewHolder(binding.root){
       fun bind(get: Region){
           binding.txtLocationItem.text = get.name
       }
    }
}