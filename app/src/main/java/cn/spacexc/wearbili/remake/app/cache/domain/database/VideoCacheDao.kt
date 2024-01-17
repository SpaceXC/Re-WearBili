package cn.spacexc.wearbili.remake.app.cache.domain.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Created by XC-Qan on 2023/9/5.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@Dao
interface VideoCacheDao {
    @Query("SELECT * FROM video_caches")
    fun getAllTasks(): Flow<List<VideoCacheFileInfo>>

    @Query("SELECT * FROM video_caches WHERE state='completed'")
    fun getCompletedTasks(): Flow<List<VideoCacheFileInfo>>

    @Query("SELECT * FROM video_caches WHERE state!='completed'")
    fun getUncompletedTasks(): Flow<List<VideoCacheFileInfo>>

    @Query("SELECT * FROM video_caches WHERE cid=:videoCid")
    suspend fun getTaskInfoByVideoCid(videoCid: Long): VideoCacheFileInfo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewTasks(vararg newTasks: VideoCacheFileInfo)

    @Update
    suspend fun updateExistingTasks(vararg tasks: VideoCacheFileInfo)

    @Delete
    suspend fun deleteExistingTasks(vararg tasks: VideoCacheFileInfo)

    @Query("DELETE FROM video_caches")
    suspend fun deleteAllTasks()
}