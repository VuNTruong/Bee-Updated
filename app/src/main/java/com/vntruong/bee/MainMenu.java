package com.vntruong.bee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainMenu extends AppCompatActivity {
    private TextView name;
    private FirebaseFirestore database;

    public static String startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        name = findViewById(R.id.name);

        database = FirebaseFirestore.getInstance();

        startTime = new String ();

        getUserInformation();
    }

    private void getUserInformation () {

        DocumentReference documentReference;

        if (Login.user != null) {
            documentReference = database.collection("user").document(Login.user.getEmail());
        } else {
            documentReference = database.collection("user").document(Welcome.user.getEmail());
        }

        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String name = documentSnapshot.getString("firstname") + " " +
                                documentSnapshot.getString("middlename") + " " +
                                documentSnapshot.getString("lastname");

                        TextView nameTextView = findViewById(R.id.name);
                        nameTextView.setText(name);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void signOut (View view) {
        if (Login.mAuth != null) {
            Login.mAuth.signOut();
            Intent intent = new Intent(getApplicationContext(), Welcome.class);
            startActivity(intent);
            this.finish();
        } else {
            Welcome.mAuth.signOut();
            Intent intent = new Intent(getApplicationContext(), Welcome.class);
            startActivity(intent);
            this.finish();
        }
    }
}
