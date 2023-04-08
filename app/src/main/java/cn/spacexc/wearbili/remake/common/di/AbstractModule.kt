package cn.spacexc.wearbili.remake.common.di

import cn.spacexc.wearbili.remake.common.domain.data.DataManager
import cn.spacexc.wearbili.remake.common.domain.data.DataStoreManager
import dagger.Binds
import dagger.Module
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
abstract class AbstractModule {
    @Binds
    @Singleton
    abstract fun bindDataManager(
        dataStoreManager: DataStoreManager
    ): DataManager
}