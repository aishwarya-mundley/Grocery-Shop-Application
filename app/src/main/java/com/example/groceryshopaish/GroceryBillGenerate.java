package com.example.groceryshopaish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class GroceryBillGenerate extends Activity
{
    TextView tvGroceryGrandTotal,tvGroceryBillDate,tvGroceryBillTime;
    String grandTotal;
    ListView lvGroceryPurchased;
    Button btGroceryPayment;
    ArrayList<String> gNameList = new ArrayList<>();
    ArrayList<String> gPriceList = new ArrayList<>();
    ArrayList<String> gQtyList = new ArrayList<>();
    ArrayList<String> gItemTotal = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_bill_generate);

        Intent ii = getIntent();
        grandTotal = ii.getStringExtra("GroceryTotal");
        gNameList = ii.getStringArrayListExtra("gname");
        gQtyList = ii.getStringArrayListExtra("gqty");
        gPriceList = ii.getStringArrayListExtra("gprice");
        gItemTotal = ii.getStringArrayListExtra("gitemtotal");

        tvGroceryGrandTotal = findViewById(R.id.tvGroceryGrandTotal);
        tvGroceryBillDate = findViewById(R.id.tvGroceryBillDate);
        tvGroceryBillTime = findViewById(R.id.tvGroceryBillTime);
        lvGroceryPurchased = findViewById(R.id.lvGroceryPurchsed);
        btGroceryPayment = findViewById(R.id.btGroceryPayment);

        tvGroceryGrandTotal.setText("Rs. "+grandTotal+"/-");

        PaymentInfo payInfo = (PaymentInfo)getApplication();
        payInfo.amount = Float.parseFloat(grandTotal);

        GroceryBillGenerateAdapter generate = new GroceryBillGenerateAdapter(GroceryBillGenerate.this,R.layout.grocerybilllist,gNameList,
                gQtyList,gPriceList,gItemTotal,tvGroceryGrandTotal);

        lvGroceryPurchased.setAdapter(generate);

        btGroceryPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(getApplicationContext(),PaymentActivity.class);
                startActivity(ii);
            }
        });
    }
}
