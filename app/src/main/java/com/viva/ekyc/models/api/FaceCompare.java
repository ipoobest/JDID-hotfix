package com.viva.ekyc.models.api;

import com.viva.ekyc.models.pojo.FaceCompareRequest;
import com.viva.ekyc.models.pojo.FaceCompareResult;
import com.viva.ekyc.models.pojo.RequestFaceCompare;
import com.viva.ekyc.models.pojo.ResponseFaceCompare;

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

    @Headers("Content-Type: application/json")
    @POST("/face/v1/algorithm/recognition/face_pair_verification")
    Call<FaceCompareResult> faceCompareBase(@Body FaceCompareRequest faceCompare);
}

