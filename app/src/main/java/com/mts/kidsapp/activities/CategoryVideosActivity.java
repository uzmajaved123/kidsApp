package com.mts.kidsapp.activities;

import static com.mts.kidsapp.PrefUtils.isConnectingToInternet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mts.kidsapp.MusicService;
import com.mts.kidsapp.PrefUtils;
import com.mts.kidsapp.R;
import com.mts.kidsapp.adapter.DataAdapter2;
import com.mts.kidsapp.model.DataModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CategoryVideosActivity extends AppCompatActivity {

    String id = "1";
    Activity activity;
    ImageView ivSetting, ivMenu;
    public static ArrayList<Object> itemList3;
    RecyclerView rvData;
    DataAdapter2 dataAdapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_category_videos);
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        activity = CategoryVideosActivity.this;
        ivSetting = findViewById(R.id.ivSetting);
        ivMenu = findViewById(R.id.ivMenu);
        rvData = findViewById(R.id.rvData);

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
                Intent intent1 = new Intent(activity,
                        CategoryActivity.class);
                startActivity(intent1);
                mp.release();
            });
            mp.start();
        });

        Intent music = new Intent();
        music.setClass(this,MusicService.class);
        startService(music);

        itemList3 = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(activity,
                        LinearLayoutManager.HORIZONTAL, false)
                {};

        progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Getting New Data");
        progressDialog.setMessage("Getting Latest Data please Wait");


        dataAdapter = new DataAdapter2(this, itemList3);
        rvData.setLayoutManager(mLayoutManager);
        rvData.setAdapter(dataAdapter);

        if (!isConnectingToInternet(activity)) {
            Toast.makeText(activity, "Please Connect to Internet",
                    Toast.LENGTH_LONG).show();
        }
        else {
            progressDialog.show();
            requestMethod();
        }
    }

    private void requestMethod() {
        Thread thread = new Thread(() -> {
            StringRequest stringRequest;
            stringRequest = new StringRequest(Request.Method.GET,
                    PrefUtils.CategoryProductAPI + id,
                    response -> {
                        progressDialog.cancel();
                        try {
                            Log.d("repo", "" + response);
                            JSONObject obj;
                            try {
                                obj = new JSONObject(response);

                                JSONArray jsonArray = obj.getJSONArray("specific_categories_poems");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String poem_id = String.valueOf(jsonObject.get("poem_id"));
                                    String poem_title = String.valueOf(jsonObject.get("poem_title"));
                                    String poem_video = String.valueOf(jsonObject.get("poem_video"));
                                    String poem_icon = String.valueOf(jsonObject.get("poem_icon"));
                                    String poem_category = String.valueOf(jsonObject.get("poem_category"));
                                    DataModel dataModel = new DataModel(poem_id, poem_title,
                                            poem_video, poem_icon, poem_category);
                                    itemList3.add(dataModel);
                                    if (i == jsonArray.length() - 1) {
                                        dataAdapter.notifyDataSetChanged();
                                    }
                                }
                            } catch (Exception e) {
                            Toast.makeText(activity, e.toString()
                                    , Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e)
                        {
                            Toast.makeText(activity, e.toString()
                                    , Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        Toast.makeText(activity, "Check Internet",
                                Toast.LENGTH_SHORT).show();
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    return new HashMap<>();
                }

                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };

            stringRequest.setShouldCache(false);
            RequestQueue requestQueue = Volley.newRequestQueue(activity);
            int x=1;
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48,                        x, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));                requestQueue.add(stringRequest);
            Log.d("stringRequest", stringRequest.toString());
        });

        thread.start();

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