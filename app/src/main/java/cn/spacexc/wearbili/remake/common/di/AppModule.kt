package cn.spacexc.wearbili.remake.common.di

import cn.spacexc.bilibilisdk.data.DataManager
import cn.spacexc.wearbili.common.domain.data.DataStoreManager
import cn.spacexc.wearbili.common.domain.network.KtorNetworkUtils
import cn.spacexc.wearbili.common.domain.network.cookie.KtorCookiesManager
import cn.spacexc.wearbili.common.domain.qrcode.QRCodeUtil
import cn.spacexc.wearbili.remake.app.Application
import cn.spacexc.wearbili.remake.app.cache.domain.database.VideoCacheRepository
import cn.spacexc.wearbili.remake.app.player.videoplayer.danmaku.DanmakuGetter
import cn.spacexc.wearbili.remake.app.settings.SettingsManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by XC-Qan on 2023/3/29.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideApplication(): Application = Application.getApplication()

    @Provides
    @Singleton
    fun provideDataStoreManager(application: Application): DataStoreManager =
        DataStoreManager.getInstance(application)

    @Provides
    @Singleton
    fun providesCookiesManager(dataManager: DataManager): KtorCookiesManager =
        KtorCookiesManager(dataManager)

    @Provides
    @Singleton
    fun providesNetworkUtils(cookiesManager: KtorCookiesManager): KtorNetworkUtils =
        KtorNetworkUtils(cookiesManager)

    @Provides
    @Singleton
    fun providesQrcodeUtils(): QRCodeUtil = QRCodeUtil()

    @Provides
    @Singleton
    fun providesSettingsManager(): SettingsManager = SettingsManager

    @Provides
    @Singleton
    fun provideVideoCacheRepository(application: Application): VideoCacheRepository =
        VideoCacheRepository(application)

    @Provides
    fun provideDanmakuGetter(networkUtils: KtorNetworkUtils): DanmakuGetter =
        DanmakuGetter(networkUtils)
}