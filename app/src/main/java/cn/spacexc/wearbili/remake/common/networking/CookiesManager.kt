package cn.spacexc.wearbili.remake.common.networking

import cn.spacexc.bilibilisdk.data.CookiesManager
import cn.spacexc.bilibilisdk.utils.UserUtils
import cn.spacexc.wearbili.common.CryptoManager
import cn.spacexc.wearbili.remake.common.networking.db.CookieEntity
import cn.spacexc.wearbili.remake.common.networking.db.CookiesRepository
import cn.spacexc.wearbili.remake.common.networking.db.toKtorCookie
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Cookie
import io.ktor.http.CookieEncoding
import io.ktor.http.Url
import kotlinx.coroutines.sync.Mutex
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CookiesManager(
    val repository: CookiesRepository,
    private val cryptoManager: CryptoManager
) : CookiesManager {
    private val mutex = Mutex()
    override suspend fun getCookieByName(name: String): Cookie? {
        return repository.dao.getCookieByName(name, UserUtils.mid())?.toKtorCookie(cryptoManager)
    }

    override suspend fun deleteAllCookies() {
        UserUtils.mid()?.let { repository.dao.deleteAll(it) }
    }

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        //不在这里保存
        /*if (cookie.name.isEmpty()) return
        mutex.withLock {
            if (cookie.name == "DedeUserID") {   //其实我觉得这里并不是很优雅的说
                UserUtils.addUser(cookie.value.toLong())
                UserUtils.setCurrentUid(cookie.value.toLong())
                val cookiesWithNoUid = repository.dao.getCookiesWithNoUid()
                cookiesWithNoUid.forEach { cookieEntity ->
                    repository.dao.updateCookie(cookieEntity.copy(uid = cookie.value.toLong()))
                }
            }
            repository.dao.addCookie(cookie.toCookieEntity(cryptoManager))
        }*/
    }

    override suspend fun HttpResponse.interceptAndSaveCookies() {
        val cookies = buildList {
            headers.getAll("set-cookie")?.forEach {
                add(parseCookie(it))
            }
        }
        val uid = if (cookies.any { it.name == "DedeUserID" }) {
            cookies.find { it.name == "DedeUserID" }!!.value.toLong()
        } else UserUtils.mid()
        if(uid != null) {
            cookies.map { it.uid = uid }
            UserUtils.addUser(uid)
            UserUtils.setCurrentUid(uid)
            cookies.forEach {
                repository.dao.addCookie(it)
            }
        }
    }

    private fun parseCookie(str: String): CookieEntity {
        val items = str.split("; ")
        val cookie = items[0].split("=")
        val name = cookie[0]
        val value = cookie[1]
        val cookieEntity =
            CookieEntity(name = name, value = value, encoding = CookieEncoding.RAW, uid = null)
        for (index in 1..<items.size) {
            val currentItem = items[index]
            if (currentItem.contains("=")) {
                val (key, itemValue) = currentItem.split("=")
                when (key.lowercase()) {
                    "domain" -> cookieEntity.domain = itemValue
                    "path" -> cookieEntity.path = itemValue
                    "expires" -> {
                        val dateString = "Sat, 04 Jan 2025 03:41:20 GMT"
                        val dateFormat =
                            SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.CHINA)

                        cookieEntity.expires = try {
                            val date: Date = dateFormat.parse(dateString)!!
                            val timestamp: Long = date.time
                            timestamp
                        } catch (e: Exception) {
                            e.printStackTrace()
                            null
                        }
                    }
                }
            } else {
                if (currentItem.lowercase() == "httponly") {
                    cookieEntity.httpOnly = true
                }
                if (currentItem.lowercase() == "secure") {
                    cookieEntity.secure = true
                }
            }
        }
        return cookieEntity
    }

    override fun close() {}

    override suspend fun get(requestUrl: Url): List<Cookie> {
        val list =
            repository.dao.getCookieByUid(UserUtils.mid()).map { it.toKtorCookie(cryptoManager) }
        println(list)
        return list
    }
}