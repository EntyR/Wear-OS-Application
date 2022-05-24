package com.harman.wearosapp.app.ui.heart_rate_screen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateMargins
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.harman.wearosapp.app.R
import com.harman.wearosapp.app.databinding.FragmentHeartRateBinding
import com.harman.wearosapp.app.hr_service.HRService
import com.harman.wearosapp.app.other.ChartValueFormatter
import org.koin.androidx.viewmodel.ext.android.viewModel

class HeartRateFragment : Fragment() {


    private lateinit var binding: FragmentHeartRateBinding

    private val viewModel: HeartRateViewModel by viewModel()

    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHeartRateBinding.inflate(inflater, container, false)

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                when (result) {
                    true -> {
                        Log.i("TAG", "Body sensors permission granted")
                        requireActivity().startForegroundService(
                            Intent(requireContext(), HRService::class.java)
                        )
                    }
                    false -> {
                        Log.i("TAG", "Body sensors permission not granted")
                    }
                }
            }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        binding.tvStartRecord.height = (screenHeight / 5.5).toInt()

        binding.tvWaitingForResult.apply {
            val params = layoutParams as ConstraintLayout.LayoutParams
            params.height = screenHeight / 7
            layoutParams = params
            (layoutParams as ViewGroup.MarginLayoutParams).leftMargin = (screenWidth / 3.3).toInt()
        }

        binding.ivStartRecord.apply {
            val params = layoutParams as ConstraintLayout.LayoutParams
            params.height = screenHeight / 12
            layoutParams = params
        }

        binding.tvRate.apply {
            val params = layoutParams as ConstraintLayout.LayoutParams
            params.height = (screenHeight / 6.5).toInt()
            params.width = screenWidth / 6
            layoutParams = params
        }


        binding.ivHeartIcon.apply {
            (layoutParams as ViewGroup.MarginLayoutParams).updateMargins(
                top = screenHeight / 30,
                bottom = screenHeight / 40,
                right = screenHeight / 20
            )
            viewModel.heartIconState.observe(viewLifecycleOwner){
                when (it) {
                    HeartIconState.Full -> setImageResource(R.drawable.heart_rate_icon_full)
                    HeartIconState.Inline -> setImageResource(R.drawable.heart_rate_icon_empty)
                    else -> Unit
                }
            }
        }

        binding.chScatterChart.apply {
            xAxis.isEnabled = false
            axisLeft.isEnabled = false
            isAutoScaleMinMaxEnabled = true
            axisRight.isEnabled = false
            description.isEnabled = false
            legend.isEnabled = false


            startChartYBoundsUpdates()
            animateY(1000)

            val layoutParam = layoutParams as ConstraintLayout.LayoutParams
            layoutParam.height = (screenHeight / 2.5).toInt()
            layoutParams = layoutParam
        }


        viewModel.receiveHeartRateMeasurement().observe(viewLifecycleOwner) { list ->
            if (list.isEmpty() || viewModel.recordState.value == RecordState.Stopped) return@observe
            viewModel.switchRecordState(RecordState.Ongoing)

            binding.chScatterChart.data = LineData(getDataSet(list))

            viewModel.updateChartYBounds(
                list.maxOf { it.y },
                list.minOf { it.y }
            )
            binding.chScatterChart.xAxis.axisMaximum = list.last().x + 10 - list.size
            binding.chScatterChart.xAxis.axisMinimum = list.first().x - 1
            viewModel.switchHeartIconState()

            if (list.size > 1) {
                binding.tvRate.text = list.last().y.toInt().toString()
                binding.chScatterChart.invalidate()
            }

        }


        viewModel.recordState.observe(viewLifecycleOwner) { state ->
            when (state) {
                RecordState.Stopped -> {
                    requireActivity().stopService(
                        Intent(requireContext(), HRService::class.java)
                    )
                    binding.btSubmit.text = requireContext().getText(R.string.start)
                    binding.tvStartRecord.visibility = View.VISIBLE
                    binding.chScatterChart.visibility = View.INVISIBLE
                    binding.tvRate.text = requireContext().getText(R.string.no_records)
                    binding.ivStartRecord.visibility = View.VISIBLE
                    binding.tvWaitingForResult.visibility = View.GONE
                    binding.tvWaitingForResult.stopAnimation()
                }
                RecordState.Ongoing -> {
                    binding.btSubmit.text = requireContext().getText(R.string.stop)
                    binding.tvStartRecord.visibility = View.GONE
                    binding.chScatterChart.visibility = View.VISIBLE
                    binding.ivStartRecord.visibility = View.GONE
                    binding.btSubmit.text = requireContext().getText(R.string.stop)
                    binding.tvWaitingForResult.visibility = View.GONE
                    binding.tvWaitingForResult.stopAnimation()
                }
                RecordState.Waiting -> {
                    askForPermission()
                    binding.btSubmit.text = requireContext().getText(R.string.start)
                    binding.tvStartRecord.visibility = View.INVISIBLE
                    binding.chScatterChart.visibility = View.INVISIBLE
                    binding.ivStartRecord.visibility = View.INVISIBLE
                    binding.tvRate.text = requireContext().getText(R.string.no_records)
                    binding.btSubmit.text = requireContext().getText(R.string.stop)
                    binding.tvWaitingForResult.visibility = View.VISIBLE
                    binding.tvWaitingForResult.startAnimation()
                }
                else -> Unit
            }
        }




        binding.btSubmit.apply {
            val params = layoutParams as ConstraintLayout.LayoutParams

            params.width = (screenWidth / 3.2).toInt()
            params.height = screenHeight / 6
            setPadding(
                0,
                screenHeight / 25,
                0,
                screenHeight / 25,
            )

            setOnClickListener {
                viewModel.switchRecordState()
            }
        }
    }

    private fun startChartYBoundsUpdates() {
        viewModel.chartBounds.observe(viewLifecycleOwner) {
            binding.chScatterChart.axisLeft.apply {
                axisMaximum = it.yMax
                axisMinimum = it.yMin
            }
        }
    }

    private fun getDataSet(list: List<Entry>): LineDataSet {
        return LineDataSet(list, "Gravity fluctuation").apply {
            color = requireContext().getColor(R.color.purple_primary)
            lineWidth = 3F
            mode = LineDataSet.Mode.STEPPED
            setDrawCircles(false)
            valueFormatter = ChartValueFormatter()
        }
    }

    private fun askForPermission() {
        permissionLauncher.launch(android.Manifest.permission.BODY_SENSORS)
    }


    companion object {

        const val TAG = "HeartRateFragment"

        @JvmStatic
        fun newInstance() = HeartRateFragment()
    }
}