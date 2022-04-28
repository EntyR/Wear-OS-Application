package com.harman.wearosapp.app.watchface
import android.graphics.drawable.Icon
import androidx.wear.watchface.complications.data.*
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService
import com.harman.wearosapp.app.R

/**
 * Provides a value from data to be displayed on complication.
 *
 * TODO display value from data base
 */
class HRComplicationDataSource : SuspendingComplicationDataSourceService() {

    override fun getPreviewData(type: ComplicationType): ComplicationData {
        return ShortTextComplicationData.Builder(
            text = PlainComplicationText.Builder(text = "60").build(),
            contentDescription = PlainComplicationText.Builder(text = "Short Text version of Number.")
                .build()
        ).setTapAction(null).build()
    }

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData? {

        //TODO replace with real value
        val number =  0
        val resultString = if (number == 0) "No Data" else number.toString()

        return when (request.complicationType) {

            ComplicationType.SHORT_TEXT -> ShortTextComplicationData.Builder(
                text = PlainComplicationText.Builder(text = resultString)
                    .build(),
                contentDescription = PlainComplicationText
                    .Builder(text = "Short Text version of Number.").build()
            ).setMonochromaticImage(
                    MonochromaticImage.Builder(
                        Icon.createWithResource(
                            this,
                            R.drawable.ic_heart)
                    ).build()
                )
                .build()

            else -> null
        }
    }


}
