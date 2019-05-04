package cecs343.pollio;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

import android.support.annotation.NonNull;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity {

    private EditText registerEmailText;
    private EditText registerPassText;
    private EditText confirmPassText;
    private EditText displayNameText;



    Button registerButton;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();

        //get the text from the app
        registerEmailText = findViewById(R.id.email);
        registerPassText = findViewById(R.id.password);
        confirmPassText = findViewById(R.id.reenter_password);
        displayNameText = findViewById(R.id.display_name);


        //when the user clicks the register button (aka they finished registering):
        registerButton = findViewById(R.id.email_register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }

        });
    }

    public void register(){
        //Store the user's data that they entered
        String email = registerEmailText.getText().toString().trim(); //getrid of white spaces
        String password = registerPassText.getText().toString();
        String confirmPass = confirmPassText.getText().toString();
        final String dName = displayNameText.getText().toString();

        //Check the fields----------------
        // If either email or passwords are empty...
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)
                || TextUtils.isEmpty(confirmPass)) {

            Toast.makeText(RegisterActivity.this, "ERROR: One or more field are missing.", Toast.LENGTH_SHORT).show();
        }
        else if(!TextUtils.equals(password, confirmPass)){
            Toast.makeText(this, "Your passwords do not match.", Toast.LENGTH_SHORT).show();
        }
        else if(password.length() < 6) {
            Toast.makeText(RegisterActivity.this, "ERROR: Password must be at least 6 characters in length.", Toast.LENGTH_SHORT).show();

        }
        //if all requirements are passed
        else{
            //store the user into the authentication
            // Stores the register result.
            Task<AuthResult> registerResult;
            registerResult = firebaseAuth.createUserWithEmailAndPassword(email, password);

            //called when a task is complete
            registerResult.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //check if the task is NOT completed
                    if(!task.isSuccessful()) {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(RegisterActivity.this, "ERROR: Registration failed. User already exists", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(RegisterActivity.this, "ERROR: Registration failed. Please make sure your password is at least 5 characters long", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        //puts displayName into the database
                        HashMap<String, String> args = new HashMap<>();
                        args.put("displayname", dName);
                        Requestor.postRequest(getApplicationContext(), "register", FirebaseAuth.getInstance().getCurrentUser(), args);

                        Intent pollFragment = new Intent(RegisterActivity.this, PollFeedActivity.class);

                        //now clear the stack so user cannot click back to go back to register
                        pollFragment.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        //FINALLY LET USER IN!
                        startActivity(pollFragment);

                    }

                }
            });


        }

    }


}


