package org.novi.activations


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.novi.core.BaseActivation
import java.text.SimpleDateFormat
import java.util.*

data class DateRangeData(val startDateTime: Date, val endDateTime: Date)

class DateTimeActivation(override var configuration: String? = "null") : BaseActivation<DateRangeData> {

    companion object {
        private const val DATE_FORMAT = "dd-MM-yyyy hh:mm"
        val mapper: ObjectMapper = jacksonObjectMapper().setDateFormat(SimpleDateFormat(DATE_FORMAT))
    }

    override fun valueOf(s: String?): DateRangeData {
        return if (s != null) mapper.readValue<DateRangeData>(s) else DateRangeData(Date(), Date())
    }


    override fun evaluate(context: String): Boolean {
        val root = mapper.readTree(context)
        val contextMap = mapper.treeToValue(root, Map::class.java)
        val df = SimpleDateFormat(DATE_FORMAT)
        val currentDateTime = df.parse(contextMap[this.javaClass.canonicalName + ".currentDateTime"] as String)
        return parsedConfig?.startDateTime!! <= currentDateTime && parsedConfig?.endDateTime!! > currentDateTime
    }
}