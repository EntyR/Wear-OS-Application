package com.harman.wearosapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.harman.wearosapp.data.entity.HREntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IHRDao {


    @Query("SELECT * from HREntity")
    fun getHRRecords(): Flow<List<HREntity>>

    @Query("DELETE from HREntity")
    suspend fun deleteAllRecords()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewRecord(record: HREntity)
}
