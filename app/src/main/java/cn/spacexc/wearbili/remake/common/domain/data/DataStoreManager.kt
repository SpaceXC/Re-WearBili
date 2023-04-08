package cn.spacexc.wearbili.remake.common.domain.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import cn.spacexc.wearbili.remake.app.Application
import kotlinx.coroutines.flow.first

/**
 * Created by XC-Qan on 2023/3/23.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

// At the top level of your kotlin file:
val Context.dataStore by preferencesDataStore(name = "appdata")

class DataStoreManager : DataManager {
    private val dataStore = Application.getApplication().dataStore

    override suspend fun saveString(name: String, value: String) {
        dataStore.edit { it[stringPreferencesKey(name)] = value }
    }

    override suspend fun saveInt(name: String, value: Int) {
        dataStore.edit { it[intPreferencesKey(name)] = value }
    }

    override suspend fun saveBool(name: String, value: Boolean) {
        dataStore.edit { it[booleanPreferencesKey(name)] = value }
    }

    override suspend fun getString(name: String, defVal: String?): String? {
        return dataStore.data.first()[stringPreferencesKey(name)] ?: defVal
    }

    override suspend fun getInt(name: String, defVal: Int?): Int? {
        return dataStore.data.first()[intPreferencesKey(name)] ?: defVal
    }

    override suspend fun getBool(name: String, defVal: Boolean?): Boolean? {
        return dataStore.data.first()[booleanPreferencesKey(name)] ?: defVal
    }
}