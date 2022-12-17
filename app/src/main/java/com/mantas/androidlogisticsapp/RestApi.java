package com.mantas.androidlogisticsapp;

import com.mantas.androidlogisticsapp.model.DtoCheckpoints;
import com.mantas.androidlogisticsapp.model.DtoPostCheckpoint;
import com.mantas.androidlogisticsapp.model.DtoRoutes;
import com.mantas.androidlogisticsapp.model.DtoUser;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestApi {

    @GET("/api/v1/users")
    Call<List<DtoUser>> getUsers();

    @GET("/api/v1/routes")
    Call<List<DtoRoutes>> getRoutes();

    @GET("/api/v1/checkpoints")
    Call<List<DtoCheckpoints>> getCheckpoints();

    @POST("/api/v1/checkpoints")
    Call<DtoPostCheckpoint> createCheckpoint(@Body DtoPostCheckpoint dtoPostCheckpoint);

    @DELETE("/api/v1/users/{id}")
    Call<ResponseBody> deleteCheckpoint(@Path("id") String userId);
}
