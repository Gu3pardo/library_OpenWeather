package com.github.openweather.library.models

import com.github.openweather.library.annotations.JsonKey

@JsonKey("", "")
class NitrogenDioxideData : JsonModel {
    private val tag: String = NitrogenDioxideData::class.java.simpleName

    @JsonKey("data", "no2")
    var no2: NitrogenDioxideDataHolder = NitrogenDioxideDataHolder()

    @JsonKey("data", "no2_strat")
    var no2Strat: NitrogenDioxideDataHolder = NitrogenDioxideDataHolder()

    @JsonKey("data", "no2_trop")
    var no2Trop: NitrogenDioxideDataHolder = NitrogenDioxideDataHolder()

    override fun toString(): String = "{Class: $tag, No2: $no2, No2Strat: $no2Strat, No2Trop: $no2Trop}"
}