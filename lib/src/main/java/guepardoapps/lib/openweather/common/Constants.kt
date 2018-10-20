package guepardoapps.lib.openweather.common

internal object Constants {
    object Defaults {
        const val Coordinates = 720.0
        const val Thousand = 1000
        const val Zero = 0
    }

    object SharedPref {
        const val Name = "OpenWeatherLib"
        const val CryptoKey = "Tc56di8="

        const val KeyCity = "City"
    }

    object String {
        const val DegreeSign = 0x00B0.toChar()
        const val Empty = ""
        const val NewLine = "\n"
    }
}