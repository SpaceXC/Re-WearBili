package cn.spacexc.wearbili.common.domain.network

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import cn.spacexc.bilibilisdk.network.APP_KEY
import cn.spacexc.bilibilisdk.network.APP_SEC
import cn.spacexc.wearbili.common.EncryptUtils
import cn.spacexc.wearbili.common.domain.log.logd
import cn.spacexc.wearbili.common.domain.network.cookie.KtorCookiesManager
import cn.spacexc.wearbili.common.domain.network.dto.BasicResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.cookies.cookies
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.readBytes
import io.ktor.client.statement.request
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.http.contentLength
import io.ktor.http.userAgent
import io.ktor.serialization.gson.gson
import io.ktor.serialization.kotlinx.protobuf.protobuf
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.isEmpty
import io.ktor.utils.io.core.readBytes
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import java.io.File
import java.net.URLEncoder
import java.util.TreeMap
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
    @OptIn(ExperimentalSerializationApi::class)
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            gson {
                serializeNulls()
                enableComplexMapKeySerialization()
            }
            protobuf()
        }
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
        install(HttpCookies) {
            storage = cookiesManager
        }
        expectSuccess = true

    }

    val downloadClient = HttpClient(CIO) {
        install(HttpTimeout)
        engine {
            requestTimeout = 0
        }
    }

    private val noRedirectClient = HttpClient(CIO) {
        followRedirects = false
    }


    suspend inline fun <reified T> get(url: String): NetworkResponse<T> {
        return try {
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
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkResponse.Failed(code = -1, message = e.message ?: "Unknown error", null)
        }
    }

    suspend fun getBytes(
        url: String,
        builder: HttpRequestBuilder.() -> Unit = {}
    ): ByteArray? {
        return try {
            val response = client.get(url) {
                userAgent(cn.spacexc.bilibilisdk.network.USER_AGENT)
                header("Referer", cn.spacexc.bilibilisdk.network.BASE_URL)
                builder()
            }
            return response.readBytes()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend inline fun <reified T> getWithAppSign(
        host: String,
        origParams: String,
        appKey: String = APP_KEY,
        appSec: String = APP_SEC,
        builder: HttpRequestBuilder.() -> Unit = {}
    ): NetworkResponse<T> {
        var temp = origParams
        temp += "&appkey=$appKey"
        temp = temp.split("&").sorted().joinToString(separator = "&")
        val sign = cn.spacexc.bilibilisdk.utils.EncryptUtils.md5("$temp$appSec")
        val param = "$temp&sign=$sign"
        return get(url = "$host?$param", builder = builder)
    }

    suspend fun getRedirectUrl(url: String): String? {
        //if(response.status != HttpStatusCode.Found) return null
        return noRedirectClient.get(url).headers["location"]
    }

    suspend inline fun <reified T> get(
        url: String,
        builder: HttpRequestBuilder.() -> Unit
    ): NetworkResponse<T> {
        return try {
            val response = client.get(url) {
                userAgent(USER_AGENT)
                header("Referer", BASE_URL)
                builder()
            }
            response.request.headers.logd("request headers for $url")
            return if (response.status == HttpStatusCode.OK) {
                NetworkResponse.Success(response.body())
            } else {
                val body = response.body<BasicResponseDto>()
                NetworkResponse.Failed(code = body.code, message = body.message, null)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkResponse.Failed(code = -1, message = e.message ?: "Unknown error", null)
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
        return try {
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
        } /*catch (e: SocketException) {
            NetworkResponse.Failed(code = -1, message = e.message ?: "Unknown error", null)
        } catch (e: JsonSyntaxException) {
            NetworkResponse.Failed(code = -1, message = e.message ?: "Unknown error", null)
        } catch (e: JsonConvertException) {
            NetworkResponse.Failed(code = -1, message = e.message ?: "Unknown error", null)
        } catch (e: UnresolvedAddressException) {
            NetworkResponse.Failed(code = -1, message = e.message ?: "Unknown error", null)
        }*/ catch (e: Exception) {
            e.printStackTrace()
            NetworkResponse.Failed(code = -1, message = e.message ?: "Unknown error", null)
        }
    }

    /**
     * @param onDownloadFinished 其实这个回调只是为了传回一个结果而已，毕竟lambda内不能直接return嘛，，，
     */
    suspend fun downloadFile(
        url: String,
        file: File,
        onProgressUpdate: suspend (Float) -> Unit,
        onDownloadFinished: suspend (Boolean) -> Unit
    ) {
        runBlocking {
            downloadClient.prepareGet(url) {
                header("User-Agent", "Mozilla/5.0 BiliDroid/*.*.* (bbcallen@gmail.com)")
                header("Referer", "https://bilibili.com/")
            }.execute { httpResponse ->
                try {
                    val channel: ByteReadChannel = httpResponse.body()
                    while (!channel.isClosedForRead) {
                        val packet =
                            channel.readRemaining(/*DEFAULT_BUFFER_SIZE.toLong()*/1024 * 1024)
                        while (!packet.isEmpty) {
                            val bytes = packet.readBytes()
                            file.appendBytes(bytes)
                            onProgressUpdate(
                                file.length().toFloat() / (httpResponse.contentLength()
                                    ?: 1).toInt()
                            )
                        }
                    }
                    onDownloadFinished(true)
                } catch (e: Exception) {
                    onDownloadFinished(false)
                }
            }
        }
        //downloadClient.close()
    }

    suspend inline fun <reified T> postWithAppSign(
        host: String,
        form: Map<String, String>,
        appKey: String = APP_KEY,
        appSec: String = APP_SEC,
    ): NetworkResponse<T> {
        val signedParams = appSign(form.toMutableMap(), appKey, appSec)
        return post(url = host, form = signedParams)


        /*val temp = form.toMutableMap()//.toSortedMap(compareBy { it.first() })
        temp += Pair("appkey", appKey)
        val final = temp.toSortedMap(compareBy { it.first() }).toMutableMap()
        val sign = EncryptUtils.md5(
            "${
                final.entries.sortedBy { it.key }.joinToString(
                    separator = "&",
                    transform = { "${it.key}=${it.value}" })
            }$appSec"
        )
        final += Pair("sign", sign)
        final.logd("final params")
        return post(url = host, form = final)*/
    }

    fun appSign(
        params: MutableMap<String, String>,
        appKey: String = APP_KEY,
        appsec: String = APP_SEC
    ): Map<String, String> {
        // 为请求参数进行 APP 签名
        params["appkey"] = appKey
        // 按照 key 重排参数
        val sortedParams: Map<String?, String?> = TreeMap(params)
        // 序列化参数
        val queryBuilder = StringBuilder()
        for (pair in sortedParams) {
            if (queryBuilder.isNotEmpty()) {
                queryBuilder.append('&')
            }
            queryBuilder
                .append(URLEncoder.encode(pair.key, "utf-8"))
                .append('=')
                .append(URLEncoder.encode(pair.value, "utf-8"))
        }
        val sign = EncryptUtils.md5(queryBuilder.append(appsec).toString())
        params["sign"] = sign
        return params
    }

    suspend fun getCookie(name: String): String? {
        return client.cookies(BASE_URL).find { it.name == name }?.value
    }

    suspend fun getCookies(): Map<String, String> {
        return client.cookies(BASE_URL).associate {
            Pair(first = it.name, second = it.value)
        }
    }

    suspend fun getImageBitmap(url: String): ImageBitmap? {
        val bytes = getBytes(url) ?: return null
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size).asImageBitmap()
    }
}