package com.example.try2;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private long backPressedTime;
    private Toast backToast;
    private Button button;
    CheckBox rememberme;
    EditText emailid,password;
    Button signinbutton;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAuth = FirebaseAuth.getInstance();
        emailid = findViewById(R.id.emailid);
        password = findViewById(R.id.password);
        button = (Button) findViewById(R.id.buttonnewuser);

        rememberme = findViewById(R.id.remember);
        rememberme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember" , "true");
                    editor.apply();
                    Toast.makeText(MainActivity.this, "We will remember you.", Toast.LENGTH_SHORT).show();
                    
                }else if(!buttonView.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember" , "false");
                    editor.apply();
                    Toast.makeText(MainActivity.this, "Unchecked", Toast.LENGTH_SHORT).show();

                }
            }
        });

        /*mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if(mFirebaseUser!=null){
                    Toast.makeText(MainActivity.this,"Logged in.",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, Home.class));
                }
                else{
                    Toast.makeText(MainActivity.this,"Please login.",Toast.LENGTH_SHORT).show();
                }
            }
        };*/


        signinbutton= (Button) findViewById(R.id.signinbutton);
        signinbutton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String email = emailid.getText().toString();
                String pwd = password.getText().toString();
                if(email.isEmpty()){
                    emailid.setError("Enter email address please.");
                    emailid.requestFocus();
                }
                else if(pwd.isEmpty()){
                    password.setError("Enter password please.");
                    password.requestFocus();
                }
                else if(email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter credentials", Toast.LENGTH_SHORT).show();
                }
                else if(!(email.isEmpty() && pwd.isEmpty())){
                    mFirebaseAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(MainActivity.this,"Error Occurred. Try again.",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                if(mFirebaseAuth.getCurrentUser().isEmailVerified()) {
                                    startActivity(new Intent(MainActivity.this, Home.class));
                                }
                                else{
                                    Toast.makeText(MainActivity.this,"Please verify email address and Try again.",Toast.LENGTH_SHORT).show();
                                }
                                }
                        }
                    });
                }
                else {
                    Toast.makeText(MainActivity.this, "Error occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openRegistration();
            }
        });
    }
    public void openRegistration() {
        Intent intent = new Intent(this, Registration.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        if(backPressedTime+2000>System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            return;
        }else{
            backToast = Toast.makeText(getBaseContext(),"Press back again to exit.",Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
    /*@Override
    protected void onStart(){
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }*/
}
