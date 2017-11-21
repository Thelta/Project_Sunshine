/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import java.io.IOException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    TextView mWeatherTextView;
    TextView mErrorMessageTextView;
    ProgressBar mWeatherRequestProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        mWeatherTextView = (TextView) findViewById(R.id.tv_weather_data);
        mErrorMessageTextView = (TextView) findViewById(R.id.tv_error_message);
        mWeatherRequestProgressBar = (ProgressBar) findViewById(R.id.pb_weather_request);
        loadWeatherData();
    }

    private void loadWeatherData()
    {
        new WeatherQueryTask().execute(SunshinePreferences.getPreferredWeatherLocation(this));
    }

    private void showWeatherData(String weatherData)
    {
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        mWeatherTextView.setText(weatherData);
    }

    private void showErrorMessage()
    {
        mWeatherTextView.setText("");
        mErrorMessageTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.forecast, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.refresh_button)
        {
            mWeatherTextView.setText("");
            loadWeatherData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class WeatherQueryTask extends AsyncTask<String, Void, String[]>
    {
        @Override
        protected void onPostExecute(String[] s)
        {
            mWeatherRequestProgressBar.setVisibility(View.INVISIBLE);
            if(s == null)
            {
                showErrorMessage();
                return;
            }

            StringBuilder builder = new StringBuilder();
            for(String weather : s)
            {
                builder.append(weather);
                builder.append("\n\n\n");
            }

            showWeatherData(builder.toString());
        }

        @Override
        protected void onPreExecute()
        {
            mWeatherRequestProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... params)
        {
            if(params.length == 0)
            {
                return null;
            }

            String json = null;
            URL queryURL = NetworkUtils.buildUrl(params[0]);
            try
            {
                json = NetworkUtils.getResponseFromOKHttpUrl(queryURL);

                String[] jsonData = OpenWeatherJsonUtils
                        .getSimpleWeatherStringsFromJson(MainActivity.this, json);

                return jsonData;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }
    }
}

