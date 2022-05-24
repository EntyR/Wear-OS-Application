package com.harman.wearosapp.app.ui.heart_rate_screen

import androidx.lifecycle.*
import com.github.mikephil.charting.data.Entry
import com.harman.wearosapp.app.hr_service.HRService
import com.harman.wearosapp.app.other.getSecondsFromMillis
import com.harman.wearosapp.domain.use_cases.HRUseCase

class HeartRateViewModel(
    private val hrUseCase: HRUseCase
) : ViewModel() {

    private val _recordState = MutableLiveData(
        if (HRService.isAlive) RecordState.Waiting
        else RecordState.Stopped
    )
    val recordState: LiveData<RecordState> = _recordState

    private val _chartBounds: MutableLiveData<ChartBounds> = MutableLiveData()
    val chartBounds: LiveData<ChartBounds> = _chartBounds

    private val _heartIconState: MutableLiveData<HeartIconState> = MutableLiveData()
    val heartIconState: LiveData<HeartIconState> = _heartIconState

    fun switchHeartIconState(){
        when(heartIconState.value){
            HeartIconState.Full -> _heartIconState.value = HeartIconState.Inline
            HeartIconState.Inline -> _heartIconState.value = HeartIconState.Full
            else -> _heartIconState.value = HeartIconState.Inline
        }
    }

    fun updateChartYBounds(maxBounds: Float, minBounds: Float) {
        val updateMax = chartBounds.value?.yMax?.let {
            it > maxBounds
        }?: true
        if (updateMax)
            _chartBounds.value = ChartBounds(
                maxBounds + 5, minBounds - 5
            )
        val updateMin = chartBounds.value?.yMin?.let {
            it < minBounds
        }?: true
        if (updateMin)
            _chartBounds.value = ChartBounds(
                maxBounds +5, minBounds -5
            )
    }

    fun receiveHeartRateMeasurement() =
        hrUseCase.receiveLatestHeartRateUpdate().asLiveData().map { list ->
            list.sortedBy { it.timestamp }.filterIndexed { index, _ ->
                index >= list.size - 10
            }.filter { it.record != 0.0 }.map {
                Entry(getSecondsFromMillis(it.timestamp).toFloat(), it.record.toFloat())
            }

        }

    fun switchRecordState(state: RecordState? = null) {
        if (state != null) {
            _recordState.value = state
            return
        }
        when (recordState.value) {
            RecordState.Stopped -> {
                _recordState.value = RecordState.Waiting
            }
            RecordState.Ongoing -> {
                _recordState.value = RecordState.Stopped
            }
            RecordState.Waiting -> {
                _recordState.value = RecordState.Stopped
            }
            else -> Unit
        }
    }
}