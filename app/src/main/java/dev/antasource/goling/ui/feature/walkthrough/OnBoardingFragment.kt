package dev.antasource.goling.ui.feature.walkthrough

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import dev.antasource.goling.R

class OnBoardingFragment : Fragment() {

    private lateinit var imageOnBoarding : ImageView

    companion object{
        private const val ARG_SECTION_NUMBER = "section_number"

        fun getInstance(position: Int) = OnBoardingFragment().apply {
            arguments = bundleOf(ARG_SECTION_NUMBER to position)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_on_boarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val position = requireArguments().getInt(ARG_SECTION_NUMBER)
        imageOnBoarding = view.findViewById(R.id.image_view_onboarding)
        when(position){
            0 -> imageOnBoarding.setImageResource(R.drawable.onboarding_pic_second)
            1 -> imageOnBoarding.setImageResource(R.drawable.onboarding_pic)
        }

    }
}