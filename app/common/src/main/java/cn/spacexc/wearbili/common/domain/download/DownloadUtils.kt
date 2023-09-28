package cn.spacexc.wearbili.common.domain.download

import android.content.Context
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.common.HttpOption
import java.io.File

/**
 * Created by XC-Qan on 2023/8/22.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

object DownloadUtils {
    fun Context.createDownloadTask(
        `object`: Any,
        fileUrl: String,
        path: String,
        fileName: String,
        requireHeader: Boolean
    ): Long {
        val downloadPath = File(filesDir, path)
        if (!downloadPath.exists()) {
            downloadPath.mkdir()

        }
        val downloadFile = File(downloadPath, fileName)
        downloadFile.createNewFile()
        return Aria.download(`object`)
            .load(fileUrl) //读取下载地址
            .option(HttpOption().apply {
                if (requireHeader) {
                    addHeader("Referer", "https://www.bilibili.com/")
                    addHeader("User-Agent", "Mozilla/5.0 BiliDroid/*.*.* (bbcallen@gmail.com)")
                }
            })
            .setFilePath(downloadFile.absolutePath) //设置文件保存的完整路径
            .create() //创建并启动下载
    }
}