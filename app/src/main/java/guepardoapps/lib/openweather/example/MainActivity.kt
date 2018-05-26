package guepardoapps.lib.openweather.example

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import guepardoapps.lib.openweather.controller.ReceiverController
import guepardoapps.lib.openweather.models.IWeatherCurrent
import guepardoapps.lib.openweather.models.IWeatherForecast
import guepardoapps.lib.openweather.services.OnWeatherUpdateListener
import guepardoapps.lib.openweather.services.OpenWeatherService

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val tag: String = MainActivity::class.java.canonicalName

    private var receiverController: ReceiverController? = null
    private var openWeatherService: OpenWeatherService? = null

    override fun onStart() {
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        receiverController = ReceiverController(this)

        openWeatherService = OpenWeatherService(this)
        openWeatherService?.setApiKey("") // TODO Add ApiKey
        openWeatherService?.setCity("Nuremberg")
        openWeatherService?.setNotificationEnabled(true)
        openWeatherService?.setReloadEnabled(true)
        openWeatherService?.setReloadTimeout(30 * 60 * 1000)
        openWeatherService?.setWallpaperEnabled(true)
        openWeatherService?.setOnWeatherUpdateListener(object : OnWeatherUpdateListener {
            override fun onCurrentWeather(currentWeather: IWeatherCurrent?, success: Boolean) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onForecastWeather(forecastWeather: IWeatherForecast?, success: Boolean) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        receiverController?.dispose()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.nav_current_weather -> {
            }
            R.id.nav_forecast_weather -> {
            }
            else -> {
            }
        }

        return true
    }
}