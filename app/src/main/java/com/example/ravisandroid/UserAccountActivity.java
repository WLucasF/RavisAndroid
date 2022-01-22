/*
W Lucas Franklin
01/19/2022
Let user customize their profile.
 */
package com.example.ravisandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAccountActivity extends AppCompatActivity {

        //acc = account
        private Button updateAccSettings;
        private EditText accName, accPhoneNumber, accAddress;
        private CircleImageView userAccImage;

        private String currentUserID;
        private FirebaseAuth mAuth;
        private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();

        initializeFields();

        updateAccSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSettings();
            }
        });

      retrieveUserInfo();

    }

    /*
    W Lucas Franklin
    01/20/2022
    Show user info in boxes where data already exists.
     */
    private void retrieveUserInfo() {
        rootRef.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && (snapshot.hasChild("name") && (snapshot.hasChild("image")))){
                    String retrieveUserName = snapshot.child("name").getValue().toString();
                    String retrieveUserPic = snapshot.child("image").getValue().toString();

                    accName.setText(retrieveUserName);
                }
                else if (snapshot.exists() && (snapshot.hasChild("name"))){
                    String retrieveUserName = snapshot.child("name").getValue().toString();
                    accName.setText(retrieveUserName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initializeFields() {
        updateAccSettings = (Button) findViewById(R.id.set_acc_update_button);
        accName = (EditText) findViewById(R.id.set_acc_name);
        accPhoneNumber = (EditText) findViewById(R.id.set_acc_phone_num);
        accAddress = (EditText) findViewById(R.id.set_acc_address);
        userAccImage = (CircleImageView) findViewById(R.id.set_acc_image);
    }

    private void updateSettings() {
        String setAccountName = accName.getText().toString();

        if (TextUtils.isEmpty(setAccountName)){
            Toast.makeText(this, "please write your name", Toast.LENGTH_SHORT).show();
        }
        else{
            HashMap<String, String> accountMap = new HashMap<>();
            accountMap.put("uid", currentUserID);
            accountMap.put("name", setAccountName);

            rootRef.child("Users").child(currentUserID).setValue(accountMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        sendUserToMainActivity();
                        Toast.makeText(UserAccountActivity.this, "profile updated", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        String message = task.getException().toString();
                        Toast.makeText( UserAccountActivity.this, "error: " + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }
    private void sendUserToMainActivity() {
        Intent mainIntent = new Intent(UserAccountActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }



}