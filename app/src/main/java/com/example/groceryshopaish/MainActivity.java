package com.example.groceryshopaish;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends Activity
{
    EditText etEmail,etPass;
    Button btLog,btSign;
    String mail,pass;
    FirebaseAuth fAuth;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);

        btLog = findViewById(R.id.btLog);
        btSign = findViewById(R.id.btSign);

        btLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail = etEmail.getText().toString();
                pass = etPass.getText().toString();

                if(mail.equals("admin@admin.com") && pass.equals("food123"))
                {
                    Toast.makeText(getApplicationContext(), "WELCOME-ADMIN", Toast.LENGTH_LONG).show();
                    Intent in1 = new Intent(getApplicationContext(),Admin.class);
                    startActivity(in1);
                }
                else {
                    fAuth = FirebaseAuth.getInstance();

                    fAuth.signInWithEmailAndPassword(mail, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "AUTHORISED USER", Toast.LENGTH_LONG).show();

                                        dbRef = FirebaseDatabase.getInstance().getReference("UDetails");
                                        Query findQ = dbRef.orderByChild("email").equalTo(mail);   // to locate required record

                                        findQ.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot)
                                            {
                                                User u1 = null;
                                                for(DataSnapshot snap : snapshot.getChildren())
                                                {
                                                    u1 = snap.getValue(User.class);
                                                }
                                                PaymentInfo payInfo = (PaymentInfo)getApplication();
                                                payInfo.name = u1.getName();
                                                payInfo.addr = u1.getAddress();
                                                payInfo.mobile = u1.getMobile();
                                                payInfo.mail = u1.getEmail();
                                                payInfo.amount = 0;
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                        Intent in2 = new Intent(getApplicationContext(),GroceryOrder.class);
                                        startActivity(in2);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "LOGIN FAIL", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

        btSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(getApplicationContext(),Registration.class);
                startActivity(i1);
            }
        });
    }
}