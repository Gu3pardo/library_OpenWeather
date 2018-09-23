package guepardoapps.lib.openweather.example

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import guepardoapps.lib.openweather.services.image.ImageService
import guepardoapps.lib.openweather.services.openweather.OpenWeatherService
import io.reactivex.disposables.Disposable
import it.sephiroth.android.library.bottomnavigation.BottomNavigation
import kotlinx.android.synthetic.main.app_bar.*

class MainActivity : FragmentActivity(), BottomNavigation.OnMenuItemSelectionListener {

    private var subscriptions: Array<Disposable?> = arrayOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity)

        bottomNavigation.setOnMenuItemClickListener(this)

        OpenWeatherService.instance.initialize(this, getString(R.string.openweather_city))
        OpenWeatherService.instance.apiKey = getString(R.string.openweather_api_key)

        ImageService.instance.initialize(this)
        ImageService.instance.accessKey = getString(R.string.image_api_access_key)

        OpenWeatherService.instance.notificationEnabled = true
        OpenWeatherService.instance.wallpaperEnabled = true
        OpenWeatherService.instance.receiverActivity = MainActivity::class.java
        OpenWeatherService.instance.reloadEnabled = true
        OpenWeatherService.instance.reloadTimeout = 30 * 60 * 1000
        OpenWeatherService.instance.start()

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_place, FragmentCity())
        fragmentTransaction.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.forEach { x -> x?.dispose() }
        subscriptions = arrayOf()
        OpenWeatherService.instance.dispose()
    }

    override fun onMenuItemSelect(itemId: Int, position: Int, fromUser: Boolean) {
        performMenuAction(itemId)
    }

    override fun onMenuItemReselect(itemId: Int, position: Int, fromUser: Boolean) {
        performMenuAction(itemId)
    }

    private fun performMenuAction(itemId: Int) {
        var fragment: Fragment? = null
        when (itemId) {
            R.id.btn_menu_city -> {
                fragment = FragmentCity()
            }
            R.id.btn_menu_current_weather -> {
                fragment = FragmentCurrentWeather()
            }
            R.id.btn_menu_forecast_weather -> {
                fragment = FragmentForecastWeather()
            }
            R.id.btn_menu_uv_index -> {
                fragment = FragmentUvIndex()
            }
        }

        if (fragment != null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_place, fragment)
            fragmentTransaction.commit()
        }
    }
}