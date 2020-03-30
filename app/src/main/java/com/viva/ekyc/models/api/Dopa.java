package com.viva.ekyc.models.api;


import com.jdid.ekyc.models.pojo.ResponseDopa;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Dopa {

    // CHECK DOPA
    @Headers("X-API-KEY: DipchipcwHf2shC6FqGm7IFu8x2jzsa5")
    @POST("/CheckCardByLaser")
    Call<ResponseDopa> checkDopa(@Body com.jdid.ekyc.models.pojo.Dopa dopa);

}
