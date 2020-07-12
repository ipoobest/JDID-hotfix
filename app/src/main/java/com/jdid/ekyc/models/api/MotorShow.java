package com.jdid.ekyc.models.api;

import com.jdid.ekyc.models.pojo.RequestImageMegvii;
import com.jdid.ekyc.models.pojo.RequestSubjectMegvii;
import com.jdid.ekyc.models.pojo.RequestUserMotorShow;
import com.jdid.ekyc.models.pojo.ResponseImageMegvii;
import com.jdid.ekyc.models.pojo.ResponseParse;
import com.jdid.ekyc.models.pojo.ResponseSubjectMegvii;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface MotorShow {

    // USER CREATE USER
    @Multipart
    @Headers("Cookie: session=a0d223e2-b6d4-42fb-8ccd-501ed67ae4d5")

    @POST("/subject/photo")
    Call<ResponseImageMegvii> postImage(@Part MultipartBody.Part photo);

    @Headers("Cookie: session=a0d223e2-b6d4-42fb-8ccd-501ed67ae4d5")
    @POST("/subject")
    Call<ResponseSubjectMegvii> postData(@Body RequestSubjectMegvii request);

    @Headers("X-Parse-Application-Id: 928f24ed35d8876dee76d0a5460ef078")
    @POST("/classes/MotorShow20Coffee")
    Call<ResponseParse> postDataToParse(@Body RequestUserMotorShow request);

}
