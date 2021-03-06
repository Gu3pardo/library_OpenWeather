package com.github.openweather.library.adapter

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.NonNull
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.github.openweather.library.R
import com.github.openweather.library.models.CarbonMonoxideData

class CarbonMonoxideListAdapter(@NonNull context: Context, @NonNull private val list: List<CarbonMonoxideData>)
    : BaseAdapter() {

    private class Holder {
        lateinit var carbonMonoxideValueTextView: TextView

        lateinit var carbonMonoxidePrecisionTextView: TextView

        lateinit var carbonMonoxidePressureTextView: TextView
    }

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int = list.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItem(position: Int): CarbonMonoxideData = list[position]

    @SuppressLint("SetTextI18n", "ViewHolder")
    override fun getView(position: Int, convertView: View?, parentView: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.listview_card_carbonmonoxide, parentView, false)

        Holder().apply {
            carbonMonoxideValueTextView = rowView.findViewById(R.id.carbonMonoxideValue)
            carbonMonoxideValueTextView.text = list[position].value.toString()

            carbonMonoxidePrecisionTextView = rowView.findViewById(R.id.carbonMonoxidePrecision)
            carbonMonoxidePrecisionTextView.text = list[position].precision.toString()

            carbonMonoxidePressureTextView = rowView.findViewById(R.id.carbonMonoxidePressure)
            carbonMonoxidePressureTextView.text = list[position].pressure.toString()
        }

        return rowView
    }
}