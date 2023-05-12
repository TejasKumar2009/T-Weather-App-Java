package com.tejas.t_weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DailyForecastActivity extends AppCompatActivity {
    ScrollView mainScrollView;
    ImageView weather_logo, back_btn;
    TextView placeHeading, tempratureHeading, weather_condition, local_time;
    TextView details_avgtemprature, details_min_temprature, details_max_temprature, details_rain_chance, details_snow_chance, max_wind_speed, details_avghumidity, details_avgvisibility, details_uvindex;
    TextView details_sunrise, details_sunset, details_moonrise, details_moonset;

    boolean is_celsius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);

        // Finding ids
        mainScrollView = findViewById(R.id.main_scroll_view);
        weather_logo = findViewById(R.id.weather_logo);
        placeHeading = findViewById(R.id.placeHeading);
        tempratureHeading = findViewById(R.id.tempratureHeading);
        weather_condition = findViewById(R.id.weather_condition);
        local_time = findViewById(R.id.local_time);
        back_btn = findViewById(R.id.back_btn);

        details_avgtemprature = findViewById(R.id.details_avgtemprature);
        details_min_temprature = findViewById(R.id.details_min_temprature);
        details_max_temprature = findViewById(R.id.details_max_temprature);
        details_rain_chance = findViewById(R.id.details_rain_chance);
        details_snow_chance = findViewById(R.id.details_snow_chance);
        max_wind_speed = findViewById(R.id.max_wind_speed);
        details_avghumidity = findViewById(R.id.details_avghumidity);
        details_avgvisibility = findViewById(R.id.details_avgvisibility);
        details_uvindex = findViewById(R.id.details_uvindex);

        details_sunrise = findViewById(R.id.details_sunrise);
        details_sunset = findViewById(R.id.details_sunset);
        details_moonrise = findViewById(R.id.details_moonrise);
        details_moonset = findViewById(R.id.details_moonset);


        // Ids & Array for todays hourly forecast
        ImageView[] hourly_forecast_logo = {findViewById(R.id.hourly_forecast_logo1), findViewById(R.id.hourly_forecast_logo2), findViewById(R.id.hourly_forecast_logo3), findViewById(R.id.hourly_forecast_logo4), findViewById(R.id.hourly_forecast_logo5), findViewById(R.id.hourly_forecast_logo6), findViewById(R.id.hourly_forecast_logo7), findViewById(R.id.hourly_forecast_logo8), findViewById(R.id.hourly_forecast_logo9), findViewById(R.id.hourly_forecast_logo10), findViewById(R.id.hourly_forecast_logo11), findViewById(R.id.hourly_forecast_logo12), findViewById(R.id.hourly_forecast_logo13), findViewById(R.id.hourly_forecast_logo14), findViewById(R.id.hourly_forecast_logo15), findViewById(R.id.hourly_forecast_logo16), findViewById(R.id.hourly_forecast_logo17), findViewById(R.id.hourly_forecast_logo18), findViewById(R.id.hourly_forecast_logo19), findViewById(R.id.hourly_forecast_logo20), findViewById(R.id.hourly_forecast_logo21), findViewById(R.id.hourly_forecast_logo22), findViewById(R.id.hourly_forecast_logo23), findViewById(R.id.hourly_forecast_logo24)};
        TextView[] hourly_forecast_temp = {findViewById(R.id.hourly_forecast_temp1), findViewById(R.id.hourly_forecast_temp2), findViewById(R.id.hourly_forecast_temp3), findViewById(R.id.hourly_forecast_temp4), findViewById(R.id.hourly_forecast_temp5), findViewById(R.id.hourly_forecast_temp6), findViewById(R.id.hourly_forecast_temp7), findViewById(R.id.hourly_forecast_temp8), findViewById(R.id.hourly_forecast_temp9), findViewById(R.id.hourly_forecast_temp10), findViewById(R.id.hourly_forecast_temp11), findViewById(R.id.hourly_forecast_temp12), findViewById(R.id.hourly_forecast_temp13), findViewById(R.id.hourly_forecast_temp14), findViewById(R.id.hourly_forecast_temp15), findViewById(R.id.hourly_forecast_temp16), findViewById(R.id.hourly_forecast_temp17), findViewById(R.id.hourly_forecast_temp18), findViewById(R.id.hourly_forecast_temp19), findViewById(R.id.hourly_forecast_temp20), findViewById(R.id.hourly_forecast_temp21), findViewById(R.id.hourly_forecast_temp22), findViewById(R.id.hourly_forecast_temp23), findViewById(R.id.hourly_forecast_temp24)};
        TextView[] hourly_forecast_temptext = {findViewById(R.id.hourly_forecast_temptext1), findViewById(R.id.hourly_forecast_temptext2), findViewById(R.id.hourly_forecast_temptext3), findViewById(R.id.hourly_forecast_temptext4), findViewById(R.id.hourly_forecast_temptext5), findViewById(R.id.hourly_forecast_temptext6), findViewById(R.id.hourly_forecast_temptext7), findViewById(R.id.hourly_forecast_temptext8), findViewById(R.id.hourly_forecast_temptext9), findViewById(R.id.hourly_forecast_temptext10), findViewById(R.id.hourly_forecast_temptext11), findViewById(R.id.hourly_forecast_temptext12), findViewById(R.id.hourly_forecast_temptext13), findViewById(R.id.hourly_forecast_temptext14), findViewById(R.id.hourly_forecast_temptext15), findViewById(R.id.hourly_forecast_temptext16), findViewById(R.id.hourly_forecast_temptext17), findViewById(R.id.hourly_forecast_temptext18), findViewById(R.id.hourly_forecast_temptext19), findViewById(R.id.hourly_forecast_temptext20), findViewById(R.id.hourly_forecast_temptext21), findViewById(R.id.hourly_forecast_temptext22), findViewById(R.id.hourly_forecast_temptext23), findViewById(R.id.hourly_forecast_temptext24)};

        // Setting Alpha Value of Background Image
        mainScrollView.getBackground().setAlpha(170);

        // Getting data from intent
        Intent intent = getIntent();
        int forecast_day_id = intent.getIntExtra("day_id",0);
        String city_name = intent.getStringExtra("city_name");
        String API_KEY = intent.getStringExtra("API_KEY");

        String url = "https://api.weatherapi.com/v1/forecast.json?key=" + API_KEY + "&q=" + city_name + "&days=10&aqi=yes&alerts=yes";
        is_celsius = intent.getBooleanExtra("is_celsius", true);

        // Calling fetch_weather method
        fetch_weather(url, hourly_forecast_temp, hourly_forecast_temptext, hourly_forecast_logo, forecast_day_id);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DailyForecastActivity.this.finish();
            }
        });


    }

    public void fetch_weather(String url, TextView[] hourly_forecast_temp, TextView[] hourly_forecast_temptext, ImageView[] hourly_forecast_logo, int day_id){
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.get(url)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            JSONArray forecast_day = jsonObject.getJSONObject("forecast").getJSONArray("forecastday");
                            JSONObject forecast_day_obj = (JSONObject) forecast_day.get(day_id);
                            JSONObject location = (JSONObject) jsonObject.getJSONObject("location");

                            JSONObject forecastday_day = forecast_day_obj.getJSONObject("day");
                            JSONObject forecastday_astro = forecast_day_obj.getJSONObject("astro");
                            JSONArray forecastday_hour = forecast_day_obj.getJSONArray("hour");

                            // Updating Data in Layout
                            placeHeading.setText(location.get("name") + ", " + location.get("country"));
                            weather_condition.setText((CharSequence) forecastday_day.getJSONObject("condition").get("text"));
                            local_time.setText(forecast_day_obj.getString("date"));

                            if (is_celsius) {
                                tempratureHeading.setText(String.valueOf(Math.round((Double) forecastday_day.get("avgtemp_c"))) + " °C");
                                details_avgtemprature.setText("Avg. Temprature    :    " + String.valueOf(Math.round((Double) forecastday_day.get("avgtemp_c"))) + " °C");
                                details_min_temprature.setText("Min. Temprature    :    " + String.valueOf(Math.round((Double) forecastday_day.get("mintemp_c"))) + " °C");
                                details_max_temprature.setText("Max. Temprature    :    " + String.valueOf(Math.round((Double) forecastday_day.get("maxtemp_c"))) + " °C");
                            } else{
                                tempratureHeading.setText(String.valueOf(Math.round((Double) forecastday_day.get("avgtemp_f"))) + " °F");
                                details_avgtemprature.setText("Avg. Temprature    :    " + String.valueOf(Math.round((Double) forecastday_day.get("avgtemp_f"))) + " °F");
                                details_min_temprature.setText("Min. Temprature    :    " + String.valueOf(Math.round((Double) forecastday_day.get("mintemp_f"))) + " °F");
                                details_max_temprature.setText("Max. Temprature    :    " + String.valueOf(Math.round((Double) forecastday_day.get("maxtemp_f"))) + " °F");
                            }

                            details_rain_chance.setText("Chance of Rain    :    " + String.valueOf(forecastday_day.get("daily_chance_of_rain")) + " %");
                            details_snow_chance.setText("Chance of Snow    :    " + String.valueOf(forecastday_day.get("daily_chance_of_snow")) + " %");
                            details_avghumidity.setText("Avg. Humidity    :    " + String.valueOf(forecastday_day.get("avghumidity")) + "%");
                            max_wind_speed.setText("Max. Wind Speed    :    " + String.valueOf(forecastday_day.get("maxwind_kph")) + " km/h");
                            details_avgvisibility.setText("Avg. Visibility    :    " + String.valueOf(forecastday_day.get("avgvis_km")) + " km");
                            details_uvindex.setText("UV Index    :    " + String.valueOf(forecastday_day.get("uv")));

                            details_sunrise.setText("Sunrise : " + forecastday_astro.getString("sunrise"));
                            details_sunset.setText("Sunset : " + forecastday_astro.getString("sunset"));
                            details_moonrise.setText("Moonrise : " + forecastday_astro.getString("moonrise"));
                            details_moonset.setText("Moonset : " + forecastday_astro.getString("moonset"));

                            // Setting Background and Logo as per Weather Condition
                            JSONObject condition = forecastday_day.getJSONObject("condition");
                            String condition_text = condition.getString("text").toLowerCase();
                            setBackgroundLogo(condition_text, true, weather_logo);

                            for (int i = 0; i < 24; i++) {
                                JSONObject forecastday_hour_obj = (JSONObject) forecastday_hour.get(i);
                                if (is_celsius)
                                    hourly_forecast_temp[i].setText(String.valueOf(Math.round(forecastday_hour_obj.getInt("temp_c"))) + " °C");
                                else
                                    hourly_forecast_temp[i].setText(String.valueOf(Math.round(forecastday_hour_obj.getInt("temp_f"))) + " °F");
                                hourly_forecast_temptext[i].setText(forecastday_hour_obj.getJSONObject("condition").getString("text"));
                                setBackgroundLogo(forecastday_hour_obj.getJSONObject("condition").getString("text"), false, hourly_forecast_logo[i]);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }


    public void setBackgroundLogo(String condition_text, boolean setBackground, ImageView imageView) {
        if (condition_text.contains("rain") || condition_text.contains("drizzle")) {
            if (setBackground) {
                mainScrollView.setBackground(ContextCompat.getDrawable(DailyForecastActivity.this, R.drawable.rain));
            }
            imageView.setImageResource(R.drawable.rain_logo);
        } else if (condition_text.contains("parlty cloudy")) {
            if (setBackground)
                mainScrollView.setBackground(ContextCompat.getDrawable(DailyForecastActivity.this, R.drawable.partly_cloudy));
            imageView.setImageResource(R.drawable.partly_cloudy_logo);
        } else if (condition_text.contains("cloudy")) {
            if (setBackground)
                mainScrollView.setBackground(ContextCompat.getDrawable(DailyForecastActivity.this, R.drawable.cloudy));
            imageView.setImageResource(R.drawable.cloudy_logo);
        } else if (condition_text.contains("overcast")) {
            if (setBackground)
                mainScrollView.setBackground(ContextCompat.getDrawable(DailyForecastActivity.this, R.drawable.overcast));
            imageView.setImageResource(R.drawable.overcast_logo);
        } else if (condition_text.contains("mist") || condition_text.contains("fog")) {
            if (setBackground)
                mainScrollView.setBackground(ContextCompat.getDrawable(DailyForecastActivity.this, R.drawable.fog));
            imageView.setImageResource(R.drawable.fog_logo);
        } else if (condition_text.contains("snow") || condition_text.contains("blizzard")) {
            if (setBackground)
                mainScrollView.setBackground(ContextCompat.getDrawable(DailyForecastActivity.this, R.drawable.snow));
            imageView.setImageResource(R.drawable.snow_logo);
        } else if (condition_text.contains("sleet") || condition_text.contains("ice pellets")) {
            if (setBackground)
                mainScrollView.setBackground(ContextCompat.getDrawable(DailyForecastActivity.this, R.drawable.sleet));
            imageView.setImageResource(R.drawable.snow_logo);
        } else if (condition_text.contains("thunder")) {
            if (setBackground)
                mainScrollView.setBackground(ContextCompat.getDrawable(DailyForecastActivity.this, R.drawable.thunder));
            imageView.setImageResource(R.drawable.thunder_logo);
        } else {
            if (setBackground)
                mainScrollView.setBackground(ContextCompat.getDrawable(DailyForecastActivity.this, R.drawable.sunny));
            imageView.setImageResource(R.drawable.sunny_logo);
        }
        if (setBackground)
            mainScrollView.getBackground().setAlpha(170);
    }

}