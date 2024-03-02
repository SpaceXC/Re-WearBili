package cn.spacexc.wearbili.remake.app.cache.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cn.spacexc.wearbili.remake.app.cache.domain.database.VideoCacheFileInfo
import cn.spacexc.wearbili.remake.app.cache.domain.database.VideoCacheRepository
import cn.spacexc.wearbili.remake.common.ToastUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import javax.inject.Inject

/**
 * Created by XC-Qan on 2023/9/9.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@HiltViewModel
class CacheListViewModel @Inject constructor(
    private val repository: VideoCacheRepository, private val application: Application
) : AndroidViewModel(application) {
    val completedTasks = repository.getAllCompletedTasks()
    val unCompletedTasks = repository.getAllUncompletedTasks()

    fun deleteCache(cacheFileInfo: VideoCacheFileInfo) {
        viewModelScope.launch {
            val downloadPath = File(application.filesDir, "videoCaches/${cacheFileInfo.videoCid}")
            cacheFileInfo.downloadedSubtitleFileNames.forEach {
                try {
                    File(downloadPath, it.value).delete()
                } catch (e: IOException) {
                    ToastUtils.showText("删除字幕文件${it.value}失败")
                    e.printStackTrace()
                    return@launch
                }
            }
            val segments = (cacheFileInfo.videoDurationMillis / (6 * 60 * 1000)).toInt() + 1
            (1..segments).forEach { index ->
                try {
                    val danmakuFile =
                        File(downloadPath, "${cacheFileInfo.videoCid}.danmaku.seg$index")
                    danmakuFile.delete()
                } catch (e: IOException) {
                    ToastUtils.showText("删除第${index}段弹幕失败")
                    e.printStackTrace()
                    return@launch
                }
            }
            try {
                val coverFile = File(downloadPath, "${cacheFileInfo.videoCid}.cover.png")
                coverFile.delete()
            } catch (e: IOException) {
                ToastUtils.showText("删除封面文件失败")
                e.printStackTrace()
                return@launch
            }
            try {
                val videoFile = File(downloadPath, "${cacheFileInfo.videoCid}.video.mp4")
                videoFile.delete()
            } catch (e: IOException) {
                ToastUtils.showText("删除视频文件失败")
                e.printStackTrace()
                return@launch
            }

            try {
                downloadPath.deleteRecursively()
            } catch (e: IOException) {
                e.printStackTrace()
                //return@launch
            }

            repository.deleteExistingTasks(cacheFileInfo)
        }
    }
}