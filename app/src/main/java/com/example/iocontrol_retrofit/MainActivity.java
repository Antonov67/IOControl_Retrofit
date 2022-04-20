package com.example.iocontrol_retrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.graphics.Color;
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

public class MainActivity extends AppCompatActivity{
    TextView textLeak;
    EditText angleValue;
    Switch switchButton;
    Button servoButton;
    public Data data;
    Button reloadButton;
    ConstraintLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        textLeak = findViewById(R.id.textLeak);
        angleValue = findViewById(R.id.angleValue);
        layout = findViewById(R.id.rootLayout);
        switchButton = findViewById(R.id.switchButton);
        servoButton = findViewById(R.id.servoButton);
        reloadButton = findViewById(R.id.reloadButton);
        Log.d("arduino", "окно собрано");

        Monitor monitor = new Monitor();
        monitor.start();

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

        servoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int angle = Integer.parseInt(angleValue.getText().toString());
                    if (angle >= 0 && angle <=180) {
                        sendData("angle", angleValue.getText().toString());
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Угол введен неправильно!", Toast.LENGTH_LONG).show();
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

    class Monitor extends Thread{
        @Override
        public void run() {
            while (true){
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
                        textLeak.setText("датчик протечки: " + data.getValue());
                        if (Integer.parseInt(data.getValue()) < 600) {
                            textLeak.setText(textLeak.getText() + " !!Протечка!!");
                            textLeak.setTextColor(Color.RED);
                        }
                        else {
                            textLeak.setTextColor(Color.GREEN);
                        }
                        Log.d("arduino", "внутри: " + data.toString());
                    }

                    @Override
                    public void onFailure(Call<Data> call, Throwable t) {
                        Log.d("arduino", "ошибка запроса" + t.getMessage());
                    }
                };
                call.enqueue(callback);

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}