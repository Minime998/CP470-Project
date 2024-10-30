package com.example.camlingo.gpt_api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface OpenAI {
    @POST("v1/models/gpt-4o-mini/predict")
    Call<gpt_api.PredictResponse> predict(@Header("Authorization") String authHeader, @Body gpt_api.ImgUpload data);
}
