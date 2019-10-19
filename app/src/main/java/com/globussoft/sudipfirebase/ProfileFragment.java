package com.globussoft.sudipfirebase;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment
{
    private EditText inputName, inputLastName, inputEmail;
    private Button btnSave;
    TextView header_name,header_email;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String userId;
    private static final String TAG = MainActivity.class.getSimpleName();

    public ProfileFragment(TextView name, TextView email) {
       this.header_name=name;
       this.header_email=email;
    }

    @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {
            View view=inflater.inflate(R.layout.fragment_profile,container,false);
            inputName = (EditText) view.findViewById(R.id.name);
            inputLastName = (EditText) view.findViewById(R.id.lastname);
            inputEmail = (EditText) view.findViewById(R.id.email);
            btnSave = (Button) view.findViewById(R.id.btn_save);
            FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
            userId=firebaseUser.getUid();
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("users").child(userId);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user=dataSnapshot.getValue(User.class);
                    String name=user.getFirstName();
                    String lastname=user.getLastName();
                    String email=user.getEmail();
                    header_name.setText(name);
                    header_email.setText(email);
                    inputName.setText(name);
                    inputLastName.setText(lastname);
                    inputEmail.setText(email);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(),"somthing went Wrong",Toast.LENGTH_SHORT).show();
                }
            });

            mFirebaseInstance = FirebaseDatabase.getInstance();
            mFirebaseDatabase = mFirebaseInstance.getReference("users");
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String firstName = inputName.getText().toString();
                    String lastName=inputLastName.getText().toString();
                    String email = inputEmail.getText().toString();
                        updateUser(firstName,lastName,email);
                }
            });
            return view;

        }
    private void updateUser(String name,String lastName,String email) {

        if (!TextUtils.isEmpty(name))
            mFirebaseDatabase.child(userId).child("firstName").setValue(name);
        if (!TextUtils.isEmpty(lastName))
            mFirebaseDatabase.child(userId).child("lastName").setValue(lastName);
        if (!TextUtils.isEmpty(email))
            mFirebaseDatabase.child(userId).child("email").setValue(email);
        Toast.makeText(getActivity(),"Details Updated",Toast.LENGTH_SHORT).show();
    }
}
