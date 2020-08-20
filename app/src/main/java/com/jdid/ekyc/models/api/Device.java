package com.jdid.ekyc.models.api;

import com.jdid.ekyc.models.pojo.RequestVrifyPin;
import com.jdid.ekyc.models.pojo.ResponVerifyPin;
import com.jdid.ekyc.models.pojo.ResponseCheckVersion;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Device {

    // Device CHECK PIN
    @Headers("X-API-KEY: 3Oi6FUtmmf0aLt6LzVS2FhZXMmEguCMb")
    @POST("/device/check_pin")
    Call<ResponVerifyPin> checkPin(@Body RequestVrifyPin requestVrifyPin);

    @Headers("X-Parse-Application-Id: 928f24ed35d8876dee76d0a5460ef078")
    @GET("/parse/classes/Config?where={ \"name\":\"ekyc_version\"}")
    Call<ResponseCheckVersion> checkVersion();
}
