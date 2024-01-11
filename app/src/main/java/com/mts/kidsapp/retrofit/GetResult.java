package com.mts.kidsapp.retrofit;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;


import com.mts.kidsapp.AppController;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetResult {

    public static MyListener myListener;

    public void callForLogin(Call<JsonObject> call, String callno) {
        if(!Utiles.internetChack()){
            Toast.makeText(AppController.mInstance,
                    "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }else {
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(@NonNull Call<JsonObject> call, @NonNull
                        Response<JsonObject> response) {
                    myListener.callback(response.body(), callno);
                    Log.d("Errrrrr", String.valueOf(response.body()));
                }

                @Override
                public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                    myListener.callback(null, callno);
                    call.cancel();
                }
            });
        }

    }

    public interface MyListener {
        void callback(JsonObject result, String callNo);
    }

    public void setMyListener(MyListener Listener) {
        myListener = Listener;
    }
}
