package com.github.openweather.library.controller

interface ISharedPreferenceController {
    fun <T : Any> save(key: String, value: T)

    fun <T : Any> load(key: String, default: T): T

    fun exists(key: String): Boolean

    fun remove(key: String)

    fun erase()
}