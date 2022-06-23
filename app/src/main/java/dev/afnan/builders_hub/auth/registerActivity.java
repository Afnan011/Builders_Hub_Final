package dev.afnan.builders_hub.auth;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import dev.afnan.builders_hub.R;

public class registerActivity extends AppCompatActivity {

    private TextView btnLogin, errorMsg;
    private EditText editName, editEmail, editPhone, editPassword1, editPassword2;
    private Button btnRegisterUser;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        btnLogin = findViewById(R.id.loginHere);
        btnRegisterUser = findViewById(R.id.createAccount);

        editName = findViewById(R.id.name);
        editEmail = findViewById(R.id.email);
        editPhone = findViewById(R.id.phone);
        editPassword1 = findViewById(R.id.password1);
        editPassword2 = findViewById(R.id.password2);
        progressBar = findViewById(R.id.register_progressBar);
        errorMsg = findViewById(R.id.errorMsg_register);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registerActivity.this, loginActivity.class);
                startActivity(intent);
            }
        });

        btnRegisterUser.setOnClickListener(v -> registerUser());

    }


    private void registerUser() {
        String name = editName.getText().toString().trim();
        String email = editEmail.getText().toString();
        String phone = editPhone.getText().toString();
        String password1 = editPassword1.getText().toString().trim();
        String password2 = editPassword2.getText().toString().trim();

        if (name.isEmpty()){
            editName.setError("Full name is required!");
            editName.requestFocus();
            return;
        }

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

        if (phone.isEmpty()){
            editPhone.setError("Phone number is required!");
            editPhone.requestFocus();
            return;
        }
        if (phone.length() != 10){
            editPhone.setError("please enter a valid phone number");
            editPhone.requestFocus();
            return;
        }

        if (password1.isEmpty()){
            editPassword1.setError("Password is required!");
            editPassword1.requestFocus();
            return;
        }
        if(password1.length() < 6){
            editPassword1.setError("Password must be at least 6 character");
            editPassword1.requestFocus();
            return;
        }
        if (password2.isEmpty()){
            errorMsg.setVisibility(View.VISIBLE);
            errorMsg.setText("Confirm your password");
            editPassword2.setBackground(ContextCompat.getDrawable(this, R.drawable.edittext_error_bg));
            editPassword2.requestFocus();
            return;
        }
        if (!password1.equals(password2)){
            errorMsg.setText("Password does not match!");
            editPassword2.setBackground(ContextCompat.getDrawable(this, R.drawable.edittext_error_bg));
            editPassword2.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        editPassword2.setBackground(ContextCompat.getDrawable(this, R.drawable.edittext_bg));
        errorMsg.setVisibility(View.GONE);
        String password = password1;

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            User user = new User(name, email, phone);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Toast.makeText(registerActivity.this, "User has registered successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(registerActivity.this, loginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(registerActivity.this, "Failed to register! try again.", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(registerActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    progressBar.setVisibility(View.GONE);

                                }
                            });

                        }else{
                            Toast.makeText(registerActivity.this, "Failed to register! try again.", Toast.LENGTH_SHORT).show();
                            Toast.makeText(registerActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });


    }
}