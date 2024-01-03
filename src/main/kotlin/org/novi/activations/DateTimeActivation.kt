package org.novi.activations


import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.novi.core.BaseActivation
import java.text.SimpleDateFormat
import java.util.*

data class DateRangeData(val startDateTime: Date, val endDateTime: Date)

class DateTimeActivation(override var configuration: DateRangeData = DateRangeData(Date(), Date())) :
    BaseActivation<DateRangeData> {

    constructor(configStr: String) : this(parseStr(configStr))

    companion object {
        private const val DATE_FORMAT = "dd-MM-yyyy hh:mm"
        val mapper = jacksonObjectMapper().setDateFormat(SimpleDateFormat(DATE_FORMAT))
        fun parseStr(configStr: String): DateRangeData {
            return mapper.readValue(configStr) as DateRangeData
        }
    }

    override fun evaluate(context: String): Boolean {
        val root = mapper.readTree(context)
        val contextMap = mapper.treeToValue(root, Map::class.java)
        val df = SimpleDateFormat(DATE_FORMAT)
        val currentDateTime = df.parse(contextMap[this.javaClass.canonicalName + ".currentDateTime"] as String)
        return configuration.startDateTime.compareTo(currentDateTime) <= 0 && configuration.endDateTime.compareTo(
            currentDateTime
        ) > 0
    }
}