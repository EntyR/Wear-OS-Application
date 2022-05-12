package com.harman.wearosapp.app.ui.hr_export_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
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

        binding.root.post {
            val screenWidth = binding.root.width
            val screenHeight = binding.root.height
            val textSize =
                screenHeight / 33 * resources.configuration.fontScale

            binding.tvMessage.apply {
                height = (screenHeight / 3.5).toInt()

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
                btnParam.width = (screenWidth / 2.4).toInt()
                btnParam.height = screenHeight / 6
            }
        }



        return binding.root
    }


    companion object {
        @JvmStatic
        fun newInstance() = SubmitFragment()
    }
}