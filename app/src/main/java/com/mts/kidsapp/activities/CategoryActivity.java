package com.mts.kidsapp.activities;

import static com.mts.kidsapp.PrefUtils.isConnectingToInternet;
import static com.mts.kidsapp.PrefUtils.player;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.mts.kidsapp.MusicService;
import com.mts.kidsapp.R;
import com.mts.kidsapp.adapter.CategoryAdapter;
import com.mts.kidsapp.model.CategoryModel;
import com.mts.kidsapp.retrofit.APIClient;
import com.mts.kidsapp.retrofit.GetResult;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;

public class CategoryActivity extends AppCompatActivity implements GetResult.MyListener{

    Activity activity;
    ImageView ivSetting, ivHome, ivBack;
    public static ArrayList<Object> categoryItemsList;
    RecyclerView rvData;
    CategoryAdapter categoryAdapter;
    GifImageView gifImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_category);
        
        activity = CategoryActivity.this;
        ivSetting = findViewById(R.id.ivSetting);
        ivBack = findViewById(R.id.ivBack);
        ivHome = findViewById(R.id.ivHome);
        rvData = findViewById(R.id.rvData);

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        ivSetting.setOnClickListener(view -> {
            final MediaPlayer mp = MediaPlayer.create(this, R.raw.button_click);
            mp.setOnCompletionListener(mp1 -> {
                Intent intent = new Intent(activity, SettingActivity.class);
                startActivity(intent);
                mp.release();
            });
            mp.start();
        });

        ivBack.setOnClickListener(view -> {
            final MediaPlayer mp = MediaPlayer.create(this, R.raw.button_click);
            mp.setOnCompletionListener(mp1 -> {
                if(player != null) {
                    MusicService.stopMusic2();
                }
                Intent intent = new Intent(activity, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                mp.release();
            });
            mp.start();
        });

        ivHome.setOnClickListener(view -> {
            final MediaPlayer mp = MediaPlayer.create(this, R.raw.button_click);
            mp.setOnCompletionListener(mp1 -> {
                if(player != null) {
                    MusicService.stopMusic2();
                }
                Intent intent = new Intent(activity, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                mp.release();
            });
            mp.start();
        });
        categoryItemsList = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(activity,
                        LinearLayoutManager.HORIZONTAL, false)
                {};

        gifImg = findViewById(R.id.gifImg);

        categoryAdapter = new CategoryAdapter(this, categoryItemsList);
        rvData.setLayoutManager(mLayoutManager);
        rvData.setAdapter(categoryAdapter);

        if (!isConnectingToInternet(activity)) {
            gifImg.setVisibility(View.GONE);
            Toast.makeText(activity, "Please Connect to Internet",
                    Toast.LENGTH_LONG).show();
        }
        else {
            gifImg.setVisibility(View.VISIBLE);
            requestMethod();
        }
    }

    private void requestMethod() {
        Call<JsonObject> call = APIClient.getInterface().
                categoriesApi();
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void callback(JsonObject result, String callNo) {
        gifImg.setVisibility(View.GONE);
        try {
            JsonArray jsonArray = result.getAsJsonArray("poems_categories");
            for(int i=0; i< jsonArray.size(); i++)
            {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                String category_id = String.valueOf(jsonObject.get("category_id"))
                        .replaceAll("^[\"']+|[\"']+$", "");
                String cat_title = String.valueOf(jsonObject.get("cat_title"))
                        .replaceAll("^[\"']+|[\"']+$", "");

                CategoryModel categoryModel = new CategoryModel(category_id, 
                        cat_title);
                categoryItemsList.add(categoryModel);
                if(i==jsonArray.size()-1)
                {
                    categoryAdapter.notifyDataSetChanged();
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
    }
}