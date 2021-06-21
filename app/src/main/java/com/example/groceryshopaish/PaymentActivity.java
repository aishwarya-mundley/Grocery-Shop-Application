package com.example.groceryshopaish;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class PaymentActivity extends Activity implements PaymentResultListener {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Checkout check = new Checkout();

        check.setKeyID("rzp_test_pDw5odW27q6rQ9");

        JSONObject json = new JSONObject();

        PaymentInfo payInfo = (PaymentInfo)getApplication();

        try
        {
            json.put("Name",payInfo.name);
            json.put("description","THANK YOU FOR PURCHASING");
            json.put("theme.color","#0093DD");
            json.put("currency","INR");
            json.put("Amount",payInfo.amount*100);
            json.put("prefill.contact",payInfo.mobile);
            json.put("prefill.email",payInfo.mail);

            check.open(this,json);

        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"PAYMENT ERROR : "+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(getApplicationContext(),"PAYMENT SUCCESSFUL : "+s,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(),"PAYMENT FAIL : "+s,Toast.LENGTH_LONG).show();
        finish();
    }
}
