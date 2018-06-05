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
import guepardoapps.lib.openweather.models.IWeatherCurrent
import guepardoapps.lib.openweather.models.IWeatherForecast
import guepardoapps.lib.openweather.services.OnWeatherUpdateListener
import guepardoapps.lib.openweather.services.OpenWeatherService
import guepardoapps.lib.openweather.utils.Logger

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val tag: String = MainActivity::class.java.canonicalName

    private lateinit var context: Context
    private lateinit var openWeatherService: OpenWeatherService

    private lateinit var mainImageView: KenBurnsView
    private lateinit var listView: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var noDataFallback: TextView

    private lateinit var searchField: EditText

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var pullRefreshLayout: PullRefreshLayout

    private lateinit var currentWeather: IWeatherCurrent
    private lateinit var forecastWeather: IWeatherForecast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity)

        context = this
        openWeatherService = OpenWeatherService(this)

        mainImageView = findViewById(R.id.kenBurnsView)
        listView = findViewById(R.id.listView)
        progressBar = findViewById(R.id.progressBar)
        noDataFallback = findViewById(R.id.fallBackTextView)

        searchField = findViewById(R.id.search)
        searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {
                val foundForecastModel = openWeatherService.searchForecast(forecastWeather, charSequence.toString())
                val forecastList = foundForecastModel.getList()
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
        pullRefreshLayout.setOnRefreshListener({
            listView.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            searchField.visibility = View.INVISIBLE
            openWeatherService.loadForecastWeather()
        })

        openWeatherService.setApiKey("") // TODO Add ApiKey
        openWeatherService.setCity("Nuremberg")
        openWeatherService.setNotificationEnabled(true)
        openWeatherService.setReloadEnabled(true)
        openWeatherService.setReloadTimeout(30 * 60 * 1000)
        openWeatherService.setWallpaperEnabled(true)
        openWeatherService.setReceiverActivity(MainActivity::class.java)

        openWeatherService.setOnWeatherUpdateListener(object : OnWeatherUpdateListener {
            override fun onCurrentWeather(currentWeather: IWeatherCurrent?, success: Boolean) {
                if (success) {
                    handleOnCurrentWeather(currentWeather!!)
                } else {
                    Logger.instance.warning(tag, "onCurrentWeather download was  not successfully")
                    progressBar.visibility = View.GONE
                    noDataFallback.visibility = View.VISIBLE
                    Toasty.warning(context, "onCurrentWeather download was  not successfully", Toast.LENGTH_LONG).show()
                }
            }

            override fun onForecastWeather(forecastWeather: IWeatherForecast?, success: Boolean) {
                pullRefreshLayout.setRefreshing(false)
                if (success) {
                    handleOnForecastWeather(forecastWeather!!)
                    searchField.setText("")
                } else {
                    Logger.instance.warning(tag, "onForecastWeather download was  not successfully")
                    progressBar.visibility = View.GONE
                    noDataFallback.visibility = View.VISIBLE
                    Toasty.warning(context, "onForecastWeather download was  not successfully", Toast.LENGTH_LONG).show()
                }
            }
        })

        openWeatherService.loadCurrentWeather()
        openWeatherService.loadForecastWeather()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.nav_current_weather -> {
                openWeatherService.loadCurrentWeather()
                Snacky.builder()
                        .setActivty(this)
                        .setText("Clicked on CurrentWeather")
                        .setDuration(Snacky.LENGTH_LONG)
                        .info()
                        .show()
            }
            R.id.nav_forecast_weather -> {
                openWeatherService.loadForecastWeather()
                Snacky.builder()
                        .setActivty(this)
                        .setText("Clicked on ForecastWeather")
                        .setDuration(Snacky.LENGTH_LONG)
                        .info()
                        .show()
            }
            else -> {
                Snacky.builder()
                        .setActivty(this)
                        .setText("Clicked on Something else")
                        .setDuration(Snacky.LENGTH_INDEFINITE)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show()
            }
        }

        return true
    }

    private fun handleOnCurrentWeather(currentWeather: IWeatherCurrent) {
        this.currentWeather = currentWeather
        Logger.instance.verbose(tag, "Implement functionality and UI to handle current weather in MainActivity")
    }

    private fun handleOnForecastWeather(forecastWeather: IWeatherForecast) {
        this.forecastWeather = forecastWeather

        val forecastList = forecastWeather.getList()
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