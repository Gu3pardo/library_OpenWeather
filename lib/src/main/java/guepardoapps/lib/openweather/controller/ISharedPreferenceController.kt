package guepardoapps.lib.openweather.controller

interface ISharedPreferenceController {
    fun <T : Any> save(key: String, value: T)
    fun <T : Any> load(key: String, default: T): T
    fun remove(key: String)
    fun erase()
}