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

internal fun City.isDefault(): Boolean {
    return id == 0 && name.isBlank() && country.isBlank() && population == 0 && geoLocation.isDefault()
}

internal fun GeoLocation.getJsonKey(): JsonKey {
    return this::class.annotations.find { it is JsonKey } as JsonKey
}

internal fun GeoLocation.getPropertyJsonKey(propertyName: String): JsonKey {
    return this::class.declaredMemberProperties.find { it.name == propertyName }?.annotations?.find { it is JsonKey } as JsonKey
}

internal fun GeoLocation.isDefault(): Boolean {
    return latitude == 720.0 && longitude == 720.0
}

internal fun UvIndex.getJsonKey(): JsonKey {
    return this::class.annotations.find { it is JsonKey } as JsonKey
}

internal fun UvIndex.getPropertyJsonKey(propertyName: String): JsonKey {
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
