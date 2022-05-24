package com.harman.wearosapp.domain.repository

sealed class ExportState(val msg: String? = null) {
    class Success(): ExportState()
    class Waiting(): ExportState()
    class Failure(msg: String? = null): ExportState(msg)
}