package guepardoapps.lib.openweather.example

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.openweather.library.extensions.airPollutionCurrentDateTime
import com.github.openweather.library.services.openweather.OpenWeatherService
import java.util.*

class FragmentAirPollution : Fragment() {

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        OpenWeatherService.instance.loadCarbonMonoxide(Calendar.getInstance().airPollutionCurrentDateTime(), 1)
        OpenWeatherService.instance.loadNitrogenDioxide(Calendar.getInstance().airPollutionCurrentDateTime(), 1)
        OpenWeatherService.instance.loadOzone(Calendar.getInstance().airPollutionCurrentDateTime(), 1)
        OpenWeatherService.instance.loadSulfurDioxide(Calendar.getInstance().airPollutionCurrentDateTime(), 1)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_air_pollution, container, false)
    }
}