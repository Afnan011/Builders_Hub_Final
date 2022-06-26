package dev.afnan.builders_hub.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dev.afnan.builders_hub.R;

public class loginActivity extends AppCompatActivity {

    private TextView register, forgot, errorMsg;
    private EditText editEmail, editPassword;
    private Button login;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register = findViewById(R.id.registerUser);
        editEmail = findViewById(R.id.email);
        editPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.login_progressBar);
        login = findViewById(R.id.login);
        forgot = findViewById(R.id.forgetPassword);
        errorMsg = findViewById(R.id.errorMsg);

        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new  Intent(loginActivity.this, registerActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogIn();
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginActivity.this, forgotPasswordActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    private void userLogIn() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (email.isEmpty()){
            editEmail.setError("Email is required!");
            editEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editEmail.setError("Please enter a valid Email");
            editEmail.requestFocus();
            return;
        }

        if (password.isEmpty()){
            errorMsg.setVisibility(View.VISIBLE);
            errorMsg.setText("Password is required!");
            editPassword.setBackground(ContextCompat.getDrawable(this, R.drawable.edittext_error_bg));
            editPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            errorMsg.setVisibility(View.VISIBLE);
            errorMsg.setText("Password must be at least 6 character");
            editPassword.setBackground(ContextCompat.getDrawable(this, R.drawable.edittext_error_bg));
            editPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        editPassword.setBackground(ContextCompat.getDrawable(this, R.drawable.edittext_bg));
        errorMsg.setVisibility(View.GONE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //redirect to user profile
                            checkUserAccessLevel(mAuth.getUid());
                        }
                        else{
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(loginActivity.this, "Failed to login! Please check your credentials.", Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });


    }

    private void checkUserAccessLevel(String uid) {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
        user = mAuth.getCurrentUser();

        myRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child("isAdmin").exists()) {
                    Intent intent = new Intent(loginActivity.this, userProfileActivity.class);
                    startActivity(intent);
                    Toast.makeText(loginActivity.this, "ADMIN", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    finish();
                }

                else if (snapshot.child("isWorker").exists()){
                    Intent intent = new Intent(loginActivity.this, userProfileActivity.class);
                    startActivity(intent);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(loginActivity.this, "Worker", Toast.LENGTH_SHORT).show();
                    finish();
                }

                else if (snapshot.child("isUser").exists()){
                    Intent intent = new Intent(loginActivity.this, userProfileActivity.class);
                    startActivity(intent);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(loginActivity.this, "USER", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(loginActivity.this, "Something went wrong\n" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }


}