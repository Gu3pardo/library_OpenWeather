package guepardoapps.lib.openweather.example

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import guepardoapps.lib.openweather.services.openweather.OpenWeatherService

class FragmentCurrentWeather : Fragment() {

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        OpenWeatherService.instance.loadWeatherCurrent()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_current_weather, container, false)
    }
}