package com.github.openweather.library.adapter

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.NonNull
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.github.openweather.library.R
import com.github.openweather.library.common.Constants
import com.github.openweather.library.enums.ForecastListType
import com.github.openweather.library.extensions.DDMMYYYY
import com.github.openweather.library.extensions.doubleFormat
import com.github.openweather.library.extensions.hhmm
import com.github.openweather.library.models.WeatherForecastPart

class ForecastListAdapter(
        @NonNull private val context: Context,
        @NonNull private val list: List<WeatherForecastPart>) : BaseAdapter() {

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
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): WeatherForecastPart {
        return list[position]
    }

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parentView: ViewGroup?): View {
        val holder = Holder()
        val forecastPart = list[position]
        lateinit var rowView: View

        when (forecastPart.listType) {
            ForecastListType.Forecast -> {
                rowView = inflater.inflate(R.layout.listview_card_forecast, parentView, false)

                holder.weatherConditionView = rowView.findViewById(R.id.weatherConditionView)
                holder.weatherConditionImageView = rowView.findViewById(R.id.weatherConditionImageView)
                holder.weatherConditionImageView.setImageResource(forecastPart.weatherCondition.iconId)
                holder.weatherHeaderTextView = rowView.findViewById(R.id.weatherHeaderTextView)

                holder.weatherHeaderTextView.text =
                        forecastPart.dateTime.hhmm() + ", " +
                        "${forecastPart.temperature.doubleFormat(2)} " +
                        "${Constants.String.DegreeSign}C, " +
                        forecastPart.description

                holder.weatherTemperatureView = rowView.findViewById(R.id.weatherTemperatureView)
                holder.weatherTemperatureImageView = rowView.findViewById(R.id.weatherTemperatureImageView)
                holder.weatherTemperatureTextView = rowView.findViewById(R.id.weatherTemperatureTextView)
                holder.weatherTemperatureTextView.text =
                        "${forecastPart.temperatureMin.doubleFormat(2)} " +
                        "${Constants.String.DegreeSign}C - " +
                        "${forecastPart.temperatureMax.doubleFormat(2)} " +
                        "${Constants.String.DegreeSign}C"

                holder.weatherPressureView = rowView.findViewById(R.id.weatherPressureView)
                holder.weatherPressureImageView = rowView.findViewById(R.id.weatherPressureImageView)
                holder.weatherPressureTextView = rowView.findViewById(R.id.weatherPressureTextView)
                holder.weatherPressureTextView.text = "${forecastPart.pressure.doubleFormat(2)} mBar"

                holder.weatherHumidityView = rowView.findViewById(R.id.weatherHumidityView)
                holder.weatherHumidityImageView = rowView.findViewById(R.id.weatherHumidityImageView)
                holder.weatherHumidityTextView = rowView.findViewById(R.id.weatherHumidityTextView)
                holder.weatherHumidityTextView.text = "${forecastPart.humidity.doubleFormat(2)} %"

                holder.weatherWindView = rowView.findViewById(R.id.weatherWindView)
                holder.weatherWindImageView = rowView.findViewById(R.id.weatherWindImageView)
                holder.weatherWindTextView = rowView.findViewById(R.id.weatherWindTextView)
                holder.weatherWindTextView.text =
                        "${forecastPart.windSpeed.doubleFormat(2)} m/s, " +
                        "${forecastPart.windDegree.doubleFormat(2)} deg"
            }
            ForecastListType.DateDivider, ForecastListType.Null -> {
                rowView = inflater.inflate(R.layout.listview_card_divider, parentView, false)
                holder.dividerCardTitleText = rowView.findViewById(R.id.dividerCardTitleText)
                holder.dividerCardTitleText.text = forecastPart.dateTime.DDMMYYYY()
            }
        }

        return rowView
    }
}