package com.example.webhw9;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisplayResults extends AppCompatActivity {

    TextView t,t2,t3;
    ProgressBar pb;
    private RecyclerView.LayoutManager myLayoutManager;
    private RecyclerView rv;
    private RecyclerView.Adapter adap;

    List<Map<String,String>> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_results);

        t=(TextView)findViewById(R.id.textView8);
        t2=(TextView)findViewById(R.id.textView9);
        t3=(TextView)findViewById(R.id.textbelowpb);
        pb=(ProgressBar)findViewById(R.id.pb);
        String jsonObj=getIntent().getStringExtra("jsonObj");


        getSupportActionBar().setTitle("Search Results");
        items = new ArrayList<>();
        t2.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);
        t3.setVisibility(View.VISIBLE);
        rv=(RecyclerView)findViewById(R.id.my_recycler_view);
        myLayoutManager=new GridLayoutManager(this,2);
        rv.setLayoutManager(myLayoutManager);
        rv.setVisibility(View.VISIBLE);

        adap=new DisplayResultsAdapter(this,items);

        rv.setAdapter(adap);
        fetchitems();
    }

    public void fetchitems(){
        final String keyword=getIntent().getStringExtra("key");
        String url = getIntent().getStringExtra("link");
        items.clear();

        RequestQueue queue = Volley.newRequestQueue(this);
        //String URL ="http://10.0.2.2:8080/some_name?got_url="+url;
        //String URL ="http://websmudgehw8-env.capwiizz34.us-east-2.elasticbeanstalk.com/some_name?got_url="+url;

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {

                        try {
                            JSONObject obj = new JSONObject(json);

                            JSONObject items_array = (JSONObject) obj.getJSONArray("findItemsAdvancedResponse").get(0);
                            JSONArray allitems = items_array.getJSONArray("searchResult");

                            if(!allitems.getJSONObject(0).has("item") || Integer.parseInt(allitems.getJSONObject(0).get("@count").toString())==0){
                                t.setVisibility(View.GONE);
                                t2.setVisibility(View.VISIBLE);
                                RecyclerView rv=(RecyclerView)findViewById(R.id.my_recycler_view);
                                rv.setVisibility(View.GONE);
                                pb.setVisibility(View.GONE);
                                t3.setVisibility(View.GONE);
                                return;
                            }

                            allitems = ((JSONObject) allitems.get(0)).getJSONArray("item");
                            //Toast.makeText(getApplicationContext(),jsonObj,Toast.LENGTH_LONG);
                            t2.setVisibility(View.GONE);
                            t.setVisibility(View.VISIBLE);

                            int n;
                            n = allitems.length();

                            t.setText(Html.fromHtml("Showing <font color='#FF4500'>" + n + "</font> results for <font color='#EF6C00'>" + keyword + "</font>"));
                            for (int i = 0; i < allitems.length(); i++) {
                                JSONObject item = (JSONObject) allitems.get(i);
                                HashMap<String, String> item_info = new HashMap<>();

                                if(item.has("condition")) {
                                    //if (((JSONObject) item.getJSONArray("condition").get(0)).getJSONArray("conditionDisplayName").get(0).toString().toLowerCase().indexOf("refurbished") == -1) {
                                    item_info.put("condition", ((JSONObject) item.getJSONArray("condition").get(0)).getJSONArray("conditionDisplayName").get(0).toString());
                                    //} else {
                                    //  item_info.put("condition", "Refurbished");
                                    //}
                                }
                                else{
                                    item_info.put("condition", "N/A");
                                }
                                if(item.has("sellingStatus")){
                                    String price = ((JSONObject) ((JSONObject) item.getJSONArray("sellingStatus").get(0)).getJSONArray("currentPrice").get(0)).get("__value__").toString();
                                    item_info.put("price", "$" + price);}
                                else{item_info.put("price", "N/A");}

                                if(item.has("shippingInfo")){
                                    JSONArray ship = ((JSONObject) item.getJSONArray("shippingInfo").get(0)).getJSONArray("shippingServiceCost");
                                    String shipping_cost = "";
                                    if (((JSONObject) ship.get(0)).getInt("__value__") == 0) {
                                        shipping_cost = "Free Shipping";
                                    } else {
                                        shipping_cost = "$" + ((JSONObject) ship.get(0)).get("__value__").toString();
                                    }
                                    item_info.put("shipping_cost", shipping_cost);}
                                else{item_info.put("shipping_cost", "N/A");}

                                if(item.has("title")){
                                    item_info.put("title", item.getJSONArray("title").get(0).toString());}
                                else{item_info.put("title", "N/A");}
                                item_info.put("item_id", item.getJSONArray("itemId").get(0).toString());
                                if(item.has("postalCode")){
                                    item_info.put("zip_code", item.getJSONArray("postalCode").get(0).toString());}
                                else{item_info.put("zip_code","N/A");}
                                if(item.has("galleryURL")){
                                    item_info.put("photo", item.getJSONArray("galleryURL").get(0).toString());}
                                else{item_info.put("photo","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSth72UGJ91K5OMpqlzsqSYvz9ElHMLozBbi2TjsoeuQ6mJB8b7");}
                                if(item.has("storeInfo")){
                                    item_info.put("store",item.getJSONArray("storeInfo").get(0).toString());
                                }
                                else{
                                    item_info.put("store","{}");
                                }
                                if(item.has("sellerInfo")){
                                    item_info.put("seller",item.getJSONArray("sellerInfo").get(0).toString());
                                }
                                else{
                                    item_info.put("seller","{}");
                                }
                                if(item.has("shippingInfo")) {
                                    item_info.put("shipping", item.getJSONArray("shippingInfo").get(0).toString());
                                }
                                else{
                                    item_info.put("shipping", "{}");
                                }

                                items.add(item_info);
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        pb.setVisibility(View.GONE);
                        t3.setVisibility(View.GONE);
                        adap.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);




    }
}