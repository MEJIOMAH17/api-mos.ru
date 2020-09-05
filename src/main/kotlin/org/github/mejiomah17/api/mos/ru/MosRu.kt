package org.github.mejiomah17.api.mos.ru

import Epd
import Flat
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.rybalkinsd.kohttp.client.defaultHttpClient
import io.github.rybalkinsd.kohttp.dsl.context.HttpPostContext
import io.github.rybalkinsd.kohttp.dsl.httpPost
import io.github.rybalkinsd.kohttp.ext.url
import okhttp3.OkHttpClient
import org.github.mejiomah17.api.mos.ru.model.epd.EpdType
import org.github.mejiomah17.api.mos.ru.model.epd.RidResult
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder

class MosRu internal constructor(
    private val sessionId: String,
    private val guid: String,
    private val token: String,
    private val client: OkHttpClient
) {
    companion object {
        //unknown prefix in each request. May be app version or something like this.
        private val token = "887033d0649e62a84f80433e823526a1"
        private val mapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


        /**
         * login in format 79161234567
         */
        fun create(login: String, password: String, client: OkHttpClient = defaultHttpClient): MosRu {
            val guid = guid()
            val result = httpPost(client) {
                url("https://emp.mos.ru/v1.1/auth/virtualLogin?token=$token")
                body {
                    json {
                        "auth" to json {
                            "login" to login
                            "password" to password
                            "guid" to guid
                        }
                    }
                }
            }.result()

            val sessionId = mapper.readTree(result)["result"]["session_id"].toString().replace("\"", "")
            return MosRu(sessionId = sessionId, guid = guid, token = token, client = client)
        }
    }


    fun getFlats(): List<Flat> {
        return post {
            url("https://emp.mos.ru/v1.0/flat/get?token=$token")
            body {
                json {
                    "auth" to json {
                        "session_id" to sessionId
                    }
                }
            }
        }
    }


    /**
     * null if epd does not exist
     */
    fun getEpd(period: YearMonth, flatId: String, type: EpdType = EpdType.CURRENT): Epd? {
        val formatter = DateTimeFormatter.ofPattern("01.MM.yyyy")


        val ridResult: RidResult = post {
            url("https://emp.mos.ru/v1.0/eepd/get?token=$token")
            body {
                json {
                    "auth" to json {
                        "session_id" to sessionId
                    }
                    "period" to period.format(formatter)
                    "flat_id" to flatId
                    "type" to type.value
                }
            }
        }

        var result: JsonNode
        do {
            //I don't know what is logic, but they send same requests each 2 seconds until success
            Thread.sleep(2000)
            val content = httpPost(client) {
                url("https://emp.mos.ru/v1.0/eepd/get?token=$token")
                body {
                    json {
                        "auth" to json {
                            "session_id" to sessionId
                        }
                        "period" to period.format(formatter)
                        "flat_id" to flatId
                        "type" to type.value
                        "rid" to ridResult.rid
                    }
                }
            }.body!!.string()

            val jsonContent = mapper.readTree(content)
            if (jsonContent.has("errorCode") && jsonContent["errorCode"].toString() != "0") {
                return null
            }
            result = jsonContent["result"]

        } while (!result.has("pdf"))

        return mapper.readValue(result.toString())
    }

    private inline fun <reified T> post(noinline init: HttpPostContext.() -> Unit): T {
        val content = httpPost(client, init).result()
        val result = mapper.readTree(content)["result"].toString()
        return mapper.readValue<T>(result)
    }
}