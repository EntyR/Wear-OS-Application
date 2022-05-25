package com.harman.wearosapp.data.repository

import android.content.Context
import android.os.Environment
import com.harman.data.R
import com.harman.wearosapp.domain.model.HRRecordModel
import com.harman.wearosapp.domain.repository.ExportState
import com.harman.wearosapp.domain.repository.IDocumentsRepository
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException


class DocumentsRepository(private val context: Context): IDocumentsRepository {

    override fun saveHRValuesToCsv(hrRecords: List<HRRecordModel>): ExportState {
        if (hrRecords.isEmpty()) return ExportState.Failure(context.getString(R.string.no_value_msg))

        val root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        return try {
            val file = File(root, "HrRecord-${hrRecords.last().timestamp}.csv")
            val fw = FileWriter(file.absoluteFile)
            val bw = BufferedWriter(fw)
            hrRecords.forEach {
                bw.write(it.convertToCsv())
                bw.newLine()
            }
            bw.close()
            ExportState.Success()
        } catch (e: IOException) {
            e.printStackTrace()
            ExportState.Failure(context.getString(R.string.error_msg))
        }
    }
    companion object {
        fun HRRecordModel.convertToCsv(): String {
            return "${this.record},${this.timestamp}"
        }
    }
}