package dev.antasource.goling.ui.feature.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dev.antasource.goling.data.networksource.NetworkRemoteSource
import dev.antasource.goling.data.repositoty.ShippingRepository
import dev.antasource.goling.databinding.FragmentHistoryBinding
import dev.antasource.goling.ui.factory.ShippingViewModelFactory
import dev.antasource.goling.ui.feature.home.viewmodel.TransactionViewModel
import dev.antasource.goling.util.SharedPrefUtil
import kotlin.getValue

class HistoryFragment : Fragment() {

    private lateinit var historyBinding : FragmentHistoryBinding

    private val transactionHistoryViewModel by viewModels<TransactionViewModel>(){
        val data = NetworkRemoteSource()
        val repo = ShippingRepository(data)
        ShippingViewModelFactory(repo)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        historyBinding = FragmentHistoryBinding.inflate(inflater, container, false)
        return historyBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val listTransaction = historyBinding.layoutHistoryFragment.listHistoryShip
//        val emptyListView = historyBinding.layoutHistoryFragment.layoutEmptyOrder

        transactionHistoryViewModel.token = SharedPrefUtil.getAccessToken(requireContext()).toString()

//        if(isNetworkAvailable(requireActivity())){
//            transactionHistoryViewModel.getHistoryOrder()
//        }else{
//            Toast.makeText(requireContext(), "Tidak ada koneksi Internet", Toast.LENGTH_SHORT).show()
//            val intent = Intent(requireActivity(), LoginActivity::class.java)
//            startActivity(intent)
//            activity?.finish()
//        }


//        transactionHistoryViewModel.historyOrderList.observe(requireActivity()){ orderTransaction->
//            if(orderTransaction.isNotEmpty()){
//                emptyListView.root.visibility = GONE
//                val adapter = TransactionListAdapter(
//                    context = requireActivity(),
//                    listTransaction = orderTransaction
//                )
//                listTransaction.adapter = adapter
//            } else{
//                emptyListView.root.visibility = VISIBLE
//                listTransaction.visibility = GONE
//            }
//        }
//        transactionHistoryViewModel.errorMsg.observe(requireActivity()){
//            emptyListView.root.visibility = VISIBLE
//            listTransaction.visibility = GONE
//        }
    }
}