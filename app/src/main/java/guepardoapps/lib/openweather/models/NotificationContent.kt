package guepardoapps.lib.openweather.models

class NotificationContent(override var title: String,
                          override var text: String,
                          override var icon: Int,
                          override var bigIcon: Int) : INotificationContent {

    private val tag: String = NotificationContent::class.java.canonicalName

    override fun toString(): String {
        return "{Class: $tag, Title: $title, Text: $text, Icon: $icon, BigIcon: $bigIcon}"
    }
}