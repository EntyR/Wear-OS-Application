package com.harman.wearosapp.app.ui.hr_export_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.harman.wearosapp.app.databinding.FragmentExportBinding


class SubmitFragment : Fragment() {

    private lateinit var binding: FragmentExportBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExportBinding.inflate(inflater, container, false)

        return binding.root
    }


    companion object {
        @JvmStatic
        fun newInstance() = SubmitFragment()
    }
}