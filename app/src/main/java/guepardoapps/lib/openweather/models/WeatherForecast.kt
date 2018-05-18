package guepardoapps.lib.openweather.models

class WeatherForecast(private val city: ICity,
                      private val wallpaperId: Int,
                      private val list: Array<IWeatherForecastPart>) : IWeatherForecast {

    override fun getCity(): ICity {
        return city
    }

    override fun getWallpaperId(): Int {
        return wallpaperId
    }

    override fun getList(): Array<IWeatherForecastPart> {
        return list
    }
}