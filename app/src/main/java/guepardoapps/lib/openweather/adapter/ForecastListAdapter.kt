package guepardoapps.lib.openweather.adapter

import android.content.Context
import android.support.annotation.NonNull
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import guepardoapps.lib.openweather.R
import guepardoapps.lib.openweather.enums.ForecastListType
import guepardoapps.lib.openweather.models.IWeatherForecastPart
import java.util.*

class ForecastListAdapter(
        @NonNull private val context: Context,
        @NonNull private val forecastList: Array<IWeatherForecastPart>) : BaseAdapter() {

    private val tag: String = ForecastListAdapter::class.java.canonicalName

    private class Holder {
        var dividerCardTitleText: TextView? = null

        var weatherConditionView: View? = null
        var weatherConditionImageView: ImageView? = null
        var weatherHeaderTextView: TextView? = null

        var weatherTemperatureView: View? = null
        var weatherTemperatureImageView: ImageView? = null
        var weatherTemperatureTextView: TextView? = null

        var weatherPressureView: View? = null
        var weatherPressureImageView: ImageView? = null
        var weatherPressureTextView: TextView? = null

        var weatherHumidityView: View? = null
        var weatherHumidityImageView: ImageView? = null
        var weatherHumidityTextView: TextView? = null

        var weatherWindView: View? = null
        var weatherWindImageView: ImageView? = null
        var weatherWindTextView: TextView? = null
    }

    private var inflater: LayoutInflater? = null

    init {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        return forecastList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): IWeatherForecastPart {
        return forecastList[position]
    }

    override fun getView(position: Int, convertView: View?, parentView: ViewGroup?): View {
        val holder = Holder()
        val forecastPart = forecastList[position]
        var rowView = View(context)

        when (forecastPart.getListType()) {
            ForecastListType.Forecast -> {
                rowView = inflater!!.inflate(R.layout.listview_card_forecast, null)
                // TODO
            }
            ForecastListType.DateDivider -> {
                rowView = inflater!!.inflate(R.layout.listview_card_divider, null)
                holder.dividerCardTitleText = rowView.findViewById(R.id.dividerCardTitleText)
                holder.dividerCardTitleText?.text = String.format(Locale.getDefault(),
                        "%2d.%2d.%4d",
                        forecastPart.getDateTime().get(Calendar.DAY_OF_MONTH),
                        forecastPart.getDateTime().get(Calendar.MONTH),
                        forecastPart.getDateTime().get(Calendar.YEAR))
            }
        }

        return rowView
    }
}