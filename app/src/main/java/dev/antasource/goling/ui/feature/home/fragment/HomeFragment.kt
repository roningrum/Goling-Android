package dev.antasource.goling.ui.feature.home.fragment

import android.content.Intent
import android.os.Bundle
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
import dev.antasource.goling.ui.feature.pickup.PickupActivity
import dev.antasource.goling.ui.feature.topup.TopUpActivity
import dev.antasource.goling.util.SharedPrefUtil
import dev.antasource.goling.util.Util
import dev.antasource.goling.R
import dev.antasource.goling.ui.feature.login.LoginActivity

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private var isBalanceVisible = false

    private val homeViewModel by viewModels<HomeViewModel>() {
        val data = NetworkRemoteSource()
        val repo = HomeRepository(data)
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

        val token = SharedPrefUtil.getAccessToken(view.context).toString()

        if(token.isEmpty()){
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        homeViewModel.getUser(token)
        homeViewModel.getBalance(token)

        binding.layoutHome.layoutHomeWallet.amountNominalTxt.text = "*******"
        binding.layoutHome.layoutHomeWallet.visibleAmountBtn.setImageResource(R.drawable.ic_visibility_off)

        binding.layoutHome.layoutHomeWallet.visibleAmountBtn.setOnClickListener {
            if (isBalanceVisible) {
                binding.layoutHome.layoutHomeWallet.amountNominalTxt.text = "*******"
                binding.layoutHome.layoutHomeWallet.visibleAmountBtn.setImageResource(R.drawable.ic_visibility_off)
            } else {
                homeViewModel.balance.observe(requireActivity()) { balance ->
                    if (balance.balance != 0) {
                        binding.layoutHome.layoutHomeWallet.amountNominalTxt.text =
                            Util.formatCurrency(balance.balance)
                    } else {
                        binding.layoutHome.layoutHomeWallet.amountNominalTxt.text =
                            Util.formatCurrency(0)

                    }

                }
                binding.layoutHome.layoutHomeWallet.visibleAmountBtn.setImageResource(R.drawable.ic_visibility)
            }
            isBalanceVisible = !isBalanceVisible;
        }

        homeViewModel.userResponse.observe(requireActivity()) { data ->
            nameUser.text = data.users.username
        }
        homeViewModel.errorMsg.observe(requireActivity()) { error ->
            binding.layoutHome.layoutHomeWallet.amountNominalTxt.text = Util.formatCurrency(0)
            if(error.contains("Expired")){
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
}