package com.example.groceryshopaish;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class DeleteGrocery extends Activity {

    Spinner sp1;
    EditText etDelName,etDelPrice,etDelStock,etDelMsr;
    Button btDel;
    List listName = new ArrayList();
    List<Grocery> listObj = new ArrayList<Grocery>();
    DatabaseReference dbRef;
    StorageReference storage;
    String gId,gUrl,gName,measure;
    int price,stock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletegrocery);

        sp1 = findViewById(R.id.sp1);

        etDelName = findViewById(R.id.etDelName);
        etDelPrice = findViewById(R.id.etDelPrice);
        etDelStock = findViewById(R.id.etDelStock);
        etDelMsr = findViewById(R.id.etDelMsr);

        btDel = findViewById(R.id.btDel);

        dbRef = FirebaseDatabase.getInstance().getReference("grocery");
        storage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://groceryshopaish.appspot.com/grocery");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listName.clear();
                listObj.clear();

                for(DataSnapshot snap : snapshot.getChildren())
                {
                    Grocery g = snap.getValue(Grocery.class);

                    listName.add(g.getGname());
                    listObj.add(g);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,listName);
                sp1.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                gId = listObj.get(position).getGid();
                gName = listObj.get(position).getGname();
                gUrl = listObj.get(position).getImageUri();
                price = listObj.get(position).getPrice();
                stock = listObj.get(position).getStock();
                measure = listObj.get(position).getMeasure();

                etDelName.setText(""+gName);
                etDelPrice.setText(""+price);
                etDelStock.setText(""+stock);
                etDelMsr.setText(""+measure);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbRef = FirebaseDatabase.getInstance().getReference("grocery");
                storage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://groceryshopaish.appspot.com/grocery");

                Query delQ = dbRef.orderByChild("gId").equalTo(gId);

                delQ.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot snap : snapshot.getChildren())
                        {
                            Grocery g1 = snap.getValue(Grocery.class);
                            snap.getRef().removeValue();  // Used to remove record from Realtime Database

                            storage = storage.child(gName);

                            storage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Toast.makeText(getApplicationContext(),gName+" HAS BEEN REMOVED SUCCESSFULLY", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }
}