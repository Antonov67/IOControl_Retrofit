package com.example.iocontrol_retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    TextView textLeak;
    EditText angleValue;
    Switch switchButton;
    Button servoButton;
    public Data data;
    Button reloadButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textLeak = findViewById(R.id.textLeak);
        angleValue = findViewById(R.id.angleValue);
        switchButton = findViewById(R.id.switchButton);
        servoButton = findViewById(R.id.servoButton);
        reloadButton = findViewById(R.id.reloadButton);
        Log.d("arduino", "окно собрано");
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //создадим подключение к серверу

                //Ретрофит
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://iocontrol.ru/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                Log.d("arduino", "ретрофит сделан");
                Api api = retrofit.create(Api.class);
                //запрос к серверу
                Call<Data> call = api.readSensorValue();

                Callback<Data> callback = new Callback<Data>() {
                    @Override
                    public void onResponse(Call<Data> call, Response<Data> response) {
                        if (!response.isSuccessful()) {
                            Log.d("arduino", "ошибка сервера" + response.code());
                            return;
                        }
                        Data data = response.body();
                        textLeak.setText("значение датчика протечки: " + data.getValue());
                        Log.d("arduino", "внутри: " + data.toString());
                    }

                    @Override
                    public void onFailure(Call<Data> call, Throwable t) {
                        Log.d("arduino", "ошибка запроса" + t.getMessage());
                    }
                };
                call.enqueue(callback);
            }
        });

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    sendData("leak", "1");
                }
                else{
                    sendData("leak", "0");
                }


            }
        });


    }
    //отправка команды на сервер
    public void sendData(String var, String value) {
        //создадим подключение к серверу

        //Ретрофит
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://iocontrol.ru/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Log.d("arduino", "ретрофит отправки сделан");
        Api api = retrofit.create(Api.class);
        //запрос к серверу
        Call<Data> call = api.sendSensorValue(var,value);

        Callback<Data> callback = new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (!response.isSuccessful()) {
                    Log.d("arduino", "ошибка сервера" + response.code());
                    return;
                }
                Data data = response.body();
                Log.d("arduino", "внутри: " + data.toString());
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Log.d("arduino", "ошибка запроса" + t.getMessage());
            }
        };
        call.enqueue(callback);
    }


}