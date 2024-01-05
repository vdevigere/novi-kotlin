package org.novi.activations


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.novi.core.BaseActivation
import org.novi.core.NoArg
import java.text.SimpleDateFormat
import java.util.*

data class DateRangeData(val startDateTime: Date, val endDateTime: Date)


@NoArg
class DateTimeActivation(override var configuration: String, dateFormat: String ="dd-MM-yyyy hh:mm") : BaseActivation<DateRangeData> {
    private val simpleDateFormat = SimpleDateFormat(dateFormat)
    private val mapper: ObjectMapper = jacksonObjectMapper().setDateFormat(simpleDateFormat)

    override fun valueOf(s: String): DateRangeData {
        return mapper.readValue<DateRangeData>(s)
    }


    override fun evaluate(context: String): Boolean {
        val root = mapper.readTree(context)
        val contextMap = mapper.treeToValue(root, Map::class.java)
        val df = simpleDateFormat
        val currentDateTime = df.parse(contextMap[this.javaClass.canonicalName + ".currentDateTime"] as String)
        return parsedConfig.startDateTime <= currentDateTime && parsedConfig.endDateTime > currentDateTime
    }
}