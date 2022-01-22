package com.example.ravisandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ravisandroid.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Authenticate user and allow them into the app.
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_circle, R.id.navigation_calendar)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }


    /*
    W Lucas Franklin
    1/18/2022
    Make user login to their account.
     */
    @Override
    protected void onStart(){
        super.onStart();

        if (currentUser == null){
            sendUserToLoginActivity();
        }
        else{
            verifyUserExistence();
        }
    }

    /*
    W Lucas Franklin
    01/19/2022
    Forces user to add data to account.
    Consider removing.
     */
    private void verifyUserExistence() {
        String currentUserID = mAuth.getCurrentUser().getUid();

        rootRef.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("name").exists()){
                }
                else{
                    sendUserToUserAccActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /*
    W Lucas Franklin
    01/19/2022
    Give functionality to top right settings buttons.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.main_logout_option){
            mAuth.signOut();
            sendUserToLoginActivity();
        }
        if (item.getItemId() == R.id.main_user_account_option){
            sendUserToUserAccActivity();
        }

        if (item.getItemId() == R.id.main_create_group_option){
            requestNewGroup();
        }

        if (item.getItemId() == R.id.main_search_option){

        }

        return true;
    }

    /*
    W Lucas Franklin
    01/20/2022
    Create groups to put people in.
    In the top right settings menu.
    */
    private void requestNewGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog);
        builder.setTitle("enter group name");

        final EditText groupNameField = new EditText(MainActivity.this);
        groupNameField.setHint("e.g. Bunker Bitches");
        builder.setView(groupNameField);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String groupName = groupNameField.getText().toString();

                if (TextUtils.isEmpty(groupName)){
                    Toast.makeText(MainActivity.this, "no name entered", Toast.LENGTH_SHORT).show();
                }
                else{
                    createNewGroup(groupName);
                }
            }
        });

        //Cancel button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void createNewGroup(String groupName) {
        rootRef.child("Groups").child(groupName).setValue("null").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(MainActivity.this, groupName + " group has been created", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /*
    sendUserTo functions.
     */
    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void sendUserToUserAccActivity() {
        Intent settingsIntent = new Intent(MainActivity.this, UserAccountActivity.class);
        startActivity(settingsIntent);
    }


}