package org.github.mejiomah17.api.mos.ru

import okhttp3.Response
import org.apache.commons.lang.StringEscapeUtils
import java.util.*

internal fun Response.result(): String {
    return StringEscapeUtils.unescapeJava(body!!.string())
}

internal fun guid(): String {
    return UUID.randomUUID().toString().replace("-", "")
}


