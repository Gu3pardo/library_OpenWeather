package guepardoapps.library.openweather.customadapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import guepardoapps.library.openweather.R;
import guepardoapps.library.openweather.common.utils.Logger;
import guepardoapps.library.openweather.models.ForecastPartModel;

@SuppressWarnings({"unused"})
public class ForecastListAdapter extends BaseAdapter {
    private static final String TAG = ForecastListAdapter.class.getSimpleName();

    private List<ForecastPartModel> _forecastList;
    private static LayoutInflater _inflater = null;

    public ForecastListAdapter(
            @NonNull Context context,
            @NonNull List<ForecastPartModel> forecastList) {
        _forecastList = forecastList;
        _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return _forecastList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class Holder {
        private ImageView _image;
        private TextView _description;
        private TextView _temperature;
        private TextView _humidity;
        private TextView _pressure;
        private TextView _time;
    }

    @SuppressLint({"InflateParams", "ViewHolder"})
    @Override
    public View getView(final int index, View convertView, ViewGroup parent) {
        final Holder holder = new Holder();
        ForecastPartModel entry = _forecastList.get(index);
        View rowView;

        switch (entry.GetForecastListType()) {
            case FORECAST:
                rowView = _inflater.inflate(R.layout.listview_card_forecast, null);

                holder._image = rowView.findViewById(R.id.weatherConditionImageView);
                holder._image.setImageResource(entry.GetCondition().GetIcon());

                holder._description = rowView.findViewById(R.id.weatherConditionTextView);
                holder._description.setText(entry.GetDescription());

                holder._temperature = rowView.findViewById(R.id.weatherTemperatureTextView);
                holder._temperature.setText(String.format(Locale.getDefault(), "Temperature: %.2f °C - %.2f °C", entry.GetTempMin(), entry.GetTempMax()));

                holder._humidity = rowView.findViewById(R.id.weatherHumidityTextView);
                holder._humidity.setText(String.format(Locale.getDefault(), "Humidity: %s %%", entry.GetHumidity()));

                holder._pressure = rowView.findViewById(R.id.weatherPressureTextView);
                holder._pressure.setText(String.format(Locale.getDefault(), "Pressure: %s mBar", entry.GetPressure()));

                holder._time = rowView.findViewById(R.id.weatherTimeTextView);
                holder._time.setText(entry.GetTime());
                break;

            case DATE_DIVIDER:
                rowView = _inflater.inflate(R.layout.listview_card_divider, null);
                holder._time = rowView.findViewById(R.id.dividerCardTitleText);
                holder._time.setText(entry.GetDate());
                break;

            default:
                Logger.getInstance().Error(TAG, String.format("Invalid ForecastListType %s!", entry.GetForecastListType()));
                rowView = _inflater.inflate(R.layout.listview_card_divider, null);
                holder._time = rowView.findViewById(R.id.dividerCardTitleText);
                holder._time.setText("");
                break;
        }

        return rowView;
    }
}