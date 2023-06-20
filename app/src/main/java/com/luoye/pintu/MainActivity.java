package com.luoye.pintu;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.FeatureInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.fragment.app.FragmentActivity;

public class MainActivity extends FragmentActivity {

    private static int screenWidth;
    private static int screenHeight;

    final String[] Nams = {"back_01.jpg", "back_02.jpg", "back_03.jpg", "back_04.jpg", "back_05.jpg", "back_06.jpg", "back_07.jpg", "back_08.jpg", "back_09.jpg", "back_10.jpg", "back_11.jpg", "back_12.jpg", "back_13.jpg", "back_14.jpg", "back_15.jpg", "back_16.jpg"};

    public String next_file(int inc) {
        SharedPreferences sh = getSharedPreferences("config", 0);
        int index = sh.getInt("index", 0);
        index = (index + inc) % Nams.length;
        sh.edit().putInt("index", index).apply();
        return Nams[index];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        getSupportFragmentManager().beginTransaction().add(R.id.main_view, StartFragment.newInstance(null, null)).commit();
//        setContentView(new MainView(this));


    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() <= 0) {
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
}
