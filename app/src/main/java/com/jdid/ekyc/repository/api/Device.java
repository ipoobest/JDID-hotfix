package com.jdid.ekyc.repository.api;

import com.jdid.ekyc.repository.pojo.Pin;
import com.jdid.ekyc.repository.pojo.ResponCheckPin;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Device {
    @Headers("X-API-KEY: 3Oi6FUtmmf0aLt6LzVS2FhZXMmEguCMb")
    @POST("/device/check_pin")
    Call<ResponCheckPin> CheckPin(@Body Pin pin);
}
