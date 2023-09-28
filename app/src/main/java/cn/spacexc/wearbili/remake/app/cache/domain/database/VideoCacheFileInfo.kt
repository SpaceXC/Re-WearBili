package cn.spacexc.wearbili.remake.app.cache.domain.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

/**
 * Created by XC-Qan on 2023/9/5.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@Entity(tableName = "video_caches")
@TypeConverters(MapTypeConverter::class)
data class VideoCacheFileInfo(
    @PrimaryKey @ColumnInfo(name = "cid") val videoCid: Long,
    @ColumnInfo(name = "cache_id") val cacheId: Long,
    @ColumnInfo(name = "url") val videoUrl: String,
    @ColumnInfo(name = "bvid") val videoBvid: String,
    @ColumnInfo(name = "name") val videoName: String,
    @ColumnInfo(name = "part_name") val videoPartName: String,
    @ColumnInfo(name = "uploader_name") val videoUploaderName: String,
    @ColumnInfo(name = "cover_url") val videoCover: String,
    @ColumnInfo(name = "file_size") val downloadFileSize: String = "",
    @ColumnInfo(name = "download_progress") val downloadProgress: Int = 0,
    @ColumnInfo(name = "is_completed") val isCompleted: Boolean = false,
    @ColumnInfo(name = "video_file_name") val downloadedVideoFileName: String = "",
    @ColumnInfo(name = "cover_file_name") val downloadedCoverFileName: String = "",
    @ColumnInfo(name = "danmaku_file_name") val downloadedDanmakuFileName: String = "$videoCid.xml",
    @ColumnInfo(name = "subtitle_urls") val videoSubtitleUrls: Map<String, String> = emptyMap(),
    @ColumnInfo(name = "subtitle_filenames") val downloadedSubtitleFileNames: Map<String, String> = emptyMap()
)