package guepardoapps.lib.openweather.extensions

import guepardoapps.lib.openweather.annotations.JsonKey
import guepardoapps.lib.openweather.models.*
import kotlin.reflect.full.declaredMemberProperties

fun City.getJsonKey(): JsonKey {
    return this::class.annotations.find { it is JsonKey } as JsonKey
}

fun City.getPropertyJsonKey(propertyName: String): JsonKey {
    return this::class.declaredMemberProperties.find { it.name == propertyName }?.annotations?.find { it is JsonKey } as JsonKey
}

fun GeoLocation.getJsonKey(): JsonKey {
    return this::class.annotations.find { it is JsonKey } as JsonKey
}

fun GeoLocation.getPropertyJsonKey(propertyName: String): JsonKey {
    return this::class.declaredMemberProperties.find { it.name == propertyName }?.annotations?.find { it is JsonKey } as JsonKey
}

fun WeatherCurrent.getJsonKey(): JsonKey {
    return this::class.annotations.find { it is JsonKey } as JsonKey
}

fun WeatherCurrent.getPropertyJsonKey(propertyName: String): JsonKey {
    return this::class.declaredMemberProperties.find { it.name == propertyName }?.annotations?.find { it is JsonKey } as JsonKey
}

fun WeatherForecastPart.getJsonKey(): JsonKey {
    return this::class.annotations.find { it is JsonKey } as JsonKey
}

fun WeatherForecastPart.getPropertyJsonKey(propertyName: String): JsonKey {
    return this::class.declaredMemberProperties.find { it.name == propertyName }?.annotations?.find { it is JsonKey } as JsonKey
}
