package com.mts.kidsapp;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class PrefUtils {

    public static final String musicPrefs = "musicPrefs";
    public static MediaPlayer player;
    public static int length = 0;
    public static final String CategoryProductAPI = "https://ocanalytica.com/apps/nursery-kids-top-learning-rhymes-and-poems-videos/specific-categories-poems.php?poem_category=";
    public static boolean soundsEnable=true;

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }


}
