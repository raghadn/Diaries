package com.example.diaries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    TextView login;
    Button regbtn;
    EditText userEmail,userPassword,userPassword2,regname;
    FirebaseAuth mAuth;
    boolean valid;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth=FirebaseAuth.getInstance();


        regbtn=(Button)findViewById(R.id.registerBtn);
        userEmail=(EditText)findViewById(R.id.regEmail);
        userPassword=(EditText)findViewById(R.id.regPass);
        userPassword2=(EditText)findViewById(R.id.regPass2);
        regname=(EditText)findViewById(R.id.regname);
        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String Email =userEmail.getText().toString();
                final String Password= userPassword.getText().toString();
                final String name= regname.getText().toString();
                final String confirmpassword=userPassword2.getText().toString();


                    if(Password.length()>9&&!name.isEmpty()&&!Email.isEmpty()&&!confirmpassword.isEmpty()) {
                        valid = isValid(Password, confirmpassword);
                        if (valid) {
                            CreateUserAccount(Email, Password, name);

                        }
                        else {
                            showMessage("Password and confirm password must be equal and contain a digit, lowercase and uppercase characters....");
                        }

                    }
                    else {
                        showMessage("All fields must be filled and password must contain 10 or more characters...");
                    }


            }
        });

        /*login=(TextView)findViewById(R.id.registerloginbtn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ClickRequest = new Intent(Register.this, Login.class);
                startActivity(ClickRequest);
                finish();
            }
        });*/




    }

    private void CreateUserAccount(final String Email, String Password,final String name){

            mAuth.createUserWithEmailAndPassword(Email,Password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                Vemail();
                               // showMessage("Registration complete..");
                                DatabaseReference Userref= FirebaseDatabase.getInstance().getReference().child("users").child(user);
                                HashMap userMap=new HashMap();
                                userMap.put("email",Email);
                                userMap.put("name",name);
                                Userref.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if(task.isSuccessful()){
                                            showMessage("Account created");
                                            sendUserToLoginActivity();
                                        }
                                        else {
                                            showMessage("Error");

                                        }

                                    }
                                });



                            }
                            else{

                                showMessage("Error");

                            }

                        }
                    });

    }
    public static boolean isValid(String passwordhere, String confirmhere) {

        Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
        Pattern lowerCasePatten = Pattern.compile("[a-z ]");
        Pattern digitCasePatten = Pattern.compile("[0-9 ]");

        boolean flag=true;

        if (!passwordhere.equals(confirmhere)) {
            flag=false;
        }
        if (!UpperCasePatten.matcher(passwordhere).find()) {
            flag=false;
        }
        if (!lowerCasePatten.matcher(passwordhere).find()) {
            flag=false;
        }
        if (!digitCasePatten.matcher(passwordhere).find()) {
            flag=false;
        }

        return flag;

    }
    private void Vemail (){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification();
        showMessage("Please Check your email");

        login=(TextView)findViewById(R.id.registerloginbtn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ClickRequest = new Intent(Register.this, Login.class);
                startActivity(ClickRequest);
                finish();
            }
        });

    }
    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(), message,Toast.LENGTH_LONG).show();
    }
    private void sendUserToLoginActivity(){
        Intent setupintent=new Intent(Register.this,Login.class);
        setupintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupintent);
        finish();
    }
}
