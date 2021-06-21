package com.example.groceryshopaish;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddGrocery extends Activity {

    EditText etAddName,etAddPrice,etAddStock,etAddMeasure;
    Button btAddImg,btAddGroc;
    ProgressBar pbGroc;
    Uri imagePath;
    DatabaseReference dbRef;
    StorageReference storage,store;
    String gName,measure;
    int price,stock;
    String imgUrl;
    String gid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addgrocery);

        Intent ia1 = getIntent();

        etAddName = findViewById(R.id.etAddName);
        etAddPrice = findViewById(R.id.etAddPrice);
        etAddStock = findViewById(R.id.etAddStock);
        etAddMeasure = findViewById(R.id.etAddMeasure);

        btAddImg = findViewById(R.id.btAddImg);
        btAddGroc = findViewById(R.id.btAddGroc);

        pbGroc = findViewById(R.id.pbGroc);
        pbGroc.setVisibility(ProgressBar.GONE);

        btAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(ii,201);
            }
        });

        btAddGroc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pbGroc.setVisibility(View.VISIBLE);

                gName = etAddName.getText().toString();
                price = Integer.parseInt(etAddPrice.getText().toString());
                stock = Integer.parseInt(etAddStock.getText().toString());
                measure = etAddMeasure.getText().toString();

                dbRef = FirebaseDatabase.getInstance().getReference("grocery");
                storage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://groceryshopaish.appspot.com/Grocery");

                store = storage.child(gName);
                gid = dbRef.push().getKey();

                store.putFile(imagePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {            //image uploaded successfully
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                store.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {          //to get url
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Toast.makeText(getApplicationContext(),"IMAGE UPLOADED...",Toast.LENGTH_LONG).show();

                                        Grocery g = new Grocery(gid,uri.toString(),gName,measure,price,stock);
                                        dbRef.child(gid).setValue(g);

                                        Toast.makeText(getApplicationContext(),"GROCERY SUCCESSFULLY REGISTERED...",Toast.LENGTH_LONG).show();

                                        pbGroc.setVisibility(ProgressBar.GONE);

                                        etAddName.setText("");
                                        etAddMeasure.setText("");
                                        etAddPrice.setText("");
                                        etAddStock.setText("");
                                    }
                                });
                            }
                        });
            }
        });

    }

    @Override
    public void onActivityResult(int reqCode,int res,Intent data)
    {
        if(res==RESULT_OK && reqCode==201)
        {
            imagePath = data.getData();

            Toast.makeText(getApplicationContext(),"IMAGE SELECTED",Toast.LENGTH_LONG).show();
        }
    }
}
