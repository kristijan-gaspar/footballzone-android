package hr.algebra.footballzone.api.dto.standing

import com.google.gson.annotations.SerializedName
import hr.algebra.footballzone.api.dto.standing.StandingsWrapperDto

data class StandingsApiResponseDto (

    @SerializedName("response") val response : List<StandingsWrapperDto>
)