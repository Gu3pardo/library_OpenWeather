package guepardoapps.lib.openweather.models

import java.io.Serializable

interface INotificationContent : Serializable {
    var title: String

    var text: String

    var icon: Int

    var bigIcon: Int
}