package com.harman.wearosapp.app.ui.hr_export_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.harman.wearosapp.app.databinding.FragmentWaitingBinding


class WaitingFragment : Fragment() {

    private lateinit var binding: FragmentWaitingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = FragmentWaitingBinding.inflate(inflater, container, false)

        binding.circularProgressBar.totalTime = 30000
        binding.circularProgressBar.isIndeterminate = true
        binding.circularProgressBar.startTimer()

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() = WaitingFragment()
    }
}