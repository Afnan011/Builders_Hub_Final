package dev.afnan.builders_hub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.UiAutomation;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import dev.afnan.builders_hub.Common.Common;
import dev.afnan.builders_hub.Models.User;
import dev.afnan.builders_hub.UserModule.UserActivity;
import dev.afnan.builders_hub.auth.loginActivity;

public class AccessLevelDecider extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String userID;

    private FirebaseDatabase database;
    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_level_decider);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
        user = mAuth.getCurrentUser();
        userID = user.getUid();

        if (userID != null){
            checkUserAccessLevel(userID);
        }
        else{
            Toast.makeText(this, "USer is not found!", Toast.LENGTH_SHORT).show();
        }

    }


    private void checkUserAccessLevel(String uid) {


        myRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child("isAdmin").exists()) {
                    Intent intent = new Intent(AccessLevelDecider.this, UserActivity.class);
                    startActivity(intent);
                    Toast.makeText(AccessLevelDecider.this, "ADMIN", Toast.LENGTH_SHORT).show();
                    finish();
                }

                else if (snapshot.child("isWorker").exists()){
                    Intent intent = new Intent(AccessLevelDecider.this, UserActivity.class);
                    startActivity(intent);
                    Toast.makeText(AccessLevelDecider.this, "Worker", Toast.LENGTH_SHORT).show();
                    finish();
                }

                else if (snapshot.child("isUser").exists()){
                    Intent intent = new Intent(AccessLevelDecider.this, UserActivity.class);
                    startActivity(intent);
                    Toast.makeText(AccessLevelDecider.this, "USER", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AccessLevelDecider.this, "Something went wrong\n" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

}