package com.example.sushmithasjois.sliceup;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    EditText e1,e2,e3,e4;
    Button b;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        e1=(EditText)findViewById(R.id.editText1);
        e2=(EditText)findViewById(R.id.editText2);
        e3=(EditText)findViewById(R.id.editText3);
        e4=(EditText)findViewById(R.id.editText4);
        b=(Button)findViewById(R.id.button11);


        b.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                db=openOrCreateDatabase("user", Context.MODE_PRIVATE,null);
                db.execSQL("create table if not exists info1(username varchar primary key, password varchar);");
                if(e1.getText().toString().trim().length()==0 || e2.getText().toString().trim().length()==0 || e3.getText().toString().trim().length()==0){
                    Toast.makeText(getBaseContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
                }

                Cursor c=db.rawQuery("select * from info where username='"+e1.getText().toString()+"';",null);
                if(c.getCount()!=0){
                    Toast.makeText(getBaseContext(), "Username already exists", Toast.LENGTH_LONG).show();
                }
                else if (!(e2.getText().toString().equals(e3.getText().toString()))){
                    Toast.makeText(getBaseContext(), "Password dont match", Toast.LENGTH_LONG).show();
                }
                else if(!(e1.getText().toString().endsWith("@gmail.com"))){
                    Toast.makeText(getBaseContext(), "Give a valid email id for username", Toast.LENGTH_LONG).show();
                }
                else if(e4.getText().toString().length()!=10){
                    Toast.makeText(getBaseContext(), "Invalid phone number", Toast.LENGTH_LONG).show();
                }
                else{
                db.execSQL("insert into info values('"+e1.getText().toString()+"','"+e2.getText().toString()+"');");
                    Toast.makeText(getBaseContext(), "Account Created", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
