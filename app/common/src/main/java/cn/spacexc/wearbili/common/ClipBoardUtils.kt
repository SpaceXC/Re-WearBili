package cn.spacexc.wearbili.common

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.core.content.ContextCompat

/**
 * Created by XC-Qan on 2023/4/13.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

fun String.copyToClipboard(context: Context, label: String = "") {
    val clipboardManager: ClipboardManager =
        ContextCompat.getSystemService(
            context,
            ClipboardManager::class.java
        ) as ClipboardManager
    val clip: ClipData =
        ClipData.newPlainText(
            label,
            this
        )
    clipboardManager.setPrimaryClip(clip)
}