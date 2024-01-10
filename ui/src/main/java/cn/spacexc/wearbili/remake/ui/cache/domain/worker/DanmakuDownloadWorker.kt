package cn.spacexc.wearbili.remake.app.cache.domain.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import cn.spacexc.bilibilisdk.sdk.video.info.VideoInfo
import cn.spacexc.wearbili.common.domain.log.logd
import cn.spacexc.wearbili.remake.app.TAG
import cn.spacexc.wearbili.remake.app.cache.domain.database.VideoCacheRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.Inflater
import javax.inject.Inject

/* 
WearBili Copyright (C) 2022 XC
This program comes with ABSOLUTELY NO WARRANTY.
This is free software, and you are welcome to redistribute it under certain conditions.
*/

/*
 * Created by XC on 2022/12/10.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

//其实本来确实应该和图片下载的合并一下的，但是我懒得抽离方法了，先将就用吧，后续在做专门的文件下载
//反正这个没有多线程下载什么的也不是很适合下载大文件
class DanmakuDownloadWorker(
    private val context: Context,
    private val workerParams: WorkerParameters,
) : CoroutineWorker(
    context,
    workerParams
) {
    @Inject
    lateinit var repository: VideoCacheRepository
    override suspend fun doWork(): Result {
        return try {
            val cid = workerParams.inputData.getLong("cid", 0)
            Log.d(TAG, "doWork: 我开始下载弹幕文件咯")
            val response = VideoInfo.getVideoDanmaku(cid)
            if (response == null) {
                Result.failure()
            }
            Log.d(TAG, "doWork: 我请求到网络文件咯")
            //File(context.filesDir, "downloadedContent/${workerParams.inputData.getString("cid")}")
            val dir = File(
                context.filesDir, "videoCaches/${
                    cid
                }"
            )
            if (!dir.exists()) {
                dir.mkdir()
            }
            val file = File(dir, "$cid.xml")
            file.path.logd("filePath")
            withContext(Dispatchers.IO) {
                val outputStream = FileOutputStream(file, false)
                val danmakuIs: ByteArray? = decompress(response!!.readBytes())
                outputStream.write(danmakuIs)
                outputStream.flush()
                Log.d(TAG, "doWork: 我把弹幕内容写入文件咯${file.path}")
                val cacheInfo = repository.getTaskInfoByVideoCid(cid)
                if (cacheInfo != null) {
                    repository.updateExistingTasks(
                        cacheInfo.copy(
                            downloadedDanmakuFileName = "$cid.xml"
                        )
                    )
                }
            }
            Result.success(workDataOf("danmakuPath" to file.path))
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    /**
     *  解压弹幕数据
     *  From CSDN
     */
    private fun decompress(data: ByteArray?): ByteArray? {
        var output: ByteArray?
        val decompresser = Inflater(true)
        decompresser.reset()
        decompresser.setInput(data)
        val o = data?.size?.let { ByteArrayOutputStream(it) }
        try {
            val buf = ByteArray(1024)
            while (!decompresser.finished()) {
                val i = decompresser.inflate(buf)
                o?.write(buf, 0, i)
            }
            output = o?.toByteArray()
        } catch (e: Exception) {
            output = data
            e.printStackTrace()
        } finally {
            try {
                o?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        decompresser.end()
        return output
    }
}