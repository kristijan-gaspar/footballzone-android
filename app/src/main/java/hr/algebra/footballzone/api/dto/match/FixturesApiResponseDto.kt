package hr.algebra.footballzone.api.dto.match

import com.google.gson.annotations.SerializedName
import hr.algebra.footballzone.api.dto.match.FixtureWrapperDto

data class FixturesApiResponseDto(
    @SerializedName("response") val response : List<FixtureWrapperDto>

)