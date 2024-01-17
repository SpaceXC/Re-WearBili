package cn.spacexc.wearbili.remake.app.update.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


/**
 * Created by XC-Qan on 2023/8/22.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class UpdateViewModel(private val application: Application) : AndroidViewModel(application) {
    var isDownloading by mutableStateOf(false)

    private val _downloadProgress = MutableStateFlow(0)
    val downloadProgress = _downloadProgress.asStateFlow()
    private val _downloadSpeed = MutableStateFlow("")
    val downloadSpeed = _downloadSpeed.asStateFlow()
    private val _downloadTimeLeft = MutableStateFlow("")
    val downloadTimeLeft = _downloadTimeLeft.asStateFlow()

    private var downloadUrl = ""


    fun download(url: String, versionCode: Int) {
        /*val downloadPath = File(application.filesDir, "/updatePackages")
        if (!downloadPath.exists()) {
            downloadPath.mkdir()

        }
        val downloadFile = File(downloadPath, "Update-Version#$versionCode.apk")
        if (downloadFile.exists()) {
            ToastUtils.showText("apk已经下载过了哦！让我们直接安装叭～", context = application)
            installPackage(downloadFile)
        } else {
            downloadUrl = url

            isDownloading = true
        }*/
    }

}