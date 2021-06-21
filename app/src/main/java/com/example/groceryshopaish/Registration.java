package com.example.groceryshopaish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration extends Activity
{
    EditText etRegEmail,etRegPass,etRegName,etRegAddr,etRegMobile;
    Button btRegRegister,btRegCancel;
    String uid,email,pass,name,addr,mobile;
    FirebaseAuth fAuth;
    DatabaseReference dbRef;
    int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPass = findViewById(R.id.etRegPass);
        etRegName = findViewById(R.id.etRegName);
        etRegAddr = findViewById(R.id.etRegAddr);
        etRegMobile = findViewById(R.id.etRegMobile);

        btRegRegister = findViewById(R.id.btRegRegister);
        btRegCancel = findViewById(R.id.btRegCancel);

        Intent i1 = getIntent();

        btRegRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etRegEmail.getText().toString();
                pass = etRegPass.getText().toString();
                name = etRegName.getText().toString();
                addr = etRegAddr.getText().toString();
                mobile = etRegMobile.getText().toString();

                String expName = "^[A-Za-z]{3,15}$";
                Pattern patName = Pattern.compile(expName);
                Matcher matName = patName.matcher(name);


                String expMail = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
                Pattern patMail = Pattern.compile(expMail);
                Matcher matMail = patMail.matcher(email);

                String expPhone = "^[678]{1}[0-9]{9}$";
                Pattern patPhone = Pattern.compile(expPhone);
                Matcher matPhone = patPhone.matcher(mobile);

                if(matName.matches()==false)
                {
                    Toast.makeText(getApplicationContext(), "EMPTY NAME OR INVALID INPUT", Toast.LENGTH_LONG).show();
                    etRegName.requestFocus();
                    flag=1;
                }

                if(matMail.matches()==false)
                {
                    Toast.makeText(getApplicationContext(), "EMPTY NAME OR INVALID INPUT", Toast.LENGTH_LONG).show();
                    etRegEmail.requestFocus();
                    flag=1;
                }

                if(matPhone.matches()==false)
                {
                    Toast.makeText(getApplicationContext(), "EMPTY NAME OR INVALID INPUT", Toast.LENGTH_LONG).show();
                    etRegMobile.requestFocus();
                    flag=1;
                }

                if(addr.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "EMPTY ADDRESS", Toast.LENGTH_LONG).show();
                    etRegAddr.requestFocus();
                    flag=1;
                }

                if(pass.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "EMPTY PASSWORD", Toast.LENGTH_LONG).show();
                    etRegPass.requestFocus();
                    flag=1;
                }

                if(flag==0) {

                    fAuth = FirebaseAuth.getInstance();

                    fAuth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        dbRef = FirebaseDatabase.getInstance().getReference("UDetails");

                                        uid = dbRef.push().getKey();

                                        User user = new User(uid, email, pass, name, addr, mobile);
                                        dbRef.child(uid).setValue(user);

                                        Toast.makeText(getApplicationContext(), "USER REGISTERED SUCCESSFULLY...", Toast.LENGTH_LONG).show();

                                        Intent ii = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(ii);
                                    }
                                }
                            });
                }
            }
        });
    }
}
