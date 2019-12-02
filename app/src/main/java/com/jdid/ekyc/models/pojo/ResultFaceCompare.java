package com.jdid.ekyc.models.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultFaceCompare {
    @SerializedName("score")
    @Expose
    private Double score;
    @SerializedName("face_list")
    @Expose
    private List<FaceList> faceList = null;

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public List<FaceList> getFaceList() {
        return faceList;
    }

    public void setFaceList(List<FaceList> faceList) {
        this.faceList = faceList;
    }

}
