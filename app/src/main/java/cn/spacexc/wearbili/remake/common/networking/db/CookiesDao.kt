package cn.spacexc.wearbili.remake.common.networking.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CookiesDao {
    @Query("select * from cookieentity")
    suspend fun getAllCookies(): List<CookieEntity>

    @Query("select * from cookieentity where domain=:domain")
    suspend fun getCookieByDomain(domain: String): List<CookieEntity>

    @Query("select * from cookieentity where uid=:uid")
    suspend fun getCookieByUid(uid: Long?): List<CookieEntity>

    @Query("select * from cookieentity where name=:name and uid=:uid")
    suspend fun getCookieByName(name: String, uid: Long?): CookieEntity?

    @Query("select * from cookieentity where uid is null or uid is 0")
    suspend fun getCookiesWithNoUid(): List<CookieEntity>

    @Insert
    suspend fun addCookie(cookie: CookieEntity)

    @Delete
    suspend fun deleteCookie(cookie: CookieEntity)

    @Update
    suspend fun updateCookie(cookie: CookieEntity)

    @Query("delete from cookieentity where uid=:uid")
    suspend fun deleteAll(uid: Long)
}