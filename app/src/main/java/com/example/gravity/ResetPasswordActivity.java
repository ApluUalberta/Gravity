package com.example.gravity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gravity.DebugTags.DebugTags;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        mAuth = FirebaseAuth.getInstance();

        final EditText emailEditText = findViewById(R.id.passwordResetActivityEnterEmailEditText);
        final TextView errorTextView = findViewById(R.id.passwordResetActivityErrorTextView);
        Button sendButton = findViewById(R.id.passwordResetActivitySendButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                if (!TextUtils.isEmpty(email)) {
                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(DebugTags.PasswordResetActivityDebugTag, "Email sent.");
                                        Toast.makeText(ResetPasswordActivity.this, "reset email sent", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            });
                } else {
                    errorTextView.setText(R.string.passwordResetActivityEmailEmptyErrorText);
                }

            }
        });
    }
}
