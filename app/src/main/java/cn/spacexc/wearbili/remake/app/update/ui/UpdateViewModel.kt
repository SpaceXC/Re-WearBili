package cn.spacexc.wearbili.remake.app.update.ui

import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageInstaller
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.FileProvider
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.AndroidViewModel
import cn.spacexc.wearbili.common.domain.download.DownloadUtils.createDownloadTask
import cn.spacexc.wearbili.common.domain.log.logd
import cn.spacexc.wearbili.remake.app.update.UpdateReceiver
import cn.spacexc.wearbili.remake.common.ToastUtils
import com.arialyy.annotations.Download
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.task.DownloadTask
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.io.IOException
import kotlin.random.Random


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

    init {
        Aria.init(application)
        Aria.download(this).register();
    }

    fun download(url: String, versionCode: Int) {
        val downloadPath = File(application.filesDir, "/updatePackages")
        if (!downloadPath.exists()) {
            downloadPath.mkdir()

        }
        val downloadFile = File(downloadPath, "Update-Version#$versionCode.apk")
        if (downloadFile.exists()) {
            ToastUtils.showText("apk已经下载过了哦！让我们直接安装叭～", context = application)
            installPackage(downloadFile)
        } else {
            downloadUrl = url
            val downloadTaskId = application.createDownloadTask(
                `object` = this,
                fileUrl = url,
                path = "/updatePackages",
                fileName = "Update-Version#$versionCode.apk",
                requireHeader = false
            )
            isDownloading = true
        }
    }

    @Download.onTaskRunning
    fun onDownloadTaskRunning(task: DownloadTask) {
        if (task.key == downloadUrl) {
            _downloadProgress.value = task.percent
            _downloadSpeed.value = task.convertSpeed
            _downloadTimeLeft.value = task.convertTimeLeft
        }
    }

    @Download.onTaskComplete
    fun onDownloadComplete(task: DownloadTask) {
        logd("Download Completed! Prepare to install...")
        /*val file = File(task.filePath)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val uri = FileProvider.getUriForFile(application, "${application.packageName}.fileprovider", file)
        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        startActivity(application, intent, null)*/
        val file = File(task.filePath)
        val apkUri =
            FileProvider.getUriForFile(application, "${application.packageName}.fileprovider", file)
        application.contentResolver.openInputStream(apkUri).use { apkStream ->
            val session = with(application.packageManager.packageInstaller) {
                val params = PackageInstaller.SessionParams(
                    PackageInstaller.SessionParams.MODE_FULL_INSTALL
                )
                openSession(createSession(params))
            }
            val document = DocumentFile.fromSingleUri(application, apkUri)
                ?: throw IOException()
            session.openWrite("NAME", 0, document.length()).use { sessionStream ->
                apkStream?.copyTo(sessionStream)
                session.fsync(sessionStream)
            }
            val pi = PendingIntent.getBroadcast(
                application, Random.nextInt(),
                Intent(application, UpdateReceiver::class.java),
                if (VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                else PendingIntent.FLAG_UPDATE_CURRENT
            )
            session.commit(pi.intentSender)
            logd("end")
        }
        if (VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val file = File(task.filePath)
            val apkUri = FileProvider.getUriForFile(
                application,
                "${application.packageName}.fileprovider",
                file
            )
            application.contentResolver.openInputStream(apkUri).use { apkStream ->
                val session = with(application.packageManager.packageInstaller) {
                    val params = PackageInstaller.SessionParams(
                        PackageInstaller.SessionParams.MODE_FULL_INSTALL
                    )
                    openSession(createSession(params))
                }
                val document = DocumentFile.fromSingleUri(application, apkUri)
                    ?: throw IOException()
                session.openWrite("NAME", 0, document.length()).use { sessionStream ->
                    apkStream?.copyTo(sessionStream)
                    session.fsync(sessionStream)
                }
                val pi = PendingIntent.getBroadcast(
                    application, Random.nextInt(),
                    Intent(application, UpdateReceiver::class.java),
                    if (VERSION.SDK_INT >= Build.VERSION_CODES.S)
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                    else PendingIntent.FLAG_UPDATE_CURRENT
                )
                session.commit(pi.intentSender)
            }
        } /*else {
            @Suppress("DEPRECATION")
            Intent(Intent.ACTION_INSTALL_PACKAGE).apply {
                setDataAndType(Storage.getFileUri(apk),
                    browserActivity.getString(R.string.mimetype_apk)
                )
                putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
                putExtra(Intent.EXTRA_INSTALLER_PACKAGE_NAME, application.packageName)
                if (Version.isNougat) {
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                }
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }.also {
                try {
                    startActivity(it)
                } catch (anf: ActivityNotFoundException) {
                    try {
                        startActivity(it.setAction(Intent.ACTION_VIEW))
                    } catch (ignored: ActivityNotFoundException) { }
                }
            }
        }*/
    }

    private fun installPackage(file: File) {
        //val file = File(filePath)
        /*val apkUri = FileProvider.getUriForFile(application, "${application.packageName}.fileprovider", file)
        application.contentResolver.openInputStream(apkUri).use { apkStream ->
            val session = with (application.packageManager.packageInstaller) {
                val params = PackageInstaller.SessionParams(
                    PackageInstaller.SessionParams.MODE_FULL_INSTALL
                )
                openSession(createSession(params))
            }
            val document = DocumentFile.fromSingleUri(application, apkUri)
                ?: throw IOException()
            session.openWrite("NAME", 0, document.length()).use { sessionStream ->
                apkStream?.copyTo(sessionStream)
                session.fsync(sessionStream)
            }
            val pi = PendingIntent.getBroadcast(
                application, Random.nextInt(),
                Intent(application, UpdateReceiver::class.java),
                if (VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                else PendingIntent.FLAG_UPDATE_CURRENT
            )
            session.commit(pi.intentSender)
            logd("end")
        }*/
        val intent = Intent(Intent.ACTION_VIEW).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
            if (VERSION.SDK_INT >= VERSION_CODES.N) {
                //flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                val apkUri = FileProvider.getUriForFile(
                    application,
                    "${application.packageName}.fileprovider",
                    file
                )
                setDataAndType(apkUri, "application/vnd.android.package-archive")
            } else {
                setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
            }
        }
        application.startActivity(intent)
    }
}