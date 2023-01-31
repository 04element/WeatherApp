package com.example.weather2;

import com.google.gson.annotations.SerializedName;

public class WeatherSysModel {
    @SerializedName("country")
    String country = "";// 나라

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
