package guepardoapps.lib.openweather.example

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.FragmentActivity
import com.github.openweather.library.services.image.ImageService
import com.github.openweather.library.services.openweathermap.OpenWeatherMapService
import it.sephiroth.android.library.bottomnavigation.BottomNavigation
import kotlinx.android.synthetic.main.activity.*

class MainActivity : FragmentActivity(), BottomNavigation.OnMenuItemSelectionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity)

        bottomNavigation.setOnMenuItemClickListener(this)

        OpenWeatherMapService.instance.initialize(this, getString(R.string.openweather_city))
        OpenWeatherMapService.instance.apiKey = getString(R.string.openweather_api_key)

        ImageService.instance.initialize(this)
        ImageService.instance.accessKey = getString(R.string.image_api_access_key)

        OpenWeatherMapService.instance.notificationEnabledWeatherCurrent = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("notifications_weather_current", false)
        OpenWeatherMapService.instance.notificationEnabledWeatherForecast = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("notifications_weather_forecast", false)
        OpenWeatherMapService.instance.notificationEnabledUvIndex = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("notifications_uv_index", false)

        OpenWeatherMapService.instance.wallpaperEnabled = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("set_wallpaper", false)

        OpenWeatherMapService.instance.receiverActivity = MainActivity::class.java
        OpenWeatherMapService.instance.reloadEnabled = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("reload_enabled", false)
        OpenWeatherMapService.instance.reloadTimeout = PreferenceManager.getDefaultSharedPreferences(this).getString("reload_frequency", "30").toLong() * 60 * 1000

        OpenWeatherMapService.instance.start()

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_place, FragmentForecastWeather())
        fragmentTransaction.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        OpenWeatherMapService.instance.dispose()
    }

    override fun onMenuItemSelect(itemId: Int, position: Int, fromUser: Boolean) {
        performMenuAction(itemId)
    }

    override fun onMenuItemReselect(itemId: Int, position: Int, fromUser: Boolean) {
        performMenuAction(itemId)
    }

    private fun performMenuAction(itemId: Int) {
        var fragment: androidx.fragment.app.Fragment? = null
        when (itemId) {
            R.id.btn_menu_forecast_weather -> {
                fragment = FragmentForecastWeather()
            }
            R.id.btn_menu_current_weather -> {
                fragment = FragmentCurrentWeather()
            }
            R.id.btn_menu_information -> {
                fragment = FragmentInformation()
            }
            R.id.btn_menu_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
        }

        if (fragment != null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_place, fragment)
            fragmentTransaction.commit()
        }
    }
}