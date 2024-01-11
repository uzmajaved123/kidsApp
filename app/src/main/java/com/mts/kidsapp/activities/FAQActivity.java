package com.mts.kidsapp.activities;

import static com.mts.kidsapp.PrefUtils.player;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.mts.kidsapp.MusicService;
import com.mts.kidsapp.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class FAQActivity extends AppCompatActivity {

    Activity activity;
    TextView tvFaqs;
    ImageView ivHome, ivMenu, ivSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_faqactivity);

        activity = FAQActivity.this;
        tvFaqs= findViewById(R.id.tvFaq);
        ivHome = findViewById(R.id.ivHome);
        ivMenu = findViewById(R.id.ivMenu);
        ivSetting = findViewById(R.id.ivSetting);

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/appfont.ttf");
        tvFaqs.setTypeface(typeface);
        ivHomeClickListener();
        ivMenuClickListener();
        ivSettingClickListener();
    }

    private void ivSettingClickListener() {
        ivSetting.setOnClickListener(view -> {
            final MediaPlayer mp = MediaPlayer.create(this, R.raw.button_click);
            mp.setOnCompletionListener(mp1 -> {
                mp.release();
                finish();
            });
            mp.start();
        });
    }

    private void ivMenuClickListener() {
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
    }

    private void ivHomeClickListener() {
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
            });
            mp.start();
        });
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