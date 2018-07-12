package guepardoapps.lib.openweather.example

import android.content.Context
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.baoyz.widget.PullRefreshLayout
import com.flaviofaria.kenburnsview.KenBurnsView
import de.mateware.snacky.Snacky
import es.dmoral.toasty.Toasty
import guepardoapps.lib.openweather.adapter.ForecastListAdapter
import guepardoapps.lib.openweather.extensions.getMostWeatherCondition
import guepardoapps.lib.openweather.models.WeatherCurrent
import guepardoapps.lib.openweather.models.WeatherForecast
import guepardoapps.lib.openweather.services.openweather.OpenWeatherService
import guepardoapps.lib.openweather.utils.Logger
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val tag: String = MainActivity::class.java.canonicalName

    private lateinit var context: Context

    private lateinit var mainImageView: KenBurnsView
    private lateinit var listView: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var noDataFallback: TextView

    private lateinit var searchField: EditText

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var pullRefreshLayout: PullRefreshLayout

    private lateinit var currentWeather: WeatherCurrent
    private lateinit var forecastWeather: WeatherForecast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity)

        context = this
        OpenWeatherService.instance.initialize(context)

        mainImageView = findViewById(R.id.kenBurnsView)
        listView = findViewById(R.id.listView)
        progressBar = findViewById(R.id.progressBar)
        noDataFallback = findViewById(R.id.fallBackTextView)

        searchField = findViewById(R.id.search)
        searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {
                val foundForecastModel = OpenWeatherService.instance.searchForecast(forecastWeather, charSequence.toString())
                val forecastList = foundForecastModel.list
                val adapter = ForecastListAdapter(context, forecastList)
                listView.adapter = adapter
                mainImageView.setImageResource(foundForecastModel.getMostWeatherCondition().wallpaperId)
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        val collapsingToolbar = findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar)
        collapsingToolbar.setExpandedTitleColor(android.graphics.Color.argb(0, 0, 0, 0))
        collapsingToolbar.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.TextIcon))

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        pullRefreshLayout = findViewById(R.id.pullRefreshLayout)
        pullRefreshLayout.setOnRefreshListener {
            listView.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            searchField.visibility = View.INVISIBLE
            OpenWeatherService.instance.loadWeatherForecast()
        }

        OpenWeatherService.instance.apiKey = getString(R.string.openweather_api_key)
        OpenWeatherService.instance.city = getString(R.string.openweather_city)
        OpenWeatherService.instance.notificationEnabled = true
        OpenWeatherService.instance.wallpaperEnabled = true
        OpenWeatherService.instance.receiverActivity = MainActivity::class.java

        OpenWeatherService.instance.weatherCurrentPublishSubject
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { response ->
                            Logger.instance.verbose(tag, "Received weather current in subscribe!")

                            if (response.value != null) {
                                handleOnCurrentWeather(response.value as WeatherCurrent)
                            } else {
                                Logger.instance.warning(tag, "weather current subscribe download was  not successfully")
                                progressBar.visibility = View.GONE
                                noDataFallback.visibility = View.VISIBLE
                                runOnUiThread {
                                    Toasty.warning(context, "weather current subscribe  was  not successfully", Toast.LENGTH_LONG).show()
                                }
                            }
                        },
                        { responseError ->
                            Logger.instance.error(tag, responseError)
                            progressBar.visibility = View.GONE
                            noDataFallback.visibility = View.VISIBLE
                        }
                )

        OpenWeatherService.instance.weatherForecastPublishSubject
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { response ->
                            Logger.instance.verbose(tag, "Received weather forecast in subscribe!")

                            pullRefreshLayout.setRefreshing(false)
                            if (response.value != null) {
                                handleOnForecastWeather(response.value as WeatherForecast)
                                searchField.setText("")
                            } else {
                                Logger.instance.warning(tag, "weather forecast subscribe was  not successfully")
                                progressBar.visibility = View.GONE
                                noDataFallback.visibility = View.VISIBLE
                                runOnUiThread {
                                    Toasty.warning(context, "weather forecast subscribe was  not successfully", Toast.LENGTH_LONG).show()
                                }
                            }
                        },
                        { responseError ->
                            Logger.instance.error(tag, responseError)
                            progressBar.visibility = View.GONE
                            noDataFallback.visibility = View.VISIBLE
                        }
                )

        OpenWeatherService.instance.reloadEnabled = true
        OpenWeatherService.instance.reloadTimeout = 30 * 60 * 1000

        OpenWeatherService.instance.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        OpenWeatherService.instance.dispose()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.nav_current_weather -> {
                OpenWeatherService.instance.loadWeatherCurrent()
                Snacky.builder()
                        .setActivty(this)
                        .setText("Reload current weather")
                        .setDuration(Snacky.LENGTH_LONG)
                        .info()
                        .show()
            }
            R.id.nav_forecast_weather -> {
                OpenWeatherService.instance.loadWeatherForecast()
                Snacky.builder()
                        .setActivty(this)
                        .setText("Reload forecast weather")
                        .setDuration(Snacky.LENGTH_LONG)
                        .info()
                        .show()
            }
            else -> {
                Snacky.builder()
                        .setActivty(this)
                        .setText("What did you click here???")
                        .setDuration(Snacky.LENGTH_INDEFINITE)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show()
            }
        }

        return true
    }

    private fun handleOnCurrentWeather(currentWeather: WeatherCurrent) {
        this.currentWeather = currentWeather
        Logger.instance.verbose(tag, "Implement functionality and UI to handle current weather in MainActivity")
    }

    private fun handleOnForecastWeather(forecastWeather: WeatherForecast) {
        this.forecastWeather = forecastWeather

        val forecastList = forecastWeather.list
        if (forecastList.isNotEmpty()) {
            noDataFallback.visibility = View.GONE
            searchField.visibility = View.VISIBLE
            listView.visibility = View.VISIBLE

            val adapter = ForecastListAdapter(this, forecastList)
            listView.adapter = adapter

            mainImageView.setImageResource(forecastWeather.getMostWeatherCondition().wallpaperId)
        } else {
            Logger.instance.warning(tag, "forecastList is empty")
        }

        progressBar.visibility = View.GONE
    }
}