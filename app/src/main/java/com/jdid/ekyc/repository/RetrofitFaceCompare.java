package com.jdid.ekyc.repository.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFaceCompare {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://203.150.199.227:9500";
//    http://203.150.199.227:9500/face/v1/algorithm/recognition/face_pair_verification


    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
