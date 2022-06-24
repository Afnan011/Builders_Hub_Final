package dev.afnan.builders_hub.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dev.afnan.builders_hub.R;

public class userProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference reference;
    private Button logout;
    private String userID = "";
    private TextView editName, editEmail, editPhone;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        logout = findViewById(R.id.btnLogout);
        editName = findViewById(R.id.txtName);
        editEmail = findViewById(R.id.txtEmail);
        editPhone = findViewById(R.id.txtPhone);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();


        user = mAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        if (user != null){
            userID = user.getUid();
        }

            reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {       //addListerForSingleValueEvent is used to read value once.
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User userProfile = snapshot.getValue(User.class);

                    if (userProfile != null){
                        String fullName = userProfile.name;
                        String email = userProfile.email;
                        String phone = userProfile.phone;

                        editName.setText(fullName);
                        editEmail.setText(email);
                        editPhone.setText(phone);
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(userProfileActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                mAuth.signOut();
                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(userProfileActivity.this, loginActivity.class));
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);
        user = mAuth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(userProfileActivity.this, loginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

}