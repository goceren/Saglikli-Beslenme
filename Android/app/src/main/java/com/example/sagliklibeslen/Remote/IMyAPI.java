package com.example.sagliklibeslen.Remote;

import com.example.sagliklibeslen.Model.Besinler;
import com.example.sagliklibeslen.Model.GetGunlukModel;
import com.example.sagliklibeslen.Model.GetSu;
import com.example.sagliklibeslen.Model.Su;
import com.example.sagliklibeslen.Model.Users;
import com.example.sagliklibeslen.Model.YedigiBesin;

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

    @GET("api/besinler/all")
    Observable<String> getBesinler();

    @GET("api/butunBesinler/{kid}")
    Observable<String> getButunBesinler(@Path("kid") int kid);

    @GET("/api/butunBesinler/besin/{bid}")
    Observable<String> besinGetir(@Path("bid") int bid);

    @POST("api/besinler/create")
    Observable<String> besinEkle(@Body Besinler besin);

    @POST("api/besinler/update")
    Observable<String> besinGuncelle(@Body Besinler besin);

    @POST("api/yedigibesin/ekle")
    Observable<String> yedigimEkle(@Body YedigiBesin besin);

    @GET("api/besinler/delete/{bid}")
    Observable<String> besinSil(@Path("bid") int bid);

    @POST("api/su/ekle")
    Observable<String> suEkle(@Body Su su);

    @POST("api/user/su/date")
    Observable<String> getGunlukSu(@Body GetSu su);
}
