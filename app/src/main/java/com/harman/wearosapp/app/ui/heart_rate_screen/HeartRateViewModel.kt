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