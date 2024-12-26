package dev.antasource.goling.ui.feature.topup.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import dev.antasource.goling.databinding.ItemChipAmountBinding

class ChipAmountAdapter(private val context: Context, private val chips: List<String>,  private val onChipClick: (String) -> Unit) : BaseAdapter() {

    override fun getCount(): Int = chips.size

    override fun getItem(position: Int): Any = chips[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: ItemChipAmountBinding
        if (convertView == null) {
            // Inflate the view using ViewBinding
            val inflater = LayoutInflater.from(context)
            binding = ItemChipAmountBinding.inflate(inflater, parent, false)
            // Set the tag to store the binding for future use
            binding.root.tag = binding
        } else {
            // Retrieve the binding from the convertView tag
            binding = convertView.tag as ItemChipAmountBinding
        }

        // Set the chip text using ViewBinding
        binding.chipChoice1.text = buildString {
        append("Rp. ")
        append(chips[position])
    }
        binding.chipChoice1.setOnClickListener{
            onChipClick(chips[position])
        }

        return binding.root
    }
}