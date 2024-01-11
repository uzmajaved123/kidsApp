package com.mts.kidsapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mts.kidsapp.MusicService;
import com.mts.kidsapp.PrefUtils;
import com.mts.kidsapp.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import static com.mts.kidsapp.PrefUtils.player;
import static com.mts.kidsapp.PrefUtils.soundsEnable;

public class SettingActivity extends AppCompatActivity {

    Activity activity;
    ImageView ivHome, ivMenu, ivSetting;
    TextView tvStore, tvLoveUs, tvOurApps, tvFaqs;
    Typeface typeface;
    private SharedPreferences prefsMusic;
    private SharedPreferences.Editor editorMusic;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_setting);
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        initViews();
        ivHomeClickListener();
        ivMenuClickListener();
        tvStoreClickListener();
        tvFaqsClickListener();
        tvLoveUsClickListener();
        tvOurAppsClickListener();
        ivSettingClickListener();
    }

    private void ivSettingClickListener() {
        ivSetting.setOnClickListener(view -> {
            final MediaPlayer mp = MediaPlayer.create(this, R.raw.button_click);
            mp.setOnCompletionListener(mp1 -> mp.release());
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

    @SuppressLint("SetTextI18n")
    private void initViews() {
        activity = SettingActivity.this;
        typeface = Typeface.createFromAsset(getAssets(), "fonts/appfont.ttf");
        ivHome = findViewById(R.id.ivHome);
        ivSetting = findViewById(R.id.ivSetting);
        tvStore = findViewById(R.id.tvStore);
        tvLoveUs = findViewById(R.id.tvLoveUs);
        tvOurApps = findViewById(R.id.tvOurApps);
        tvFaqs = findViewById(R.id.tvFaqs);
        ivMenu = findViewById(R.id.ivMenu);

        tvStore.setTypeface(typeface);
        tvLoveUs.setTypeface(typeface);
        tvOurApps.setTypeface(typeface);
        tvFaqs.setTypeface(typeface);

        prefsMusic = getApplicationContext().getSharedPreferences(PrefUtils.musicPrefs,
                MODE_PRIVATE);
        editorMusic = prefsMusic.edit();
        if(prefsMusic.getBoolean("sound", true))
        {
            tvStore.setText("Sound Enabled");
        }
        else {
            tvStore.setText("Sound Disabled");
        }
    }

    private void tvOurAppsClickListener() {
        tvOurApps.setOnClickListener(view -> {
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.button_click);
        mp.setOnCompletionListener(mp1 -> {
                    Uri uri = Uri.parse("https://play.google.com/store/apps/developer?id=Tyche.app");
                    Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    try {
                        startActivity(myAppLinkToMarket);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(activity, "Play store not found on this device",
                                Toast.LENGTH_LONG).show();
                }
            mp.release();
            });
            mp.start();
        });
    }

    private void tvLoveUsClickListener() {
        tvLoveUs.setOnClickListener(view -> {
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.button_click);
        mp.setOnCompletionListener(mp1 -> {
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName());
                    Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    try {
                        startActivity(myAppLinkToMarket);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(activity, "Play store not found on this device", Toast.LENGTH_LONG).show();
                }
                    mp.release();
            });
            mp.start();
        });
    }

    private void tvFaqsClickListener() {
        tvFaqs.setOnClickListener(view -> {
            final MediaPlayer mp = MediaPlayer.create(this, R.raw.button_click);
            mp.setOnCompletionListener(mp1 -> {
                Intent intent = new Intent(activity, FAQActivity.class);
                startActivity(intent);
                mp.release();
            });
            mp.start();
        });
    }

    @SuppressLint("SetTextI18n")
    private void tvStoreClickListener() {
        tvStore.setOnClickListener(view -> {
            final MediaPlayer mp = MediaPlayer.create(this, R.raw.button_click);
            mp.setOnCompletionListener(mp1 -> {
                if(prefsMusic.getBoolean("sound", true))
                {
                    editorMusic.putBoolean("sound", false);
                    editorMusic.commit();
                    tvStore.setText("Sound Disabled");
                    soundsEnable=false;
                    if(isMyServiceRunning(MusicService.class))
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
                    tvStore.setText("Sound Enabled");
                    Intent music = new Intent();
                    music.setClass(getApplicationContext(),MusicService.class);
                    startService(music);
                }
            });
            mp.start();
        });
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
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