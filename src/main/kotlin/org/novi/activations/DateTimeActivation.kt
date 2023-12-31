package org.novi.activations


import org.novi.core.BaseActivation
import java.text.SimpleDateFormat
import java.util.Date
import com.fasterxml.jackson.module.kotlin.*
data class DateRangeData(val startDateTime: Date, val endDateTime: Date)

class DateTimeActivation(override var configuration: DateRangeData? = DateRangeData(Date(), Date())) : BaseActivation<DateRangeData> {

    val DATE_FORMAT = "dd-MM-yyyy hh:mm"

    override fun valueOf(configStr: String): BaseActivation<DateRangeData> {
        val mapper = jacksonObjectMapper()
        mapper.setDateFormat(SimpleDateFormat(DATE_FORMAT))
        val dateRange: DateRangeData = if (configStr != null) mapper.readValue(configStr) else DateRangeData(Date(), Date())
        return DateTimeActivation(dateRange)
    }

    override fun evaluate(context: String): Boolean {
        TODO("Not yet implemented")
    }
}