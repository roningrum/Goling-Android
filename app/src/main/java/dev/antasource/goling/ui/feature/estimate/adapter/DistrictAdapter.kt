package dev.antasource.goling.ui.feature.estimate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.antasource.goling.data.model.country.Districs
import dev.antasource.goling.databinding.ItemRegionListBinding

class DistrictAdapter(private val district: List<Districs>, private val onDistrictClick:(Districs)-> Unit): RecyclerView.Adapter<DistrictAdapter.DistricViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DistricViewHolder {
        val binding = ItemRegionListBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return DistricViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DistricViewHolder, position: Int) {
       holder.getBinding(district[position])
        holder.itemView.setOnClickListener{ v->
            onDistrictClick(Districs(district[position].id, district[position].regency_id, district[position].name))
        }
    }

    override fun getItemCount(): Int = district.size

    inner class DistricViewHolder(val binding: ItemRegionListBinding): RecyclerView.ViewHolder(binding.root){
        fun getBinding(bind: Districs){
            binding.txtLocationItem.text= bind.name
        }
    }
}