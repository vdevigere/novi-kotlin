package org.novi.activations


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.novi.persistence.BaseActivationWithId
import java.text.SimpleDateFormat
import java.util.*

data class DateRangeData(val startDateTime: Date, val endDateTime: Date)

class DateTimeActivationWithId(
    id: Long? = null,
    configuration: String? = null,
    parsedConfig: DateRangeData? = null,
    dateFormat: String = "dd-MM-yyyy hh:mm"
) :
    BaseActivationWithId<DateRangeData>(id, configuration, parsedConfig) {

    // An explicit no-arg constructor is required despite the annotation @NoArg because of
    //https://youtrack.jetbrains.com/issue/KT-33502/No-arg-compiler-plugin-property-initializers-defined-in-the-primary-constructor-are-not-called


    private val simpleDateFormat = SimpleDateFormat(dateFormat)
    private val mapper: ObjectMapper = jacksonObjectMapper().setDateFormat(simpleDateFormat)

    override fun valueOf(s: String): DateRangeData = mapper.readValue<DateRangeData>(s)


    override fun evaluate(context: String): Boolean {
        val root = mapper.readTree(context)
        val contextMap = mapper.treeToValue(root, Map::class.java)
        val df = simpleDateFormat
        val currentDateTime = df.parse(contextMap[this.javaClass.canonicalName + ".currentDateTime"] as String)
        return parsedConfig?.startDateTime!! <= currentDateTime && parsedConfig?.endDateTime!! > currentDateTime
    }
}