package com.example.unutmazchatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    Button registerButton;
    EditText usernameInput;
    EditText emailInput;
    EditText passwordInput;
    EditText confirmPasswordInput;

    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton = findViewById(R.id.registerButton);
        usernameInput = findViewById(R.id.usernameInput);
        emailInput = findViewById(R.id.registerEmailInput);
        passwordInput = findViewById(R.id.registerPassInput);
        confirmPasswordInput = findViewById(R.id.registerConfirmPasswordInput);

        auth = FirebaseAuth.getInstance();

    }

    public void registerButtonClicked(View view){
        String username = usernameInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();

        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)){
            Toast.makeText(RegisterActivity.this, "Input field can not be empty.", Toast.LENGTH_SHORT).show();
        }
        else if (!password.equals(confirmPassword)){
            Toast.makeText(RegisterActivity.this, "Passwords must match.", Toast.LENGTH_SHORT).show();
        }
        else if (password.length() <= 6){
            Toast.makeText(RegisterActivity.this, "Password must be at least 7 characters long.", Toast.LENGTH_SHORT).show();
        }
        else{
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                FirebaseUser firebaseUser = auth.getCurrentUser();
                                assert firebaseUser != null;
                                String userId = firebaseUser.getUid();

                                reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("id", userId);
                                hashMap.put("username", username);

                                reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Intent intent = new Intent(RegisterActivity.this, ChatroomActivity.class);
                                            startActivity(intent);
                                        }
                                        else{
                                            Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, "An error occured.", Toast.LENGTH_SHORT).show();
                                Log.d("FirebaseAuth", "onComplete" + task.getException().getMessage());
                            }
                        }
                    });
        }
    }
}