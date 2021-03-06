package android.example.speelenlees.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.example.speelenlees.activities.ui.homepages.HomeActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import android.example.speelenlees.R;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LogInActivity.class);
                startActivity(intent);
                finish();

            }
        }, SPLASH_TIME_OUT);
    }
}
