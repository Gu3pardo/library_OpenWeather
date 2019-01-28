package com.github.openweather.library.extensions

import com.github.openweather.library.annotations.JsonKey
import com.github.openweather.library.common.Constants
import com.github.openweather.library.models.*
import kotlin.reflect.full.declaredMemberProperties

internal fun JsonModel.getJsonKey(): JsonKey = this::class.annotations.find { it is JsonKey } as JsonKey

internal fun JsonModel.getPropertyJsonKey(propertyName: String): JsonKey = this::class.declaredMemberProperties.find { it.name == propertyName }?.annotations?.find { it is JsonKey } as JsonKey

internal fun AddressComponent.isDefault(): Boolean = shortName.isBlank() && types.isEmpty() && longName.isBlank()

internal fun CarbonMonoxide.isDefault(): Boolean = coordinates.isDefault() && data.isEmpty()

internal fun CarbonMonoxideData.isDefault(): Boolean = precision.isZero() && pressure.isZero() && value.isZero()

internal fun City.isDefault(): Boolean = id.isZero() && name.isBlank() && country.isBlank() && population.isZero() && coordinates.isDefault()

internal fun City2.isDefault(): Boolean = addressComponents.isEmpty() && geometry.isDefault() && types.isEmpty()

internal fun Coordinates.isDefault(): Boolean = lat == Constants.Defaults.Coordinates && lon == Constants.Defaults.Coordinates

internal fun Coordinates2.isDefault(): Boolean = lat == Constants.Defaults.Coordinates && lng == Constants.Defaults.Coordinates

internal fun Coordinates3.isDefault(): Boolean = latitude == Constants.Defaults.Coordinates && longitude == Constants.Defaults.Coordinates

internal fun Geometry.isDefault(): Boolean = locationType.isBlank() && viewport.isDefault() && location.isDefault()

internal fun NitrogenDioxide.isDefault(): Boolean = coordinates.isDefault() && data.isDefault()

internal fun NitrogenDioxideData.isDefault(): Boolean = no2.isDefault() && no2Strat.isDefault() && no2Trop.isDefault()

internal fun NitrogenDioxideDataHolder.isDefault(): Boolean = precision.isZero() && value.isZero()

internal fun Ozone.isDefault(): Boolean = coordinates.isDefault() && data.isZero()

internal fun SulfurDioxide.isDefault(): Boolean = coordinates.isDefault() && data.isEmpty()

internal fun SulfurDioxideData.isDefault(): Boolean = precision.isZero() && pressure.isZero() && value.isZero()

internal fun UvIndex.isDefault(): Boolean = coordinates.isDefault()

internal fun Viewport.isDefault(): Boolean = northeast.isDefault() && southwest.isDefault()
