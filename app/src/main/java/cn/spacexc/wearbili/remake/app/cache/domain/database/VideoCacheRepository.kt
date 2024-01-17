package cn.spacexc.wearbili.remake.app.cache.domain.database

import android.app.Application
import androidx.room.Room
import kotlinx.coroutines.flow.Flow

/**
 * Created by XC-Qan on 2023/9/5.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class VideoCacheRepository(
    application: Application
) {
    private val database = Room.databaseBuilder(
        context = application,
        klass = VideoCacheDataBase::class.java,
        name = "wearbili_videos"
    ).fallbackToDestructiveMigration().build()

    private val videoCacheDao = database.videoCacheDao()

    fun getAllTasks(): Flow<List<VideoCacheFileInfo>> = videoCacheDao.getAllTasks()
    fun getAllCompletedTasks(): Flow<List<VideoCacheFileInfo>> = videoCacheDao.getCompletedTasks()
    fun getAllUncompletedTasks(): Flow<List<VideoCacheFileInfo>> =
        videoCacheDao.getUncompletedTasks()


    suspend fun getTaskInfoByVideoCid(cid: Long): VideoCacheFileInfo? =
        videoCacheDao.getTaskInfoByVideoCid(cid)

    suspend fun insertNewTasks(vararg newTasks: VideoCacheFileInfo) =
        videoCacheDao.insertNewTasks(*newTasks)

    suspend fun updateExistingTasks(vararg tasks: VideoCacheFileInfo) =
        videoCacheDao.updateExistingTasks(*tasks)

    suspend fun deleteExistingTasks(vararg tasks: VideoCacheFileInfo) =
        videoCacheDao.deleteExistingTasks(*tasks)

    suspend fun deleteAllTasks() = videoCacheDao.deleteAllTasks()
}