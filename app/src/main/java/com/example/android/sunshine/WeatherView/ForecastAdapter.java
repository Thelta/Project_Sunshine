package com.example.android.sunshine.WeatherView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.sunshine.R;

/**
 * Created by atidur on 1/3/2018.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder>
{
    private String[] mWeatherData;
    private ForecastAdapterOnClickHandler mClickHandler;

    public ForecastAdapter(ForecastAdapterOnClickHandler handler)
    {
        mClickHandler = handler;
    }

    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.forecast_list_item, parent, false);
        return new ForecastAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder holder, int position)
    {
        holder.mWeatherTextView.setText(mWeatherData[position]);
    }

    @Override
    public int getItemCount()
    {
        return mWeatherData == null ? 0 : mWeatherData.length;
    }

    public void setmWeatherData(String[] mWeatherData)
    {
        this.mWeatherData = mWeatherData;
        notifyDataSetChanged();
    }

    public interface ForecastAdapterOnClickHandler
    {
        void onClickList(String weather);
    }

    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener
    {
        public final TextView mWeatherTextView;

        public ForecastAdapterViewHolder(View itemView)
        {
            super(itemView);

            mWeatherTextView = (TextView) itemView.findViewById(R.id.tv_weather_data);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClickList(mWeatherData[adapterPosition]);
        }
    }
}
