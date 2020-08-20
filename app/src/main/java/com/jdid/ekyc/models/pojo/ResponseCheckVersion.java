package com.jdid.ekyc.models.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseCheckVersion {
    @SerializedName("results")
    @Expose
    private List<ResultCheckVersion> results = null;

    public List<ResultCheckVersion> getResults() {
        return results;
    }

    public void setResults(List<ResultCheckVersion> results) {
        this.results = results;
    }
}
