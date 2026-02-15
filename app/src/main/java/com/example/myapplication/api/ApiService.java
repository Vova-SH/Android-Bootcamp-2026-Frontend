package com.example.myapplication.api;

import com.example.myapplication.models.api.MeetingsResponse;
import com.example.myapplication.models.api.UserResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("/api/meetings/paginated")
    Call<MeetingsResponse> getMeetings(
            @Query("page") int page,
            @Query("size") int size,
            @Header("Authorization") String auth
    );

    @GET("/api/users/{id}")
    Call<UserResponse> getUserById(
            @Path("id") long id,
            @Header("Authorization") String auth
    );
}