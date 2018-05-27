package guepardoapps.lib.openweather.adapter

import android.annotation.SuppressLint
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
import guepardoapps.lib.openweather.extensions.decimalFormat
import guepardoapps.lib.openweather.extensions.format
import guepardoapps.lib.openweather.models.IWeatherForecastPart
import java.util.*

class ForecastListAdapter(
        @NonNull private val context: Context,
        @NonNull private val forecastList: Array<IWeatherForecastPart>) : BaseAdapter() {

    // private val tag: String = ForecastListAdapter::class.java.canonicalName

    private class Holder {
        lateinit var dividerCardTitleText: TextView

        lateinit var weatherConditionView: View
        lateinit var weatherConditionImageView: ImageView
        lateinit var weatherHeaderTextView: TextView

        lateinit var weatherTemperatureView: View
        lateinit var weatherTemperatureImageView: ImageView
        lateinit var weatherTemperatureTextView: TextView

        lateinit var weatherPressureView: View
        lateinit var weatherPressureImageView: ImageView
        lateinit var weatherPressureTextView: TextView

        lateinit var weatherHumidityView: View
        lateinit var weatherHumidityImageView: ImageView
        lateinit var weatherHumidityTextView: TextView

        lateinit var weatherWindView: View
        lateinit var weatherWindImageView: ImageView
        lateinit var weatherWindTextView: TextView
    }

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return forecastList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): IWeatherForecastPart {
        return forecastList[position]
    }

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parentView: ViewGroup?): View {
        val holder = Holder()
        val forecastPart = forecastList[position]
        lateinit var rowView: View

        when (forecastPart.getListType()) {
            ForecastListType.Forecast -> {
                rowView = inflater.inflate(R.layout.listview_card_forecast, parentView, false)

                holder.weatherConditionView = rowView.findViewById(R.id.weatherConditionView)
                holder.weatherConditionImageView = rowView.findViewById(R.id.weatherConditionImageView)
                holder.weatherConditionImageView.setImageResource(forecastPart.getWeatherCondition().iconId)
                holder.weatherHeaderTextView = rowView.findViewById(R.id.weatherHeaderTextView)

                val dateTime = forecastPart.getDateTime()
                holder.weatherHeaderTextView.text =
                        "${dateTime.get(Calendar.HOUR_OF_DAY).format(2)}:" +
                        "${dateTime.get(Calendar.MINUTE).format(2)}, " +
                        "${forecastPart.getTemperature().decimalFormat(2)} " +
                        "${0x00B0.toChar()}C, " +
                        forecastPart.getDescription()

                holder.weatherTemperatureView = rowView.findViewById(R.id.weatherTemperatureView)
                holder.weatherTemperatureImageView = rowView.findViewById(R.id.weatherTemperatureImageView)
                holder.weatherTemperatureTextView = rowView.findViewById(R.id.weatherTemperatureTextView)
                holder.weatherTemperatureTextView.text =
                        "${forecastPart.getTemperatureMin().decimalFormat(2)} " +
                        "${0x00B0.toChar()}C - " +
                        "${forecastPart.getTemperatureMax().decimalFormat(2)} " +
                        "${0x00B0.toChar()}C"

                holder.weatherPressureView = rowView.findViewById(R.id.weatherPressureView)
                holder.weatherPressureImageView = rowView.findViewById(R.id.weatherPressureImageView)
                holder.weatherPressureTextView = rowView.findViewById(R.id.weatherPressureTextView)
                holder.weatherPressureTextView.text = "${forecastPart.getPressure().decimalFormat(2)} mBar"

                holder.weatherHumidityView = rowView.findViewById(R.id.weatherHumidityView)
                holder.weatherHumidityImageView = rowView.findViewById(R.id.weatherHumidityImageView)
                holder.weatherHumidityTextView = rowView.findViewById(R.id.weatherHumidityTextView)
                holder.weatherHumidityTextView.text = "${forecastPart.getHumidity().decimalFormat(2)} %"

                holder.weatherWindView = rowView.findViewById(R.id.weatherWindView)
                holder.weatherWindImageView = rowView.findViewById(R.id.weatherWindImageView)
                holder.weatherWindTextView = rowView.findViewById(R.id.weatherWindTextView)
                holder.weatherWindTextView.text =
                        "${forecastPart.getWindSpeed().decimalFormat(2)} m/s, " +
                        "${forecastPart.getWindDegree().decimalFormat(2)} deg"
            }
            ForecastListType.DateDivider -> {
                rowView = inflater.inflate(R.layout.listview_card_divider, parentView, false)
                holder.dividerCardTitleText = rowView.findViewById(R.id.dividerCardTitleText)

                val dateTime = forecastPart.getDateTime()
                holder.dividerCardTitleText.text =
                        "${dateTime.get(Calendar.DAY_OF_MONTH).format(2)}." +
                        "${(dateTime.get(Calendar.MONTH) + 1).format(2)}." +
                        dateTime.get(Calendar.YEAR).format(4)
            }
        }

        return rowView
    }
}