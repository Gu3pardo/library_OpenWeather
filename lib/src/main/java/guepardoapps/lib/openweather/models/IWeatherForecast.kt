package guepardoapps.lib.openweather.models

import java.io.Serializable

interface IWeatherForecast : Serializable {
    fun getCity(): ICity

    fun getList(): Array<IWeatherForecastPart>
}