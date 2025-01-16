package dev.antasource.goling.ui.feature.home.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dev.antasource.goling.R
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

        profileViewModel.token = SharedPrefUtil.getAccessToken(view.context).toString()
        binding.logoutButton.setOnClickListener {
            profileViewModel.logout()
        }

        profileViewModel.message.observe(requireActivity()) { message ->
            CoroutineScope(Dispatchers.Main).launch {
                delay(3000)
                message?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    SharedPrefUtil.clear(view.context)
                    val intent = Intent(requireActivity(), LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
            }
        }

    }
}