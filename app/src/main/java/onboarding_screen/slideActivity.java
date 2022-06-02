package onboarding_screen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import dev.afnan.builders_hub.MainActivity;
import dev.afnan.builders_hub.R;


public class slideActivity extends AppCompatActivity {

    public static ViewPager viewPager;
    slideViewPagerAdapter adapter;
    Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);

        viewPager = findViewById(R.id.viewPager);
        adapter = new slideViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer( true, new PageTransformer());

        anim = AnimationUtils.loadAnimation(this, R.anim.onboard_page_transition_animation);
        viewPager.startAnimation(anim);

        if (isOpenAlready()){
            Intent intent = new Intent(slideActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else{
            SharedPreferences.Editor editor = getSharedPreferences("slide", MODE_PRIVATE).edit();
            editor.putBoolean("slide", true);
            editor.commit();
        }

    }

    private boolean isOpenAlready() {
//        SharedPreferences sharedPreferences = getSharedPreferences("slide",MODE_PRIVATE);
//        boolean result = sharedPreferences.getBoolean("slide", false);
//        return result;
        return false;

    }
}