package guepardoapps.lib.openweather.example

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import es.dmoral.toasty.Toasty
import guepardoapps.lib.openweather.adapter.ForecastListAdapter
import guepardoapps.lib.openweather.extensions.getMostWeatherCondition
import guepardoapps.lib.openweather.models.WeatherForecast
import guepardoapps.lib.openweather.services.openweather.OpenWeatherService
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_forecast_weather.*

class FragmentForecastWeather : Fragment() {
    private lateinit var forecastWeather: WeatherForecast
    private var subscriptions: Array<Disposable?> = arrayOf()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Activity) {

            subscriptions = subscriptions.plus(
                    OpenWeatherService.instance.weatherForecastPublishSubject
                            .subscribeOn(Schedulers.io())
                            .subscribe(
                                    { response ->
                                        pull_refresh_layout.setRefreshing(false)
                                        if (response.value != null) {
                                            handleOnForecastWeather(response.value as WeatherForecast)
                                            search_edit_text.setText("")
                                        } else {
                                            progress_bar.visibility = View.GONE
                                            no_data_fallback.visibility = View.VISIBLE
                                            if (isAdded) {
                                                context.runOnUiThread {
                                                    Toasty.warning(context, "weather forecast subscribe was not successfully", Toast.LENGTH_LONG).show()
                                                }
                                            }
                                        }
                                    },
                                    { _ ->
                                        progress_bar.visibility = View.GONE
                                        no_data_fallback.visibility = View.VISIBLE
                                    }
                            ))

            OpenWeatherService.instance.loadWeatherForecast()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_forecast_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val collapsingToolbar = activity?.findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar)
        collapsingToolbar?.setExpandedTitleColor(android.graphics.Color.argb(0, 0, 0, 0))
        collapsingToolbar?.setCollapsedTitleTextColor(ContextCompat.getColor(context!!, R.color.TextIcon))

        search_edit_text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {
                val foundForecastModel = OpenWeatherService.instance.searchForecast(forecastWeather, charSequence.toString())
                val forecastList = foundForecastModel.list
                val adapter = ForecastListAdapter(context!!, forecastList)
                list_view.adapter = adapter
                mainImageView.setImageResource(foundForecastModel.getMostWeatherCondition().wallpaperId)
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        pull_refresh_layout.setOnRefreshListener {
            list_view.visibility = View.GONE
            progress_bar.visibility = View.VISIBLE
            search_edit_text.visibility = View.INVISIBLE
            OpenWeatherService.instance.loadWeatherForecast()
        }
    }

    override fun onDetach() {
        super.onDetach()
        subscriptions.forEach { x -> x?.dispose() }
        subscriptions = arrayOf()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun handleOnForecastWeather(forecastWeather: WeatherForecast) {
        this.forecastWeather = forecastWeather

        val forecastList = forecastWeather.list
        if (forecastList.isNotEmpty()) {
            no_data_fallback.visibility = View.GONE
            search_edit_text.visibility = View.VISIBLE
            list_view.visibility = View.VISIBLE

            val adapter = ForecastListAdapter(context!!, forecastList)
            list_view.adapter = adapter

            mainImageView.setImageResource(forecastWeather.getMostWeatherCondition().wallpaperId)
        }

        progress_bar.visibility = View.GONE
    }
}