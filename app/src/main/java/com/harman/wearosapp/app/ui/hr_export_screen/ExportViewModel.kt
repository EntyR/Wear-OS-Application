package com.harman.wearosapp.app.ui.hr_export_screen

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harman.data.R
import com.harman.wearosapp.domain.repository.ExportState
import com.harman.wearosapp.domain.use_cases.DocumentsUseCase
import com.harman.wearosapp.domain.use_cases.HRUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class ExportViewModel(
    private val exportUseCase: DocumentsUseCase,
    private val hrUseCases: HRUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _exportState = MutableLiveData<ExportState>()
    val exportState: LiveData<ExportState> = _exportState

    fun exportAllRecord() = viewModelScope.launch(dispatcher) {
        _exportState.postValue(ExportState.Waiting())
        val records = hrUseCases.receiveLatestHeartRateUpdate().firstOrNull()?.filter { hr ->
            hr.record != 0.0
        }
        Log.e("TAG", "exportAllRecord: $records")
        records?.let {
            val result = exportUseCase.exportHRRecord(it)
            _exportState.postValue(result)
        } ?: _exportState.postValue(
            ExportState.Failure(
                Resources.getSystem().getString(R.string.error_msg)
            )
        )

    }
}