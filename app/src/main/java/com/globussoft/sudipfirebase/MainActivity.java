package com.globussoft.sudipfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {
public EditText emailId,passwd,fname,lname;
Button btnSignUp;
TextView signIn;
FirebaseAuth firebaseAuth;
ProgressBar progressBar;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseInstance;
private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onStart() {
        super.onStart();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    if(firebaseAuth.getCurrentUser().isEmailVerified())
                    {
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        finishAffinity();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "signup to continue", Toast.LENGTH_SHORT).show();
                }
            }
        };
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth=FirebaseAuth.getInstance();
        emailId=findViewById(R.id.ETemail);
        passwd=findViewById(R.id.ETpassword);
        fname=findViewById(R.id.fname);
        lname=findViewById(R.id.lname);
        btnSignUp=findViewById(R.id.btnSignUp);
        signIn=findViewById(R.id.TVSignIn);
        progressBar=findViewById(R.id.progressBar);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseInstance.getReference("users");
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstName=fname.getText().toString();
                final String lastName=lname.getText().toString();
                final String emailID = emailId.getText().toString();
                final String paswd = passwd.getText().toString();

                if (emailID.isEmpty() && paswd.isEmpty()&&firstName.isEmpty()&&lastName.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                }
                else if (firstName.isEmpty()) {
                    fname.setError("Give your first name");
                    fname.requestFocus();
                }else if (lastName.isEmpty()) {
                    lname.setError("Give your last name   ");
                    lname.requestFocus();
                } else if (emailID.isEmpty()) {
                    emailId.setError("Email cannot be empty");
                    emailId.requestFocus();
                } else if (paswd.isEmpty()) {
                    passwd.setError("Set your password");
                    passwd.requestFocus();
                }
                    else if (!(emailID.isEmpty() && paswd.isEmpty())) {
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.createUserWithEmailAndPassword(emailID, paswd)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.INVISIBLE);

                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this.getApplicationContext(),
                                        "SignUp unsuccessful: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                firebaseAuth.getCurrentUser().sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    String id=firebaseAuth.getCurrentUser().getUid();
                                                    User user=new User(firstName,lastName,emailID);
                                                    mDatabaseReference.child(id).setValue(user);
                                                    Toast.makeText(MainActivity.this.getApplicationContext(),
                                                            "SignUp successful:please check your email vor verification link",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                finishAffinity();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(I);
                finishAffinity();
            }
        });
    }

}

