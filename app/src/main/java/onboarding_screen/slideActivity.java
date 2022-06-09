package onboarding_screen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dev.afnan.builders_hub.R;
import dev.afnan.builders_hub.auth.loginActivity;
import dev.afnan.builders_hub.auth.userProfileActivity;


public class slideActivity extends AppCompatActivity {

    public static ViewPager viewPager;
    slideViewPagerAdapter adapter;
    Animation anim;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);

        viewPager = findViewById(R.id.viewPager);
        adapter = new slideViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer( true, new PageTransformer());

        mAuth = FirebaseAuth.getInstance();

        anim = AnimationUtils.loadAnimation(this, R.anim.onboard_page_transition_animation);
        viewPager.startAnimation(anim);

        if (isOpenAlready()){
            Intent intent = new Intent(slideActivity.this, userProfileActivity.class);
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
        SharedPreferences sharedPreferences = getSharedPreferences("slide",MODE_PRIVATE);
        boolean result = sharedPreferences.getBoolean("slide", false);

        if (result){
            if (isLoggedIn()){
                return true;
            }
        }

        return false;

    }

    private boolean isLoggedIn(){
        user = mAuth.getCurrentUser();
        if (user != null){
            return true;
        }
        else{
            return false;
        }
    }

}