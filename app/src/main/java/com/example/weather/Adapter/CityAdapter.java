package com.example.weather.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {

    public interface OnCityClickListener {
        void onCityClick(String cityName);
    }

    private final List<String> cityList;
    private final OnCityClickListener listener;

    public CityAdapter(List<String> cityList, OnCityClickListener listener) {
        this.cityList = cityList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // ✅ Use built-in Android layout (no custom XML)
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        String city = cityList.get(position);
        holder.txtCity.setText(city);

        holder.txtCity.setTextColor(Color.parseColor("#ffffff")); // teal color

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCityClick(city);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    static class CityViewHolder extends RecyclerView.ViewHolder {
        TextView txtCity;

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            // ✅ Reference built-in TextView ID
            txtCity = itemView.findViewById(android.R.id.text1);
        }
    }
}
