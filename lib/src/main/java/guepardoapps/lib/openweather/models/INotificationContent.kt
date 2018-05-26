package guepardoapps.lib.openweather.models

import java.io.Serializable

interface INotificationContent : Serializable {
    var id: Int

    var title: String

    var text: String

    var icon: Int

    var largeIcon: Int

    var receiver: Class<*>
}