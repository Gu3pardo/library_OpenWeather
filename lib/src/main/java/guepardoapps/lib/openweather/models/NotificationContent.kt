package guepardoapps.lib.openweather.models

class NotificationContent(override var id: Int,
                          override var title: String,
                          override var text: String,
                          override var icon: Int,
                          override var largeIcon: Int,
                          override var receiver: Class<*>) : INotificationContent {

    private val tag: String = NotificationContent::class.java.simpleName

    override fun toString(): String {
        return "{Class: $tag, Id: $id, Title: $title, Text: $text, Icon: $icon, LargeIcon: $largeIcon, Receiver: $receiver}"
    }
}