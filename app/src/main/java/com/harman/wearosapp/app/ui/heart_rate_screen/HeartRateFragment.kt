package com.harman.wearosapp.app.ui.heart_rate_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateMargins
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.harman.wearosapp.app.R
import com.harman.wearosapp.app.databinding.FragmentHeartRateBinding
import com.harman.wearosapp.app.other.ChartValueFormatter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HeartRateFragment : Fragment() {


    private lateinit var binding: FragmentHeartRateBinding

    //TODO Test values observe value from view model later on
    private val fakeData = MutableLiveData<List<Entry>>()
    private val recordState = MutableLiveData(RecordState.Stopped)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHeartRateBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.post {

            val screenWidth = binding.root.width
            val screenHeight = binding.root.height

            binding.tvStartRecord.height = (screenHeight / 5.5).toInt()


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
            }

            binding.chScatterChart.apply {
                xAxis.isEnabled = false
                axisLeft.isEnabled = false
                isAutoScaleMinMaxEnabled = true
                axisRight.isEnabled = false
                description.isEnabled = false
                legend.isEnabled = false

                //TODO Use x axis max value from hr records time
                xAxis.axisMaximum = 20F
                //TODO change value for maximum possible hr record
                axisLeft.axisMaximum = 30f
                axisLeft.axisMinimum = 0f

                val layoutParam = layoutParams as ConstraintLayout.LayoutParams
                layoutParam.height = (screenHeight / 2.5).toInt()
                layoutParams = layoutParam
            }


            fakeData.observe(viewLifecycleOwner) {
                if (it.isEmpty()) return@observe
                binding.chScatterChart.data = LineData(getDataSet(it))

                if (it.size > 1) {
                    binding.tvRate.text = it.last().y.toInt().toString()
                    binding.chScatterChart.invalidate()
                }
            }


            recordState.observe(viewLifecycleOwner) { state ->
                when (state) {
                    RecordState.Stopped -> {
                        binding.btSubmit.text = requireContext().getText(R.string.start)
                        binding.tvStartRecord.visibility = View.VISIBLE
                        binding.chScatterChart.visibility = View.INVISIBLE
                        binding.tvRate.text = requireContext().getText(R.string.no_records)
                        binding.ivStartRecord.visibility = View.VISIBLE
                    }
                    RecordState.Ongoing -> {
                        binding.btSubmit.text = requireContext().getText(R.string.stop)
                        binding.tvStartRecord.visibility = View.GONE
                        binding.chScatterChart.visibility = View.VISIBLE
                        binding.ivStartRecord.visibility = View.GONE
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

                //TODO Observe for real hr censor values
                var job: Job? = null
                setOnClickListener {
                    when (recordState.value) {
                        RecordState.Stopped -> job = lifecycleScope.launch {
                            startRecording()
                        }
                        RecordState.Ongoing -> {
                            job?.cancel()
                            fakeData.value = mutableListOf()
                        }
                        else -> Unit
                    }
                    switchRecordState()
                }
            }
        }
    }


    private fun getDataSet(list: List<Entry>): LineDataSet {
        return LineDataSet(list, "Gravity fluctuation").apply {
            color = requireContext().getColor(R.color.purple_primary)
            lineWidth = 3F
            mode = LineDataSet.Mode.CUBIC_BEZIER
            setDrawCircles(false)
            valueFormatter = ChartValueFormatter()
        }
    }

    //TODO Move function to view model
    private fun switchRecordState() {
        when (recordState.value) {
            RecordState.Stopped -> {
                recordState.value = RecordState.Ongoing
            }
            RecordState.Ongoing -> {
                recordState.value = RecordState.Stopped
            }
            else -> Unit
        }
    }

    //TODO Test function delete when receive actual data
    private suspend fun startRecording() {

        entries.forEach {
            val newList = fakeData.value?.toMutableList() ?: mutableListOf()
            newList.add(it)
            fakeData.value = newList
            delay(1000)
        }
        switchRecordState()
    }


    companion object {

        @JvmStatic
        fun newInstance() = HeartRateFragment()
    }
}