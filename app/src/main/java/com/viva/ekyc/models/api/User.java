package com.viva.ekyc.models.api;


import com.viva.ekyc.models.pojo.RequestOTPForVerify;
import com.viva.ekyc.models.pojo.ResponseVerifyUser;
import com.viva.ekyc.models.pojo.ResponseCreateUser;
import com.viva.ekyc.models.pojo.UserInformation;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface User {

    // USER CREATE USER
    @Headers("X-API-KEY: 3Oi6FUtmmf0aLt6LzVS2FhZXMmEguCMb")
    @POST("/user")
    Call<ResponseCreateUser> createUser(@Body com.viva.ekyc.models.pojo.User user);

    // USER VERIFY USeR
    @Headers("X-API-KEY: 3Oi6FUtmmf0aLt6LzVS2FhZXMmEguCMb")
    @PUT("/user/verify/{id}")
    Call<ResponseVerifyUser> verifyUser(@Path("id") String id, @Body RequestOTPForVerify requestOTPForVerify);

    // EDIT USER
    @Headers("X-API-KEY: 3Oi6FUtmmf0aLt6LzVS2FhZXMmEguCMb")
    @PUT("/user/{id}")
    Call<ResponseVerifyUser> editteUser(@Path("id") String id,@Body com.viva.ekyc.models.pojo.User user);

    //GET USRT
    @Headers("X-API-KEY: 3Oi6FUtmmf0aLt6LzVS2FhZXMmEguCMb")
    @GET("/user/{id}")
    Call<UserInformation> getUser(@Path("id") String id);

}
