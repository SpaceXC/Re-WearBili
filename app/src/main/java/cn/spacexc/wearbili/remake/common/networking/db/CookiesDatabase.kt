package cn.spacexc.wearbili.remake.common.networking.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CookieEntity::class], version = 3, exportSchema = false)
abstract class CookiesDatabase : RoomDatabase() {
    abstract fun dao(): CookiesDao
}