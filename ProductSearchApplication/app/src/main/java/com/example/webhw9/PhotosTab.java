package com.example.webhw9;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class PhotosTab extends Fragment {
    View view;
    LinearLayout l;
    TextView t3;
    ProgressBar pb;
    JSONObject j;
    String tite="";
    String photos_url="";
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.photos_tab, container, false);
        l=(LinearLayout)view.findViewById(R.id.line);
        Intent i=getActivity().getIntent();
        tite=i.getStringExtra("title");
        tite=tite.replaceAll("[^a-zA-Z0-9 ]","");
        t3=(TextView)view.findViewById(R.id.textbelowpb);
        pb=(ProgressBar)view.findViewById(R.id.pb);
        pb.setVisibility(View.VISIBLE);
        t3.setVisibility(View.VISIBLE);
        photos_url="https://www.googleapis.com/customsearch/v1?q="+tite+"&cx=002967907230494438995:lpoblisp2to&imgSize=huge&imgType=news&num=8&searchType=image&key=AIzaSyAl_O1ljvOUpVQet5x_dI_BOaE0cA31fH4";
        RequestQueue q = Volley.newRequestQueue(getActivity().getApplicationContext());

        try {
            photos_url= URLEncoder.encode(photos_url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //String url ="http://10.0.2.2:8080/photos?photo_url="+photos_url;
        //http://websmudgehw8-env.capwiizz34.us-east-2.elasticbeanstalk.com
        String url ="http://websmudgehw8-env.capwiizz34.us-east-2.elasticbeanstalk.com/photos?photo_url="+photos_url;
        StringRequest req = new StringRequest(Request.Method.GET, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getActivity().getApplicationContext(),response,Toast.LENGTH_LONG).show();
                pb.setVisibility(View.GONE);
                t3.setVisibility(View.GONE);
                try {
                    JSONObject res=new JSONObject(response);
                    if(res.has("items")) {
                        JSONArray item = res.getJSONArray("items");

                        for (int j = 0; j < item.length(); j++) {
                            ImageView i = new ImageView(getActivity().getApplicationContext());
                            String pic = item.getJSONObject(j).get("link").toString();
                            Picasso.with(getActivity().getApplicationContext()).load(pic).into(i);
                            i.setAdjustViewBounds(true);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            lp.setMargins(30, 30, 30, 0);
                            i.setLayoutParams(lp);
                            l.addView(i);

                        }
                    }
                    else{
                        //Toast.makeText(getActivity().getApplicationContext(),"No photos to show", Toast.LENGTH_LONG).show();
                        TextView a=new TextView(getActivity().getApplicationContext());
                        a.setText("No Photos To Show");
                        a.setTextColor(Color.BLACK);
                        a.setTypeface(a.getTypeface(), Typeface.BOLD);
                        LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        lps.setMargins(350, 600, 0, 0);
                        //a.setGravity(Gravity.CENTER);
                        a.setLayoutParams(lps);
                        l.addView(a);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //t.setText(error.toString());
                    }
                });
        q.add(req);

        return view;
    }

}
