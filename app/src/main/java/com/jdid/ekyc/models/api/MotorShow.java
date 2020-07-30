package com.jdid.ekyc.models.api;

import com.jdid.ekyc.models.pojo.RequestImageMegvii;
import com.jdid.ekyc.models.pojo.RequestSubjectMegvii;
import com.jdid.ekyc.models.pojo.RequestUserMotorShow;
import com.jdid.ekyc.models.pojo.ResponseImageMegvii;
import com.jdid.ekyc.models.pojo.ResponseParse;
import com.jdid.ekyc.models.pojo.ResponsePhoneNumber;
import com.jdid.ekyc.models.pojo.ResponseSubjectMegvii;
import com.jdid.ekyc.models.pojo.ResultCheckPhoneNUmber;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface MotorShow {

    // USER CREATE USER
//    @Multipart
//    @Headers("Cookie: session=a0d223e2-b6d4-42fb-8ccd-501ed67ae4d5")
//
//    @POST("/subject/photo")
//    Call<ResponseImageMegvii> postImage(@Part MultipartBody.Part photo);

    // USER CREATE USER
    @Multipart
    @Headers("Cookie: session=a0d223e2-b6d4-42fb-8ccd-501ed67ae4d5")

    @POST("/megvii")
    Call<ResponseImageMegvii> postImage(@Part MultipartBody.Part photo,
                                        @Part("name") RequestBody name);


    @Headers("Cookie: session=a0d223e2-b6d4-42fb-8ccd-501ed67ae4d5")
    @POST("/subject")
    Call<ResponseSubjectMegvii> postData(@Body RequestSubjectMegvii request);

    @Headers("X-Parse-Application-Id: 928f24ed35d8876dee76d0a5460ef078")
    @POST("/parse/classes/MotorShow20Coffee")
    Call<ResponseParse> postDataToParse(@Body RequestUserMotorShow request);

//    https://api.jfin.network/parse/classes/MotorShow20Coffee?where={"objectId":"F2PXN0qSH2"}

    @Headers("X-Parse-Application-Id: 928f24ed35d8876dee76d0a5460ef078")
    @GET("/parse/classes/MotorShow20Coffee")
    Call<ResponsePhoneNumber> checkPhoneNumber(@Query("where") String phoneNumber);

}
