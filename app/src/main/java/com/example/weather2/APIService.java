package com.example.weather2;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    @POST("data/2.5/{path}")
    Call<WeatherInfoModel> doGetJsonData(
            @Path("path") String path,
            @Query("q") String q,
            @Query("appid") String appid
    );
}
