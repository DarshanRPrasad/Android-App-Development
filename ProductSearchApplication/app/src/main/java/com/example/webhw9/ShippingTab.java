package com.example.webhw9;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wssholmes.stark.circular_score.CircularScoreView;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShippingTab extends Fragment {
    View view;
    ImageView i1,i2,i3;
    String shipping_cost;
    LinearLayout l1,l2,l3,l4,l5,l6,l7,l8,l9,l10,l11,l12;
    JSONObject response,item,rp;
    TextView t1,t2,t3,t4,t5,t6;
    JSONObject store_obj,seller_obj,ship_obj;
    LinearLayout soldby,soldbydetails,ship,shipdetails,returnpolicy,returnpolicydetails;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.shipping_tab, container, false);
        i1=(ImageView)view.findViewById(R.id.img);
        i2=(ImageView)view.findViewById(R.id.img2);
        i3=(ImageView)view.findViewById(R.id.img3);
        i1.setImageResource(R.drawable.truck2);
        i2.setImageResource(R.drawable.ferry);
        i3.setImageResource(R.drawable.dump_truck);
        l1=(LinearLayout)view.findViewById(R.id.one);
        l2=(LinearLayout)view.findViewById(R.id.two);
        l3=(LinearLayout)view.findViewById(R.id.three);
        l4=(LinearLayout)view.findViewById(R.id.four);
        l5=(LinearLayout)view.findViewById(R.id.five);
        l6=(LinearLayout)view.findViewById(R.id.six);
        l7=(LinearLayout)view.findViewById(R.id.seven);
        l8=(LinearLayout)view.findViewById(R.id.eight);
        l9=(LinearLayout)view.findViewById(R.id.nine);
        l10=(LinearLayout)view.findViewById(R.id.ten);
        l11=(LinearLayout)view.findViewById(R.id.eleven);
        l12=(LinearLayout)view.findViewById(R.id.twelve);
        soldby=(LinearLayout)view.findViewById(R.id.sold_by_ll);
        soldbydetails=(LinearLayout)view.findViewById(R.id.sold_by_details);
        ship=(LinearLayout)view.findViewById(R.id.shipping_info_ll);
        shipdetails=(LinearLayout)view.findViewById(R.id.shipping_info_details);
        returnpolicy=(LinearLayout)view.findViewById(R.id.return_policy_ll);
        returnpolicydetails=(LinearLayout)view.findViewById(R.id.return_policy_details);

        Intent i=getActivity().getIntent();
        shipping_cost=i.getStringExtra("shipping_cost");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(50, 0,0,0);
        LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lps.setMargins(20, 0,0,0);



        try {
            store_obj=new JSONObject(i.getStringExtra("store"));
            ship_obj=new JSONObject(i.getStringExtra("shipping"));
            seller_obj=new JSONObject(i.getStringExtra("seller"));
            response=new JSONObject(i.getStringExtra("response"));
            item=response.getJSONObject("Item");
            rp=item.getJSONObject("ReturnPolicy");



        if (store_obj.has("storeURL")||(seller_obj.has("feedbackScore"))||(seller_obj.has("positiveFeedbackPercent"))||(seller_obj.has("feedbackRatingStar"))){
            soldby.setVisibility(View.VISIBLE);
            soldbydetails.setVisibility(View.VISIBLE);
            if(store_obj.has("storeURL")){
                l1.setVisibility(View.VISIBLE);
                t1=(TextView)view.findViewById(R.id.t1);
                t1.setText("Store Name");
                t1.setTextColor(Color.BLACK);
                t2=(TextView)view.findViewById(R.id.t2);
                //String anchor="";
                //anchor="<a href='"+store_obj.getJSONArray("storeURL").get(0).toString()+"'>"+store_obj.getJSONArray("storeName").get(0).toString()+"</a>";
                t2.setText(Html.fromHtml("<u>"+store_obj.getJSONArray("storeName").get(0).toString()+"</u>"));
                t2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri open = null;
                        try {
                            open = Uri.parse(store_obj.getJSONArray("storeURL").get(0).toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent j = new Intent(Intent.ACTION_VIEW, open);
                        j.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(j);
                    }
                });
            }
            else{
                l1.setVisibility(View.GONE);
            }
            if(seller_obj.has("feedbackScore")){
                l2.setVisibility(View.VISIBLE);
                t3=(TextView)view.findViewById(R.id.t3);
                t3.setText("Feedback Score");
                t3.setTextColor(Color.BLACK);
                //t1.setWidth(300);
                //t3.setLayoutParams(lp);
                //l2.addView(t1);
                t4=(TextView)view.findViewById(R.id.t4);
                t4.setText(seller_obj.getJSONArray("feedbackScore").get(0).toString());
                //t4.setLayoutParams(lps);
                //l2.addView(t2);
            }
            else{
                l2.setVisibility(View.GONE);
            }
            if(seller_obj.has("positiveFeedbackPercent")){
                l3.setVisibility(View.VISIBLE);
                t5=(TextView)view.findViewById(R.id.t5);
                t5.setText("Popularity");
                t5.setTextColor(Color.BLACK);
                CircularScoreView circularScoreView = (CircularScoreView) view.findViewById(R.id.circle);
                float num=Float.parseFloat(seller_obj.getJSONArray("positiveFeedbackPercent").get(0).toString());
                circularScoreView.setScore(Math.round(num));

            }
            else{
                l3.setVisibility(View.GONE);
            }
            if(seller_obj.has("feedbackRatingStar")){
                l4.setVisibility(View.VISIBLE);
                TextView t1=(TextView)view.findViewById(R.id.t7);
                t1.setText("Feedback Star");
                t1.setTextColor(Color.BLACK);
                ImageView imav=(ImageView)view.findViewById(R.id.star);
                //imav.setImageResource(R.drawable.star);
                String col = seller_obj.getJSONArray("feedbackRatingStar").get(0).toString();
                Drawable d;
                if(col.toLowerCase().equals("silver")){
                    col="#C0C0C0";
                }
                if(col.toLowerCase().equals("purple")){
                    col="#800080";
                }
                if(col.toLowerCase().equals("turquoise")){
                    col="#40e0d0";
                }
                if(col.toLowerCase().equals("none")){
                    col="#d3d3d3";
                }
                if(col.toLowerCase().indexOf("shooting")==-1){
                    d = MaterialDrawableBuilder.with(getActivity()).setIcon(MaterialDrawableBuilder.IconValue.STAR_OUTLINE).setColor(Color.parseColor(col)).setToActionbarSize().build();
                }

                else{
                    col = col.substring(0,col.toLowerCase().indexOf("shooting"));
                    d = MaterialDrawableBuilder.with(getActivity()).setIcon(MaterialDrawableBuilder.IconValue.STAR_CIRCLE).setColor(Color.parseColor(col)).setToActionbarSize().build();
                }
                imav.setImageDrawable(d);
            }
            else{
                l4.setVisibility(View.GONE);
            }
        }
        else{
            soldby.setVisibility(View.GONE);
            soldbydetails.setVisibility(View.GONE);
        }

        if (shipping_cost!=null||item.has("GlobalShipping")||item.has("HandlingTime")||item.has("ConditionDescription")){
            ship.setVisibility(View.VISIBLE);
            shipdetails.setVisibility(View.VISIBLE);
            if(shipping_cost!=null){
                l5.setVisibility(View.VISIBLE);
                TextView t1=new TextView(getActivity().getApplicationContext());
                t1.setText("Shipping Cost");
                t1.setTextColor(Color.BLACK);
                t1.setWidth(300);
                t1.setLayoutParams(lp);
                l5.addView(t1);
                TextView t2=new TextView(getActivity().getApplicationContext());
                t2.setText(shipping_cost);
                t2.setLayoutParams(lps);
                l5.addView(t2);
            }
            else{
                l5.setVisibility(View.GONE);
            }
            if(item.has("GlobalShipping")){
                l6.setVisibility(View.VISIBLE);
                TextView t1=new TextView(getActivity().getApplicationContext());
                t1.setText("Global Shipping");
                t1.setTextColor(Color.BLACK);
                t1.setWidth(300);
                t1.setLayoutParams(lp);
                l6.addView(t1);
                TextView t2=new TextView(getActivity().getApplicationContext());
                String global="";
                if(item.get("GlobalShipping").toString().equals("true")){global="Yes";}
                else{global="No";}
                t2.setText(global);
                t2.setLayoutParams(lps);
                l6.addView(t2);
            }
            else{
                l6.setVisibility(View.GONE);
            }
            if(item.has("HandlingTime")){
                l7.setVisibility(View.VISIBLE);
                TextView t1=new TextView(getActivity().getApplicationContext());
                t1.setText("Handling Time");
                t1.setTextColor(Color.BLACK);
                t1.setWidth(300);
                t1.setLayoutParams(lp);
                l7.addView(t1);
                String handling="";
                if(item.get("HandlingTime").toString().equals("1") || item.get("HandlingTime").toString().equals("0")){handling=item.get("HandlingTime").toString()+" Day";}
                else{handling=item.get("HandlingTime").toString()+" Days";}
                TextView t2=new TextView(getActivity().getApplicationContext());
                t2.setText(handling);
                t2.setLayoutParams(lps);
                l7.addView(t2);
            }
            else{
                l7.setVisibility(View.GONE);
            }
            if(item.has("ConditionDescription")){
                l8.setVisibility(View.VISIBLE);
                TextView t1=new TextView(getActivity().getApplicationContext());
                t1.setText("Condition");
                t1.setTextColor(Color.BLACK);
                t1.setWidth(300);
                t1.setLayoutParams(lp);
                l8.addView(t1);
                TextView t2=new TextView(getActivity().getApplicationContext());
                t2.setText(item.get("ConditionDescription").toString());
                t2.setLayoutParams(lps);
                l8.addView(t2);
            }
            else{
                l8.setVisibility(View.GONE);
            }
        }
        else{
            ship.setVisibility(View.GONE);
            shipdetails.setVisibility(View.GONE);
        }
        if (rp.has("ReturnsAccepted")||rp.has("ReturnsWithin")||rp.has("Refund")||rp.has("ShippingCostPaidBy")){
            returnpolicy.setVisibility(View.VISIBLE);
            returnpolicydetails.setVisibility(View.VISIBLE);
            if(rp.has("ReturnsAccepted")){
                l9.setVisibility(View.VISIBLE);
                TextView t1=new TextView(getActivity().getApplicationContext());
                t1.setText("Policy");
                t1.setTextColor(Color.BLACK);
                t1.setWidth(300);
                t1.setLayoutParams(lp);
                l9.addView(t1);
                TextView t2=new TextView(getActivity().getApplicationContext());
                t2.setText(rp.get("ReturnsAccepted").toString());
                t2.setLayoutParams(lps);
                l9.addView(t2);
            }
            else{
                l9.setVisibility(View.GONE);
            }
            if(rp.has("ReturnsWithin")){
                l10.setVisibility(View.VISIBLE);
                TextView t1=new TextView(getActivity().getApplicationContext());
                t1.setText("Returns within");
                t1.setTextColor(Color.BLACK);
                t1.setWidth(300);
                t1.setLayoutParams(lp);
                l10.addView(t1);
                TextView t2=new TextView(getActivity().getApplicationContext());
                t2.setText(rp.get("ReturnsWithin").toString());
                t2.setLayoutParams(lps);
                l10.addView(t2);
            }
            else{
                l10.setVisibility(View.GONE);
            }
            if(rp.has("Refund")){
                l11.setVisibility(View.VISIBLE);
                TextView t1=new TextView(getActivity().getApplicationContext());
                t1.setText("Refund Mode");
                t1.setTextColor(Color.BLACK);
                t1.setWidth(300);
                t1.setLayoutParams(lp);
                l11.addView(t1);
                TextView t2=new TextView(getActivity().getApplicationContext());
                t2.setText(rp.get("Refund").toString());
                t2.setLayoutParams(lps);
                l11.addView(t2);
            }
            else{
                l11.setVisibility(View.GONE);
            }
            if(rp.has("ShippingCostPaidBy")){
                l12.setVisibility(View.VISIBLE);
                TextView t1=new TextView(getActivity().getApplicationContext());
                t1.setText("Shipped By");
                t1.setTextColor(Color.BLACK);
                t1.setWidth(300);
                t1.setLayoutParams(lp);
                l12.addView(t1);
                TextView t2=new TextView(getActivity().getApplicationContext());
                t2.setText(rp.get("ShippingCostPaidBy").toString());
                t2.setLayoutParams(lps);
                l12.addView(t2);
            }
            else{
                l12.setVisibility(View.GONE);
            }
        }
        else{
            returnpolicy.setVisibility(View.GONE);
            returnpolicydetails.setVisibility(View.GONE);
        }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return view;
    }

}
