package dev.antasource.goling.ui.feature.home.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.antasource.goling.data.model.pickup.response.Order
import dev.antasource.goling.databinding.ItemOrderListBinding
import dev.antasource.goling.ui.feature.home.adapter.TransactionListAdapter.TransactionViewHolder
import dev.antasource.goling.ui.feature.home.fragment.order.OrderDetailActivity
import dev.antasource.goling.util.Util.convertIsoToDate

class TransactionListAdapter(private val listTransaction: List<Order>, val context: Context) : RecyclerView.Adapter<TransactionViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionViewHolder {
        val binding = ItemOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.getBindingTransaction(listTransaction[position])
        holder.itemView.setOnClickListener{
            val intent = Intent(context, OrderDetailActivity::class.java)
            intent.putExtra("orderId", listTransaction[position].orderId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listTransaction.size
    inner class TransactionViewHolder(val binding: ItemOrderListBinding) : RecyclerView.ViewHolder(binding.root){
        fun getBindingTransaction(transaction : Order){
            transaction.let {
                binding.txtDestinationAddress.text = transaction.destinationAddress
                binding.txtAddressOrigin.text = transaction.originAddress
                binding.txtDateOrderCreated.text = convertIsoToDate(transaction.createdAt)
            }
        }
    }
}