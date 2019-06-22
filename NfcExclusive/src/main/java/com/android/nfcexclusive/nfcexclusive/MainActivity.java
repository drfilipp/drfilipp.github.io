package com.android.nfcexclusive.nfcexclusive;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcBarcode;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    private PendingIntent mPendingIntent;
    static Tag mytag;
    String paths;
    static boolean select=false;
    static boolean select_CHECK=false;
    static Context context2;
    String tempp="";
    String patchsplus="";
    private IntentFilter[] mFilters;
    public TextView mText;
    private int mCount = 0;
    private String[][] mTechLists;
    private NfcAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //setContentView(R.layout.foreground_dispatch);
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        //mText = (TextView) findViewById(R.id.textViewInfo);
        //mText.setText("Scan a tag");
        context2=getApplicationContext();
        paths =getApplicationContext().getFilesDir().getParent()+"/files/defkey.keys";
        Log.i("ujhhhm", paths);
        patchsplus="defkey.keys";
        File file = new File(paths);
        if(!file.exists()){
            Log.i("Not keyss2", paths);
            for (int m=0; m!=7+1; m++){
                tempp+=CommonTask.byte2HexString(CommonTask.getKeys(m));
           }
            writeData(tempp);
            patchsplus="mykey.keys";
            writeData(CommonTask.byte2HexString(CommonTask.getKeys(0)));
        }
        mAdapter = NfcAdapter.getDefaultAdapter(this);
       // if (mAdapter == null) return; // NFC not available on this device
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        try {
            ndef.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        mFilters = new IntentFilter[] {
                ndef,
        };
        mTechLists = new String[][]{
                new String[]{NfcF.class.getName()},
                new String[] { NfcA.class.getName() },
                new String[] { Ndef.class.getName() },
                new String[] { NdefFormatable.class.getName() },
                new String[] { MifareClassic.class.getName() },
                new String[] { MifareUltralight.class.getName() },
                new String[] { NdefFormatable.class.getName() },
                new String[] { NfcA.class.getName() },
                new String[] { NfcB.class.getName() },
                new String[] { NfcV.class.getName() },
                new String[] { NfcBarcode.class.getName() }
        };
        if(getIntent().getAction()!=null){
        resolveIntent(getIntent());}
    }
    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null) mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters ,
                mTechLists);
    }
    public void writeData(String data) {
        try {
            Context context = getApplicationContext();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(patchsplus, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (Exception e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    @Override
    protected void onNewIntent(final Intent intent) {

        super.onNewIntent(intent);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                setIntent(intent);
                resolveIntent(intent);
            }
        }, 0);
    }

    public void resolveIntent(Intent intent) {
        String action = intent.getAction();
        mytag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        //Intent activinf = new Intent(MainActivity.this, infonfc.class);
        Intent activinf = new Intent(MainActivity.this, rwnfc.class);
        if(action.equals(NfcAdapter.ACTION_TAG_DISCOVERED) ||
                action.equals(NfcAdapter.ACTION_TECH_DISCOVERED) ){
            //Log.i("Fore", " tultra");
            //mText.setText(infcard.getTagInfo(mytag));
            //MifareUltra.readTag(mytag);

            startActivity(activinf);

        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (mAdapter != null) mAdapter.disableForegroundDispatch(this);
    }
    public void OnClicksl(View view) {
        select=true;
        Intent activinf7 = new Intent(MainActivity.this, select.class);
        startActivity(activinf7);
    }
    public void OnClicks(View view) {
        Intent activinf = new Intent(MainActivity.this, rwnfc.class);
        startActivity(activinf);
    }
    public void OnClicks2(View view) {
        Intent activinf2 = new Intent(MainActivity.this, wrnfc.class);
        startActivity(activinf2);
    }
    public void OnClickskey(View view) {
        Intent activinf3 = new Intent(MainActivity.this, keys.class);
        startActivity(activinf3);
    }
}
