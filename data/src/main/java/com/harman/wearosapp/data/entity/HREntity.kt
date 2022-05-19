package com.harman.wearosapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class HREntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val record: Double,
    val timestamp: Long
)