package com.jdid.ekyc.repository.api;

import com.jdid.ekyc.repository.pojo.Otp;
import com.jdid.ekyc.repository.pojo.ResponseOtp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface VerifyUser {

    @Headers("X-API-KEY: 3Oi6FUtmmf0aLt6LzVS2FhZXMmEguCMb")
    @PUT("/user/verify/{id}")
    Call<ResponseOtp> verifyUser(@Path("id") String id, @Body Otp otp);

}
