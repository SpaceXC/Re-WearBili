package cn.spacexc.wearbili.remake.app.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import cn.spacexc.wearbili.remake.app.Application
import cn.spacexc.wearbili.remake.common.data.appConfigurationDataStore
import cn.spacexc.wearbili.remake.proto.settings.AppConfiguration
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

/**
 * Created by XC-Qan on 2023/4/6.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

object SettingsManager {
    private val context = Application.getApplication()
    private val configurationDataStore = context.appConfigurationDataStore

    fun getConfiguration() = runBlocking { configurationDataStore.data.first() }
    val configuration
        @Composable get() = configurationDataStore.data.collectAsState(initial = getConfiguration())

    suspend fun updateConfiguration(newConfiguration: AppConfiguration.() -> AppConfiguration) {
        configurationDataStore.updateData { currentConfig ->
            currentConfig.newConfiguration()
        }
    }
}

val LocalConfiguration = staticCompositionLocalOf<AppConfiguration> {
    error("No AppConfiguration provided")
}

@Composable
fun ProvideConfiguration(
    content: @Composable () -> Unit
) {
    val configuration by SettingsManager.configuration
    CompositionLocalProvider(LocalConfiguration provides configuration) {
        content()
    }
}