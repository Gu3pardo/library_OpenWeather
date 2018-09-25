package guepardoapps.lib.openweather.views

import android.annotation.SuppressLint
import android.content.Context
import androidx.coordinatorlayout.widget.CoordinatorLayout
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.baoyz.widget.PullRefreshLayout
import guepardoapps.lib.openweather.R
import guepardoapps.lib.openweather.adapter.ForecastListAdapter
import guepardoapps.lib.openweather.extensions.getMostWeatherCondition
import guepardoapps.lib.openweather.services.openweather.OpenWeatherService
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@SuppressLint("CheckResult", "SetTextI18n")
class ForecastWeatherView(context: Context, attrs: AttributeSet?) : CoordinatorLayout(context, attrs) {

    private var forecastImageView: ImageView? = null
    private var searchEditText: EditText? = null
    private var progressBar: ProgressBar? = null
    private var fallbackTextView: TextView? = null
    private var pullRefreshLayout: PullRefreshLayout? = null
    private var listView: ListView? = null

    private var subscriptions: Array<Disposable?> = arrayOf()

    init {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layoutInflater.inflate(R.layout.lib_forecast_weather, this, true)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        forecastImageView = findViewById(R.id.lib_forecast_weather_image)
        searchEditText = findViewById(R.id.lib_forecast_weather_search)
        progressBar = findViewById(R.id.lib_forecast_weather_progress_bar)
        fallbackTextView = findViewById(R.id.lib_forecast_weather_no_data_fallback)
        pullRefreshLayout = findViewById(R.id.pull_lib_forecast_weather_refresh_layout)
        listView = findViewById(R.id.lib_forecast_weather_list_view)

        searchEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {
                val foundForecastModel = OpenWeatherService.instance.searchForecast(charSequence.toString())
                val forecastList = foundForecastModel.list
                val adapter = ForecastListAdapter(context!!, forecastList)
                listView?.adapter = adapter
                forecastImageView?.setImageResource(foundForecastModel.getMostWeatherCondition().wallpaperId)
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        pullRefreshLayout?.setOnRefreshListener {
            listView?.visibility = View.GONE
            progressBar?.visibility = View.VISIBLE
            searchEditText?.visibility = View.INVISIBLE
            OpenWeatherService.instance.loadWeatherForecast()
        }

        subscriptions = subscriptions.plus(
                OpenWeatherService.instance.weatherForecastPublishSubject
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                { response ->
                                    pullRefreshLayout?.setRefreshing(false)
                                    progressBar?.visibility = View.GONE
                                    fallbackTextView?.visibility = View.VISIBLE

                                    val forecastWeather = response?.value
                                    if (forecastWeather != null) {
                                        searchEditText?.setText("")

                                        val forecastList = forecastWeather.list
                                        if (forecastList.isNotEmpty()) {
                                            fallbackTextView?.visibility = View.GONE
                                            searchEditText?.visibility = View.VISIBLE
                                            listView?.visibility = View.VISIBLE

                                            val adapter = ForecastListAdapter(context!!, forecastList)
                                            listView?.adapter = adapter

                                            forecastImageView?.setImageResource(forecastWeather.getMostWeatherCondition().wallpaperId)
                                        }
                                    }
                                },
                                { _ ->
                                    progressBar?.visibility = View.GONE
                                    fallbackTextView?.visibility = View.VISIBLE
                                }
                        ))
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        subscriptions.forEach { x -> x?.dispose() }
    }
}
