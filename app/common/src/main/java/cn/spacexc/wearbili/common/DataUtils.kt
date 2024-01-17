package cn.spacexc.wearbili.common

fun formatBytes(bytes: Long): String {
    if (bytes <= 0) {
        return "0B"
    }

    val units = arrayOf("B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB")
    var size = bytes.toDouble()
    var unitIndex = 0

    while (size > 1024 && unitIndex < units.size - 1) {
        size /= 1024
        unitIndex++
    }

    val formattedSize = if (size % 1 == 0.0) {
        // 如果 size 是整数，则直接返回整数部分
        size.toInt().toString()
    } else {
        // 否则返回带有两位小数的格式化字符串
        "%.2f".format(size)
    }

    return "$formattedSize${units[unitIndex]}"
}