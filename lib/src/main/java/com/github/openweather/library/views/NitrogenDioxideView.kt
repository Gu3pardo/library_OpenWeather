package com.github.openweather.library.views

import android.annotation.SuppressLint
import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.TextView
import com.github.openweather.library.R
import com.github.openweather.library.extensions.DDMMYYYY
import com.github.openweather.library.extensions.airPollutionCurrentDateTime
import com.github.openweather.library.extensions.doubleFormat
import com.github.openweather.library.extensions.hhmmss
import com.github.openweather.library.services.openweathermap.OpenWeatherMapService
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

@SuppressLint("CheckResult", "SetTextI18n")
class NitrogenDioxideView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private var coordinatesTextView: TextView? = null
    private var datetimeTextView: TextView? = null
    private var valueTextView: TextView? = null
    private var valueStratTextView: TextView? = null
    private var valueTropTextView: TextView? = null
    private var reloadImageButton: ImageButton? = null

    private var subscriptions: Array<Disposable?> = arrayOf()

    init {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layoutInflater.inflate(R.layout.lib_nitrogendioxide, this, true)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        coordinatesTextView = findViewById(R.id.lib_nitrogen_dioxide_coordinates)
        datetimeTextView = findViewById(R.id.lib_nitrogen_dioxide_datetime)
        valueTextView = findViewById(R.id.lib_nitrogen_dioxide_value)
        valueStratTextView = findViewById(R.id.lib_nitrogen_dioxide_strat_value)
        valueTropTextView = findViewById(R.id.lib_nitrogen_dioxide_trop_value)
        reloadImageButton = findViewById(R.id.lib_nitrogen_dioxide_reload)
        reloadImageButton?.setOnClickListener {
            OpenWeatherMapService.instance.loadNitrogenDioxide(Calendar.getInstance().airPollutionCurrentDateTime(), 1)
        }

        subscriptions = subscriptions.plus(OpenWeatherMapService.instance.nitrogenDioxidePublishSubject
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { response ->
                            val nitrogenDioxide = response?.value
                            if (nitrogenDioxide != null) {
                                coordinatesTextView!!.text = "Lat: ${nitrogenDioxide.coordinates.latitude.doubleFormat(2)}, Lon:${nitrogenDioxide.coordinates.longitude.doubleFormat(2)}"
                                datetimeTextView!!.text = "${nitrogenDioxide.dateTime.DDMMYYYY()} - ${nitrogenDioxide.dateTime.hhmmss()}"
                                valueTextView!!.text = "NO2: ${nitrogenDioxide.data.no2.value.doubleFormat(2)}"
                                valueStratTextView!!.text = "NO2 Strat: ${nitrogenDioxide.data.no2Strat.value.doubleFormat(2)}"
                                valueTropTextView!!.text = "NO2 Trop: ${nitrogenDioxide.data.no2Trop.value.doubleFormat(2)}"
                            }
                        },
                        { }))
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        subscriptions.forEach { x -> x?.dispose() }
    }
}