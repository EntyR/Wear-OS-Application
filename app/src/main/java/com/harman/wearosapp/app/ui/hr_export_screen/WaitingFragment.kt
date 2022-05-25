package com.harman.wearosapp.app.ui.hr_export_screen

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.harman.wearosapp.app.databinding.FragmentWaitingBinding
import com.harman.wearosapp.app.other.SUCCESS_TRANSACTION
import com.harman.wearosapp.domain.repository.ExportState
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class WaitingFragment : Fragment() {

    private lateinit var binding: FragmentWaitingBinding
    private val exportViewModel: ExportViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentWaitingBinding.inflate(inflater, container, false)
        binding.circularSpinningView.startSpinnerAnimation()
        binding.root.post {
            val screenHeight = binding.root.height

            binding.tvExportMesage.apply {
                height = screenHeight / 10

            }
        }

        //Using timer because data is saved too quickly and fragment doesn't seen

        exportViewModel.exportState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ExportState.Failure -> startTimer()
                is ExportState.Success -> startTimer()
                is ExportState.Waiting -> Unit
            }
        }
        return binding.root
    }

    private fun startTimer() {
        object : CountDownTimer(1000, 1000) {
            override fun onTick(p0: Long) = Unit

            override fun onFinish() {
                ExportActivity.navigateTo(
                    SuccessFragment.newInstance(),
                    SUCCESS_TRANSACTION,
                    parentFragmentManager
                )
            }
        }.start()
    }

    companion object {

        @JvmStatic
        fun newInstance() = WaitingFragment()
    }


}