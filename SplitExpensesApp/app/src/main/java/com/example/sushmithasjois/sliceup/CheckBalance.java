package com.example.sushmithasjois.sliceup;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CheckBalance extends AppCompatActivity {
     ListView lv;
    ArrayList<String> array;
    ArrayAdapter<String> arrayAdapter;
    SQLiteDatabase db;
    String msg;
    Button b4;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_balance);
        lv=(ListView)findViewById(R.id.listview);
        b4=(Button)findViewById(R.id.button4);
        try{
        db=openOrCreateDatabase("user", Context.MODE_PRIVATE, null);
        Cursor c=db.rawQuery("select * from debt",null);
        array=new ArrayList<String>();

        if(c.getCount()==0){

        }
        while(c.moveToNext()){
            msg=c.getString(0)+" -> "+c.getString(1)+"  Rs."+c.getInt(2);
            array.add(msg);
        }
        arrayAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,array);

        lv.setAdapter(arrayAdapter);}
        catch(Exception e){
            Toast.makeText(CheckBalance.this,"All debts are cleared",Toast.LENGTH_SHORT).show();
        }

        b4.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                try{
                db.execSQL("drop table Members;");
                db.execSQL("drop table event;");
                db.execSQL("drop table debt;");
                onCreate(savedInstanceState);}
                catch(Exception e){
                    Toast.makeText(CheckBalance.this, "All debts cleared", Toast.LENGTH_SHORT).show();
                }Toast.makeText(CheckBalance.this, "All debts cleared", Toast.LENGTH_SHORT).show();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder=new AlertDialog.Builder(CheckBalance.this);
                builder.setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int id) {
                      String item=  arrayAdapter.getItem(position);
                      arrayAdapter.remove(item);
                        lv.setAdapter(arrayAdapter);
                        String[] items=item.split(" -> ");
                        String item1=items[0];
                        String[] items2=items[1].split(" Rs.");
                        db.execSQL("delete from debt where toPay='"+item1.trim()+"' and toBePaid='"+items2[0].trim()+"';");
                    }
                });
                builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });
                builder.setCancelable(true); builder.setTitle("SETTLE DEBT?"); builder.show();
            }
        });
    }
}
