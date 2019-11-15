package com.example.gravity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.gravity.DebugTags.DebugTags;
import com.example.gravity.FirestoreTags.FirestoreTags;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserInfoActivity extends AppCompatActivity {
    TextView errorTextView;
    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText ageEditText;
    EditText genderEditText;
    Button submitButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        errorTextView = findViewById(R.id.userInfoErrorTextView);
        firstNameEditText = findViewById(R.id.userInfoFirstNameEditText);
        lastNameEditText = findViewById(R.id.userInfoLastNameEditText);
        ageEditText = findViewById(R.id.userInfoAgeEditText);
        genderEditText = findViewById(R.id.userInfoGenderEditText);
        submitButton = findViewById(R.id.userInfoSubmitInfoButton);

        // refresh users current data
        // pull from firestore
        LoadCurrentUserData();


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add user info to firestore
                if (AllFieldsAreFilled()) {
                    UpdateUserData();
                } else {
                    errorTextView.setText(R.string.inputFieldNotFilledError);
                }
            }
        });
    }

    private boolean AllFieldsAreFilled() {
        boolean output = true;
        if (TextUtils.isEmpty(firstNameEditText.getText().toString())) {
            output = false;
        }
        if (TextUtils.isEmpty(lastNameEditText.getText().toString())) {
            output = false;
        }
        if (TextUtils.isEmpty(ageEditText.getText().toString())) {
            output = false;
        }
        if (TextUtils.isEmpty(genderEditText.getText().toString())) {
            output = false;
        }
        return output;
    }

    private void LoadCurrentUserData() {

    }

    private void UpdateUserData() {
        Map<String, Object> userData = new HashMap<>();
        userData.put(FirestoreTags.userFirstName, firstNameEditText.getText().toString());
        userData.put(FirestoreTags.userLastName, lastNameEditText.getText().toString());
        userData.put(FirestoreTags.userAge, ageEditText.getText().toString());
        userData.put(FirestoreTags.userGender, genderEditText.getText().toString());

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            mFirestore.collection(FirestoreTags.users).document(user.getUid())
                    .update(userData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(DebugTags.UserInfoActivityDebugTag, "DocumentSnapshot successfully written!");
                            startActivity(new Intent(UserInfoActivity.this, UserProfileActivity.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(DebugTags.UserInfoActivityDebugTag, "Error writing document", e);
                        }
                    });
        } else {
            Log.d(DebugTags.UserInfoActivityDebugTag, "UserInfoActivity: current user is null");
        }


    }
}
