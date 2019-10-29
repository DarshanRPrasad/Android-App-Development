package com.example.webhw9;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class ProductTab extends Fragment {
    View view;
    JSONArray listimg;
    private LinearLayout mGallery;
    private LayoutInflater mInflater;
    ImageView i1,i2;
    private HorizontalScrollView horizontalScrollView;
    TextView title,price,shipping,high,spec;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Drawable d1,d2;
        view = inflater.inflate(R.layout.product_tab, container, false);
        Intent intent = getActivity().getIntent();
        String resp=intent.getStringExtra("response");

        mInflater = LayoutInflater.from(getActivity().getApplicationContext());
        //Toast.makeText(getApplicationContext(),resp,Toast.LENGTH_LONG).show();
        i1=(ImageView)view.findViewById(R.id.img);
        i2=(ImageView)view.findViewById(R.id.img2);
        i1.setImageResource(R.drawable.information);
        i2.setImageResource(R.drawable.wrench);
        title=(TextView)view.findViewById(R.id.text_title);
        price=(TextView)view.findViewById(R.id.text_price);
        shipping=(TextView)view.findViewById(R.id.text_shipping);
        //title.setText(resp);



        try {
            JSONObject res= new JSONObject(resp);
            JSONObject product=(JSONObject)res.get("Item");
            listimg=product.getJSONArray("PictureURL");
            String pri=intent.getStringExtra("shipping_cost");
            String tit=intent.getStringExtra("title");
            title.setText(tit);
            if(pri.toLowerCase().indexOf("shipping")!=-1){
                shipping.setText("With "+pri);
            }
            else{
                shipping.setText("With $"+pri+" Shipping");
            }
            String p="$"+product.getJSONObject("CurrentPrice").get("Value").toString();
            price.setText(p);
            if(product.has("Subtitle")){
                LinearLayout l=(LinearLayout)view.findViewById(R.id.one);
                l.setVisibility(View.VISIBLE);
                TextView t1=new TextView(getActivity().getApplicationContext());
                t1.setText("Subtitle");
                t1.setTextColor(Color.BLACK);
                t1.setWidth(200);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(30, 0, 0, 0);
                t1.setLayoutParams(lp);
                LinearLayout l1=(LinearLayout)view.findViewById(R.id.one);
                l1.addView(t1);
                TextView t=new TextView(getActivity().getApplicationContext());
                t.setText(product.get("Subtitle").toString());
                l1.addView(t);
            }
            else{
                LinearLayout l=(LinearLayout)view.findViewById(R.id.one);
                l.setVisibility(View.GONE);
            }
            JSONArray itemspecifics=product.getJSONObject("ItemSpecifics").getJSONArray("NameValueList");
            int i;

            TextView t4=new TextView(getActivity().getApplicationContext());
            t4.setText("Price");
            t4.setTextColor(Color.BLACK);
            t4.setWidth(200);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(30, 0, 0, 0);
            t4.setLayoutParams(lp);
            LinearLayout l3=(LinearLayout)view.findViewById(R.id.two);
            l3.addView(t4);
            TextView t5=new TextView(getActivity().getApplicationContext());
            t5.setText(p);
            l3.addView(t5);

            for (i=0;i<itemspecifics.length();i++){
                if(itemspecifics.getJSONObject(i).get("Name").toString().equals("Brand")){
                    //Toast.makeText(getActivity().getApplicationContext(),"hi",Toast.LENGTH_LONG).show();
                    TextView t2=new TextView(getActivity().getApplicationContext());
                    t2.setText("Brand");
                    t2.setTextColor(Color.BLACK);
                    t2.setWidth(200);
                    LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp1.setMargins(30, 0, 0, 0);
                    t2.setLayoutParams(lp1);
                    LinearLayout l2=(LinearLayout)view.findViewById(R.id.three);
                    l2.addView(t2);
                    TextView t3=new TextView(getActivity().getApplicationContext());
                    t3.setText(itemspecifics.getJSONObject(i).getJSONArray("Value").get(0).toString());
                    //t3.setLayoutParams(lp1);
                    l2.addView(t3);
                    TextView t6=new TextView(getActivity().getApplicationContext());
                    String value = itemspecifics.getJSONObject(i).getJSONArray("Value").get(0).toString();
                    t6.setText("• "+value.substring(0,1).toUpperCase() + value.substring(1));
                    t6.setLayoutParams(lp1);
                    LinearLayout l4=(LinearLayout)view.findViewById(R.id.spe);
                    l4.addView(t6);
                }
            }

            for (i=0;i<itemspecifics.length();i++){
                TextView t7=new TextView(getActivity().getApplicationContext());
                String val = itemspecifics.getJSONObject(i).getJSONArray("Value").get(0).toString();
                t7.setText("• "+val.substring(0,1).toUpperCase() + val.substring(1));
                t7.setLayoutParams(lp);
                LinearLayout l5=(LinearLayout)view.findViewById(R.id.spe);
                l5.addView(t7);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        showphotos();


        return view;
    }
    private void showphotos()
    {
        mGallery = (LinearLayout) view.findViewById(R.id.id_gallery);

        for (int i = 0; i < listimg.length(); i++)
        {

            View newview = mInflater.inflate(R.layout.product_tab_images, mGallery, false);
            ImageView img = (ImageView) newview.findViewById(R.id.id_index_gallery_item_image);
            try {
                Picasso.with(getActivity().getApplicationContext()).load(listimg.get(i).toString()).resize(390, 410).into(img);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mGallery.addView(newview);
        }
    }
}