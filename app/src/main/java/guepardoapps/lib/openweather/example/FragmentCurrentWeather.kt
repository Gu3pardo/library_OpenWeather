package guepardoapps.lib.openweather.example

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import es.dmoral.toasty.Toasty
import guepardoapps.lib.openweather.extensions.doubleFormat
import guepardoapps.lib.openweather.models.WeatherCurrent
import guepardoapps.lib.openweather.services.openweather.OpenWeatherService
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_current_weather.*

class FragmentCurrentWeather : Fragment() {
    private lateinit var currentWeather: WeatherCurrent
    private var subscriptions: Array<Disposable?> = arrayOf()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Activity) {
            subscriptions = subscriptions.plus(
                    OpenWeatherService.instance.weatherCurrentPublishSubject
                            .subscribeOn(Schedulers.io())
                            .subscribe(
                                    { response ->
                                        if (response.value != null) {
                                            handleOnCurrentWeather(response.value as WeatherCurrent)
                                        } else {
                                            if (isAdded) {
                                                context.runOnUiThread {
                                                    Toasty.warning(context, "weather current subscribe was not successfully", Toast.LENGTH_LONG).show()
                                                }
                                            }
                                        }
                                    },
                                    { _ -> }
                            ))

            OpenWeatherService.instance.loadWeatherCurrent()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_current_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDetach() {
        super.onDetach()
        subscriptions.forEach { x -> x?.dispose() }
        subscriptions = arrayOf()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    @SuppressLint("SetTextI18n")
    private fun handleOnCurrentWeather(currentWeather: WeatherCurrent) {
        this.currentWeather = currentWeather
        weather_description.text = this.currentWeather.description
        weather_temperature.text = "${this.currentWeather.temperature.doubleFormat(2)}${0x00B0.toChar()}C (${this.currentWeather.temperatureMin.doubleFormat(2)}${0x00B0.toChar()}C - ${this.currentWeather.temperatureMax.doubleFormat(2)}${0x00B0.toChar()}C)"
        weather_humidity.text = "${this.currentWeather.humidity.doubleFormat(2)} %"
        weather_pressure.text = "${this.currentWeather.pressure.doubleFormat(2)} mBar"
    }
}