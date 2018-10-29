package com.github.openweather.library.adapter

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.NonNull
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import guepardoapps.lib.openweather.R
import com.github.openweather.library.models.CarbonMonoxideData

class CarbonMonoxideListAdapter(
        @NonNull context: Context,
        @NonNull private val list: List<CarbonMonoxideData>) : BaseAdapter() {

    private class Holder {
        lateinit var carbonMonoxideValueTextView: TextView
        lateinit var carbonMonoxidePrecisionTextView: TextView
        lateinit var carbonMonoxidePressureTextView: TextView
    }

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): CarbonMonoxideData {
        return list[position]
    }

    @SuppressLint("SetTextI18n", "ViewHolder")
    override fun getView(position: Int, convertView: View?, parentView: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.listview_card_carbonmonoxide, parentView, false)
        val holder = Holder()

        holder.carbonMonoxideValueTextView = rowView.findViewById(R.id.carbonMonoxideValue)
        holder.carbonMonoxideValueTextView.text = list[position].value.toString()

        holder.carbonMonoxidePrecisionTextView = rowView.findViewById(R.id.carbonMonoxidePrecision)
        holder.carbonMonoxidePrecisionTextView.text = list[position].precision.toString()

        holder.carbonMonoxidePressureTextView = rowView.findViewById(R.id.carbonMonoxidePressure)
        holder.carbonMonoxidePressureTextView.text = list[position].pressure.toString()

        return rowView
    }
}