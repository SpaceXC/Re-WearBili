package cn.spacexc.wearbili.remake.common.domain.network

import android.util.Log
import cn.spacexc.wearbili.remake.common.domain.log.logd
import cn.spacexc.wearbili.remake.common.domain.network.cookie.KtorCookiesManager
import cn.spacexc.wearbili.remake.common.domain.network.dto.BasicResponseDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import javax.inject.Inject

/**
 * Created by XC-Qan on 2023/3/22.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

const val USER_AGENT =
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36"
const val BASE_URL = "https://www.bilibili.com" //Referer必须符合^https?://(\S+).bilibili.com

class KtorNetworkUtils @Inject constructor(private val cookiesManager: KtorCookiesManager) {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            gson {

            }
        }
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
        install(HttpCookies) {
            storage = cookiesManager
        }
    }


    suspend inline fun <reified T> get(url: String): NetworkResponse<T> {
        val response = client.get(url) {
            userAgent(USER_AGENT)
            header("Referer", BASE_URL)
        }
        response.request.headers.logd("request headers for $url")
        return if (response.status == HttpStatusCode.OK) {
            NetworkResponse.Success(response.body())
        } else {
            val body = response.body<BasicResponseDto>()
            NetworkResponse.Failed(code = body.code, message = body.message, null)
        }
    }

    suspend inline fun <reified T> post(
        url: String,
        form: Map<String, String>
    ): NetworkResponse<T> {
        val params = Parameters.build {
            form.forEach {
                append(it.key, it.value)
            }
        }

        val response = client.submitForm(formParameters = params, url = url) {
            userAgent(USER_AGENT)
            header("Referer", BASE_URL)
        }
        return if (response.status == HttpStatusCode.OK) {
            NetworkResponse.Success(response.body())
        } else {
            val body = response.body<BasicResponseDto>()
            NetworkResponse.Failed(code = body.code, message = body.message, null)
        }
    }

    suspend fun getCookie(name: String): String? {
        return client.cookies(BASE_URL).find { it.name == name }?.value
    }

    suspend fun getCookies(): Map<String, String> {
        return client.cookies(BASE_URL).associate {
            Pair(first = it.name, second = it.value)
        }
    }
}