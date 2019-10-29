package com.example.webhw9;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimilarTab extends Fragment {
    View view;
    private RecyclerView.LayoutManager myLayoutManager;
    private RecyclerView rv;
    private RecyclerView.Adapter adap;
    List<Map<String,String>> items ;
    Spinner dropdown1,dropdown2;

    public void additems(){
        Intent i=getActivity().getIntent();
        String item_id=i.getStringExtra("item_id");
        String similar_url="http://svcs.ebay.com/MerchandisingService?OPERATION-NAME=getSimilarItems&SERVICE-NAME=MerchandisingService&SERVICE-VERSION=1.1.0&CONSUMER-ID=DarshanR-WebHW6-PRD-416e2f149-9d936485&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD&itemId="+item_id+"&maxResults=20";
        try {
            similar_url= URLEncoder.encode(similar_url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String name="http://websmudgehw8-env.capwiizz34.us-east-2.elasticbeanstalk.com/similar_items?sim_url="+similar_url;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, name,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                        /*Intent i=new Intent(getActivity().getApplicationContext(),DisplayResults.class);
                        i.putExtra("jsonObj",response);
                        i.putExtra("key",key.getText().toString());
                        startActivity(i);*/
                        try {
                            TextView t=(TextView)view.findViewById(R.id.textView8);
                            t.setVisibility(View.GONE);
                            JSONObject obj = new JSONObject(response);

                            if(!obj.getJSONObject("getSimilarItemsResponse").getJSONObject("itemRecommendations").has("item")){
                                t.setVisibility(View.VISIBLE);
                                dropdown1.setEnabled(false);
                                dropdown2.setEnabled(false);
                                return;
                            }
                            JSONArray all=obj.getJSONObject("getSimilarItemsResponse").getJSONObject("itemRecommendations").getJSONArray("item");
                            if (all.length()==0){
                                t.setVisibility(View.VISIBLE);
                                dropdown1.setEnabled(false);
                                dropdown2.setEnabled(false);
                                return;
                            }
                            for (int i = 0; i < all.length(); i++) {
                                JSONObject item = (JSONObject) all.get(i);
                                HashMap<String, String> item_info = new HashMap<>();
                                if(item.has("imageURL")){
                                    item_info.put("image_url",item.get("imageURL").toString());
                                }
                                else{item_info.put("image_url","N/A");}
                                if(item.has("title")){
                                    item_info.put("title",item.get("title").toString());
                                }
                                else{item_info.put("title","N/A");}
                                if(item.has("shippingCost")){
                                    String a="";
                                    String s=item.getJSONObject("shippingCost").get("__value__").toString();
                                    if(s.equals("0.00")){a="Free Shipping";}
                                    else{a = "$ "+s;}
                                    item_info.put("shipping_cost",a);
                                }
                                else{item_info.put("shipping_cost","N/A");}
                                if(item.has("timeLeft")){
                                    String s=(item.get("timeLeft").toString());
                                    s=s.substring(1,s.indexOf("D"));
                                    if(s.equals("0") || s.equals("1")){s=s+" Day Left";}
                                    else{s=s+" Days Left";}
                                    item_info.put("time_left",s);
                                }
                                else{item_info.put("time_left","N/A");}
                                if(item.has("buyItNowPrice")){
                                    item_info.put("price","$ "+ item.getJSONObject("buyItNowPrice").get("__value__").toString());
                                }
                                else{item_info.put("time_left","N/A");}
                                if(item.has("viewItemURL")){
                                    item_info.put("view_item_url",item.get("viewItemURL").toString());
                                }
                                else{item_info.put("view_item_url","N/A");}

                                items.add(item_info);


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                       // TextView t = (TextView) view.findViewById(R.id.textView8) ;
                        //t.setText(items.toString());
                       // Toast.makeText(getActivity().getApplicationContext(),items,Toast.LENGTH_LONG).show();
                        adap.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.similar_tab, container, false);
        items = new ArrayList<>();
        additems();

        dropdown1 = (Spinner) view.findViewById(R.id.spin1);
        String[] items2 = new String[]{"Default","Name","Price","Days"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, items2);
        dropdown1.setAdapter(adapter);

        dropdown2 = (Spinner) view.findViewById(R.id.spin2);
        String[] items3 = new String[]{"Ascending","Descending"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, items3);
        dropdown2.setAdapter(adapter2);

        dropdown1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spin1 = dropdown1.getSelectedItem().toString();
                if(!spin1.equals("Default")){
                    dropdown2.setEnabled(true);
                }
                else{
                    dropdown2.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        rv=(RecyclerView)view.findViewById(R.id.my_recycler_views);
        myLayoutManager=new GridLayoutManager(getActivity().getApplicationContext(),1);
        rv.setLayoutManager(myLayoutManager);

        adap=new SimilarItemsAdapter(getActivity().getApplicationContext(),items);

        rv.setAdapter(adap);



        //additems();
        return view;
    }

}
