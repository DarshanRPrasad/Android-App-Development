package com.example.sushmithasjois.sliceup;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.TaskStackBuilder;
import android.app.NotificationManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
//import android.support.v7.ActionBarActivity;
import android.content.res.Resources;

public class AddEvent extends AppCompatActivity{
    EditText e1,e2,e3,e4;
    Button b;
    NotificationManager manager;
    TextView t;
    String msg="";

    Spinner spin;
    String[] friends;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        Intent i5=getIntent();
        final int i=i5.getIntExtra("i",5);
        e1=(EditText)findViewById(R.id.editText2);
        e2=(EditText)findViewById(R.id.editText3);
        e3=(EditText)findViewById(R.id.editText5);
        //e2.setHintTextColor(Color.WHITE);
        b=(Button)findViewById(R.id.button7);


        final SQLiteDatabase db=openOrCreateDatabase("user", Context.MODE_PRIVATE,null);
        e2.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v,boolean onFocus){
                if (onFocus==false){
                    friends =e2.getText().toString().split(",");
                    spin= (Spinner)findViewById(R.id.spinner);

                    ArrayAdapter<String> adapter=new ArrayAdapter<String>(AddEvent.this,android.R.layout.simple_spinner_dropdown_item,friends);
                    spin.setAdapter(adapter);
                }
            }
        });


    b.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            //i+=1;

           /* db.execSQL("drop table Members;");
            db.execSQL("drop table event;");
            db.execSQL("drop table debt;");*/
            if(e1.getText().toString().trim().length()==0 || e2.getText().toString().trim().length()==0 || e3.getText().toString().trim().length()==0){
                Toast.makeText(AddEvent.this,"Please fill all fields",Toast.LENGTH_SHORT).show();
            }
            else{

            String whoPaid = spin.getSelectedItem().toString();
            db.execSQL("create table if not exists event(id INTEGER PRIMARY KEY, name varchar,whoPaid varchar,amount int)");
            ContentValues cv = new ContentValues();
            cv.put("id", i);
            cv.put("name", e1.getText().toString());
            cv.put("whoPaid", whoPaid);
            cv.put("amount", Integer.parseInt(e3.getText().toString()));
            db.insert("event", null, cv);


            db.execSQL("create table if not exists Members(id INTEGER,member varchar)");

            ContentValues cv1 = new ContentValues();
            int l = friends.length;
            for (int j = 0; j < l; j++) {
                cv1.put("id", i);
                cv1.put("member", friends[j]);

                db.insert("Members", null, cv1);
            }

            // Toast.makeText(AddEvent.this,"event added", Toast.LENGTH_SHORT).show();
            // i++;


            db.execSQL("create table if not exists debt(toPay varchar,toBePaid varchar,amount INTEGER);");

            Cursor c = db.rawQuery("select * from Members where id=" + i + ";", null);
            int count = c.getCount();


            // try{
            int split = (Integer.parseInt(e3.getText().toString())) / count;
            /*c=db.rawQuery("select member from Members where id="+i+";",null);
            c.moveToFirst();
                //StringBuffer buffer=new StringBuffer();
            while(c.moveToNext()){
                if (!(c.getString(0).equals(whoPaid))) {
                    msg=msg+" "+c.getString(0);
                }}
                t.setText(msg);*/
            for (int k = 0; k < friends.length; k++) {
                if (!(friends[k].equals(whoPaid))) {
                    Cursor c1 = db.rawQuery("select * from debt where toPay='" + friends[k] + "' and toBePaid='" + whoPaid + "';", null);
                    if (c1.getCount() != 0) {
                        c1.moveToFirst();
                        db.execSQL("update debt set amount=amount+" + split + " where toPay='" + friends[k] + "' and toBePaid='" + whoPaid + "';");
                    }

                    Cursor c2 = db.rawQuery("select * from debt where toBePaid='" + friends[k] + "' and toPay='" + whoPaid + "';", null);

                    if (c1.getCount() == 0 && c2.getCount() == 0) {
                        db.execSQL("insert into debt values('" + friends[k] + "','" + whoPaid + "'," + split + ");");
                    } else if (c2.getCount() != 0) {

                        Cursor c3 = db.rawQuery("select amount from debt where toBePaid='" + friends[k] + "' and toPay='" + whoPaid + "'; ", null);
                        c3.moveToFirst();
                        int amount2 = c3.getInt(0);
                        if (amount2 > split) {
                            //int remainder=amount2-split;
                            db.execSQL("update debt set amount=amount-" + split + " where toBePaid='" + friends[k] + "' and toPay='" + whoPaid + "';");
                        } else if (amount2 < split) {
                            int remainder1 = (split - amount2);
                            db.execSQL("delete from debt where toBePaid='" + friends[k] + "' and toPay='" + whoPaid + "';");
                            db.execSQL("insert into debt values('" + friends[k] + "','" + whoPaid + "'," + remainder1 + ");");
                        }

                    }
                }
            }
            Toast.makeText(AddEvent.this, "Event added", Toast.LENGTH_SHORT).show();

                /*Cursor c1=db.rawQuery("select * from debt where toPay='"+c.getString(0)+"' and toBePaid='"+whoPaid+"';",null);

                if(c1.getCount()!=0){
                    c1.moveToFirst();
                    db.execSQL("update debt set amount=amount+"+split+" where toPay='"+c.getString(0)+"' and toBePaid='"+whoPaid+"';");}

                Cursor c2=db.rawQuery("select * from debt where toBePaid='"+c.getString(0)+"' and toPay='"+whoPaid+"';",null);

                if(c1.getCount()==0 && c2.getCount()==0){
                   db.execSQL("insert into debt values('"+c.getString(0)+"','"+whoPaid+"',"+split+");");
                }
                else if(c2.getCount()!=0){

                    Cursor c3=db.rawQuery("select amount from debt where toBePaid='"+c.getString(0)+"' and toPay='"+whoPaid+"'; ",null);
                    c3.moveToFirst();
                    int amount2=c3.getInt(0);
                    if(amount2>split){
                    int remainder=amount2-split;
                    db.execSQL("update debt set amount="+remainder+" where toPay='"+c.getString(0)+"' and toBePaid='"+whoPaid+"';");}
                    else{
                        int remainder1=split-amount2;
                        db.execSQL("delete from debt where toBePaid='"+c.getString(0)+"' and toPay='"+whoPaid+"';");
                        db.execSQL("insert into debt values('"+whoPaid+"','"+c.getString(0)+"',"+remainder1+");");
                    }
                }}

            Toast.makeText(AddEvent.this, "Event added", Toast.LENGTH_SHORT).show();
                }}
catch(Exception e){
    Toast.makeText(AddEvent.this, e.toString(), Toast.LENGTH_SHORT).show();
}


                   /*Cursor c2=db.rawQuery("select count from debt where toBePaid='"+c.getString(0)+"';",null);
                    if(c2.getCount()==0){
                        db.execSQL("insert into debt values('"+c.getString(0)+"','"+whoPaid+"',"+split+");");
            }}}*/


        /*try{ StringBuffer buffer=new StringBuffer();
                    Cursor c=db.rawQuery("select * from Members",null);
                    if(c.getCount()==0)    {
                        buffer.append("Error"+ "No records found");}
                  // c.moveToFirst();
                    while(c.moveToNext()){
                        buffer.append("toPay: "+c.getString(0)+"\n");
                        //buffer.append("toBePaid: "+c.getString(1)+"\n");
                       // buffer.append("amount: "+c.getInt(1)+"\n\n");
                       // buffer.append("Id: "+c.getInt(0)+"\n");
                        buffer.append("member: "+c.getString(1)+"\n\n");
                    }
                    AlertDialog.Builder builder=new AlertDialog.Builder(AddEvent.this);
                    builder.setCancelable(true); builder.setTitle("details"); builder.setMessage(buffer); builder.show();}
                 catch(Exception e){
                     Toast.makeText(AddEvent.this, "Error", Toast.LENGTH_SHORT).show();
                 }*/

            addNotification();



        }}



    });

}
    private void addNotification()
    {
        String msg="";
        Log.i("Start","notification");
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        builder.setContentTitle("New Event added");
        builder.setContentText("Click to view balance");
        builder.setTicker("New Message Alert");
        builder.setSmallIcon(R.drawable.ic_menu_send);
        builder.setNumber(10);
        NotificationCompat.InboxStyle inbox=new NotificationCompat.InboxStyle();
        String[] events=new String[5];
        SQLiteDatabase db=openOrCreateDatabase("user", Context.MODE_PRIVATE,null);
        Cursor c5=db.rawQuery("select * from debt ",null);
        //c5.moveToFirst();
        int m=0;
        while(c5.moveToNext()){
             msg= c5.getString(0)+" owes "+ c5.getString(1)+ " Rs."+ c5.getInt(2);
            events[m]=new String(msg);
            m++;
        }

        //inbox.addLine(events[0]);
        //events[0]=new String("This is line one");
        //events[1]=new String("This is line two");

        inbox.setBigContentTitle("Debts");

       for(int i=0;i<events.length;i++)
        {
            inbox.addLine(events[i]);

        }
        builder.setStyle(inbox);
        manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Intent result=new Intent(this,CheckBalance.class);
        TaskStackBuilder stackBuilder=TaskStackBuilder.create(this);
        stackBuilder.addParentStack(AddEvent.class);
        stackBuilder.addNextIntent(result);
        PendingIntent resultPendingIntent=stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        //builder.getNotification().flags |=Notification.FLAG_AUTO_CANCEL;
        builder.setAutoCancel(true);
        manager.notify(0,builder.build());



        /*NotificationCompat.Builder builder=new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher_round).setContentTitle("Notification alert").setContentText("Notification from Slice up");
        Intent notificationIntent=new Intent(this,Ntifications.class);
        PendingIntent contentIntent=PendingIntent.getActivity(this,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());*/
    }
}

