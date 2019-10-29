package com.example.webhw9;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

public class SimilarItemsAdapter extends RecyclerView.Adapter<SimilarItemsAdapter.ViewHolder>{
    private Context con;
    //private Random r = new Random();
    private List<Map<String,String>> data;

    public SimilarItemsAdapter(Context context,List<Map<String,String>> DataSet){
        con = context;
        data = DataSet;
    }
    private void createClickEvent(ViewHolder holder,final HashMap item){
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Uri u = Uri.parse(item.get("view_item_url").toString());
                Intent intent = new Intent(Intent.ACTION_VIEW, u);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                con.startActivity(intent);
            }
        });
    }

    @Override
    public SimilarItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        View v = LayoutInflater.from(con).inflate(R.layout.similar_cards,parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView shipping,title,price,days;
        //LinearLayout mLinearLayout;
        ImageView photo;
        public ViewHolder(View v){
            super(v);
            days = (TextView) v.findViewById(R.id.days);
            photo = (ImageView) v.findViewById(R.id.image);
            title = (TextView) v.findViewById(R.id.title);
            price = (TextView) v.findViewById(R.id.price);
            shipping = (TextView) v.findViewById(R.id.shipping);
            //mLinearLayout = (LinearLayout) v.findViewById(R.id.ll);
        }
    }
    @Override
    public int getItemCount(){
        return data.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HashMap product = new HashMap<>(data.get(position));
        holder.price.setText(product.get("price").toString());
        holder.days.setText(product.get("time_left").toString());
        //holder.condition.setText(product.get("condition").toString());
        //holder.zip.setText("Zip: "+product.get("zip_code").toString());
        holder.title.setText(product.get("title").toString().toUpperCase());
        holder.shipping.setText(product.get("shipping_cost").toString());
        Picasso.with(con).load(product.get("image_url").toString()).resize(390, 410).into(holder.photo);
        createClickEvent(holder,product);
    }
}
