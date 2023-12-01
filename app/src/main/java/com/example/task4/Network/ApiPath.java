package com.example.task4.Network;

import com.example.task4.Activity.BookRide;
import com.example.task4.DataModels.AllVehicals;
import com.example.task4.DataModels.CityVihicals;
import com.example.task4.DataModels.Country;
import com.example.task4.DataModels.DirectionsResponse;
import com.example.task4.DataModels.LocationZone;
import com.example.task4.DataModels.Locationdata;
import com.example.task4.DataModels.Login;
import com.example.task4.DataModels.LoginParameter;
import com.example.task4.DataModels.RideBody;
import com.example.task4.DataModels.RidesRespons;
import com.example.task4.DataModels.Settings;
import com.example.task4.DataModels.User;
import com.example.task4.DataModels.UserCards;
import com.example.task4.DataModels.UserPhone;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiPath {
    @POST("login")
    Call<Login> getUser(@Body LoginParameter parameters);

    @GET("country")
    Call<List<Country>> getGountries(@Header("Authorization")String token);
    @POST("user/phone")
    Call<User> getUserbyNumber(@Header("Authorization")String token, @Body UserPhone phone);
    @GET("setting")
    Call<List<Settings>> getSettings(@Header("Authorization") String token);
    @POST("/city/find")
    Call<List<LocationZone>> checkZone(@Header("Authorization") String token,@Body Locationdata data);
    @GET("maps/api/directions/json")
    Call<DirectionsResponse> getDirections(@Query("origin") String origin, @Query("destination") String destination,@Query("waypoints") String query, @Query("key") String apiKey);
    @GET("/vehiclePrice/fetchAll/{city}")
    Call<List<CityVihicals>> getCityvihicals(@Path("city") String city,@Header("Authorization") String token);
    @POST("/ride/create")
    Call<RideBody.Response> bookRide( @Body RideBody ridebody,@Header("Authorization") String token);
    @POST("/card")
    Call<UserCards> getcards(@Header("Authorization") String token,@Body UserCards.Id id);
    @GET("/ride")
    Call<RidesRespons> getallRides(@Header("Authorization") String token,@Query("page") String pagenum,@Query("search") String search,@Query("vehicalType") String vihical,@Query("rideDateFrom") String dateFrom,@Query("rideDateTo") String dateto,@Query("status") String status);
    @GET("/vehicle")
    Call<List<AllVehicals>> getAllVehicals(@Header("Authorization") String token);
}
