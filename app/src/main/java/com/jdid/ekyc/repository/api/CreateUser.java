package com.jdid.ekyc.repository.api;

import com.jdid.ekyc.repository.pojo.User;
import com.jdid.ekyc.repository.pojo.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CreateUser {
    @Headers("X-API-KEY: 3Oi6FUtmmf0aLt6LzVS2FhZXMmEguCMb")
    @POST("/user")
    Call<UserResponse> createUser(@Body User user);
}
