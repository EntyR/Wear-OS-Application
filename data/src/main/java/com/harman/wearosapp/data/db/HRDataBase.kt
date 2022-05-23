package com.harman.wearosapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.harman.wearosapp.data.dao.IHRDao
import com.harman.wearosapp.data.entity.HREntity


@Database(entities = [HREntity::class], version = 1)
abstract class HRDataBase : RoomDatabase() {
    abstract fun hrDao(): IHRDao

}
