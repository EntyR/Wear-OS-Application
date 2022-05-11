package com.harman.wearosapp.app.ui.hr_export_screen

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.harman.wearosapp.app.R
import com.harman.wearosapp.app.databinding.FragmentWaitingBinding
import com.harman.wearosapp.app.other.SUCCESS_TRANSACTION


class WaitingFragment : Fragment() {

    private lateinit var binding: FragmentWaitingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentWaitingBinding.inflate(inflater, container, false)
        val screenWidth = resources.configuration.screenWidthDp
        val textSize =
            screenWidth / 17 * resources.configuration.fontScale

        binding.tvExportMesage.textSize = textSize
        binding.circularSpinningView.startSpinnerAnimation()

        //TODO Navigate when download is done instead of timer
        object: CountDownTimer(1000, 10){
            override fun onTick(p0: Long) = Unit

            override fun onFinish() {
                ExportActivity.navigateTo(SuccessFragment.newInstance(), SUCCESS_TRANSACTION, parentFragmentManager)
            }
        }.start()

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() = WaitingFragment()
    }


}