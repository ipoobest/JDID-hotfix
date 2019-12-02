package com.jdid.ekyc.models.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyDetail {
    @SerializedName("score_list")
    @Expose
    private int scoreList;
    @SerializedName("query_face_rect")
    @Expose
    private int queryFaceRect;

    public int getScoreList() {
        return scoreList;
    }

    public void setScoreList(int scoreList) {
        this.scoreList = scoreList;
    }

    public int getQueryFaceRect() {
        return queryFaceRect;
    }

    public void setQueryFaceRect(int queryFaceRect) {
        this.queryFaceRect = queryFaceRect;
    }
}
