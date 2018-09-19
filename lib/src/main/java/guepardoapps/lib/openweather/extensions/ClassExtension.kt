package guepardoapps.lib.openweather.extensions

import guepardoapps.lib.openweather.annotations.JsonKey
import guepardoapps.lib.openweather.models.*
import kotlin.reflect.full.declaredMemberProperties

internal fun AddressComponent.getJsonKey(): JsonKey {
    return this::class.annotations.find { it is JsonKey } as JsonKey
}

internal fun AddressComponent.getPropertyJsonKey(propertyName: String): JsonKey {
    return this::class.declaredMemberProperties.find { it.name == propertyName }?.annotations?.find { it is JsonKey } as JsonKey
}

internal fun AddressComponent.isDefault(): Boolean {
    return shortName.isBlank() && types.isEmpty() && longName.isBlank()
}

internal fun City.getJsonKey(): JsonKey {
    return this::class.annotations.find { it is JsonKey } as JsonKey
}

internal fun City.getPropertyJsonKey(propertyName: String): JsonKey {
    return this::class.declaredMemberProperties.find { it.name == propertyName }?.annotations?.find { it is JsonKey } as JsonKey
}

internal fun City.isDefault(): Boolean {
    return id == 0 && name.isBlank() && country.isBlank() && population == 0 && coordinates.isDefault()
}

internal fun City2.getJsonKey(): JsonKey {
    return this::class.annotations.find { it is JsonKey } as JsonKey
}

internal fun City2.getPropertyJsonKey(propertyName: String): JsonKey {
    return this::class.declaredMemberProperties.find { it.name == propertyName }?.annotations?.find { it is JsonKey } as JsonKey
}

internal fun City2.isDefault(): Boolean {
    return addressComponents.isEmpty() && geometry.isDefault() && types.isEmpty()
}

internal fun Coordinates.getJsonKey(): JsonKey {
    return this::class.annotations.find { it is JsonKey } as JsonKey
}

internal fun Coordinates.getPropertyJsonKey(propertyName: String): JsonKey {
    return this::class.declaredMemberProperties.find { it.name == propertyName }?.annotations?.find { it is JsonKey } as JsonKey
}

internal fun Coordinates.isDefault(): Boolean {
    return lat == 720.0 && lon == 720.0
}

internal fun Coordinates2.getJsonKey(): JsonKey {
    return this::class.annotations.find { it is JsonKey } as JsonKey
}

internal fun Coordinates2.getPropertyJsonKey(propertyName: String): JsonKey {
    return this::class.declaredMemberProperties.find { it.name == propertyName }?.annotations?.find { it is JsonKey } as JsonKey
}

internal fun Coordinates2.isDefault(): Boolean {
    return lat == 720.0 && lng == 720.0
}

internal fun Geometry.getJsonKey(): JsonKey {
    return this::class.annotations.find { it is JsonKey } as JsonKey
}

internal fun Geometry.getPropertyJsonKey(propertyName: String): JsonKey {
    return this::class.declaredMemberProperties.find { it.name == propertyName }?.annotations?.find { it is JsonKey } as JsonKey
}

internal fun Geometry.isDefault(): Boolean {
    return locationType.isBlank() && viewport.isDefault() && location.isDefault()
}

internal fun UvIndex.getJsonKey(): JsonKey {
    return this::class.annotations.find { it is JsonKey } as JsonKey
}

internal fun UvIndex.getPropertyJsonKey(propertyName: String): JsonKey {
    return this::class.declaredMemberProperties.find { it.name == propertyName }?.annotations?.find { it is JsonKey } as JsonKey
}

internal fun UvIndex.isDefault(): Boolean {
    return coordinates.isDefault()
}

internal fun Viewport.getJsonKey(): JsonKey {
    return this::class.annotations.find { it is JsonKey } as JsonKey
}

internal fun Viewport.getPropertyJsonKey(propertyName: String): JsonKey {
    return this::class.declaredMemberProperties.find { it.name == propertyName }?.annotations?.find { it is JsonKey } as JsonKey
}

internal fun Viewport.isDefault(): Boolean {
    return northeast.isDefault() && southwest.isDefault()
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
