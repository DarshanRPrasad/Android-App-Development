package com.example.webhw9;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

public class Form_tab extends AppCompatActivity {

    private SectionAdapter sa;
    private ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_tab);
        sa=new SectionAdapter(getSupportFragmentManager());
        vp=(ViewPager) findViewById(R.id.title_bar);
        setupViewPager(vp);
        TabLayout tl=(TabLayout) findViewById(R.id.alltabs);
        tl.setupWithViewPager(vp);
    }
    private void setupViewPager(ViewPager vp) {
        SectionAdapter adapter=new SectionAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1(), "SEARCH");
        adapter.addFragment(new Tab2(), "WISH LIST");
        vp.setAdapter(adapter);
    }
}

