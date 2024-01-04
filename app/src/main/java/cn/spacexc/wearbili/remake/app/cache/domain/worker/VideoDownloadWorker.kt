/*
package cn.spacexc.wearbili.remake.app.cache.domain.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import rxhttp.RxHttpPlugins
import rxhttp.wrapper.annotation.OkClient
import rxhttp.wrapper.param.RxHttp
import java.io.File


class VideoDownloadWorker(
    private val appContext: Context,
    private val params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        params.inputData.getString("videoUrl")?.let { videoUrl ->
            val downloadPath = File(appContext.filesDir, "/videoCaches/${it.first.second}")
            if (downloadPath.exists()) {
                downloadPath.delete()
            } else {
                downloadPath.mkdir()
            }
            if(videoUrl.isNotEmpty()) {
                RxHttp.get(videoUrl)
                    .addHeader("")
            }
        }
    }
}
*/
