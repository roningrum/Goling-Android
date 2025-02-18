package dev.antasource.goling.ui.feature.home.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dev.antasource.goling.data.networksource.NetworkRemoteSource
import dev.antasource.goling.data.repositoty.AuthenticationRepository
import dev.antasource.goling.databinding.FragmentProfileBinding
import dev.antasource.goling.ui.factory.AuthViewModelFactory
import dev.antasource.goling.ui.feature.home.viewmodel.ProfileViewModel
import dev.antasource.goling.ui.feature.login.LoginActivity
import dev.antasource.goling.util.SharedPrefUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.getValue

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    private val profileViewModel by viewModels<ProfileViewModel>() {
        val data = NetworkRemoteSource()
        val repo = AuthenticationRepository(data)
        AuthViewModelFactory(repo)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.token = SharedPrefUtil.getAccessToken(requireContext()).toString()
        profileViewModel.getUserProfile()
        binding.logoutButton.setOnClickListener {
            profileViewModel.logout()
        }

        profileViewModel.userResponse.observe(requireActivity()){ user ->
            binding.txtNameProfile.text = user.users.username
            binding.txtEmailProfile.text = user.users.email

        }


        profileViewModel.message.observe(requireActivity()) { message ->
            CoroutineScope(Dispatchers.Main).launch {
                delay(3000)
                message?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireActivity(), LoginActivity::class.java)
                    startActivity(intent)
                    SharedPrefUtil.clearAccessToken(requireContext())
                    activity?.finish()
                }
            }
        }

    }
}