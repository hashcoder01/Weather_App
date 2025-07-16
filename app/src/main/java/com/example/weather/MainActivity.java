package com.example.weather;

import com.example.weather.Fragments.change_cityFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView txt_country, txt_city, txt_condition, txt_date, txt_temperature,
            txt_feellike, txt_humidity, txt_wind, txt_pressure, txt_cloud, txt_Rain;

    ImageView weather_icon;
    ImageButton btn_setting, btn_refresh;

    String cityName;

    // 🌍 WeatherAPI URL
    String baseUrl = "https://api.weatherapi.com/v1/forecast.json?key=abb0369475c54aeb8be120754230907&q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Find views
        txt_country = findViewById(R.id.txtcountry);
        txt_city = findViewById(R.id.txtcity);
        txt_condition = findViewById(R.id.txtcondition);
        txt_date = findViewById(R.id.txtdate);
        txt_temperature = findViewById(R.id.txtTemprature);
        weather_icon = findViewById(R.id.weather_icon);
        txt_feellike = findViewById(R.id.txtfeellike);
        txt_humidity = findViewById(R.id.txtHumidity);
        txt_wind = findViewById(R.id.txtwind);
        txt_pressure = findViewById(R.id.txtpressure);
        txt_cloud = findViewById(R.id.txtcloud);
        txt_Rain = findViewById(R.id.txtrainchance);
        btn_setting = findViewById(R.id.btnsetting);
        btn_refresh = findViewById(R.id.refreshbtn);

        // Get saved city or use default
        SharedPreferences prefs = getSharedPreferences("weather_pref", Context.MODE_PRIVATE);
        cityName = prefs.getString("selected_city", "Lahore");

        // Load weather data
        loadWeatherData(cityName);

        // Refresh button
        btn_refresh.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Weather Updated...", Toast.LENGTH_SHORT).show();
            loadWeatherData(cityName);
        });

        // Open bottom sheet on settings button
        btn_setting.setOnClickListener(v -> {
            change_cityFragment bottomSheet = new change_cityFragment();
            bottomSheet.setOnCitySelectedListener(city -> {
                cityName = city;

                SharedPreferences.Editor editor = getSharedPreferences("weather_pref", Context.MODE_PRIVATE).edit();
                editor.putString("selected_city", cityName);
                editor.apply();

                loadWeatherData(cityName);
                Toast.makeText(this, "Selected City: " + cityName, Toast.LENGTH_SHORT).show();
            });
            bottomSheet.show(getSupportFragmentManager(), "CityBottomSheet");
        });
    }

    // Load weather data from API
    private void loadWeatherData(String city) {
        String url = baseUrl + city;

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject weather = new JSONObject(response);
                JSONObject location = weather.getJSONObject("location");
                JSONObject current = weather.getJSONObject("current");
                JSONObject condition = current.getJSONObject("condition");

                JSONObject forecast = weather.getJSONObject("forecast");
                JSONObject forecastDay = forecast.getJSONArray("forecastday").getJSONObject(0);
                JSONObject day = forecastDay.getJSONObject("day");

                String rainChance = day.getString("daily_chance_of_rain");

                // Set data
                txt_country.setText(location.getString("country"));
                txt_city.setText(location.getString("name"));
                txt_condition.setText(condition.getString("text"));
                txt_date.setText(location.getString("localtime"));
                txt_temperature.setText(current.getString("temp_c") + "°C");
                txt_feellike.setText(current.getString("feelslike_c") + "°C");
                txt_humidity.setText(current.getString("humidity") + "%");
                txt_wind.setText(current.getString("wind_kph") + " km/h");
                txt_pressure.setText(current.getString("pressure_mb") + " mb");
                txt_cloud.setText(current.getString("cloud") + "%");
                txt_Rain.setText(rainChance + "%");

                Glide.with(this).load("https:" + condition.getString("icon")).into(weather_icon);

            } catch (JSONException e) {
                Toast.makeText(this, "Parsing Error", Toast.LENGTH_SHORT).show();
                Log.e("JSON_ERROR", e.toString());
            }
        }, error -> {
            Toast.makeText(this, "Network Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("API_ERROR", error.toString());
        });

        Volley.newRequestQueue(this).add(request);
    }
}
