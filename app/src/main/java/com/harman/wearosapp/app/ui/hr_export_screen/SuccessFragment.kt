package com.harman.wearosapp.app.ui.hr_export_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.harman.wearosapp.app.databinding.FragmentSuccessBinding


class SuccessFragment : Fragment() {

    private lateinit var binding: FragmentSuccessBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSuccessBinding.inflate(inflater, container, false)

        binding.root.post {
            val screenWidth = binding.root.width
            val screenHeight = binding.root.height

            binding.tvMessage.apply {
                height = screenHeight / 6
            }
            val ivParams = binding.ivPulc.layoutParams as ConstraintLayout.LayoutParams
            ivParams.width = screenWidth / 3
        }


        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = SuccessFragment()
    }
}