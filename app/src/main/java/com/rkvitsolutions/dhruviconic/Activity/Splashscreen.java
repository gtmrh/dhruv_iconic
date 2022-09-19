package com.rkvitsolutions.dhruviconic.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.rkvitsolutions.dhruviconic.R;

public class Splashscreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1000;
    RelativeLayout layout;
    private ImageView imgLogo;
    private String Status, userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        getWindow().setFlags(WindowManager
                .LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        layout = findViewById(R.id.splash);

        SharedPreferences mypref = getSharedPreferences("UserData", MODE_PRIVATE);
        Status = mypref.getString("Status", null);
        userType = mypref.getString("userType", null);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Status != null) {

                        startActivity(new Intent(Splashscreen.this, MainHome.class));

                } else
                    startActivity(new Intent(Splashscreen.this, Login.class));

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
