package guepardoapps.lib.openweather.views

import android.annotation.SuppressLint
import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import guepardoapps.lib.openweather.R
import guepardoapps.lib.openweather.extensions.doubleFormat
import guepardoapps.lib.openweather.services.openweather.OpenWeatherService
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@SuppressLint("CheckResult", "SetTextI18n")
class CurrentWeatherView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private var weatherImageView: ImageView? = null
    private var descriptionTextView: TextView? = null
    private var temperatureTextView: TextView? = null
    private var humidityTextView: TextView? = null
    private var pressureTextView: TextView? = null

    private var subscriptions: Array<Disposable?> = arrayOf()

    init {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layoutInflater.inflate(R.layout.lib_current_weather, this, true)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        weatherImageView = findViewById(R.id.lib_current_weather_image)
        descriptionTextView = findViewById(R.id.lib_current_weather_description)
        temperatureTextView = findViewById(R.id.lib_current_weather_temperature)
        humidityTextView = findViewById(R.id.lib_current_weather_humidity)
        pressureTextView = findViewById(R.id.lib_current_weather_pressure)

        subscriptions = subscriptions.plus(
                OpenWeatherService.instance.weatherCurrentPublishSubject
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                { response ->
                                    val currentWeather = response.value
                                    if (currentWeather != null) {
                                        weatherImageView?.setImageResource(currentWeather.weatherCondition.wallpaperId)
                                        descriptionTextView?.text = currentWeather.description
                                        temperatureTextView?.text = "${currentWeather.temperature.doubleFormat(2)}${0x00B0.toChar()}C (${currentWeather.temperatureMin.doubleFormat(2)}${0x00B0.toChar()}C - ${currentWeather.temperatureMax.doubleFormat(2)}${0x00B0.toChar()}C)"
                                        humidityTextView?.text = "${currentWeather.humidity.doubleFormat(2)} %"
                                        pressureTextView?.text = "${currentWeather.pressure.doubleFormat(2)} mBar"
                                    }
                                },
                                { _ -> }
                        ))
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        subscriptions.forEach { x -> x?.dispose() }
    }
}
