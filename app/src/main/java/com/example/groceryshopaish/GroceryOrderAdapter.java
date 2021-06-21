package com.example.groceryshopaish;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import java.util.ArrayList;

public class GroceryOrderAdapter extends RecyclerView.Adapter<GroceryOrderAdapter.MyViewHolder>
{
    Context cont;
    ArrayList<Grocery> dataSet;
    TextView tvGroceryOrderBill;
    Button btGroceryOrderConfirm;

    int total[];
    int grandTotal = 0;
    int checkIndex[];
    int totalNoOfGroceryBought = 0;

    ArrayList<String> gNameList = new ArrayList<String>();
    ArrayList<String> gQtyList = new ArrayList<String>();
    ArrayList<String> gPriceList = new ArrayList<String>();
    ArrayList<String> gTotalList = new ArrayList<String>();

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView groceryImage;
        TextView groceryName,groceryPrice;
        ElegantNumberButton groceryButton;

        MyViewHolder(View itemView)
        {
            super(itemView);

            groceryImage = itemView.findViewById(R.id.cardGroceryOrderImage);
            groceryName = itemView.findViewById(R.id.tvCardGroceryOrderName);
            groceryPrice = itemView.findViewById(R.id.tvCardGroceryOrderPrice);
            groceryButton = itemView.findViewById(R.id.elegantNumGroceryOrder);

        }
    }

    GroceryOrderAdapter(Context cont, ArrayList<Grocery> list, TextView tvGroceryOrderBill, Button btGroceryOrderConfirm)
    {
        this.cont = cont;
        this.dataSet = list;
        this.tvGroceryOrderBill = tvGroceryOrderBill;
        this.btGroceryOrderConfirm = btGroceryOrderConfirm;

        checkIndex = new int[dataSet.size()];
        total = new int[dataSet.size()];

        for(int i=0;i<checkIndex.length;i++)
        {
            checkIndex[i]=-1;
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_grocery_order_cardview,parent,false);
        // it is used to connect any xml file with non-activity class in android

        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        // this method will be called no of times matches with the no getItemCount() returns
        // everytime it is called it creates following views for each component

        ImageView groceryImage = holder.groceryImage;
        TextView groceryName = holder.groceryName;
        TextView groceryPrice = holder.groceryPrice;
        ElegantNumberButton groceryButton = holder.groceryButton;

        groceryName.setText(dataSet.get(position).getGname());
        groceryPrice.setText("Rs. "+dataSet.get(position).getPrice());

        final int i = position;

        Glide.with(cont)
                .load(dataSet.get(position).getImageUri())
                .override(800,400)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(groceryImage);

        groceryButton.setOnClickListener(new ElegantNumberButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(cont,"NAME : "+dataSet.get(i).getGname(),Toast.LENGTH_LONG).show();              //here context of GroceryOrder is taken, otherwise getApplicationContext can't be taken for non-Activity class

                int qty = Integer.parseInt(groceryButton.getNumber());

                if(qty==0)  // it will be executed when user shift stock to 0 after buying grocery
                {
                    grandTotal = 0;
                    for (int i=0;i<checkIndex.length;i++)
                    {
                        if(checkIndex[i]==position)
                        {
                            for(int j=i;j<checkIndex.length-1;j++)
                            {
                                checkIndex[j] = checkIndex[j+1];
                                total[j] = total[j+1];
                            }

                            if(position == checkIndex.length-1)
                            {
                                checkIndex[position] = -1;
                                total[position] = 0;
                                grandTotal = 0;
                            }

                            totalNoOfGroceryBought--;
                            gNameList.remove(i);
                            gPriceList.remove(i);
                            gQtyList.remove(i);
                            gTotalList.remove(i);
                            break;
                        }
                    }
                }
                else
                {
                    // if user increase or decrease the qty of grocery
                    int flag = 0, exists = 0;
                    grandTotal =0;

                    for(exists=0;exists<checkIndex.length;exists++)        // loop is used to find grocery in checkIndex
                    {
                        if(checkIndex[exists]==position)       // we found grocery in checkIndex ==> client has bought this grocery
                        {
                            flag = 1;
                            break;
                        }
                    }

                    if(flag==0)      // user has not bought before, he is buying for first time
                    {
                        checkIndex[totalNoOfGroceryBought] = position;
                        total[totalNoOfGroceryBought++] = dataSet.get(position).getPrice()*Integer.parseInt(groceryButton.getNumber());
                        gNameList.add(dataSet.get(position).getGname());
                        gPriceList.add(""+dataSet.get(position).getPrice());
                        gQtyList.add(""+dataSet.get(position).getStock());
                        gTotalList.add(""+(dataSet.get(position).getPrice() * Integer.parseInt(groceryButton.getNumber())));
                    }
                    else            // programming for grocery already bought with quantity
                    {
                        grandTotal = 0;
                        gQtyList.set(exists,""+groceryButton.getNumber());
                        gTotalList.set(exists,""+(dataSet.get(position).getPrice() * Integer.parseInt(groceryButton.getNumber())));
                        total[exists] = (dataSet.get(position).getPrice() * Integer.parseInt(groceryButton.getNumber()));
                    }

                    String str = "";
                    for(int r=0;r<gNameList.size();r++)
                    {
                        str = str + gNameList.get(r)+" , "+gQtyList.get(r)+" , "+gPriceList.get(r)+"\n";
                    }

                    //Toast.makeText(cont,str,Toast.LENGTH_LONG).show();

                }
                for (int r=0;r<gNameList.size();r++)
                {
                    grandTotal = grandTotal + total[r];
                }
                tvGroceryOrderBill.setText("Rs. "+grandTotal);
            }
        });

        btGroceryOrderConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(cont,GroceryBillGenerate.class);

                ii.putExtra("GroceryTotal",""+grandTotal);
                ii.putStringArrayListExtra("gname",gNameList);
                ii.putStringArrayListExtra("gqty",gQtyList);
                ii.putStringArrayListExtra("gprice",gPriceList);
                ii.putStringArrayListExtra("gitemtotal",gTotalList);

                cont.startActivity(ii);
            }
        });
    }

    @Override
    public int getItemCount()   // it decides how many object of grocery should be displayed on recyclerView
    {
        return dataSet.size();   // according to this, it generates rows for recyclerView
    }

}
