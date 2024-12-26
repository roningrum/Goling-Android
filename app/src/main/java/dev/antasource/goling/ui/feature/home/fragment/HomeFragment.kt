package dev.antasource.goling.ui.feature.home.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dev.antasource.goling.data.networksource.NetworkRemoteSource
import dev.antasource.goling.data.repositoty.HomeRepository
import dev.antasource.goling.databinding.FragmentHomeBinding
import dev.antasource.goling.ui.factory.MainViewModelFactory
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

        val token = SharedPrefUtil.getAccessToken(view.context).toString()

        homeViewModel.userResponse.observe(requireActivity()){ data ->
            Log.d("Data User", "Data $data")
        }
        homeViewModel.message.observe(requireActivity()){ nama ->
            nama?.let {
                Toast.makeText(view.context, "Welcome $nama", Toast.LENGTH_SHORT).show()
            }

        }
        homeViewModel.errorMsg.observe(requireActivity()){ error ->
            Log.e("Error Message", "Error $error")
        }
        Log.d("Token User", "Token $token")
        homeViewModel.getUser(token)

        val topUpWalletbtn = binding.layoutHome.layoutHomeWallet.topUpWalletButton
        topUpWalletbtn.setOnClickListener{ v->
            val intent = Intent(v.context, TopUpActivity::class.java)
            startActivity(intent)
        }
    }
}