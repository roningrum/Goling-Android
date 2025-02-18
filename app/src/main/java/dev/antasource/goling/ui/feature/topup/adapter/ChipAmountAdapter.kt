package dev.antasource.goling.ui.feature.topup.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import dev.antasource.goling.R
import dev.antasource.goling.util.Util.formatCurrency

class ChipAmountAdapter(
    private val context: Context,
    private val chips: List<String>,
    private val onChipClick: (String) -> Unit
) : RecyclerView.Adapter<ChipAmountAdapter.ChipViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChipViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_chip_amount, parent, false)
        return ChipViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ChipViewHolder,
        position: Int
    ) {
        val chipText = chips[position]
        holder.chip.text = buildString {
            append("Rp.")
            append(formatCurrency(chipText.toInt()))
        }
        holder.chip.setOnClickListener {
            onChipClick(chipText)
        }
    }

    override fun getItemCount(): Int = chips.size

    inner class ChipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chip: Chip = itemView.findViewById(R.id.chip_choice_1)
    }
}