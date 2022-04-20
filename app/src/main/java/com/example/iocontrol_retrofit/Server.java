package com.example.iocontrol_retrofit;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Server {
      public Data readData(){

         //Ретрофит
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://iocontrol.ru/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Log.d("arduino", "ретрофит сделан");
       // data = new Data();
        Api api = retrofit.create(Api.class);
        //запрос к серверу
         final Data[] data = new Data[1];
        Call<Data> call = api.readSensorValue();
        Callback<Data> callback =  new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (!response.isSuccessful()) {
                    Log.d("arduino", "ошибка сервера" + response.code());
                    return;
                }
                data[0] = response.body();


                Log.d("arduino", "внутри: "  + data[0].toString());
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Log.d("arduino", "ошибка запроса" + t.getMessage());
            }
        };
        call.enqueue(callback);
        Log.d("arduino", "данные");
          Log.d("arduino", data[0].toString());
        return data[0];
    }

}
