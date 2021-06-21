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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class UpdateGrocery extends Activity {

    Spinner UpdSp;
    EditText etUpdPrice,etUpdStock,etUpdMsr;
    Button btUpdGroc,btUpdImg;
    List<Grocery> listObj = new ArrayList<Grocery>();
    List listName = new ArrayList();
    DatabaseReference dbRef;
    StorageReference storage;
    int price,stock;
    String gId,gUrl,gName,measure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updategrocery);

        UpdSp=  findViewById(R.id.UpdSp);
        etUpdPrice = findViewById(R.id.etUpdPrice);
        etUpdStock = findViewById(R.id.etUpdStock);
        etUpdMsr = findViewById(R.id.etUpdMsr);
        btUpdImg = findViewById(R.id.btUpdImg);
        btUpdGroc = findViewById(R.id.btUpdGroc);

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
                UpdSp.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        UpdSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                gId = listObj.get(position).getGid();
                gName = listObj.get(position).getGname();
                gUrl = listObj.get(position).getImageUri();
                price = listObj.get(position).getPrice();
                stock = listObj.get(position).getStock();
                measure = listObj.get(position).getMeasure();

                etUpdPrice.setText(""+price);
                etUpdStock.setText(""+stock);
                etUpdMsr.setText(""+measure);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btUpdImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btUpdGroc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbRef = FirebaseDatabase.getInstance().getReference("grocery");

                price = Integer.parseInt(etUpdPrice.getText().toString());
                stock = Integer.parseInt(etUpdStock.getText().toString());
                measure = etUpdMsr.getText().toString();

                Grocery gr  = new Grocery(gId,gUrl,gName,measure,price,stock);

                dbRef.child(gId).setValue(gr);

                Toast.makeText(getApplicationContext(), "GROCERY UPDATED...", Toast.LENGTH_LONG).show();
            }
        });
    }
}
