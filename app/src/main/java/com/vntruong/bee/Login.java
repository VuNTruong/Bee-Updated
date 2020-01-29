package com.vntruong.bee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    FirebaseFirestore database;
    private EditText userNameField;
    private EditText passwordField;
    public static FirebaseUser user;
    public static FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        database = FirebaseFirestore.getInstance();

        passwordField = findViewById(R.id.password);
        userNameField = findViewById(R.id.userName);

        mAuth = FirebaseAuth.getInstance();
    }

    private void signIn(String email, String password) {
        if (!validateForm()) {
            return;
        }

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                String userEmail = mAuth.getCurrentUser().getEmail();
                                database.collection("user").document(userEmail)
                                .get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                String role = documentSnapshot.getString("role");

                                                if (role.equals("student")) {
                                                    // Sign in success, update UI with the signed-in user's information
                                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });

                                Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT);
                                user = mAuth.getCurrentUser();
                                Login.this.finish();
                            } else {
                                // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    public void login (View view) {
        signIn(userNameField.getText().toString(), passwordField.getText().toString());
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = userNameField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            userNameField.setError("Required.");
            valid = false;
        } else {
            userNameField.setError(null);
        }

        String password = passwordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Required.");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        return valid;
    }

    public void cancel (View view) {
        Intent intent = new Intent(getApplicationContext(), Welcome.class);
        startActivity(intent);
    }
}
