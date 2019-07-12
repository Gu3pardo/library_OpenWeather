package com.github.openweather.library.extensions

import com.github.openweather.library.annotations.JsonKey
import com.github.openweather.library.models.*
import kotlin.reflect.full.declaredMemberProperties

internal fun JsonModel.getJsonKey(): JsonKey = this::class.annotations.find { it is JsonKey } as JsonKey

internal fun JsonModel.getPropertyJsonKey(propertyName: String): JsonKey = this::class.declaredMemberProperties.find { it.name == propertyName }?.annotations?.find { it is JsonKey } as JsonKey

internal fun City.isDefault(): Boolean = id.isZero() && name.isBlank() && country.isBlank() && population.isZero() && coordinates.isDefault()

internal fun Coordinates.isDefault(): Boolean = lat == 720.0 && lon == 720.0
