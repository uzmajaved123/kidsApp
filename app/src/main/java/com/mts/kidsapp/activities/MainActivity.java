package com.mts.kidsapp.activities;

import static com.mts.kidsapp.PrefUtils.isConnectingToInternet;
import static com.mts.kidsapp.PrefUtils.soundsEnable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.mts.kidsapp.MusicService;
import com.mts.kidsapp.PrefUtils;
import com.mts.kidsapp.R;
import com.mts.kidsapp.adapter.DataAdapter;
import com.mts.kidsapp.model.DataModel;
import com.mts.kidsapp.retrofit.APIClient;
import com.mts.kidsapp.retrofit.GetResult;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity implements GetResult.MyListener{


    Activity activity;
    ImageView ivSetting, ivMenu, ivSound;
    public static ArrayList<Object> itemList;
    RecyclerView rvData;
    DataAdapter dataAdapter;
    GifImageView gifImg;
    private SharedPreferences prefsMusic;
    private SharedPreferences.Editor editorMusic;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        activity = MainActivity.this;
        ivSetting = findViewById(R.id.ivSetting);
        ivMenu = findViewById(R.id.ivMenu);
        rvData = findViewById(R.id.rvData);
        ivSound = findViewById(R.id.ivSound);

        prefsMusic = getApplicationContext().getSharedPreferences(PrefUtils.musicPrefs,
                MODE_PRIVATE);
        editorMusic = prefsMusic.edit();

        MobileAds.initialize(this, initializationStatus -> {

        });

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        ivSound.setOnClickListener(view -> {
            if(prefsMusic.getBoolean("sound", true))
            {
                editorMusic.putBoolean("sound", false);
                editorMusic.commit();
                ivSound.setImageDrawable(getResources().getDrawable(R.drawable.music_disabled_icon));
                soundsEnable=false;
                if(isMyServiceRunning())
                {
                    MusicService.stopMusic2();
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), MusicService.class);
                    stopService(intent);
                }

            }
            else {
                editorMusic.putBoolean("sound", true);
                editorMusic.commit();
                soundsEnable=true;
                ivSound.setImageDrawable(getResources().getDrawable(R.drawable.music_enabled_icon));
                Intent music = new Intent();
                music.setClass(getApplicationContext(),MusicService.class);
                startService(music);
            }
        });

        ivSetting.setOnClickListener(view -> {
            MusicService.pauseMusic();
            final MediaPlayer mp = MediaPlayer.create(this, R.raw.button_click);
            mp.setOnCompletionListener(mp1 -> {
                Intent intent1 = new Intent(activity,
                        SettingActivity.class);
                startActivity(intent1);
                mp.release();
            });
            mp.start();
        });

        ivMenu.setOnClickListener(view -> {
            final MediaPlayer mp = MediaPlayer.create(this, R.raw.button_click);
            mp.setOnCompletionListener(mp1 -> {
                Intent intent = new Intent(activity, 
                        CategoryActivity.class);
                startActivity(intent);
                mp.release();
            });
            mp.start();
        });

        Intent music = new Intent();
        music.setClass(this,MusicService.class);
        startService(music);

        itemList = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(activity,
                        LinearLayoutManager.HORIZONTAL, false)
                {};

        gifImg = findViewById(R.id.gifImg);


        dataAdapter = new DataAdapter(this, itemList);
        rvData.setLayoutManager(mLayoutManager);
        rvData.setAdapter(dataAdapter);

        if (!isConnectingToInternet(activity)) {
            gifImg.setVisibility(View.GONE);
            Toast.makeText(activity, "Please Connect to Internet",
                    Toast.LENGTH_LONG).show();
        }
        else {
            gifImg.setVisibility(View.VISIBLE);
            Handler handler = new Handler();
            handler.postDelayed(this::requestMethod,2000);

        }
    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (MusicService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    private void requestMethod() {
        Call<JsonObject> call = APIClient.getInterface().
                callApi();
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void callback(JsonObject result, String callNo) {
        gifImg.setVisibility(View.GONE);
        try {
            JsonArray jsonArray = result.getAsJsonArray("poems");
            for(int i=0; i< jsonArray.size(); i++)
            {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                String poem_id = String.valueOf(jsonObject.get("poem_id"))
                        .replaceAll("^[\"']+|[\"']+$", "");
                String poem_title = String.valueOf(jsonObject.get("poem_title"))
                        .replaceAll("^[\"']+|[\"']+$", "");
                String poem_video = String.valueOf(jsonObject.get("poem_video"))
                        .replaceAll("^[\"']+|[\"']+$", "");
                String poem_icon = String.valueOf(jsonObject.get("poem_icon"))
                        .replaceAll("^[\"']+|[\"']+$", "");
                String poem_category = String.valueOf(jsonObject.get("poem_category"))
                        .replaceAll("^[\"']+|[\"']+$", "");
                DataModel dataModel = new DataModel(poem_id, poem_title,
                        poem_video, poem_icon, poem_category);
                itemList.add(dataModel);
                if(i==jsonArray.size()-1)
                {
                    dataAdapter.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            Toast.makeText(activity, e.toString()
                    , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MusicService.pauseMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MusicService.resumeMusic();
        if(prefsMusic.getBoolean("sound", true))
        {
            ivSound.setImageDrawable(getResources().getDrawable(R.drawable.music_enabled_icon));
        }
        else {
            ivSound.setImageDrawable(getResources().getDrawable(R.drawable.music_disabled_icon));
        }

    }
}