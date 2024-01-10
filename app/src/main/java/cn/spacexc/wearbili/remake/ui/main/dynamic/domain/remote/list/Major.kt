package cn.spacexc.wearbili.remake.ui.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class Major(
    @SerializedName("archive")
    val archive: Archive?,
    @SerializedName("pgc")
    val pgc: PgcMajor,
    @SerializedName("draw")
    val draw: Draw?,
    @SerializedName("type")
    val type: String
)