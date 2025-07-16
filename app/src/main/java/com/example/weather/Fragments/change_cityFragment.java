package com.example.weather.Fragments ;

import android.content.Context;
import com.example.weather.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.weather.Adapter.CityAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class change_cityFragment extends BottomSheetDialogFragment {

    public interface OnCitySelectedListener {
        void onCitySelected(String cityName);
    }

    private OnCitySelectedListener listener;

    public void setOnCitySelectedListener(OnCitySelectedListener listener) {
        this.listener = listener;
    }

    private final List<String> cityList = Arrays.asList(
            "Dera Ghazi Khan", "Multan" , "Lahore","Bahawalpur", "Karachi", "Islamabad", "Peshawar", "Quetta",
            "Rawalpindi", "Gujranwala" ,"Hyderabad" ,"Sialkot" ,"chakwal","sheikupura","jhelum","sargodha","Layyah"
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_city, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerCity);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new CityAdapter(cityList, city -> {
            if (listener != null) {
                listener.onCitySelected(city);
                dismiss(); // Close bottom sheet after selection
            }
        }));

        return view;
    }
}
