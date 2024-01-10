package cn.spacexc.wearbili.remake.common

import cn.spacexc.wearbili.common.domain.log.logd
import cn.spacexc.wearbili.remake.ui.cache.domain.database.VideoCacheRepository
import com.arialyy.annotations.DownloadGroup
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.download.DownloadEntity
import com.arialyy.aria.core.task.DownloadGroupTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by XC-Qan on 2023/9/6.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class AriaDownloadProgressSyncer(private val repository: VideoCacheRepository) {

    init {
        Aria.download(this).register()
    }

    @DownloadGroup.onTaskRunning
    fun onTaskRunning(task: DownloadGroupTask) {
        CoroutineScope(Dispatchers.IO).launch {
            val taskInfo =
                repository.getTaskInfoById(task.entity.id).logd("task ${task.key} running...")!!
            repository.updateExistingTasks(
                taskInfo.copy(
                    downloadProgress = task.percent,
                    downloadFileSize = task.convertFileSize
                )
            )

        }
    }

    @DownloadGroup.onTaskComplete
    fun onTaskComplete(task: DownloadGroupTask) {
        CoroutineScope(Dispatchers.IO).launch {
            val taskInfo =
                repository.getTaskInfoById(task.entity.id).logd("task ${task.key} completed")!!
            repository.updateExistingTasks(
                taskInfo.copy(
                    downloadProgress = 100,
                    downloadFileSize = task.convertFileSize,
                    isCompleted = true
                )
            )

        }
    }

    @DownloadGroup.onSubTaskStart
    fun onSubTaskStart(task: DownloadGroupTask, subEntity: DownloadEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            val taskInfo =
                repository.getTaskInfoById(task.entity.id).logd("task ${task.key} completed")!!
            when (subEntity.fileName.split(".").last()) {
                "mp4" -> {
                    repository.updateExistingTasks(
                        taskInfo.copy(
                            downloadedVideoFileName = subEntity.fileName
                        )
                    )
                }

                "json" -> {
                    val language =
                        taskInfo.videoSubtitleUrls.entries.find { it.value == subEntity.url }?.key
                    if (language != null) {
                        val map = taskInfo.downloadedSubtitleFileNames.toMutableMap()
                        map[language] = subEntity.fileName
                        repository.updateExistingTasks(
                            taskInfo.copy(
                                downloadedSubtitleFileNames = map
                            )
                        )
                    }
                }

                else -> {
                    repository.updateExistingTasks(
                        taskInfo.copy(
                            downloadedCoverFileName = subEntity.fileName
                        )
                    )
                }
            }
        }
    }

    fun onDestroy() {
        Aria.download(this).unRegister()
    }
}