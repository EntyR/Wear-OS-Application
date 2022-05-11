package com.harman.wearosapp.app.ui.hr_export_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateMargins
import androidx.fragment.app.Fragment
import com.harman.wearosapp.app.databinding.FragmentExportBinding
import com.harman.wearosapp.app.other.WAITING_TRANSACTION


class SubmitFragment : Fragment() {

    private lateinit var binding: FragmentExportBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExportBinding.inflate(inflater, container, false)
        val screenWidth = resources.configuration.screenWidthDp
        val textSize =
            screenWidth / 17 * resources.configuration.fontScale

        binding.tvMessage.apply {
            val tvMarginParams = layoutParams as ViewGroup.MarginLayoutParams
            tvMarginParams.updateMargins(left = screenWidth/5, right = screenWidth/5)
            this.textSize = textSize
        }


        binding.btSubmit.apply {
            this.textSize = textSize
            setOnClickListener {
                ExportActivity.navigateTo(
                    WaitingFragment.newInstance(),
                    WAITING_TRANSACTION,
                    parentFragmentManager
                )
            }
            val btnParam = layoutParams as ConstraintLayout.LayoutParams
            btnParam.width = (screenWidth / 1.4).toInt()
            btnParam.height = (screenWidth / 3.8).toInt()
        }


        return binding.root
    }


    companion object {
        @JvmStatic
        fun newInstance() = SubmitFragment()
    }
}