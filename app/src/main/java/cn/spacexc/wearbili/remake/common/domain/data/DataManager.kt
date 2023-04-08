package cn.spacexc.wearbili.remake.common.domain.data

/**
 * Created by XC-Qan on 2023/3/23.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

interface DataManager {
    suspend fun saveString(name: String, value: String)
    suspend fun saveInt(name: String, value: Int)
    suspend fun saveBool(name: String, value: Boolean)

    suspend fun getString(name: String, defVal: String? = null): String?
    suspend fun getInt(name: String, defVal: Int? = null): Int?
    suspend fun getBool(name: String, defVal: Boolean? = null): Boolean?
}