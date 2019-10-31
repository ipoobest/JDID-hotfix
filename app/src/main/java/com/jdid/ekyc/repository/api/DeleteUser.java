package com.jdid.ekyc.repository.api;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DeleteUser {
    @Headers("X-API-KEY: 3Oi6FUtmmf0aLt6LzVS2FhZXMmEguCMb")
    @DELETE("/user/{id}")
    Call<JSONObject> deleteUser(@Path("id") int id);
}
