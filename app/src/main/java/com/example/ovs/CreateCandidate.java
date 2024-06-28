package com.example.ovs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateCandidate extends AppCompatActivity {

    private EditText candidateName, candidateParty;
    private Button submitBtn;
    private Uri mainUri = null;
    StorageReference reference;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_candidate);

        reference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        candidateName = findViewById(R.id.cand_name);
        candidateParty = findViewById(R.id.cand_party_name);
        submitBtn = findViewById(R.id.submitbtn);


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = candidateName.getText().toString().trim();
                String party = candidateParty.getText().toString().trim();

                if(!TextUtils.isEmpty(name) || !TextUtils.isEmpty(party) || mainUri != null){
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    Map<String, Object> map = new HashMap<>();
                    map.put("name", name);
                    map.put("party", party);
                    map.put("timestamp", FieldValue.serverTimestamp());
                                        firebaseFirestore.collection("Candidate").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                if(task.isSuccessful()){
                                                    startActivity(new Intent(CreateCandidate.this, Home.class));
                                                    finish();
                                                }
                                                else{
                                                    Toast.makeText(CreateCandidate.this, "DATA NOT STORED", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                }
                else{
                    Toast.makeText(CreateCandidate.this, "PLEASE FILL UP ALL THE FIELDS", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}