package com.example.webhw9;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static java.lang.Boolean.TRUE;

public class Tab1 extends Fragment {
    CheckBox c;
    View view;
    Button search,clear;
    EditText key,zip,miles;
    TextView keyword_err,zipcode_err;
    Spinner dropdown;
    CheckBox ne,used,un,local,free;
    RadioButton current,other;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab1, container, false);

        search=(Button)view.findViewById(R.id.search);
        clear=(Button)view.findViewById(R.id.clear);

        key=(EditText) view.findViewById(R.id.keyword);
        miles=(EditText)view.findViewById(R.id.miles);
        zip=(EditText) view.findViewById(R.id.zipcode_text);
        ne=(CheckBox)view.findViewById(R.id.checkbox_new);
        used=(CheckBox)view.findViewById(R.id.checkbox_used);
        un=(CheckBox)view.findViewById(R.id.checkbox_unspecified);
        local=(CheckBox)view.findViewById(R.id.checkbox_local);
        free=(CheckBox)view.findViewById(R.id.checkbox_free);
        current=(RadioButton)view.findViewById(R.id.current);
        other=(RadioButton)view.findViewById(R.id.zipcode);
        c=(CheckBox)view.findViewById(R.id.checkbox_enable);

        //Initialize
        View v9 = view.findViewById(R.id.rel2);
        v9.setVisibility(View.GONE);
        keyword_err=(TextView)view.findViewById(R.id.keyword_error);
        zipcode_err=(TextView)view.findViewById(R.id.zipcode_error);
        keyword_err.setVisibility(View.GONE);
        zipcode_err.setVisibility(View.GONE);
        zip.setEnabled(false);

        dropdown = (Spinner) view.findViewById(R.id.category);
        String[] items = new String[]{"All","Art","Baby","Books","Clothing, Shoes & Accessories","Computer, Tablets & Networking","Health & Beauty","Music","Video Games & Consoles"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c.isChecked()== TRUE) {
                    //Toast.makeText(getActivity(), "This is my Toast message!", Toast.LENGTH_LONG).show();
                    View v9 = view.findViewById(R.id.rel2);
                    v9.setVisibility(View.VISIBLE);
                }
                else {
                    //Toast.makeText(getActivity(), "This!", Toast.LENGTH_LONG).show();
                    View v9 = view.findViewById(R.id.rel2);
                    v9.setVisibility(View.GONE);
                    miles.setText("");
                    zip.setText("");
                    current.setChecked(true);
                }
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Searching", Toast.LENGTH_SHORT).show();
                String k=key.getText().toString();
                String z=zip.getText().toString();
                int flag=0;
                if(k.trim().length()==0){
                    keyword_err.setVisibility(View.VISIBLE);
                    flag=1;
                    Toast.makeText(getActivity(), "Please fix all errors", Toast.LENGTH_SHORT).show();
                }
                if(other.isChecked()&& ((z.trim().length()>0 && z.trim().length()!=5) || !(z.matches("[0-9]+")))){
                    zipcode_err.setText("Invalid zipcode");
                    zipcode_err.setVisibility(View.VISIBLE);
                    flag=1;
                    Toast.makeText(getActivity(), "Please fix all errors", Toast.LENGTH_SHORT).show();
                }
                if(other.isChecked()&& z.trim().length()==0){
                    zipcode_err.setText("Please enter mandatory field");
                    zipcode_err.setVisibility(View.VISIBLE);
                    flag=1;
                    Toast.makeText(getActivity(), "Please fix all errors", Toast.LENGTH_SHORT).show();
                }
                if (flag==0){
                    keyword_err.setVisibility(View.GONE);
                    zipcode_err.setVisibility(View.GONE);
                    //Toast.makeText(getActivity(), "Fetching Data", Toast.LENGTH_SHORT).show();
                    //getting form data
                    int cat;
                    cat=dropdown.getSelectedItemPosition();

                    String url="http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0&SECURITY-APPNAME=DarshanR-WebHW6-PRD-416e2f149-9d936485&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD=&paginationInput.entriesPerPage=50&";

                    url+="keywords="+k;

                    int category_id=0;
                    switch (cat){
                        case 0:
                            break;
                        case 1:
                            category_id=550;
                            break;
                        case 2:
                            category_id=2984;
                            break;
                        case 3:
                            category_id=267;
                            break;
                        case 4:
                            category_id=11450;
                            break;
                        case 5:
                            category_id=58058;
                            break;
                        case 6:
                            category_id=26395;
                            break;
                        case 7:
                            category_id=11233;
                            break;
                        case 8:
                            category_id=1249;
                            break;

                    }

                    if (cat!=0){url+="&categoryID="+category_id;}

                    if(c.isChecked()) {
                        if (current.isChecked()) {
                            url = url + "&buyerPostalCode=" + 90007;
                        } else {
                            url = url + "&buyerPostalCode=" + z;
                        }
                    }

                    int i=0;
                    if((ne.isChecked()==true && used.isChecked()==true && un.isChecked()==true)||(ne.isChecked()==false && used.isChecked()==false && un.isChecked()==false)){
                        String temp="&itemFilter("+i+").name=Condition&itemFilter("+i+").value(0)=New&itemFilter("+i+").value(1)=Used&itemFilter("+i+").value(2)=Unspecified";
                        url=url+temp;
                        i++;
                    }
                    else{
                        String temp="&itemFilter("+i+").name=Condition";
                        int j=0;
                        if(ne.isChecked()==true){
                            temp=temp+"&itemFilter("+i+").value("+j+")=New";
                            j++;}
                        if(used.isChecked() ==true){
                            temp=temp+"&itemFilter("+i+").value("+j+")=Used";
                            j++;}
                        if(un.isChecked() ==true){
                            temp=temp+"&itemFilter("+i+").value("+j+")=Unspecified";
                            j++;}
                        i++;
                        url=url+temp;
                    }

                    if((local.isChecked()==true && free.isChecked()==true)|| (local.isChecked()==false && free.isChecked()==false)){
                        String t="&itemFilter("+i+").name=FreeShippingOnly&itemFilter("+i+").value=true";
                        i++;
                        t = t+"&itemFilter("+i+").name=LocalPickupOnly&itemFilter("+i+").value=true";
                        i++;
                        url = url+t;
                    }
                    else{
                        if(free.isChecked()==true){
                            String t="&itemFilter("+i+").name=FreeShippingOnly&itemFilter("+i+").value=true";
                            url=url+t;
                            i++;}
                        if (local.isChecked() == true) {
                            String t="&itemFilter("+i+").name=LocalPickupOnly&itemFilter("+i+").value=true";
                            url=url+t;
                            i++;}
                    }

                    if(c.isChecked()) {
                        if (miles.getText().toString().trim().matches("")) {
                            //Toast.makeText(getActivity(),"hello!", Toast.LENGTH_LONG).show();
                            url = url + "&itemFilter(" + i + ").name=MaxDistance&itemFilter(" + i + ").value=10";
                            i++;
                        } else {
                            url = url + "&itemFilter(" + i + ").name=MaxDistance&itemFilter(" + i + ").value=" + miles.getText().toString();
                            i++;
                        }
                    }
                    url=url+"&itemFilter("+i+").name=HideDuplicateItems&itemFilter("+i+").value=true"+"&outputSelector(0)=SellerInfo&outputSelector(1)=StoreInfo";
                    try {
                        url= URLEncoder.encode(url, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String URL ="http://websmudgehw8-env.capwiizz34.us-east-2.elasticbeanstalk.com/some_name?got_url="+url;
                    Intent into=new Intent(getActivity().getApplicationContext(),DisplayResults.class);
                    into.putExtra("link",URL);
                    into.putExtra("key",key.getText().toString());
                    startActivity(into);

                    //Toast.makeText(getActivity(), url, Toast.LENGTH_SHORT).show();

                 /*   RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                    //String URL ="http://10.0.2.2:8080/some_name?got_url="+url;
                    String URL ="http://websmudgehw8-env.capwiizz34.us-east-2.elasticbeanstalk.com/some_name?got_url="+url;

// Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Display the first 500 characters of the response string.
                                    //Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                                    Intent i=new Intent(getActivity().getApplicationContext(),DisplayResults.class);
                                    i.putExtra("jsonObj",response);
                                    i.putExtra("key",key.getText().toString());
                                    startActivity(i);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    });

// Add the request to the RequestQueue.
                    queue.add(stringRequest);*/
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Clear", Toast.LENGTH_SHORT).show();
                int s=dropdown.getSelectedItemPosition();
                //Toast.makeText(getActivity(), Integer.toString(s), Toast.LENGTH_SHORT).show();
                key.setText(null);
                miles.setText(null);
                zip.setText(null);
                keyword_err.setVisibility(View.GONE);
                zipcode_err.setVisibility(View.GONE);
                c.setChecked(false);
                View v9 = view.findViewById(R.id.rel2);
                v9.setVisibility(View.GONE);
                current.setChecked(true);
                zip.setEnabled(false);
                dropdown.setSelection(0);

            }
        });

        current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zip.setEnabled(false);
                zipcode_err.setVisibility(View.GONE);
            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zip.setEnabled(true);
            }
        });

        key.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                keyword_err.setVisibility(View.GONE);
                //Toast.makeText(getActivity(), "here", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        zip.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                zipcode_err.setVisibility(View.GONE);
                return false;
            }
        });

        return view;

    }

}
