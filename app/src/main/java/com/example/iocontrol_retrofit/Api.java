package com.example.iocontrol_retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {
    @GET("readData/itcube67/sensorValue")
    Call<Data> readSensorValue();

    @GET("sendData/itCube67/{var}/{value}")
    Call<Data> sendSensorValue(@Path("var") String var, @Path("value") String value);
}
