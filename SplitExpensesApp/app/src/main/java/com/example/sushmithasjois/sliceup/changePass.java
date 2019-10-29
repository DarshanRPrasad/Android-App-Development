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

public class changePass extends AppCompatActivity {
    EditText e1,e2,e3;
    Button b1;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
       Intent get1=getIntent();
        name=get1.getStringExtra("username");
        e1=(EditText)findViewById(R.id.editText6);
        e2=(EditText)findViewById(R.id.editText7);
        e3=(EditText)findViewById(R.id.editText8);
        b1=(Button)findViewById(R.id.button9);

        final SQLiteDatabase db=openOrCreateDatabase("user", Context.MODE_PRIVATE,null);
        b1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){


            Cursor c=db.rawQuery("Select * from info where username='"+name+"';",null);
                   c.moveToFirst();
            if(e1.getText().toString().trim().length()==0 || e2.getText().toString().trim().length()==0 || e3.getText().toString().trim().length()==0 ){
                Toast.makeText(changePass.this,"Please fill all fields",Toast.LENGTH_SHORT).show();
            }

            else if (!(e1.getText().toString().equals(c.getString(1)))) {
                Toast.makeText(changePass.this,"wrong password",Toast.LENGTH_SHORT).show();
            }
            else if (!(e2.getText().toString().equals(e3.getText().toString()))){
                Toast.makeText(changePass.this,"passwords don't match",Toast.LENGTH_SHORT).show();
            }
            else{
                 db.execSQL("update info set password='"+e2.getText().toString()+"' where username='"+name+"';");
                Toast.makeText(changePass.this,"Password changed",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
