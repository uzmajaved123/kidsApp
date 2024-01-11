package com.mts.kidsapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.mts.kidsapp.R;
import com.mts.kidsapp.model.DataModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class PlayVideoActivity extends AppCompatActivity {

    Activity activity;
    double current_pos, total_duration;
    ImageView ivControl;
    SeekBar seekBar;
    VideoView videoView;
    ImageView ivSetting, ivHome, ivBack, ivPre, ivNext, ivMenu;
    int start = 1, paused = 0, videoPos = 0;
    ArrayList<Object> itemList = new ArrayList<>();
    GifImageView gifImg;

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_play_video);
        activity = PlayVideoActivity.this;
        videoView = findViewById(R.id.videoview);
        ivSetting = findViewById(R.id.ivSetting);
        ivBack = findViewById(R.id.ivBack);
        ivPre = findViewById(R.id.ivPre);
        ivNext = findViewById(R.id.ivNext);
        ivHome = findViewById(R.id.ivHome);
        ivMenu = findViewById(R.id.ivMenu);
        gifImg = findViewById(R.id.gifImg);

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Intent intent = getIntent();

        String data = intent.getStringExtra("data");
        videoPos = intent.getIntExtra("pos", videoPos);
        String path = "";
        itemList.addAll(MainActivity.itemList);
        for(int i=0;i<MainActivity.itemList.size(); i++)
        {
            DataModel dataModel = (DataModel) itemList.get(i);
            if(data.equals(dataModel.getPoem_id()))
            {
                path = dataModel.getpoem_video();
            }
        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int h = displaymetrics.heightPixels;
        int w = displaymetrics.widthPixels;
        videoView.setLayoutParams(new RelativeLayout.LayoutParams(w,h));
        videoView.setVideoURI(Uri.parse(path));
        gifImg.setVisibility(View.VISIBLE);

        ivSetting.setOnClickListener(view -> {
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

        ivPre.setOnClickListener(view -> {
            if(videoPos == 0)
            {
                final MediaPlayer mp = MediaPlayer.create(this, R.raw.button_click);
                mp.start();

                Toast.makeText(activity, "No previous video found",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                gifImg.setVisibility(View.VISIBLE);
                videoView.stopPlayback();
                videoView.resume();
                final MediaPlayer mp = MediaPlayer.create(this, R.raw.button_click);
                mp.start();
                videoView.setVisibility(View.INVISIBLE);
                videoPos = videoPos - 1;
                DataModel dataModel = (DataModel) itemList.get(videoPos);
                String path1 = dataModel.getpoem_video();
                videoView.setVideoURI(Uri.parse(path1));
                videoView.start();
                videoView.setVisibility(View.VISIBLE);
            }
        });
        ivNext.setOnClickListener(view -> {
            if(videoPos == itemList.size()-1)
            {
                final MediaPlayer mp = MediaPlayer.create(this, R.raw.button_click);
                mp.start();

                Toast.makeText(activity, "No further video found",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                gifImg.setVisibility(View.VISIBLE);
                videoView.stopPlayback();
                final MediaPlayer mp = MediaPlayer.create(this, R.raw.button_click);
                mp.start();

                videoView.resume();
                videoView.setVisibility(View.INVISIBLE);
                videoPos = videoPos + 1;
                DataModel dataModel = (DataModel) itemList.get(videoPos);
                String path1 = dataModel.getpoem_video();
                videoView.setVideoURI(Uri.parse(path1));
                videoView.start();
                videoView.setVisibility(View.VISIBLE);
            }
        });
        ivBack.setOnClickListener(view -> {
            final MediaPlayer mp = MediaPlayer.create(this, R.raw.button_click);
            mp.setOnCompletionListener(mp1 -> {
                mp.release();
                finish();
            });
            mp.start();
        });
        ivHome.setOnClickListener(view -> {
            final MediaPlayer mp = MediaPlayer.create(this, R.raw.button_click);
            mp.setOnCompletionListener(mp1 -> {
                mp.release();
                finish();
            });
            mp.start();
        });

        videoView.setOnPreparedListener(mp -> {
            start = 1;
            videoView.start();
            setVideoProgress();
            gifImg.setVisibility(View.GONE);
        });

        seekBar = findViewById(R.id.seekbar);
        ivControl = findViewById(R.id.pause);
        ivControl.setOnClickListener(view -> {
            if(start == 1)
            {
                videoView.pause();
                start = 0;
                ivControl.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
            }
            else {
                videoView.start();
                start = 1;
                ivControl.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
            }
        });
    }

    public void setVideoProgress() {
        current_pos = videoView.getCurrentPosition();
        total_duration = videoView.getDuration();

        seekBar.setMax((int) total_duration);
        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if(paused == 0) {
                        current_pos = videoView.getCurrentPosition();
                        seekBar.setProgress((int) current_pos);
                        handler.postDelayed(this, 1000);
                    }
                } catch (IllegalStateException ed){
                    ed.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1000);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                current_pos = seekBar.getProgress();
                videoView.seekTo((int) current_pos);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        paused = 0;
        videoView.seekTo((int) current_pos);
        setVideoProgress();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onPause() {
        super.onPause();
        ivControl.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
        paused = 1;
        current_pos = videoView.getCurrentPosition();
    }

}