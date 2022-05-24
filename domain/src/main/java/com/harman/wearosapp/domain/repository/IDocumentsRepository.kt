package com.harman.wearosapp.domain.repository

import com.harman.wearosapp.domain.model.HRRecordModel

interface IDocumentsRepository {
    fun saveHRValuesToCsv(hrRecords: List<HRRecordModel>): ExportState
}