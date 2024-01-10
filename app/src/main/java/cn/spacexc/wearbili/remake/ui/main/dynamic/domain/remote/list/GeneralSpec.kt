package cn.spacexc.wearbili.remake.ui.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class GeneralSpec(
    @SerializedName("pos_spec")
    val posSpec: PosSpec,
    @SerializedName("render_spec")
    val renderSpec: RenderSpec,
    @SerializedName("size_spec")
    val sizeSpec: SizeSpec
)