package com.harman.wearosapp.app.ui.heart_rate_screen

import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.shareIn

val entries = listOf(
    Entry(2f, 3f),
    Entry(3f, 2f),
    Entry(4f, 10f),
    Entry(5f, 3f),
    Entry(6f, 2f),
    Entry(7f, 10f),
    Entry(8f, 9f),
    Entry(9f, 10f),
    Entry(10f, 7f),
    Entry(11f, 10f),
)