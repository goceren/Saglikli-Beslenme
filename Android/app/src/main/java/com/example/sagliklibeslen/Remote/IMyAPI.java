package com.example.sagliklibeslen.Remote;

import com.example.sagliklibeslen.Model.GetGunlukModel;
import com.example.sagliklibeslen.Model.Users;
import io.reactivex.Observable;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IMyAPI {

    //https://localhost:44337/api/user/login

    @POST("api/user/register")
    Observable<String> registerUser(@Body Users user);

    @POST("api/user/login")
    Observable<String> loginUser(@Body Users user);

    @POST("api/user/updateDetails")
    Observable<String> updateUser(@Body Users user);

    @POST("api/user/getbesin/date")
    Observable<String> getGunlukByDate(@Body GetGunlukModel model);

    @GET("api/user/{id}")
    Observable<String> getUser(@Path("id") int id);
}
