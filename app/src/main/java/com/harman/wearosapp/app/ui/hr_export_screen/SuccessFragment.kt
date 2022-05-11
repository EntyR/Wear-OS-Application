package com.harman.wearosapp.app.ui.hr_export_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.harman.wearosapp.app.R
import com.harman.wearosapp.app.databinding.FragmentSuccessBinding


class SuccessFragment : Fragment() {

    private lateinit var binding: FragmentSuccessBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSuccessBinding.inflate(inflater, container, false)
        val screenWidth = resources.configuration.screenWidthDp
        val textSize =
            screenWidth / 17 * resources.configuration.fontScale
        val ivParams = binding.ivPulc.layoutParams as ConstraintLayout.LayoutParams

        binding.tvMessage.textSize = textSize
        ivParams.width = (screenWidth / 1.5).toInt()
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = SuccessFragment()
    }
}