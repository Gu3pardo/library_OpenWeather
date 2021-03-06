package guepardoapps.lib.openweather.example

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.openweather.library.services.openweathermap.OpenWeatherMapService

class FragmentForecastWeather : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        OpenWeatherMapService.instance.loadWeatherForecast()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_forecast_weather, container, false)
}