package com.harman.wearosapp.app.ui.feature_screen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import androidx.wear.widget.WearableLinearLayoutManager
import com.harman.wearosapp.app.R
import com.harman.wearosapp.app.adapter.FeatureListAdapter
import com.harman.wearosapp.app.databinding.FragmentAppFeatureListBinding
import com.harman.wearosapp.app.ui.heart_rate_screen.HeartRateActivity
import com.harman.wearosapp.app.ui.hr_export_screen.ExportActivity
import com.harman.wearosapp.domain.model.FeatureModel
import java.lang.IllegalArgumentException
import kotlin.math.abs


class FeatureListFragment : Fragment() {

    private lateinit var binding: FragmentAppFeatureListBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppFeatureListBinding.inflate(inflater, container, false)

        val lnManagerCallBack = object : WearableLinearLayoutManager.LayoutCallback() {
            var relativeToCenter: Float = 0f
            var progressToCenter: Float = 0f
            override fun onLayoutFinished(child: View?, parent: RecyclerView) {
                child?.apply {
                    val offset = height.toFloat() / 2.0f / parent.height.toFloat()
                    relativeToCenter = y / parent.height + offset
                    progressToCenter = abs(0.5f - relativeToCenter)
                    val button = findViewById<AppCompatButton>(R.id.btFeatureButton)
                    button.background.alpha = if (progressToCenter < 0.15f) 255
                    else 100
                }
            }
        }
        val adapter = FeatureListAdapter { id ->
            startActivity(id)
        }
        binding.rvRecyclerView.adapter = adapter
        adapter.submitList(
            listOf(
                FeatureModel(
                    getString(R.string.mesure_hr),
                    id = Destinations.HRRecord.destinationId
                ),
                FeatureModel(
                    getString(R.string.export_hr),
                    id = Destinations.HRExport.destinationId
                ),
            )
        )
        binding.rvRecyclerView.layoutManager = WearableLinearLayoutManager(
            requireContext(),
            lnManagerCallBack
        )
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvRecyclerView)
        return binding.root
    }

    companion object {
        fun newInstance() = FeatureListFragment()
    }

    private fun startActivity(destinationID: Int) {
        val intent =  when (destinationID) {
            Destinations.HRRecord.destinationId -> Intent(requireContext(), HeartRateActivity::class.java)
            Destinations.HRExport.destinationId -> Intent(requireContext(), ExportActivity::class.java)
            else -> throw IllegalArgumentException("Destination doesn't exist")
        }
        startActivity(intent)
    }

}

enum class Destinations(val destinationId: Int) {
    HRRecord(destinationId = 0),
    HRExport(destinationId = 1)
}