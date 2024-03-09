package cn.spacexc.wearbili.common.domain.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import cn.spacexc.bilibilisdk.data.DataManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * Created by XC-Qan on 2023/3/23.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

// At the top level of your kotlin file:
val Context.dataStore by preferencesDataStore(name = "appdata")

class DataStoreManager(application: Context) : DataManager {
    private val dataStore = application.dataStore
    override fun getBoolFlow(name: String, defVal: Boolean?): Flow<Boolean> = dataStore.data
        .catch {
            //当读取数据遇到错误时，如果是 `IOException` 异常，发送一个 emptyPreferences 来重新使用
            //但是如果是其他的异常，最好将它抛出去，不要隐藏问题
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map {
            it[booleanPreferencesKey(name)] ?: defVal ?: false
        }

    override fun getStringFlow(name: String, defVal: String?): Flow<String> = dataStore.data
        .catch {
            //当读取数据遇到错误时，如果是 `IOException` 异常，发送一个 emptyPreferences 来重新使用
            //但是如果是其他的异常，最好将它抛出去，不要隐藏问题
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map {
            it[stringPreferencesKey(name)] ?: defVal ?: ""
        }

    override suspend fun saveString(name: String, value: String?) {
        if (value == null) {
            dataStore.edit { it.remove(stringPreferencesKey(name)) }
        } else {
            dataStore.edit { it[stringPreferencesKey(name)] = value }
        }
    }

    override suspend fun saveInt(name: String, value: Int) {
        dataStore.edit { it[intPreferencesKey(name)] = value }
    }

    override suspend fun saveBool(name: String, value: Boolean) {
        dataStore.edit { it[booleanPreferencesKey(name)] = value }
    }

    override suspend fun deleteString(name: String) {
        dataStore.edit { it.remove(stringPreferencesKey(name)) }
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

    companion object {
        private var mInstance: DataStoreManager? = null
        fun getInstance(context: Context): DataStoreManager {
            if (mInstance == null) mInstance = DataStoreManager(context)
            return mInstance!!
        }
    }
}