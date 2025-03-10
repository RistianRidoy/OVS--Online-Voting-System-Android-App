package com.example.ovs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class VotingActivity extends AppCompatActivity {

    private TextView name, party;
    private Button voteBtn;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);

        firebaseFirestore = FirebaseFirestore.getInstance();
        name = findViewById(R.id.name);
        party = findViewById(R.id.party);
        voteBtn = findViewById(R.id.votebutton);

        String name1 = getIntent().getStringExtra("name");
        String party1 = getIntent().getStringExtra("party");
        String id = getIntent().getStringExtra("id");

        name.setText(name1);
        party.setText(party1);
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getUid());

        voteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String finish = "voted";
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("finish", finish);
                userMap.put("deviceIp", getDeviceIP());
                userMap.put(party1, id);

                firebaseFirestore.collection("Users").document(uid).update(userMap);

                Map<String, Object> candidateMap = new HashMap<>();
                candidateMap.put("deviceIp", getDeviceIP());
                candidateMap.put("party", party1);
                candidateMap.put("timestamp", FieldValue.serverTimestamp());

                firebaseFirestore.collection("Candidate/" + id + "/Vote").document().set(candidateMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(VotingActivity.this, "VOTE GIVEN SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(VotingActivity.this, Result.class));
                            finish();
                        } else {
                            Toast.makeText(VotingActivity.this, "VOTE NOT GIVEN", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    private String getDeviceIP() {
        try {
            for(Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();){
                NetworkInterface inf = en.nextElement();
                for(Enumeration<InetAddress> enumIpAddr = inf.getInetAddresses(); enumIpAddr.hasMoreElements();){
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if(!inetAddress.isLoopbackAddress()){
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            Toast.makeText(VotingActivity.this, ""+e, Toast.LENGTH_SHORT).show();;
        }
        return null;
    }
}