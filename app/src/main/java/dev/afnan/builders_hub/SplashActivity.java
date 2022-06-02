package dev.afnan.builders_hub;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import onboarding_screen.*;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    ImageView logo;
    TextView tagline, footer;
    Animation scale, top, fade;

    private static int SPLASH_SCREEN_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_splash);

        logo = findViewById(R.id.logo);
        tagline = findViewById(R.id.tagline);
        footer = findViewById(R.id.footer);

        scale = AnimationUtils.loadAnimation(this, R.anim.scale);
        top = AnimationUtils.loadAnimation(this, R.anim.top);
        fade = AnimationUtils.loadAnimation(this, R.anim.fade);

        logo.setAnimation(scale);
        tagline.setAnimation(top);
        footer.setAnimation(fade);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, slideActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN_TIME
        );

    }
}