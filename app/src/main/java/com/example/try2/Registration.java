package com.example.try2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class Registration extends AppCompatActivity {
    EditText emailid,password;
    Button Register;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mFirebaseAuth = FirebaseAuth.getInstance();
        user = mFirebaseAuth.getCurrentUser();
        emailid = findViewById(R.id.emailid);
        password = findViewById(R.id.password);
        Register = findViewById(R.id.Register);
        Register.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(Registration.this, "Please enter credentials", Toast.LENGTH_SHORT).show();
                }
                else if(!(email.isEmpty() && pwd.isEmpty())){
                    mFirebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                FirebaseAuthException e = (FirebaseAuthException )task.getException();
                                Toast.makeText(Registration.this, "Failed Registration: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(Registration.this,"Pre-Registration Successful. Please check your inbox for verifying your account.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Registration.this, MainActivity.class));
                                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(Registration.this,"Email sent.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(Registration.this,"Error occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        // Toast.makeText(getBaseContext(),"Press back again to exit.",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Registration.this,MainActivity.class));
    }
}
