package com.jdid.ekyc.models.api;

import com.jdid.ekyc.models.pojo.FaceCompareRequest;
import com.jdid.ekyc.models.pojo.FaceCompareResult;
import com.jdid.ekyc.models.pojo.RequestFaceCompare;
import com.jdid.ekyc.models.pojo.ResponseFaceCompare;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface FaceCompare {

// compare face
    @Headers("X-API-KEY: HaZxbQhVpu6xwZvog0Nph8PmZErB6aha")
    @POST("/face-api/v3/face/match")
    Call<ResponseFaceCompare> faceCompare(@Body List<RequestFaceCompare> requestFaceCompare);


}


