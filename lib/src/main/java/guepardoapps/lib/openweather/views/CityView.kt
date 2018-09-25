package guepardoapps.lib.openweather.views

import android.annotation.SuppressLint
import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import guepardoapps.lib.openweather.R
import guepardoapps.lib.openweather.enums.UnsplashImageOrientation
import guepardoapps.lib.openweather.extensions.doubleFormat
import guepardoapps.lib.openweather.logging.Logger
import guepardoapps.lib.openweather.services.image.ImageService
import guepardoapps.lib.openweather.services.openweather.OpenWeatherService
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@SuppressLint("CheckResult", "SetTextI18n")
class CityView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {
    private val tag: String = CityView::class.java.simpleName

    private var cityImageView: ImageView? = null
    private var nameTextView: TextView? = null
    private var countryTextView: TextView? = null
    private var populationTextView: TextView? = null
    private var coordinatesTextView: TextView? = null

    private var subscriptions: Array<Disposable?> = arrayOf()

    init {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layoutInflater.inflate(R.layout.lib_city, this, true)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        cityImageView = findViewById(R.id.lib_city_image)
        nameTextView = findViewById(R.id.lib_city_name)
        countryTextView = findViewById(R.id.lib_city_country)
        populationTextView = findViewById(R.id.lib_city_population)
        coordinatesTextView = findViewById(R.id.lib_city_coordinates)

        subscriptions = subscriptions.plus(OpenWeatherService.instance.cityPublishSubject
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { response ->
                            val city = response?.value
                            if (city != null) {
                                nameTextView!!.text = city.name
                                countryTextView!!.text = city.country
                                populationTextView!!.text = "Population: ${city.population}"
                                coordinatesTextView!!.text = "Lat: ${city.coordinates.lat.doubleFormat(2)}, Lon:${city.coordinates.lon.doubleFormat(2)}"

                                ImageService.instance.receiveImagePictureUrl(city.name, UnsplashImageOrientation.Landscape)
                            }
                        },
                        { _ -> }))

        subscriptions = subscriptions.plus(ImageService.instance.urlPublishSubject
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { response ->
                            val url = response?.value
                            if (url != null) {
                                Logger.instance.info(tag, "Received url $url")
                                Picasso.get().load(url).into(cityImageView)
                            } else {
                                Logger.instance.warning(tag, "Received empty url")
                            }
                        },
                        { _ -> }))
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        subscriptions.forEach { x -> x?.dispose() }
    }
}