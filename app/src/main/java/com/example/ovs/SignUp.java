package com.example.ovs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText userName, userEmail, userNID, userPassword;
    Button signUpBtn;
    TextView loginText;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userName = findViewById(R.id.user_name);
        userEmail = findViewById(R.id.user_email);
        userNID = findViewById(R.id.user_nid);
        userPassword = findViewById(R.id.user_password);
        signUpBtn =  findViewById(R.id.buttonsignup);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //if(mAuth.getCurrentUser() != null){
            //startActivity(new Intent(SignUp.this, Home.class));
            //finish();
        //}
        findViewById(R.id.loginText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, Login.class));
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = userName.getText().toString().trim();
                String email = userEmail.getText().toString().trim();
                String nid = userNID.getText().toString().trim();
                String password = userPassword.getText().toString().trim();

                if (email.equals("") || password.equals(""))
                    Toast.makeText(SignUp.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();

                else {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUp.this, "ACCOUNT IS CREATED", Toast.LENGTH_SHORT).show();
                                uid = mAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = db.collection("Users").document(uid);

                                Map<String, Object> map = new HashMap<>();
                                map.put("name", name);
                                map.put("email", email);
                                map.put("nid", nid);
                                map.put("password", password);
                                map.put("uid", uid);

                                documentReference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG, "USER IS STORED IN DATABASE");
                                    }
                                });
                                startActivity(new Intent(SignUp.this, Login.class));
                            } else {
                                Toast.makeText(SignUp.this, "INVALID CREDENTIALS", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });
    }
}