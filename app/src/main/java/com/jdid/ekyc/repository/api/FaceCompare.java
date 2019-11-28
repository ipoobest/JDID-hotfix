package com.jdid.ekyc.repository.api;

import com.jdid.ekyc.repository.pojo.FaceCompareRequest;
import com.jdid.ekyc.repository.pojo.FaceCompareResult;
import com.jdid.ekyc.repository.pojo.RequestFaceCompare;
import com.jdid.ekyc.repository.pojo.ResponseFaceCompare;

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


