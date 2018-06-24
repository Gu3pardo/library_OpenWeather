package guepardoapps.lib.openweather.models

import guepardoapps.lib.openweather.R

class WeatherForecast(private val city: ICity,
                      private val list: Array<IWeatherForecastPart>,
                      val wallpaperId: Int = R.drawable.weather_wallpaper_dummy) : IWeatherForecast {

    private val tag: String = WeatherForecast::class.java.simpleName

    override fun getCity(): ICity {
        return city
    }

    override fun getList(): Array<IWeatherForecastPart> {
        return list
    }

    override fun toString(): String {
        return "{Class: $tag, City: $city, List: $list, WallpaperId: $wallpaperId}"
    }
}