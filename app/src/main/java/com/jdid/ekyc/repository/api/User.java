package com.jdid.ekyc.repository.api;

import com.jdid.ekyc.repository.pojo.RequestOTPForVerify;
import com.jdid.ekyc.repository.pojo.RequestCreateUser;
import com.jdid.ekyc.repository.pojo.RequestPutUser;
import com.jdid.ekyc.repository.pojo.ResponseVerifyUser;
import com.jdid.ekyc.repository.pojo.ResponseCreateUser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface User {

    // USER CREATE USER
    @Headers("X-API-KEY: 3Oi6FUtmmf0aLt6LzVS2FhZXMmEguCMb")
    @POST("/user")
    Call<ResponseCreateUser> createUser(@Body RequestCreateUser user);

    // USER VERIFY USeR
    @Headers("X-API-KEY: 3Oi6FUtmmf0aLt6LzVS2FhZXMmEguCMb")
    @PUT("/user/verify/{id}")
    Call<ResponseVerifyUser> verifyUser(@Path("id") String id, @Body RequestOTPForVerify requestOTPForVerify);

    @Headers("X-API-KEY: 3Oi6FUtmmf0aLt6LzVS2FhZXMmEguCMb")
    @PUT("/user/{id}")
    Call<ResponseVerifyUser> editteUser(@Path("id") String id,@Body RequestPutUser user);


}
