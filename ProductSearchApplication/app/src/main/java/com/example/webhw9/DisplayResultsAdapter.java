package com.example.webhw9;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.drawable.GradientDrawable;
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

import org.json.JSONObject;

public class DisplayResultsAdapter extends RecyclerView.Adapter<DisplayResultsAdapter.ViewHolder>{
    private Context con;
    //private Random r = new Random();
    private List<Map<String,String>> data;

    public DisplayResultsAdapter(Context context,List<Map<String,String>> DataSet){
        con = context;
        data = DataSet;
    }
    private void createClickEvent(ViewHolder holder,final HashMap prod){
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                RequestQueue q = Volley.newRequestQueue(con);
                String myurl="http://open.api.ebay.com/shopping?callname=GetSingleItem&responseencoding=JSON&appid=DarshanR-WebHW6-PRD-416e2f149-9d936485&siteid=0&version=967&ItemID="+prod.get("item_id").toString()+"&IncludeSelector=Description,Details,ItemSpecifics";
                try {
                    myurl= URLEncoder.encode(myurl, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                //String url ="http://10.0.2.2:8080/itemSearch?item_search_url="+myurl;
                //http://websmudgehw8-env.capwiizz34.us-east-2.elasticbeanstalk.com
                String url ="http://websmudgehw8-env.capwiizz34.us-east-2.elasticbeanstalk.com/itemSearch?item_search_url="+myurl;
                StringRequest req = new StringRequest(Request.Method.GET, url,new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                                //Toast.makeText(con,response,Toast.LENGTH_LONG).show();
                                Intent i = new Intent(con, ProductInfo.class);
                                i.putExtra("title",prod.get("title").toString());
                                i.putExtra("item_id",prod.get("item_id").toString());
                                i.putExtra("response",response.toString());
                                //i.putExtra("first_jsonObj",prod.get("first_jsonObj").toString());
                                i.putExtra("shipping_cost",prod.get("shipping_cost").toString());
                                i.putExtra("store",prod.get("store").toString());
                                i.putExtra("seller",prod.get("seller").toString());
                                i.putExtra("shipping",prod.get("shipping").toString());
                                con.startActivity(i);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //t.setText(error.toString());
                            }
                        });
                q.add(req);
            }
        });
    }

    @Override
    public DisplayResultsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        View v = LayoutInflater.from(con).inflate(R.layout.card,parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView shipping,condition,condition2,title,zip,price2,price;
        //LinearLayout mLinearLayout;
        ImageView photo;
        public ViewHolder(View v){
            super(v);
            zip = (TextView) v.findViewById(R.id.zip);
            photo = (ImageView) v.findViewById(R.id.image);
            title = (TextView) v.findViewById(R.id.title);
            price = (TextView) v.findViewById(R.id.price);
            price2 = (TextView) v.findViewById(R.id.price2);
            condition = (TextView) v.findViewById(R.id.condition);
            condition2 = (TextView) v.findViewById(R.id.condition2);
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


        holder.zip.setText("Zip: "+product.get("zip_code").toString());
        holder.title.setText(product.get("title").toString().toUpperCase());
        holder.shipping.setText(product.get("shipping_cost").toString());
        Picasso.with(con).load(product.get("photo").toString()).resize(390, 410).into(holder.photo);
        createClickEvent(holder,product);

        if (product.get("condition").toString().toLowerCase().indexOf("refurbished") == -1) {
            holder.price2.setText(product.get("price").toString());
            holder.condition2.setText(product.get("condition").toString());
            holder.condition.setVisibility(View.GONE);
            holder.condition2.setVisibility(View.VISIBLE);
            holder.price.setVisibility(View.GONE);
            holder.price2.setVisibility(View.VISIBLE);
        } else {
            holder.price.setText(product.get("price").toString());
            holder.condition.setText(product.get("condition").toString());
            holder.condition2.setVisibility(View.GONE);
            holder.condition.setVisibility(View.VISIBLE);
            holder.price2.setVisibility(View.GONE);
            holder.price.setVisibility(View.VISIBLE);
        }


    }
}
