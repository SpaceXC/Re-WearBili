package cn.spacexc.wearbili.remake.app

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import cn.leancloud.LCLogger
import cn.leancloud.LeanCloud
import cn.spacexc.bilibilisdk.BilibiliSdkManager
import cn.spacexc.bilibilisdk.data.DataManager
import cn.spacexc.bilibilisdk.sdk.user.profile.UserProfileInfo
import cn.spacexc.wearbili.common.LEANCLOUD_APP_ID
import cn.spacexc.wearbili.common.LEANCLOUD_APP_KEY
import cn.spacexc.wearbili.common.domain.color.parseColor
import cn.spacexc.wearbili.common.domain.data.DataStoreManager
import cn.spacexc.wearbili.remake.BuildConfig
import cn.spacexc.wearbili.remake.app.cache.domain.database.VideoCacheRepository
import cn.spacexc.wearbili.remake.app.crash.ui.CrashActivity
import cn.spacexc.wearbili.remake.app.crash.ui.PARAM_EXCEPTION_DESCRIPTION
import cn.spacexc.wearbili.remake.app.crash.ui.PARAM_EXCEPTION_STACKTRACE
import cn.spacexc.wearbili.remake.app.player.audio.AudioPlayerManager
import cn.spacexc.wearbili.remake.common.networking.CookiesManager
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import kotlin.system.exitProcess


/**
 * Created by XC-Qan on 2023/3/23.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

const val TAG = "Re:WearBili"
val APP_VERSION_NAME = Application.getVersionName()
val APP_VERSION_CODE = Application.getReleaseNumber()

var Context.isAudioServiceUp by mutableStateOf(false)

@HiltAndroidApp
class Application : android.app.Application(), Configuration.Provider {
    @Inject
    lateinit var dataManager: DataManager   //TODO 别在application下放这种对象啊喂！（虽然是迫不得已的啦，不过还是找个时间研究一下拿走罢（20230711
    @Inject
    lateinit var cookiesManager: CookiesManager

    @Inject
    lateinit var repository: VideoCacheRepository

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface HiltWorkerFactoryEntryPoint {
        fun workerFactory(): HiltWorkerFactory
    }

    override var workManagerConfiguration =
        Configuration.Builder()
            .setExecutor(Dispatchers.Default.asExecutor())
            .setWorkerFactory(
                EntryPoints.get(this, HiltWorkerFactoryEntryPoint::class.java).workerFactory()
            )
            .setTaskExecutor(Dispatchers.Default.asExecutor())
            .build()

    @SuppressLint("WearRecents")    //懒得弄了...反正国内厂商把东西都阉割得差不多了
    override fun onCreate() {
        super.onCreate()
        //System.load()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e(
                TAG,
                "onCreate: I caught an exception at thread ${thread.name}: ${throwable.message}"
            )
            throwable.printStackTrace()
            startActivity(Intent(this, CrashActivity::class.java).apply {
                putExtra(PARAM_EXCEPTION_STACKTRACE, throwable.stackTraceToString())
                putExtra(PARAM_EXCEPTION_DESCRIPTION, throwable.message)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
            exitProcess(2)
            //throw throwable
        }
        LeanCloud.initialize(
            this,
            LEANCLOUD_APP_ID,
            LEANCLOUD_APP_KEY,
            "https://mae7lops.lc-cn-n1-shared.com"
        )
        LeanCloud.setLogLevel(LCLogger.Level.ALL)
        val cachePath = File(filesDir, "/videoCaches/")
        cachePath.mkdir()
        val videoFile = File(filesDir, "video_silk_background.mp4")
        if (!videoFile.exists()) {
            videoFile.createNewFile()
            resources.openRawResource(cn.spacexc.wearbili.remake.R.raw.video_silk_background)
                .copyTo(videoFile.outputStream())
            /*val videoPath = "android.resource://$packageName/raw/video_silk_background.mp4"
            val rawVideoFile = File(videoPath)
            rawVideoFile.copyTo(videoFile)*/
        }
        val dataStore = DataStoreManager(this)
        BilibiliSdkManager.initSdk(
            dataManager = dataStore,
            cookiesManager = cookiesManager
        )
        Log.e(
            TAG,
            "20230708: 记住，在这里每一处不合理的地方，都有他的合理之处和存在于此的理由 ————XC于00:17有感而发"
        )
        Log.e(
            TAG,
            "20231104: 妥协不可耻还有用 ————XC于23:17有感而发"
        )
        checkIfAudioServiceIsUp()
        getThemeColor()
    }

    private fun checkIfAudioServiceIsUp() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                this@Application.isAudioServiceUp = AudioPlayerManager.isAudioPlayerOn()
                    //isForegroundServiceRunning(this@Application, AudioPlayerService::class.java)
                delay(800)
            }
        }
    }

    private fun getThemeColor() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                UserProfileInfo.getUserInfoByMid(208259).data?.data?.vip?.let { //笑点解析：
                    BilibiliPink = parseColor(it.nicknameColor)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                getThemeColor()
            }
        }
    }

    private fun isForegroundServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        return activityManager.getRunningServices(Int.MAX_VALUE)
            .find { it.service.className == serviceClass.name } != null
    }

    init {
        mApplication = this
    }

    companion object {
        private lateinit var mApplication: Application
        fun getApplication(): Application = mApplication

        fun getVersionName(): String {
            val packageInfo =
                getApplication().packageManager.getPackageInfo(getApplication().packageName, 0)
            return packageInfo.versionName
        }

        fun getReleaseNumber(): Long {
            val packageInfo =
                getApplication().packageManager.getPackageInfo(getApplication().packageName, 0)
            return if (Build.VERSION.SDK_INT >= 28) packageInfo.longVersionCode else packageInfo.versionCode.toLong()
        }

        fun getVersionCode(): Int {
            return BuildConfig.releaseNumber
        }
    }
}