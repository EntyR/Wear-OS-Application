package com.harman.wearosapp.app.ui.hr_export_screen

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.harman.wearosapp.app.databinding.FragmentWaitingBinding
import com.harman.wearosapp.app.other.SUCCESS_TRANSACTION


class WaitingFragment : Fragment() {

    private lateinit var binding: FragmentWaitingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentWaitingBinding.inflate(inflater, container, false)

        binding.root.post {
            val screenHeight = binding.root.height

            binding.tvExportMesage.apply {
                height = screenHeight / 10

            }
        }

        binding.circularSpinningView.startSpinnerAnimation()


        //TODO Navigate when download is done instead of timer
        object : CountDownTimer(1000, 10) {
            override fun onTick(p0: Long) = Unit

            override fun onFinish() {
                ExportActivity.navigateTo(
                    SuccessFragment.newInstance(),
                    SUCCESS_TRANSACTION,
                    parentFragmentManager
                )
            }
        }.start()

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() = WaitingFragment()
    }


}