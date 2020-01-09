package com.viva.ekyc.models.api;

import com.viva.ekyc.models.pojo.RequestOTPForRegister;
import com.viva.ekyc.models.pojo.RequestOTPForVerify;
import com.viva.ekyc.models.pojo.ResponseOTPForRegister;
import com.viva.ekyc.models.pojo.ResponseOTPForVerify;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Admin {

    // ADMIN SEND OTP
    @Headers("X-API-KEY: 3Oi6FUtmmf0aLt6LzVS2FhZXMmEguCMb")
    @POST("/admin/send_otp")
    Call<ResponseOTPForRegister> sentOTP(@Body RequestOTPForRegister RequestOTPForRegister);

    // ADMIN VERIFY OTP
    @Headers("X-API-KEY: 3Oi6FUtmmf0aLt6LzVS2FhZXMmEguCMb")
    @POST("/admin/verify_login")
    Call<ResponseOTPForVerify> verifyOTP(@Body RequestOTPForVerify requestOTPForVerify);
}
