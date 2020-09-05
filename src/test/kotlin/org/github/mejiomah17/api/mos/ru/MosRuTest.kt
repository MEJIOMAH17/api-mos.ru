package org.github.mejiomah17.api.mos.ru

import io.github.rybalkinsd.kohttp.dsl.httpGet
import io.github.rybalkinsd.kohttp.ext.url
import io.github.rybalkinsd.kohttp.interceptors.logging.HttpLoggingInterceptor
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldStartWith
import okhttp3.OkHttpClient
import org.github.mejiomah17.api.mos.ru.model.epd.EpdType
import org.junit.jupiter.api.Test
import java.io.File
import java.time.Month
import java.time.YearMonth
import java.util.concurrent.Executors
import java.util.concurrent.Future


class MosRuTest {

    @Test
    fun getFlats() {
        val login = System.getenv("login")
        val password = System.getenv("password")
        val logging = HttpLoggingInterceptor()
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        val mosRu = MosRu.create(login, password, client)
        val flats = mosRu.getFlats()
        flats shouldHaveSize  1
        val flat = flats.first().flat_id
        val epd=mosRu.getEpd(YearMonth.of(2020, 8), flat, EpdType.CURRENT)
        epd?.pdf shouldStartWith "https"

    }
}