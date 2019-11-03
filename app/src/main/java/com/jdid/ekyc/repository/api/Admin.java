package com.jdid.ekyc.repository.api;

import com.jdid.ekyc.repository.pojo.RequestOTPForRegister;
import com.jdid.ekyc.repository.pojo.RequestOTPForVerify;
import com.jdid.ekyc.repository.pojo.ResponseOTPForRegister;
import com.jdid.ekyc.repository.pojo.ResponseOTPForVerify;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Admin {

    // ADMIN SEND OTP
    @Headers("X-API-KEY: 3Oi6FUtmmf0aLt6LzVS2FhZXMmEguCMb")
    @POST("/admin/login")
    Call<ResponseOTPForRegister> sentOTP(@Body RequestOTPForRegister RequestOTPForRegister);

    // ADMIN VERIFY OTP
    @Headers("X-API-KEY: 3Oi6FUtmmf0aLt6LzVS2FhZXMmEguCMb")
    @POST("/admin/verify_login")
    Call<ResponseOTPForVerify> verifyOTP(@Body RequestOTPForVerify requestOTPForVerify);
}
