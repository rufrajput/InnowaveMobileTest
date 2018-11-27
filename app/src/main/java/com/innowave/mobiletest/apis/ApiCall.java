package com.innowave.mobiletest.apis;

import com.innowave.mobiletest.models.UsersResponse.Item;
import com.innowave.mobiletest.models.UsersResponse.UsersResults;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiCall {

    @GET("search/users")
    Observable<UsersResults> getUsers(@Query("q") String q, @Header("Authorization") String authHeader);

    @GET("users/{username}")
    Observable<Item> getUserDetails(@Path("username") String username, @Header("Authorization") String authHeader);

    @GET("users/{username}/followers")
    Observable<List<Item>> getFollowers(@Path("username") String username, @Header("Authorization") String authHeader);

}
