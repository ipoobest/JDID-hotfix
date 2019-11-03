package com.jdid.ekyc.repository.api;

import com.jdid.ekyc.repository.pojo.RequestVrifyPin;
import com.jdid.ekyc.repository.pojo.ResponVerifyPin;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Device {

    // Device CHECK PIN
    @Headers("X-API-KEY: 3Oi6FUtmmf0aLt6LzVS2FhZXMmEguCMb")
    @POST("/device/check_pin")
    Call<ResponVerifyPin> checkPin(@Body RequestVrifyPin requestVrifyPin);
}
