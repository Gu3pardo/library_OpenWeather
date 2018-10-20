package guepardoapps.lib.openweather.models

import guepardoapps.lib.openweather.annotations.JsonKey
import guepardoapps.lib.openweather.common.Constants
import guepardoapps.lib.openweather.enums.ForecastListType
import guepardoapps.lib.openweather.enums.WeatherCondition
import guepardoapps.lib.openweather.extensions.integerFormat
import java.util.*

@JsonKey("list", "weather")
class WeatherForecastPart {
    private val tag: String = WeatherForecastPart::class.java.simpleName

    @JsonKey("weather", "main")
    var main: String = Constants.String.Empty
    var weatherCondition: WeatherCondition = WeatherCondition.Null

    @JsonKey("weather", "description")
    var description: String = Constants.String.Empty

    @JsonKey("weather", "icon")
    var weatherDefaultIcon: String = Constants.String.Empty

    @JsonKey("main", "temp")
    var temperature: Double = Constants.Defaults.Zero.toDouble()

    @JsonKey("main", "temp_min")
    var temperatureMin: Double = Constants.Defaults.Zero.toDouble()

    @JsonKey("main", "temp_max")
    var temperatureMax: Double = Constants.Defaults.Zero.toDouble()

    @JsonKey("main", "temp_kf")
    var temperatureKf: Double = Constants.Defaults.Zero.toDouble()

    @JsonKey("main", "pressure")
    var pressure: Double = Constants.Defaults.Zero.toDouble()

    @JsonKey("main", "sea_level")
    var pressureSeaLevel: Double = Constants.Defaults.Zero.toDouble()

    @JsonKey("main", "grnd_level")
    var pressureGroundLevel: Double = Constants.Defaults.Zero.toDouble()

    @JsonKey("main", "humidity")
    var humidity: Double = Constants.Defaults.Zero.toDouble()

    @JsonKey("clouds", "all")
    var cloudsAll: Int = Constants.Defaults.Zero

    @JsonKey("wind", "speed")
    var windSpeed: Double = Constants.Defaults.Zero.toDouble()

    @JsonKey("wind", "deg")
    var windDegree: Double = Constants.Defaults.Zero.toDouble()

    @JsonKey(Constants.String.Empty, "dt")
    var dateTime: Calendar = Calendar.getInstance()

    var listType: ForecastListType = ForecastListType.Null

    override fun toString(): String {
        return "{Class: $tag, " +
                "Main: $main, " +
                "Description: $description, " +
                "Temperature: $temperature, " +
                "TemperatureMin: $temperatureMin, " +
                "TemperatureMax: $temperatureMax, " +
                "TemperatureKf: $temperatureKf, " +
                "Humidity: $humidity, " +
                "Pressure: $pressure, " +
                "SeaLevel: $pressureSeaLevel, " +
                "GroundLevel: $pressureGroundLevel, " +
                "CloudsAll: $cloudsAll, " +
                "WindSpeed: $windSpeed, " +
                "WindDegree: $windDegree, " +
                "DateTime: $dateTime, " +
                "WeatherDefaultIcon: $weatherDefaultIcon, " +
                "WeatherCondition: $weatherCondition, " +
                "ListType: $listType, " +
                "Day: ${dateTime.get(Calendar.DAY_OF_MONTH)}, " +
                "Month: ${dateTime.get(Calendar.MONTH) + 1}," +
                "Year: ${dateTime.get(Calendar.YEAR)}, " +
                "Hour: ${dateTime.get(Calendar.HOUR_OF_DAY)}" +
                "Minute: ${dateTime.get(Calendar.MINUTE)}" +
                "Date:${dateTime.get(Calendar.DAY_OF_MONTH).integerFormat(2)}.${(dateTime.get(Calendar.MONTH) + 1).integerFormat(2)}.${dateTime.get(Calendar.YEAR).integerFormat(4)}" +
                "Time:${dateTime.get(Calendar.HOUR_OF_DAY).integerFormat(2)}:${dateTime.get(Calendar.MINUTE).integerFormat(2)}}"
    }
}