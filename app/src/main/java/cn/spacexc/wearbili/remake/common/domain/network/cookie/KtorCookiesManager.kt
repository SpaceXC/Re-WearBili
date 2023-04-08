package cn.spacexc.wearbili.remake.common.domain.network.cookie

import cn.spacexc.wearbili.remake.common.domain.data.DataManager
import cn.spacexc.wearbili.remake.common.domain.data.DataStoreManager
import com.google.gson.Gson
import io.ktor.client.plugins.cookies.*
import io.ktor.http.*
import io.ktor.util.*
import io.ktor.util.date.*
import kotlinx.atomicfu.AtomicLong
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import kotlin.math.min

/**
 * Created by XC-Qan on 2023/3/25.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class KtorCookiesManager(private val dataManager: DataManager) : CookiesStorage {
    private var container: MutableList<Cookie> = mutableListOf()
    private val oldestCookie: AtomicLong = atomic(0L)
    private val mutex = Mutex()

    override suspend fun get(requestUrl: Url  /* 这就一个站的网络请求，不判断子域名了，反正都是通用的 */): List<Cookie> =
        mutex.withLock {
            refreshCookies()
            val date = GMTDate()
            if (date.timestamp >= oldestCookie.value) cleanup(date.timestamp)

            return@withLock container/*.filter { it.matches(requestUrl) }*/
        }

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie): Unit = mutex.withLock {
        refreshCookies()
        with(cookie) {
            if (name.isBlank()) return@withLock
        }

        container.removeAll { it.name == cookie.name /*&& it.matches(requestUrl)*/ }
        container.add(cookie.fillDefaults(requestUrl))
        cookie.expires?.timestamp?.let { expires ->
            if (oldestCookie.value > expires) {
                oldestCookie.value = expires
            }
        }
        saveCookies()

    }

    override fun close() {
    }

    private suspend fun cleanup(timestamp: Long) {
        refreshCookies()
        container.removeAll { cookie ->
            val expires = cookie.expires?.timestamp ?: return@removeAll false
            expires < timestamp
        }

        val newOldest = container.fold(Long.MAX_VALUE) { acc, cookie ->
            cookie.expires?.timestamp?.let { min(acc, it) } ?: acc
        }

        oldestCookie.value = newOldest
        saveCookies()
    }

    private suspend fun refreshCookies() {
        val cookiesString = dataManager.getString("cookies", "")
        val cookiesObj = Gson().fromJson(cookiesString, Cookies::class.java)
        container = cookiesObj?.cookies?.toMutableList() ?: mutableListOf()
        oldestCookie.value = cookiesObj?.oldest ?: 0
    }

    private suspend fun saveCookies() {
        val cookiesString = Gson().toJson(Cookies(container, oldestCookie.value))
        dataManager.saveString("cookies", cookiesString)
    }

    private fun Cookie.matches(requestUrl: Url): Boolean {
        val domain = domain?.toLowerCasePreservingASCIIRules()?.trimStart('.')
            ?: error("Domain field should have the default value")

        val path = with(path) {
            val current = path ?: error("Path field should have the default value")
            if (current.endsWith('/')) current else "$path/"
        }

        val host = requestUrl.host.toLowerCasePreservingASCIIRules()
        val requestPath = let {
            val pathInRequest = requestUrl.encodedPath
            if (pathInRequest.endsWith('/')) pathInRequest else "$pathInRequest/"
        }

        if (host != domain && (hostIsIp(host) || !host.endsWith(".$domain"))) {
            return false
        }

        if (path != "/" &&
            requestPath != path &&
            !requestPath.startsWith(path)
        ) return false

        return !(secure && !requestUrl.protocol.isSecure())
    }

    private fun Cookie.fillDefaults(requestUrl: Url): Cookie {
        var result = this

        if (result.path?.startsWith("/") != true) {
            result = result.copy(path = requestUrl.encodedPath)
        }

        if (result.domain.isNullOrBlank()) {
            result = result.copy(domain = requestUrl.host)
        }

        return result
    }

    private data class Cookies(
        val cookies: List<Cookie>,
        val oldest: Long
    )
}