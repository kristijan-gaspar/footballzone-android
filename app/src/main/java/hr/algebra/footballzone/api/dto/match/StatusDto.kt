package hr.algebra.footballzone.api.dto.match

import com.google.gson.annotations.SerializedName

data class StatusDto (
    @SerializedName("long") val long : String,
    @SerializedName("short") val short : String,
    @SerializedName("elapsed") val elapsed : Int,
    @SerializedName("extra") val extra : String
)
