package guepardoapps.lib.openweather.example

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.openweather.library.extensions.airPollutionCurrentDateTime
import com.github.openweather.library.services.openweathermap.OpenWeatherMapService
import java.util.*

class FragmentInformation : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        OpenWeatherMapService.instance.loadCarbonMonoxide(Calendar.getInstance().airPollutionCurrentDateTime(), 1)
        OpenWeatherMapService.instance.loadNitrogenDioxide(Calendar.getInstance().airPollutionCurrentDateTime(), 1)
        OpenWeatherMapService.instance.loadOzone(Calendar.getInstance().airPollutionCurrentDateTime(), 1)
        OpenWeatherMapService.instance.loadSulfurDioxide(Calendar.getInstance().airPollutionCurrentDateTime(), 1)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_information, container, false)
}