package com.example.android.sunshine.app;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class ForecastAdapter extends CursorAdapter {

    private final int VIEW_TYPE_TODAY = 0;
    private final int VIEW_TYPE_FUTURE_DAY = 1;

    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
    private String formatHighLows(double high, double low) {
        boolean isMetric = Utility.isMetric(mContext);
        String highLowStr = Utility.formatTemperature(high, isMetric) + "/" + Utility.formatTemperature(low, isMetric);
        return highLowStr;
    }

    /*
        This is ported from FetchWeatherTask --- but now we go straight from the cursor to the
        string.
     */
    private String convertCursorRowToUXFormat(Cursor cursor) {
        String highAndLow = formatHighLows(
                cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP),
                cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP));

        return Utility.formatDate(cursor.getLong(ForecastFragment.COL_WEATHER_DATE)) +
                " - " + cursor.getString(ForecastFragment.COL_WEATHER_DESC) +
                " - " + highAndLow;
    }

    /*
            Remember that these views are reused as needed.
         */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int viewType = getItemViewType(cursor.getPosition());

        int layoutId = -1;

        if (viewType == VIEW_TYPE_TODAY)
            layoutId = R.layout.list_item_forecast_today;
        else if (viewType == VIEW_TYPE_FUTURE_DAY)
            layoutId = R.layout.list_item_forecast;

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);

        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.

        final ViewHolder holder = (ViewHolder) view.getTag();
        final boolean isMetric = Utility.isMetric(context);

        holder.icon.setImageResource(R.drawable.ic_launcher);
        holder.date.setText(
                Utility.getFriendlyDayString(context, cursor.getLong(ForecastFragment.COL_WEATHER_DATE)));
        holder.detail.setText(cursor.getString(ForecastFragment.COL_WEATHER_DESC));
        holder.high.setText(
                Utility.formatTemperature(
                        cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP), isMetric));
        holder.low.setText(
                Utility.formatTemperature(
                        cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP), isMetric));
    }


    public static class ViewHolder {
        public final TextView date;
        public final TextView detail;
        public final TextView high;
        public final TextView low;
        public final ImageView icon;

        public ViewHolder(View view) {
            date = (TextView) view.findViewById(R.id.list_item_date_textview);
            detail = (TextView) view.findViewById(R.id.list_item_forecast_textview);
            high = (TextView) view.findViewById(R.id.list_item_high_textview);
            low = (TextView) view.findViewById(R.id.list_item_low_textview);
            icon = (ImageView) view.findViewById(R.id.list_item_icon);
        }
    }
}










































