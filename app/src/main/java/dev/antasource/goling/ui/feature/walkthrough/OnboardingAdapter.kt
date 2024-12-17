package dev.antasource.goling.ui.feature.walkthrough

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment = OnBoardingFragment.getInstance(position)
    override fun getItemCount(): Int = 2

}