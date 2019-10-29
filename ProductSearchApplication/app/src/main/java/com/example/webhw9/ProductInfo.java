package com.example.webhw9;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ProductInfo extends AppCompatActivity {
    private SectionAdapter sa;
    private ViewPager vp;
    Toolbar tool;
    ImageView im;
    JSONArray listimg;
    String pri="",item_url="";
    String t;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);
        sa=new SectionAdapter(getSupportFragmentManager());
        vp=(ViewPager) findViewById(R.id.title_bar);
        Intent intent=getIntent();
        im=(ImageView)findViewById(R.id.fb);
        im.setImageResource(R.drawable.facebook);
        String tit=intent.getStringExtra("title");
        //Toolbar set title here
        tool=(Toolbar)findViewById(R.id.toolbar);
        tool.setNavigationIcon(R.drawable.back);
        TextView a=(TextView)findViewById(R.id.title) ;
        tit=tit.substring(0,40);
        a.setText(tit);
        //tool.setTitle(tit);
        //tool.setMinimumWidth(50);
        //Toast.makeText(this,tit,Toast.LENGTH_LONG).show();
        String res = intent.getStringExtra("response");
        t=intent.getStringExtra("title");
        try {
            JSONObject item = new JSONObject(res).getJSONObject("Item");
            pri = item.getJSONObject("CurrentPrice").get("Value").toString();
            item_url =item.get("ViewItemURLForNaturalSearch").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        setupViewPager(vp);
        final int[] ICONS = new int[]{
                R.drawable.product,
                R.drawable.truck,
                R.drawable.photos,
                R.drawable.similar
        };

        tool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TabLayout tl=(TabLayout) findViewById(R.id.alltabs);
        tl.setupWithViewPager(vp);

        tl.getTabAt(0).setIcon(ICONS[0]);
        tl.getTabAt(1).setIcon(ICONS[1]);
        tl.getTabAt(2).setIcon(ICONS[2]);
        tl.getTabAt(3).setIcon(ICONS[3]);
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    t= URLEncoder.encode(t,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Uri open = Uri.parse("https://www.facebook.com/dialog/share?app_id=919408081784174&display=popup&href="+item_url+"&quote=Buy "+t+" at $"+pri+" from link below&hashtag=%23CSCI571Spring2019Ebay");
                Intent j = new Intent(Intent.ACTION_VIEW, open);
                j.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(j);
            }
        });


    }
    private void setupViewPager(ViewPager vp) {
        SectionAdapter adapter=new SectionAdapter(getSupportFragmentManager());
        adapter.addFragment(new ProductTab(), "PRODUCT");
        adapter.addFragment(new ShippingTab(), "SHIPPING");
        adapter.addFragment(new PhotosTab(), "PHOTOS");
        adapter.addFragment(new SimilarTab(), "SIMILAR");
        vp.setAdapter(adapter);
    }



}
