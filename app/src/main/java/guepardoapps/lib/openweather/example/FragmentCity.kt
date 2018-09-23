package guepardoapps.lib.openweather.example

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import es.dmoral.toasty.Toasty
import guepardoapps.lib.openweather.models.City
import guepardoapps.lib.openweather.services.openweather.OpenWeatherService
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import android.app.Activity
import guepardoapps.lib.openweather.extensions.doubleFormat
import kotlinx.android.synthetic.main.fragment_city.*

class FragmentCity : Fragment() {
    private lateinit var city: City
    private var subscriptions: Array<Disposable?> = arrayOf()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Activity) {
            subscriptions = subscriptions.plus(
                    OpenWeatherService.instance.cityPublishSubject
                            .subscribeOn(Schedulers.io())
                            .subscribe(
                                    { response ->
                                        if (response.value != null) {
                                            handleOnCity(response.value as City)
                                        } else {
                                            if (isAdded) {
                                                context.runOnUiThread {
                                                    Toasty.warning(context, "city subscribe was not successfully", Toast.LENGTH_LONG).show()
                                                }
                                            }
                                        }
                                    },
                                    { _ -> }
                            ))

            OpenWeatherService.instance.loadCityData(getString(R.string.openweather_city))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_city, container, false)
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
    private fun handleOnCity(city: City) {
        this.city = city
        city_name.text = this.city.name
        city_country.text = this.city.country
        city_population.text = this.city.population.toString()
        city_coordinates.text = "Lat: ${this.city.coordinates.lat.doubleFormat(2)}Lon: ${this.city.coordinates.lon.doubleFormat(2)}"
    }
}