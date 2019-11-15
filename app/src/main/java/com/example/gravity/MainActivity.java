package com.example.gravity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gravity.DebugTags.DebugTags;
import com.example.gravity.FirestoreTags.FirestoreTags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private TextView mainTitleTextView;
    private TextView errorTextView;
    private EditText fullnameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private TextView createAnAccountTextView;
    private TextView forgotPasswordTextView;
    private Button registerLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        final String createAnAccountString = getResources().getString(R.string.mainActivityCreateAnAccountTitle);
        final String alreadyHaveAnAccountString = getResources().getString(R.string.mainActivityAlreadyHaveAnAccount);

        mainTitleTextView = findViewById(R.id.mainActivityTitle);
        errorTextView = findViewById(R.id.mainActivityErrorTextView);
        fullnameEditText = findViewById(R.id.mainActivityFullnameEditText);
        emailEditText = findViewById(R.id.mainActivityEmailEditText);
        passwordEditText = findViewById(R.id.mainActivityPasswordEditText);
        createAnAccountTextView = findViewById(R.id.mainActivityCreateAnAccountTextView);
        forgotPasswordTextView = findViewById(R.id.mainActivityForgotPasswordTextView);
        registerLoginButton = findViewById(R.id.mainActivityRegisterLoginButton);

        createAnAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentText = createAnAccountTextView.getText().toString();
                if (currentText == createAnAccountString) {
                    // need to switch to register layout
                    mainTitleTextView.setText(R.string.mainActivityRegisterTitle);
                    fullnameEditText.setVisibility(View.INVISIBLE);
                    createAnAccountTextView.setText(R.string.mainActivityAlreadyHaveAnAccount);
                    forgotPasswordTextView.setVisibility(View.INVISIBLE);
                    registerLoginButton.setText(R.string.mainActivityRegisterTitle);
                } else if (currentText == alreadyHaveAnAccountString) {
                    // need to switch to login layout
                    mainTitleTextView.setText(R.string.mainActivityLoginTitle);
                    fullnameEditText.setVisibility(View.INVISIBLE);
                    createAnAccountTextView.setText(R.string.mainActivityCreateAnAccountTitle);
                    forgotPasswordTextView.setVisibility(View.VISIBLE);
                    registerLoginButton.setText(R.string.mainActivityLoginTitle);
                } else {
                    // unrecognized text
                    Log.d(DebugTags.MainActivityDebugTag, "unrecognized text for create an account");
                }
            }
        });

        registerLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (registerLoginButton.getText() == getResources().getString(R.string.mainActivityRegisterTitle)) {
                    // user wants to register
                    if (EmailIsValid(emailEditText.getText().toString())) {
                        RegisterUser(emailEditText.getText().toString(), passwordEditText.getText().toString());
                    }

                } else if (registerLoginButton.getText() == getResources().getString(R.string.mainActivityLoginTitle)) {
                    // user wants to sign-in
                    SignInUser(emailEditText.getText().toString(), passwordEditText.getText().toString());
                } else {
                    // unrecognized text
                    Log.d(DebugTags.MainActivityDebugTag, "unrecognized text for register/login button");
                }
            }
        });
    }

    private boolean EmailIsValid(String email) {
        // ensure email is valid
        return true;
    }

    private void RegisterUser(String email, String password) {
        // user Firebase Auth to create a new user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(DebugTags.MainActivityDebugTag, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            InitializeUserDataForFirestore(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(DebugTags.MainActivityDebugTag, "createUserWithEmail:failure", task.getException());
                            errorTextView.setText("Failed to create account");
                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void SignInUser(String email, String password) {
        // use Firebase Auth to sign-in a current user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(DebugTags.MainActivityDebugTag, "signInWithEmail:success");
                            // go to user profile activity
                            Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(DebugTags.MainActivityDebugTag, "signInWithEmail:failure", task.getException());
                            errorTextView.setText("Failed to sign-in");
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void InitializeUserDataForFirestore(FirebaseUser user) {
        // initialize data for a particular user when they create a new account
        Map<String, Object> userData = new HashMap<>();
        userData.put(FirestoreTags.userFullName, fullnameEditText.getText().toString());

        mFirestore.collection(FirestoreTags.users).document(user.getUID())
                .set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(DebugTags.MainActivityDebugTag, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(DebugTags.MainActivityDebugTag, "Error writing document", e);
                    }
                });
    }
}


