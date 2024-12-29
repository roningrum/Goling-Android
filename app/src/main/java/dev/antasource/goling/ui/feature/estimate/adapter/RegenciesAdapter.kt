package dev.antasource.goling.ui.feature.estimate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.antasource.goling.data.model.country.Regencies
import dev.antasource.goling.databinding.ItemRegionListBinding

class RegenciesAdapter(private val regencies: List<Regencies>,  private val onRegenciesClick: (Regencies) -> Unit) : RecyclerView.Adapter<RegenciesAdapter.RegenciesViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RegenciesViewHolder {
        val binding = ItemRegionListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RegenciesViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RegenciesViewHolder,
        position: Int
    ) {
        holder.bind(regencies[position])
        holder.itemView.setOnClickListener{
            onRegenciesClick(Regencies(regencies[position].id, regencies[position].province_id, regencies[position].name))
        }
    }

    override fun getItemCount() = regencies.size

    class RegenciesViewHolder(val binding: ItemRegionListBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(get: Regencies){
            binding.txtLocationItem.text = get.name
        }
    }
}