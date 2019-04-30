package cecs343.pollio;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

//THIS IS THE ACTIVITY WHERE THE USER RESETS THEIR PASS


public class forgotPasswordActivity extends AppCompatActivity {
    private EditText emailForPassReset;
    private Button resetPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        emailForPassReset = findViewById(R.id.emailPassChange);
        resetPassword = findViewById(R.id.resetButton);

        mAuth = FirebaseAuth.getInstance();
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //trim white spaces
                String userEmail = emailForPassReset.getText().toString().trim();
                //if user doesn't type anything
                if (userEmail.equals("")) {
                    Toast.makeText(forgotPasswordActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(forgotPasswordActivity.this, "Password reset email sent!", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(forgotPasswordActivity.this, LoginActivity.class));

                            } else {
                                Toast.makeText(forgotPasswordActivity.this, "Password reset failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

    }

}
