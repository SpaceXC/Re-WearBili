package cn.spacexc.wearbili.remake.app.player.videoplayer.mirroring.dlna

import org.fourthline.cling.model.meta.Device

/**
 * Created by XC-Qan on 2023/10/22.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

object WearbiliCastManager {
    private var currentDevice: Device<*, *, *>? = null

    fun connectTo(device: Device<*, *, *>) {
        currentDevice = device
    }

    fun disconnect() {
        currentDevice = null
    }

    fun currentDevice() = currentDevice
}