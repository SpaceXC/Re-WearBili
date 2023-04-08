package cn.spacexc.wearbili.remake.common.di

import cn.spacexc.wearbili.remake.common.domain.data.DataManager
import cn.spacexc.wearbili.remake.common.domain.data.DataStoreManager
import cn.spacexc.wearbili.remake.common.domain.network.KtorNetworkUtils
import cn.spacexc.wearbili.remake.common.domain.network.cookie.KtorCookiesManager
import cn.spacexc.wearbili.remake.common.domain.qrcode.QRCodeUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
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
    fun provideDataStoreManager(): DataStoreManager = DataStoreManager()

    @Provides
    @Singleton
    fun providesCookiesManager(dataManager: DataManager): KtorCookiesManager = KtorCookiesManager(dataManager)

    @Provides
    @Singleton
    fun providesNetworkUtils(cookiesManager: KtorCookiesManager): KtorNetworkUtils = KtorNetworkUtils(cookiesManager)

    @Provides
    @Singleton
    fun providesQrcodeUtils(): QRCodeUtil = QRCodeUtil()
}