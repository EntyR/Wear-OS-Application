package com.harman.wearosapp.data.repository

import com.harman.wearosapp.data.dao.IHRDao
import com.harman.wearosapp.data.data_source.HealthServicesManager
import com.harman.wearosapp.data.entity.HREntity
import com.harman.wearosapp.domain.model.HRRecordModel
import com.harman.wearosapp.domain.repository.IHealthRepository
import kotlinx.coroutines.flow.map

class HealthRepository(val dataSource: HealthServicesManager, val hrDao: IHRDao) : IHealthRepository {
    override suspend fun saveHRValueToDb(hrRecord: HRRecordModel) {
        hrDao.addNewRecord(
            HREntity(
                record = hrRecord.record, timestamp = hrRecord.timestamp
            )
        )
    }

    override fun getValueFromDb() = hrDao.getHRRecords().map {
        it.map { entity->
            HRRecordModel(
                entity.record, entity.timestamp
            )
        }
    }


    override fun getHRCensorFlow() =
        dataSource.heartRateMeasureFlow().map {
            it.map { data ->
                data.value.asDouble()
            }
        }

    override suspend fun deletePreviousValue() {
        hrDao.deleteAllRecords()
    }
}