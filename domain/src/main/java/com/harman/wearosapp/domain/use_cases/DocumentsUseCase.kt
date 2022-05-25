package com.harman.wearosapp.domain.use_cases

import com.harman.wearosapp.domain.model.HRRecordModel
import com.harman.wearosapp.domain.repository.ExportState
import com.harman.wearosapp.domain.repository.IDocumentsRepository
import com.harman.wearosapp.domain.repository.IHealthRepository

class DocumentsUseCase(private val healthRepository: IHealthRepository, private val docRepository: IDocumentsRepository) {
    suspend fun exportHRRecord(hrValues: List<HRRecordModel>): ExportState{
        val result = docRepository.saveHRValuesToCsv(hrValues)
        healthRepository.deletePreviousValue()
        return result
    }
}