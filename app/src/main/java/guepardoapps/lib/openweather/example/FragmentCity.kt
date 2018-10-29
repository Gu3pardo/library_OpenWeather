package guepardoapps.lib.openweather.example

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.openweather.library.services.openweather.OpenWeatherService

class FragmentCity : Fragment() {

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        OpenWeatherService.instance.loadCityData(getString(R.string.openweather_city))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_city, container, false)
    }
}