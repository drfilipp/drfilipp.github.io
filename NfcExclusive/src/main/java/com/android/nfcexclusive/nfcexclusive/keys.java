package com.android.nfcexclusive.nfcexclusive;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.OutputStreamWriter;

import static android.R.attr.id;

public class keys extends AppCompatActivity {
EditText edit1;
    String edtext="";
    byte[][] dats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keys);
        edit1= (EditText)  findViewById(R.id.editTextt);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Keys");
        CommonTask.check=3;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg="";
                String msgend="";
                int sub=0;
                msg= edit1.getText().toString();
                msg=msg.replaceAll("\\s+", "");
                msg=msg.replaceAll("\\n", "");
                Log.i("jj,h",edit1.getText().length()/12+"");
                Log.i("jj,h","kyhk");
                for(int mk=0;mk!=(msg.length())/12;mk++)
                {

                    if(!CommonTask.isHexAnd12Byte(msg.substring(sub,sub+12),getApplicationContext(),mk+1,12)){msg=null;break; }
                   // Log.i("jj,hsub",sub+"");
                   // Log.i("jj,hmk",mk+"");

                    msgend+=msg.substring(sub,sub+12);
                   // Log.i("jj,hend",msgend);
                    sub=sub+12;
                }
                if(!CommonTask.isHexAnd1Byte(msg,getApplicationContext())){msg=null;}
                if(msg!=null) {writeData(msgend);}
                Snackbar.make(view, "Сохранено, успешно!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        int l=0;
        dats=CommonTask.getKeyssize2();
        l=CommonTask.getKeyssizer1();
        Log.i("lllllll",l+"");
        int li=l;
       while(li!=CommonTask.getKeyssizer()){li++;edtext+=CommonTask.byte2HexString(dats[li])+"\n";}
        edit1.setText(edtext);


    }
    public void writeData(String data) {
        try {
            Context context = getApplicationContext();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("mykey.keys", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (Exception e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

}
