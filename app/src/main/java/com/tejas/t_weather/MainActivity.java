package com.tejas.t_weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Location & other variables
    String city_name;
    String API_KEY;
    FusedLocationProviderClient fusedLocationProviderClient;
    private final static int REQUEST_CODE = 100;

    ImageView[] hourly_forecast_logo;
    TextView[] hourly_forecast_temp;
    TextView[] hourly_forecast_temptext;

    ScrollView mainScrollView;
    LinearLayout main_linear_layout, extra_linear_layout, search_linear_layout, loading_linear_layout;
    TextView extra_text;
    EditText search_input;
    ImageView weather_logo, extra_image, settings_btn;
    Button tomorrow_forecast_btn, overmorrow_forecast_btn;
    TextView placeHeading, tempratureHeading, weather_condition, local_time;
    TextView details_temprature, details_min_temprature, details_max_temprature, details_feels_like, details_rain_chance, details_snow_chance, details_humidity, details_windspeed, details_winddegree, details_pressure, details_clouds_cover, details_visibility, details_uvindex, details_windgust, details_air_quality;
    TextView details_location, details_region, details_country, details_timezone, details_latitude, details_longitude;
    TextView details_sunrise, details_sunset, details_moonrise, details_moonset;

    boolean is_celsius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Finding ids
        main_linear_layout = findViewById(R.id.main_linear_layout);
        extra_linear_layout = findViewById(R.id.extra_linear_layout);
        search_linear_layout = findViewById(R.id.search_linear_layout);
        loading_linear_layout = findViewById(R.id.loading_linear_layout);
        extra_text = findViewById(R.id.extra_text);
        extra_image = findViewById(R.id.extra_image);
        settings_btn = findViewById(R.id.settings_btn);

        mainScrollView = findViewById(R.id.main_scroll_view);
        search_input = findViewById(R.id.search_input);
        weather_logo = findViewById(R.id.weather_logo);
        placeHeading = findViewById(R.id.placeHeading);
        tempratureHeading = findViewById(R.id.tempratureHeading);
        weather_condition = findViewById(R.id.weather_condition);
        local_time = findViewById(R.id.local_time);

        details_temprature = findViewById(R.id.details_temprature);
        details_min_temprature = findViewById(R.id.details_min_temprature);
        details_max_temprature = findViewById(R.id.details_max_temprature);
        details_feels_like = findViewById(R.id.details_feels_like);
        details_rain_chance = findViewById(R.id.details_rain_chance);
        details_snow_chance = findViewById(R.id.details_snow_chance);
        details_humidity = findViewById(R.id.details_humidity);
        details_windspeed = findViewById(R.id.details_windspeed);
        details_winddegree = findViewById(R.id.details_winddegree);
        details_pressure = findViewById(R.id.details_pressure);
        details_clouds_cover = findViewById(R.id.details_clouds_cover);
        details_visibility = findViewById(R.id.details_visibility);
        details_uvindex = findViewById(R.id.details_uvindex);
        details_windgust = findViewById(R.id.details_windgust);
        details_air_quality = findViewById(R.id.details_air_quality);

        details_location = findViewById(R.id.details_location);
        details_region = findViewById(R.id.details_region);
        details_country = findViewById(R.id.details_country);
        details_timezone = findViewById(R.id.details_timezone);
        details_latitude = findViewById(R.id.details_latitude);
        details_longitude = findViewById(R.id.details_longitude);

        details_sunrise = findViewById(R.id.details_sunrise);
        details_sunset = findViewById(R.id.details_sunset);
        details_moonrise = findViewById(R.id.details_moonrise);
        details_moonset = findViewById(R.id.details_moonset);

        tomorrow_forecast_btn = findViewById(R.id.tomorrow_forecast_btn);
        overmorrow_forecast_btn = findViewById(R.id.overmorrow_forecast_btn);

        // Defining Location Variables & Executing Functions
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        // Defining Required Variables
        API_KEY = "YOUR_API_KEY_HERE"; // I am using weatherapi.com

        is_celsius = check_settings();


        // Ids & Array for todays hourly forecast
        hourly_forecast_logo = new ImageView[]{findViewById(R.id.hourly_forecast_logo1), findViewById(R.id.hourly_forecast_logo2), findViewById(R.id.hourly_forecast_logo3), findViewById(R.id.hourly_forecast_logo4), findViewById(R.id.hourly_forecast_logo5), findViewById(R.id.hourly_forecast_logo6), findViewById(R.id.hourly_forecast_logo7), findViewById(R.id.hourly_forecast_logo8), findViewById(R.id.hourly_forecast_logo9), findViewById(R.id.hourly_forecast_logo10), findViewById(R.id.hourly_forecast_logo11), findViewById(R.id.hourly_forecast_logo12), findViewById(R.id.hourly_forecast_logo13), findViewById(R.id.hourly_forecast_logo14), findViewById(R.id.hourly_forecast_logo15), findViewById(R.id.hourly_forecast_logo16), findViewById(R.id.hourly_forecast_logo17), findViewById(R.id.hourly_forecast_logo18), findViewById(R.id.hourly_forecast_logo19), findViewById(R.id.hourly_forecast_logo20), findViewById(R.id.hourly_forecast_logo21), findViewById(R.id.hourly_forecast_logo22), findViewById(R.id.hourly_forecast_logo23), findViewById(R.id.hourly_forecast_logo24)};
        hourly_forecast_temp = new TextView[]{findViewById(R.id.hourly_forecast_temp1), findViewById(R.id.hourly_forecast_temp2), findViewById(R.id.hourly_forecast_temp3), findViewById(R.id.hourly_forecast_temp4), findViewById(R.id.hourly_forecast_temp5), findViewById(R.id.hourly_forecast_temp6), findViewById(R.id.hourly_forecast_temp7), findViewById(R.id.hourly_forecast_temp8), findViewById(R.id.hourly_forecast_temp9), findViewById(R.id.hourly_forecast_temp10), findViewById(R.id.hourly_forecast_temp11), findViewById(R.id.hourly_forecast_temp12), findViewById(R.id.hourly_forecast_temp13), findViewById(R.id.hourly_forecast_temp14), findViewById(R.id.hourly_forecast_temp15), findViewById(R.id.hourly_forecast_temp16), findViewById(R.id.hourly_forecast_temp17), findViewById(R.id.hourly_forecast_temp18), findViewById(R.id.hourly_forecast_temp19), findViewById(R.id.hourly_forecast_temp20), findViewById(R.id.hourly_forecast_temp21), findViewById(R.id.hourly_forecast_temp22), findViewById(R.id.hourly_forecast_temp23), findViewById(R.id.hourly_forecast_temp24)};
        hourly_forecast_temptext = new TextView[]{findViewById(R.id.hourly_forecast_temptext1), findViewById(R.id.hourly_forecast_temptext2), findViewById(R.id.hourly_forecast_temptext3), findViewById(R.id.hourly_forecast_temptext4), findViewById(R.id.hourly_forecast_temptext5), findViewById(R.id.hourly_forecast_temptext6), findViewById(R.id.hourly_forecast_temptext7), findViewById(R.id.hourly_forecast_temptext8), findViewById(R.id.hourly_forecast_temptext9), findViewById(R.id.hourly_forecast_temptext10), findViewById(R.id.hourly_forecast_temptext11), findViewById(R.id.hourly_forecast_temptext12), findViewById(R.id.hourly_forecast_temptext13), findViewById(R.id.hourly_forecast_temptext14), findViewById(R.id.hourly_forecast_temptext15), findViewById(R.id.hourly_forecast_temptext16), findViewById(R.id.hourly_forecast_temptext17), findViewById(R.id.hourly_forecast_temptext18), findViewById(R.id.hourly_forecast_temptext19), findViewById(R.id.hourly_forecast_temptext20), findViewById(R.id.hourly_forecast_temptext21), findViewById(R.id.hourly_forecast_temptext22), findViewById(R.id.hourly_forecast_temptext23), findViewById(R.id.hourly_forecast_temptext24)};

        // Setting Alpha Value of Background Image
        mainScrollView.getBackground().setAlpha(170);

        // Initializing Android Networking Library
        AndroidNetworking.initialize(getApplicationContext());

        // Searching Cities Weather
        search_input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    // For hiding keyboard
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mainScrollView.getWindowToken(), 0);

                    city_name = search_input.getText().toString();

                    fetch_weather(city_name, API_KEY, hourly_forecast_temp, hourly_forecast_temptext, hourly_forecast_logo);

                    return true;
                }
                return false;
            }
        });

        tomorrow_forecast_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DailyForecastActivity.class);
                intent.putExtra("day_id", 1);
                intent.putExtra("city_name", city_name);
                intent.putExtra("API_KEY", API_KEY);
                intent.putExtra("is_celsius", is_celsius);
                startActivity(intent);
            }
        });

        overmorrow_forecast_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DailyForecastActivity.class);
                intent.putExtra("day_id", 2);
                intent.putExtra("city_name", city_name);
                intent.putExtra("API_KEY", API_KEY);
                intent.putExtra("is_celsius", is_celsius);
                startActivity(intent);
            }
        });

        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                intent.putExtra("is_celsius", is_celsius);
                startActivity(intent);
            }
        });

    }

    private void getLastLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null){
                                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                                try {
                                    List<Address> address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    String current_location = address.get(0).getLocality()+" "+address.get(0).getCountryName();
                                    fetch_weather(current_location, API_KEY, hourly_forecast_temp, hourly_forecast_temptext, hourly_forecast_logo);
                                    city_name = current_location;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });


        }
        else{
            askPermission();
        }
    }

    private void askPermission(){
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else{
                Toast.makeText(this, "Please provide the permission for better experience!", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public boolean check_settings(){
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        String temp_unit = sharedPreferences.getString("temprature_unit", "Celsius");
        boolean temp_unit_bool = false;
        if (temp_unit.equals("Celsius")){
            temp_unit_bool = true;
        }
        return temp_unit_bool;
    }

    public void fetch_weather(String city_name, String API_KEY, TextView[] hourly_forecast_temp, TextView[] hourly_forecast_temptext, ImageView[] hourly_forecast_logo) {
        String url = "https://api.weatherapi.com/v1/forecast.json?key=" + API_KEY + "&q=" + city_name + "&days=10&aqi=yes&alerts=yes";

        mainScrollView.setBackgroundResource(0);
        loading_linear_layout.setVisibility(View.VISIBLE);
        search_linear_layout.setVisibility(View.GONE);
        extra_linear_layout.setVisibility(View.GONE);

        AndroidNetworking.get(url)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            // Getting Json Data
                            JSONObject location = (JSONObject) jsonObject.get("location");
                            JSONObject current = (JSONObject) jsonObject.get("current");
                            JSONArray forecast_day = jsonObject.getJSONObject("forecast").getJSONArray("forecastday");
                            JSONObject forecast_day_today = (JSONObject) forecast_day.get(0);

                            JSONObject forecastday_today_day = (JSONObject) forecast_day_today.getJSONObject("day");
                            JSONObject forecastday_today_astro = (JSONObject) forecast_day_today.getJSONObject("astro");
                            JSONArray forecastday_today_hour = (JSONArray) forecast_day_today.getJSONArray("hour");

                            // Updating Data in Layout
                            placeHeading.setText(location.get("name") + ", " + location.get("country"));
                            weather_condition.setText((CharSequence) current.getJSONObject("condition").get("text"));
                            local_time.setText((CharSequence) location.get("localtime"));

                            // All elements where temprature unit is used
                            if (is_celsius) {
                                tempratureHeading.setText(String.valueOf(Math.round((Double) current.get("temp_c"))) + " °C");
                                details_temprature.setText("Temprature    :    " + String.valueOf(Math.round((Double) current.get("temp_c"))) + " °C");
                                details_min_temprature.setText("Min. Temprature    :    " + String.valueOf(Math.round((Double) forecastday_today_day.get("mintemp_c"))) + " °C");
                                details_max_temprature.setText("Max. Temprature    :    " + String.valueOf(Math.round((Double) forecastday_today_day.get("maxtemp_c"))) + " °C");
                                details_feels_like.setText("Feels Like    :    " + String.valueOf(Math.round((Double) current.get("feelslike_c"))) + " °C");
                            } else{
                                tempratureHeading.setText(String.valueOf(Math.round((Double) current.get("temp_f"))) + " °F");
                                details_temprature.setText("Temprature    :    " + String.valueOf(Math.round((Double) current.get("temp_f"))) + " °F");
                                details_min_temprature.setText("Min. Temprature    :    " + String.valueOf(Math.round((Double) forecastday_today_day.get("mintemp_f"))) + " °F");
                                details_max_temprature.setText("Max. Temprature    :    " + String.valueOf(Math.round((Double) forecastday_today_day.get("maxtemp_f"))) + " °F");
                                details_feels_like.setText("Feels Like    :    " + String.valueOf(Math.round((Double) current.get("feelslike_f"))) + " °F");
                            }

                            details_rain_chance.setText("Chance of Rain    :    " + String.valueOf(forecastday_today_day.get("daily_chance_of_rain")) + " %");
                            details_snow_chance.setText("Chance of Snow    :    " + String.valueOf(forecastday_today_day.get("daily_chance_of_snow")) + " %");
                            details_humidity.setText("Humidity    :    " + String.valueOf(current.get("humidity")) + "%");
                            details_windspeed.setText("Wind Speed    :    " + String.valueOf(current.get("wind_kph")) + " km/h");
                            details_winddegree.setText("Wind Degree    :    " + String.valueOf(current.get("wind_degree")) + " °");
                            details_pressure.setText("Pressure    :    " + String.valueOf(Math.round((Double) current.get("pressure_mb"))) + " mb");
                            details_clouds_cover.setText("Clouds Cover    :    " + String.valueOf(current.get("cloud")) + " %");
                            details_visibility.setText("Visibility    :    " + String.valueOf(current.get("vis_km")) + " km");
                            details_uvindex.setText("UV Index    :    " + String.valueOf(current.get("uv")));
                            details_windgust.setText("Wind Gust    :    " + String.valueOf(current.get("gust_kph")) + " km/h");
                            String aqi_text = aqi_parser(current.getJSONObject("air_quality").getInt("us-epa-index"));
                            details_air_quality.setText("Air Quality    :    " + aqi_text);

                            details_location.setText("Location    :    " + location.getString("name"));
                            details_region.setText("Region    :    " + location.getString("region"));
                            details_country.setText("Country    :    " + location.getString("country"));
                            details_timezone.setText("Timezone    :    " + location.getString("tz_id"));
                            details_latitude.setText("Latitude    :    " + location.getString("lat"));
                            details_longitude.setText("Longitude    :    " + location.getString("lon"));

                            details_sunrise.setText("Sunrise : " + forecastday_today_astro.getString("sunrise"));
                            details_sunset.setText("Sunset : " + forecastday_today_astro.getString("sunset"));
                            details_moonrise.setText("Moonrise : " + forecastday_today_astro.getString("moonrise"));
                            details_moonset.setText("Moonset : " + forecastday_today_astro.getString("moonset"));

                            // Setting Background and Logo as per Weather Condition
                            JSONObject condition = current.getJSONObject("condition");
                            String condition_text = condition.getString("text").toLowerCase();
                            setBackgroundLogo(condition_text, true, weather_logo);


                            for (int i = 0; i < 24; i++) {
                                JSONObject forecastday_today_hour_obj = (JSONObject) forecastday_today_hour.get(i);
                                if (is_celsius)
                                    hourly_forecast_temp[i].setText(String.valueOf(Math.round(forecastday_today_hour_obj.getInt("temp_c"))) + " °C");
                                else
                                    hourly_forecast_temp[i].setText(String.valueOf(Math.round(forecastday_today_hour_obj.getInt("temp_f"))) + " °F");
                                hourly_forecast_temptext[i].setText(forecastday_today_hour_obj.getJSONObject("condition").getString("text"));
                                setBackgroundLogo(forecastday_today_hour_obj.getJSONObject("condition").getString("text"), false, hourly_forecast_logo[i]);
                            }

                            // Setting Daily Forecast Button Text
                            tomorrow_forecast_btn.setText(((JSONObject) forecast_day.get(1)).getString("date")+" Forecast");
                            overmorrow_forecast_btn.setText(((JSONObject) forecast_day.get(2)).getString("date")+" Forecast");

                            // Setting visibility of the layouts
                            loading_linear_layout.setVisibility(View.GONE);
                            search_linear_layout.setVisibility(View.VISIBLE);
                            extra_linear_layout.setVisibility(View.GONE);
                            main_linear_layout.setVisibility(View.VISIBLE);


                        } catch (JSONException e) {
                            mainScrollView.setBackgroundResource(R.drawable.sunny);
                            loading_linear_layout.setVisibility(View.GONE);
                            search_linear_layout.setVisibility(View.VISIBLE);
                            main_linear_layout.setVisibility(View.GONE);
                            extra_linear_layout.setVisibility(View.VISIBLE);
                            extra_text.setText("No Results found for "+city_name);
                            extra_image.setImageResource(R.drawable.results_not_found);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        mainScrollView.setBackgroundResource(R.drawable.sunny);
                        loading_linear_layout.setVisibility(View.GONE);
                        search_linear_layout.setVisibility(View.VISIBLE);
                        main_linear_layout.setVisibility(View.GONE);
                        extra_linear_layout.setVisibility(View.VISIBLE);
                        extra_text.setText("Something went wrong!");
                        extra_image.setImageResource(R.drawable.results_not_found);

                    }
                });


    }


    public void setBackgroundLogo(String condition_text, boolean setBackground, ImageView imageView) {
        if (condition_text.contains("rain") || condition_text.contains("drizzle")) {
            if (setBackground) {
                mainScrollView.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.rain));
            }
            imageView.setImageResource(R.drawable.rain_logo);
        } else if (condition_text.contains("parlty cloudy")) {
            if (setBackground)
                mainScrollView.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.partly_cloudy));
            imageView.setImageResource(R.drawable.partly_cloudy_logo);
        } else if (condition_text.contains("cloudy")) {
            if (setBackground)
                mainScrollView.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.cloudy));
            imageView.setImageResource(R.drawable.cloudy_logo);
        } else if (condition_text.contains("overcast")) {
            if (setBackground)
                mainScrollView.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.overcast));
            imageView.setImageResource(R.drawable.overcast_logo);
        } else if (condition_text.contains("mist") || condition_text.contains("fog")) {
            if (setBackground)
                mainScrollView.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.fog));
            imageView.setImageResource(R.drawable.fog_logo);
        } else if (condition_text.contains("snow") || condition_text.contains("blizzard")) {
            if (setBackground)
                mainScrollView.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.snow));
            imageView.setImageResource(R.drawable.snow_logo);
        } else if (condition_text.contains("sleet") || condition_text.contains("ice pellets")) {
            if (setBackground)
                mainScrollView.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sleet));
            imageView.setImageResource(R.drawable.snow_logo);
        } else if (condition_text.contains("thunder")) {
            if (setBackground)
                mainScrollView.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.thunder));
            imageView.setImageResource(R.drawable.thunder_logo);
        } else {
            if (setBackground)
                mainScrollView.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sunny));
            imageView.setImageResource(R.drawable.sunny_logo);
        }
        if (setBackground)
            mainScrollView.getBackground().setAlpha(170);
    }

    public String aqi_parser(int aqi_index) {
        if (aqi_index == 1) {
            return "Good";
        } else if (aqi_index == 2) {
            return "Moderate";
        } else if (aqi_index == 3) {
            return "Unhealthy for sensitive group";
        } else if (aqi_index == 4) {
            return "Unhealthy";
        } else if (aqi_index == 5) {
            return "Very Unhealthy";
        } else {
            return "Hazardous";
        }
    }
}