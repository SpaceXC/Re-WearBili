package cn.spacexc.wearbili.common

/**
 * Created by XC-Qan on 2023/12/24.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

fun Number?.ifNullOrZero(then: () -> Number) = if (this == null || this == 0) then() else this

fun Float.ifNaN(then: () -> Float) = if (this.isNaN()) then() else this