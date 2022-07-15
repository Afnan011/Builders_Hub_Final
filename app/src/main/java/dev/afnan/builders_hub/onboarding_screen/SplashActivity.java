package dev.afnan.builders_hub.onboarding_screen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import dev.afnan.builders_hub.R;

public class SplashActivity extends AppCompatActivity {

    ImageView logo;
    TextView tagLine, footer;
    Animation scale, fade;

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
        tagLine = findViewById(R.id.tagline);
        footer = findViewById(R.id.footer);

        scale = AnimationUtils.loadAnimation(this, R.anim.scale);
//        top = AnimationUtils.loadAnimation(this, R.anim.top);
        fade = AnimationUtils.loadAnimation(this, R.anim.fade);

        logo.setAnimation(scale);
        tagLine.setAnimation(fade);
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