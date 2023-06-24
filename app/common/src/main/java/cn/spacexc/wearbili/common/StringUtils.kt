package cn.spacexc.wearbili.common

/*
 * Created by XC on 2022/11/2.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

fun String?.ifNullOrEmpty(string: () -> String): String {
    return if (this.isNullOrEmpty()) {
        string()
    } else {
        this
    }
}