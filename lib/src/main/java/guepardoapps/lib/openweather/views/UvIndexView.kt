package guepardoapps.lib.openweather.views

import android.annotation.SuppressLint
import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import guepardoapps.lib.openweather.R
import guepardoapps.lib.openweather.extensions.doubleFormat
import guepardoapps.lib.openweather.services.openweather.OpenWeatherService
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@SuppressLint("CheckResult", "SetTextI18n")
class UvIndexView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private var valueTextView: TextView? = null
    private var coordinatesTextView: TextView? = null

    private var subscriptions: Array<Disposable?> = arrayOf()

    init {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layoutInflater.inflate(R.layout.lib_uv_index, this, true)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        valueTextView = findViewById(R.id.lib_uv_index_value)
        coordinatesTextView = findViewById(R.id.lib_uv_index_coordinates)

        subscriptions = subscriptions.plus(OpenWeatherService.instance.uvIndexPublishSubject
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { response ->
                            val uvIndex = response?.value
                            if (uvIndex != null) {
                                valueTextView!!.text = uvIndex.value.doubleFormat(2)
                                coordinatesTextView!!.text = "Lat: ${uvIndex.coordinates.lat.doubleFormat(2)}, Lon:${uvIndex.coordinates.lon.doubleFormat(2)}"
                            }
                        },
                        { _ -> }))
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        subscriptions.forEach { x -> x?.dispose() }
    }
}