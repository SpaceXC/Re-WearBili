package cn.spacexc.wearbili.remake.common.networking

import cn.spacexc.bilibilisdk.data.CookiesManager
import cn.spacexc.bilibilisdk.utils.UserUtils
import cn.spacexc.wearbili.common.CryptoManager
import cn.spacexc.wearbili.remake.common.networking.db.CookiesRepository
import cn.spacexc.wearbili.remake.common.networking.db.toCookieEntity
import cn.spacexc.wearbili.remake.common.networking.db.toKtorCookie
import io.ktor.http.Cookie
import io.ktor.http.Url
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

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
        if (cookie.name.isEmpty()) return
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
        }
    }

    override fun close() {}

    override suspend fun get(requestUrl: Url): List<Cookie> {
        return repository.dao.getCookieByUid(UserUtils.mid()).map { it.toKtorCookie(cryptoManager) }
    }
}