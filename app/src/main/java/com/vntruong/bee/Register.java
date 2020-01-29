package com.vntruong.bee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    FirebaseFirestore database;

    private EditText firstNameField;
    private EditText middleNameField;
    private EditText lastNameField;
    private EditText passwordField;
    private EditText userNameField;
    private DocumentReference documentReference;
    private boolean result;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        database = FirebaseFirestore.getInstance();

        firstNameField = findViewById(R.id.firstNameField);
        middleNameField = findViewById(R.id.middleNameField);
        lastNameField = findViewById(R.id.lastNameField);
        passwordField = findViewById(R.id.passwordField);
        userNameField = findViewById(R.id.usernameField);

        mAuth = FirebaseAuth.getInstance();
    }

    public void done (View view) {
        CollectionReference users = database.collection("user");

        Map<String, Object> data = new HashMap<>();

        if (validateUsername(userNameField.getText().toString())) {
            new AlertDialog.Builder(Register.this)
                    .setTitle("Username already exist")
                    .setMessage("The username has already been used")
                    .setPositiveButton("OK", null);
            return;
        }

        // Put the information into the map
        data.put("firstname", firstNameField.getText().toString());
        data.put("middlename", middleNameField.getText().toString());
        data.put("lastname", lastNameField.getText().toString());
        data.put("password", passwordField.getText().toString());
        data.put("username", userNameField.getText().toString());
        data.put("role", "student");
        data.put("attempts", new HashMap<>());

        users.document(userNameField.getText().toString()).set(data);

        createAccount(userNameField.getText().toString(), passwordField.getText().toString());

        // Add a spot for the user in the userattempt collection so that the information for the test can be saved over there

        System.out.println(userNameField.getText().toString());

        Map <String, Object> sampleMap = new HashMap<>();
        HashMap <String, Object> nameMap = new HashMap<>();

        nameMap.put("firstname", firstNameField.getText().toString());
        nameMap.put("middlename", middleNameField.getText().toString());
        nameMap.put("lastname", lastNameField.getText().toString());

        sampleMap.put("user info", nameMap);

        database.collection("userattempts").document(userNameField.getText().toString()).set(sampleMap);

        // Exit the screen when the registration is done
        this.finish();
    }

    public void cancel (View view) {
        Intent intent = new Intent(getApplicationContext(), Welcome.class);
        startActivity(intent);
    }

    public boolean validateUsername (final String username) {

        documentReference = database.collection("user").document(username);

        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            result = true;
                        } else {
                            result = false;
                        }
                    }
                });
        return result;
    }

    private boolean validateEmail (String email) {
        documentReference = database.collection("email").document(email);
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            result = true;
                        } else {
                            result = false;
                        }
                    }
                });
        return result;
    }

    private void createAccount(String email, String password) {
        if (!validateForm()) {
            return;
        }


        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                        } else {
                            // If sign in fails, display a message to the user.
                        }

                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;

        String firstname = firstNameField.getText().toString();
        if (TextUtils.isEmpty(firstname)) {
            firstNameField.setError("Required.");
            valid = false;
        } else {
            firstNameField.setError(null);
        }

        String lastname = lastNameField.getText().toString();
        if (TextUtils.isEmpty(lastname)) {
            lastNameField.setError("Required.");
            valid = false;
        } else {
            lastNameField.setError(null);
        }

        String middlename = middleNameField.getText().toString();
        if (TextUtils.isEmpty(firstname)) {
            middleNameField.setError("Required.");
            valid = false;
        } else {
            middleNameField.setError(null);
        }

        String password = passwordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Required.");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        String username = userNameField.getText().toString();
        if (TextUtils.isEmpty(username)) {
            userNameField.setError("Required.");
            valid = false;
        } else {
            userNameField.setError(null);
        }

        return valid;
    }
}
