package com.example.sushmithasjois.sliceup;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText e1, e2;
    Button b1,b2;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //userdb= new DatabaseHelper(this);
        db = openOrCreateDatabase("user", Context.MODE_PRIVATE, null);
        db.execSQL("create table if not exists info(username varchar primary key, password varchar);");
        /*try{
            db.execSQL("insert into info values('fida@gmail.com','fida');");
        }catch(Exception e){
            Toast.makeText(getBaseContext(), "Error inserting", Toast.LENGTH_LONG).show();
        }*/
        e1 = (EditText) findViewById(R.id.editText);
        e2 = (EditText) findViewById(R.id.editText4);
        b1 = (Button) findViewById(R.id.button);
        b2=(Button)findViewById(R.id.button8);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u, p;
                u = e1.getText().toString();
                p = e2.getText().toString();
                if(p.trim().length()==0 || u.trim().length()==0){
                    Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                else{
                try{
                    Cursor c = db.rawQuery("select password from info where username='" + u + "';'", null);
                c.moveToFirst();
                    if (p.equals(c.getString(0)) && c.getCount()!=0) {
                        Intent go=new Intent(MainActivity.this,Main2Activity.class);
                        go.putExtra("username",u);
                        startActivity(go);
                        Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                    }


                else
                {
                    Toast.makeText(MainActivity.this, "Incorrect email/password", Toast.LENGTH_SHORT).show();
                }}
                catch(Exception e){
                            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }}}
            });

         b2.setOnClickListener(new View.OnClickListener(){
             public void onClick(View v){
                 Intent i=new Intent(MainActivity.this,SignUp.class);
                 startActivity(i);
             }




            });

}}