package com.example.ovs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

public class Home extends AppCompatActivity {

    TextView c_email, c_nid, adminmsg, votermsg;
    Button createBtn, voteBtn, startBtn;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        c_email = findViewById(R.id.admin_email);
        c_nid = findViewById(R.id.admin_nid);
        adminmsg = findViewById(R.id.admin_msg);
        votermsg = findViewById(R.id.voter_msg);
        createBtn = findViewById(R.id.admin_button);
        voteBtn = findViewById(R.id.give_vote);
        startBtn = findViewById(R.id.start_vote);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        uid = mAuth.getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("Users").document(uid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                String email = value.getString("email");
                String nid = value.getString("nid");

                c_email.setText(value.getString("email"));
                c_nid.setText(value.getString("nid"));

                if(email.equals("admin01@gmail.com")) {
                    adminmsg.setVisibility(View.VISIBLE);
                    votermsg.setVisibility(View.GONE);
                    createBtn.setVisibility(View.VISIBLE);
                    startBtn.setVisibility(View.VISIBLE);
                    voteBtn.setVisibility(View.GONE);
                }
                else{
                    adminmsg.setVisibility(View.GONE);
                    votermsg.setVisibility(View.VISIBLE);
                    createBtn.setVisibility(View.GONE);
                    startBtn.setVisibility(View.GONE);
                    voteBtn.setVisibility(View.VISIBLE);
                }
            }
        });

//        findViewById(R.id.sign_out).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(Home.this, Login.class));
//                finish();
//            }
 //       });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, CreateCandidate.class));
            }
        });
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, AllCandidateActivity.class));
            }
        });
        voteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, AllCandidateActivity.class));
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        //SharedPreferences.Editor pref = sharedPreferences.edit();
        switch (id){
            case R.id.show_result:
                startActivity(new Intent(Home.this, Result.class));
                return true;
            case R.id.logout:
                //FirebaseAuth.getInstance().signOut();
                //pref.putBoolean(IsLogIn, false);
                //pref.commit();
                startActivity(new Intent(Home.this, Login.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}