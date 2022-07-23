package dev.afnan.builders_hub.UserModule;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import dev.afnan.builders_hub.Common.Common;
import dev.afnan.builders_hub.Models.User;
import dev.afnan.builders_hub.R;
import dev.afnan.builders_hub.utility.NetworkChangeListener;

import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.google.android.material.navigation.Navigation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class UserActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment;
    private BroadcastReceiver broadcastReceiver;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID = "";
    private String name, phone, email;
    private User userData;
    private FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        broadcastReceiver = new NetworkChangeListener();
        homeFragment = new HomeFragment();
        storage = FirebaseStorage.getInstance();

        bottomNavigationView = findViewById(R.id.bottom_nav);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, homeFragment).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.home:
                        selectedFragment = new HomeFragment();
                        break;

                    case R.id.bookings:
                        selectedFragment = new BookingFragment();
                        break;

                    case R.id.profile:
                        selectedFragment = new ProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment).commit();
                return true;
            }
        });

        saveData();

    }


    private void saveData() {

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        if (user != null) {
            userID = user.getUid();
        }

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userData = snapshot.getValue(User.class);

                if (userData != null) {
                    name = userData.name;
                    phone = userData.phone;
                    email = userData.email;
                    Common.CurrentUser = userData;
                    Common.CurrentUser.setUserID(userID);

                    SharedPreferences.Editor editor = getSharedPreferences("userInfo", MODE_PRIVATE).edit();
                    editor.putString("name", name);
                    editor.putString("phone", phone);
                    editor.putString("email", email);
                    editor.apply();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });


    }


//    @Override
//    protected void onStart() {
//        try {
//            IntentFilter filter = new IntentFilter();
//            registerReceiver(broadcastReceiver, filter);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        super.onStart();
//    }
//
//
//    @Override
//    protected void onStop() {
//        try {
//            unregisterReceiver(broadcastReceiver);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        super.onStop();
//    }


}


