package guepardoapps.lib.openweather.extensions

import guepardoapps.lib.openweather.annotations.JsonKey
import guepardoapps.lib.openweather.models.*
import kotlin.reflect.full.declaredMemberProperties

internal fun City.getJsonKey(): JsonKey {
    return this::class.annotations.find { it is JsonKey } as JsonKey
}

internal fun City.getPropertyJsonKey(propertyName: String): JsonKey {
    return this::class.declaredMemberProperties.find { it.name == propertyName }?.annotations?.find { it is JsonKey } as JsonKey
}

internal fun GeoLocation.getJsonKey(): JsonKey {
    return this::class.annotations.find { it is JsonKey } as JsonKey
}

internal fun GeoLocation.getPropertyJsonKey(propertyName: String): JsonKey {
    return this::class.declaredMemberProperties.find { it.name == propertyName }?.annotations?.find { it is JsonKey } as JsonKey
}

internal fun WeatherCurrent.getJsonKey(): JsonKey {
    return this::class.annotations.find { it is JsonKey } as JsonKey
}

internal fun WeatherCurrent.getPropertyJsonKey(propertyName: String): JsonKey {
    return this::class.declaredMemberProperties.find { it.name == propertyName }?.annotations?.find { it is JsonKey } as JsonKey
}

internal fun WeatherForecastPart.getJsonKey(): JsonKey {
    return this::class.annotations.find { it is JsonKey } as JsonKey
}

internal fun WeatherForecastPart.getPropertyJsonKey(propertyName: String): JsonKey {
    return this::class.declaredMemberProperties.find { it.name == propertyName }?.annotations?.find { it is JsonKey } as JsonKey
}
