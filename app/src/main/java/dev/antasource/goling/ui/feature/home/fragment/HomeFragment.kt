package dev.antasource.goling.ui.feature.home.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dev.antasource.goling.R
import dev.antasource.goling.data.networksource.ApiResult
import dev.antasource.goling.data.networksource.NetworkRemoteSource
import dev.antasource.goling.data.repositoty.HomeRepositoryRepository
import dev.antasource.goling.databinding.FragmentHomeBinding
import dev.antasource.goling.ui.factory.MainViewModelFactory
import dev.antasource.goling.ui.feature.estimate.EstimateDeliveryActivity
import dev.antasource.goling.ui.feature.home.viewmodel.HomeViewModel
import dev.antasource.goling.ui.feature.login.LoginActivity
import dev.antasource.goling.ui.feature.pickup.PickupActivity
import dev.antasource.goling.ui.feature.topup.TopUpActivity
import dev.antasource.goling.util.ConnectivityManagerUtil.isNetworkAvailable
import dev.antasource.goling.util.SharedPrefUtil
import dev.antasource.goling.util.Util
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var isBalanceVisible = false
    private val homeViewModel by viewModels<HomeViewModel>() {
        val data = NetworkRemoteSource()
        val repo = HomeRepositoryRepository(data)
        MainViewModelFactory(repo)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topUpWalletbtn = binding.layoutHome.layoutHomeWallet.topUpWalletButton
        val nameUser = binding.layoutHome.layoutHomeWallet.nameUserWallet
        val estimateMenu = binding.layoutHome.layoutGolingMenu.checkPostageMenu
        val pickupMenu = binding.layoutHome.layoutGolingMenu.pickupMenu

        val token = SharedPrefUtil.getAccessToken(requireActivity()).toString()

        if (token.isEmpty()) {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        if(isNetworkAvailable(requireActivity())){
            homeViewModel.getUser(token)
            homeViewModel.getBalance(token)
        } else{
            Toast.makeText(requireContext(), "Tidak ada koneksi Internet", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }


        binding.layoutHome.layoutHomeWallet.amountNominalTxt.text = "*******"
        binding.layoutHome.layoutHomeWallet.visibleAmountBtn.setImageResource(R.drawable.ic_visibility_off)

        binding.layoutHome.layoutHomeWallet.visibleAmountBtn.setOnClickListener {
            showBalanceProfile(isBalanceVisible)

            isBalanceVisible = !isBalanceVisible;
        }

        homeViewModel.userResponse.observe(requireActivity()) { data ->
            nameUser.text = data.users.username
        }
        homeViewModel.errorMsg.observe(requireActivity()) { error ->
            if (error.contains("expired")) {
                SharedPrefUtil.clearAccessToken(requireContext())
                Toast.makeText(requireContext(), "Sesi berakhir", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                startActivity(intent)
                activity?.finish()
            } else {
                binding.layoutHome.layoutHomeWallet.amountNominalTxt.text = Util.formatCurrency(0)
                Toast.makeText(requireContext(), "Terjadi Kesalahan", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }

        }

        topUpWalletbtn.setOnClickListener { v ->
            val intent = Intent(v.context, TopUpActivity::class.java)
            startActivity(intent)
        }
        estimateMenu.setOnClickListener { v ->
            val intent = Intent(v.context, EstimateDeliveryActivity::class.java)
            startActivity(intent)
        }

        pickupMenu.setOnClickListener { v ->
            val intent = Intent(v.context, PickupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showBalanceProfile(isBalanceVisible: Boolean) {
        lifecycleScope.launch {
            homeViewModel.balance.collect { state ->
                when (state) {
                    is ApiResult.Loading -> {
                        if (isBalanceVisible) {
                            binding.layoutHome.layoutHomeWallet.amountNominalTxt.text = "*******"
                            binding.layoutHome.layoutHomeWallet.visibleAmountBtn.setImageResource(R.drawable.ic_visibility_off)
                        }
                    }

                    is ApiResult.Success -> {
                        val data = state.data
                        data?.let {
                            if (data.balance == 0) {
                                binding.layoutHome.layoutHomeWallet.amountNominalTxt.text =Util.formatCurrency(0)
                            } else {
                                binding.layoutHome.layoutHomeWallet.amountNominalTxt.text =  Util.formatCurrency(data.balance)
                            }
                        }
                    }
                    is ApiResult.Error ->{
                        binding.layoutHome.layoutHomeWallet.amountNominalTxt.text = Util.formatCurrency(0)
                    }
                }
            }
        }
    }


}




