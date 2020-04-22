package com.example.diaries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class Login extends AppCompatActivity {
    TextView regist;
    FirebaseAuth mAuth;
    EditText mail,pass;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();
        mail=(EditText)findViewById(R.id.loginEmail);
        pass=(EditText)findViewById(R.id.loginPassword);
        login=(Button)findViewById(R.id.loginBtn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Email =mail.getText().toString();
                final String Password= pass.getText().toString();





                    if(!Password.isEmpty()&&!Email.isEmpty())
                        signin(Email,Password);

                    else {
                        showMessage("Please fill all fields...");

                }

            }
        });



        regist=(TextView)findViewById(R.id.loginregisterbtn);
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ClickRequest = new Intent(Login.this, Register.class);
                startActivity(ClickRequest);
                finish();
            }
        });
    }


    private void signin(String email, String password) {

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    if(mAuth.getCurrentUser().isEmailVerified()){
                        SendUserToMainActivity();
                    }else {
                        showMessage("Please Verify your Email");
                    }

                }
                else {

                   showMessage("Incorrect email or password");

                }

            }
        });

    }
    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(), message,Toast.LENGTH_LONG).show();
    }
    private void SendUserToMainActivity(){
        Intent setupintent=new Intent(Login.this,MainActivity.class);
        setupintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupintent);
        finish();
    }

}
