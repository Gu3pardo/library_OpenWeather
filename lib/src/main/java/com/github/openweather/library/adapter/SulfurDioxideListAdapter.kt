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
import com.github.openweather.library.models.SulfurDioxideData

class SulfurDioxideListAdapter(
        @NonNull context: Context,
        @NonNull private val list: List<SulfurDioxideData>) : BaseAdapter() {

    private class Holder {
        lateinit var sulfurDioxideValueTextView: TextView
        lateinit var sulfurDioxidePrecisionTextView: TextView
        lateinit var sulfurDioxidePressureTextView: TextView
    }

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): SulfurDioxideData {
        return list[position]
    }

    @SuppressLint("SetTextI18n", "ViewHolder")
    override fun getView(position: Int, convertView: View?, parentView: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.listview_card_sulfurdioxide, parentView, false)
        val holder = Holder()

        holder.sulfurDioxideValueTextView = rowView.findViewById(R.id.sulfurDioxideValue)
        holder.sulfurDioxideValueTextView.text = list[position].value.toString()

        holder.sulfurDioxidePrecisionTextView = rowView.findViewById(R.id.sulfurDioxidePrecision)
        holder.sulfurDioxidePrecisionTextView.text = list[position].precision.toString()

        holder.sulfurDioxidePressureTextView = rowView.findViewById(R.id.sulfurDioxidePressure)
        holder.sulfurDioxidePressureTextView.text = list[position].pressure.toString()

        return rowView
    }
}