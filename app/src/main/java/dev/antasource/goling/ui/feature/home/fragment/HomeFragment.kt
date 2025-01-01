package dev.antasource.goling.ui.feature.home.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dev.antasource.goling.data.networksource.NetworkRemoteSource
import dev.antasource.goling.data.repositoty.HomeRepository
import dev.antasource.goling.databinding.FragmentHomeBinding
import dev.antasource.goling.ui.factory.MainViewModelFactory
import dev.antasource.goling.ui.feature.estimate.EstimateDeliveryActivity
import dev.antasource.goling.ui.feature.home.viewmodel.HomeViewModel
import dev.antasource.goling.ui.feature.topup.TopUpActivity
import dev.antasource.goling.util.SharedPrefUtil

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val homeViewModel by viewModels<HomeViewModel>(){
        val data = NetworkRemoteSource()
        val repo = HomeRepository(data)
        MainViewModelFactory(repo)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container, false )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topUpWalletbtn = binding.layoutHome.layoutHomeWallet.topUpWalletButton
        val nameUser = binding.layoutHome.layoutHomeWallet.nameUserWallet
        val estimateMenu = binding.layoutHome.layoutGolingMenu.checkPostageMenu

        val token = SharedPrefUtil.getAccessToken(view.context).toString()

        homeViewModel.userResponse.observe(requireActivity()){ data ->
            nameUser.text = data.user.username.toString()
        }

        homeViewModel.balance.observe(requireActivity()) { balance ->
            val balanceTopUp = balance
            binding.layoutHome.layoutHomeWallet.amountNominalTxt.text = "${balanceTopUp.balance}"
        }

        homeViewModel.errorMsg.observe(requireActivity()){ error ->
            Log.e("Error Message", "Error $error")
        }
        Log.d("Token User", "Token $token")

        homeViewModel.getUser(token)
        homeViewModel.getBalance(token)

        topUpWalletbtn.setOnClickListener{ v->
            val intent = Intent(v.context, TopUpActivity::class.java)
            startActivity(intent)
        }
        estimateMenu.setOnClickListener{ v->
            val intent = Intent(v.context, EstimateDeliveryActivity::class.java)
            startActivity(intent)
        }
    }
}