package com.example.paul.livecoding.Endpoints;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CodingCategories {

    @GET("/api/codingcategories/")
    Call<List<com.example.paul.livecoding.POJOs.CodingCategories>> getData();
}
