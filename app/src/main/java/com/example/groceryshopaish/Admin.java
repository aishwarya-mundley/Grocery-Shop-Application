package com.example.groceryshopaish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Admin extends Activity {

    Button show,add,del,upd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        show = findViewById(R.id.show);
        add = findViewById(R.id.add);
        del = findViewById(R.id.del);
        upd = findViewById(R.id.upd);

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ia1 = new Intent(getApplicationContext(),ShowUser.class);
                startActivity(ia1);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ia2 = new Intent(getApplicationContext(),AddGrocery.class);
                startActivity(ia2);
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ia3 = new Intent(getApplicationContext(),DeleteGrocery.class);
                startActivity(ia3);
            }
        });

        upd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ia4 = new Intent(getApplicationContext(),UpdateGrocery.class);
                startActivity(ia4);
            }
        });
    }
}
