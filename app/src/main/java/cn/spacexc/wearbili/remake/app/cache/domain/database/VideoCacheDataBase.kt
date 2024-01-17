package cn.spacexc.wearbili.remake.app.cache.domain.database

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Created by XC-Qan on 2023/9/5.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@Database(entities = [VideoCacheFileInfo::class], version = 12, exportSchema = false)
abstract class
VideoCacheDataBase : RoomDatabase() {
    abstract fun videoCacheDao(): VideoCacheDao
}