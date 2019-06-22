package com.android.nfcexclusive.nfcexclusive;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcBarcode;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.R.id.list;
import static android.app.PendingIntent.getActivity;
import static java.lang.Thread.sleep;

public class rwnfc extends AppCompatActivity {
    private PendingIntent mPendingIntent;
    static Tag mytags;
    static String[] datsAsync2 = new String[120];
    static MifareClassic mifareClassicTag1;
    String names = "errors";
     int r = 1;
    ListView itemlist;
    String[] catNames;
    String[] catNames2;
    Toast toast;
    static String techs;
    private IntentFilter[] mFilters;
    public TextView info;
    public TextView nameTag;
    ProgressDialog dialog;
    public TextView datatemp;
    public TextView uid;
    public TextView Sectors;
    public static TextView tech;
    public TextView size;
    public ProgressBar centerProgres;
    public CheckBox standartKeys;

    SpannableStringBuilder builder ;
    public CheckBox customKeys;
    public ImageView image;
    static int mCount = 0;
    FloatingActionButton fab2;
    private String[][] mTechLists;
    private NfcAdapter mAdapter;
    static String dats[] = new String[120];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rwnfc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        info = (TextView) findViewById(R.id.textView2);

        standartKeys = (CheckBox) findViewById(R.id.checkBox2);
        image=(ImageView) findViewById(R.id.imageView2);
        customKeys = (CheckBox) findViewById(R.id.checkBox);
       itemlist = (ListView)findViewById(R.id.listView);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activinf = new Intent(rwnfc.this, dumps.class);
                startActivity(activinf);

            }
        });
        itemlist.setVisibility(View.INVISIBLE);
        itemlist.setEnabled(false);
        mytags = MainActivity.mytag;
        //names = getTagInfo(mytags);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Read");}

        // info.setText(names+" \n Reading...");
        //names =
fab2.setVisibility(View.INVISIBLE);
        //   infcard.getTagInfo(mytags);
        // MifareUltra.readTag(mytags);
        //info.setText(getTagInfo(mytags) + " \n Blocks");
        toast = Toast.makeText(getApplicationContext(),
                "Приложите NFC метку", Toast.LENGTH_SHORT);
        toast.show();
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        // if (mAdapter == null) return; // NFC not available on this device
        mPendingIntent = getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        try {
            ndef.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        mFilters = new IntentFilter[]{
                ndef,
        };
        mTechLists = new String[][]{
                new String[]{NfcF.class.getName()},
                new String[]{NfcA.class.getName()},
                new String[]{Ndef.class.getName()},
                new String[]{NdefFormatable.class.getName()},
                new String[]{MifareClassic.class.getName()},
                new String[]{MifareUltralight.class.getName()},
                new String[]{NdefFormatable.class.getName()},
                new String[]{NfcA.class.getName()},
                new String[]{NfcB.class.getName()},
                new String[]{NfcV.class.getName()},
                new String[]{NfcBarcode.class.getName()}
        };

        //  resolveIntent(getIntent());


    }


    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null) mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters,
                mTechLists);
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
        Log.i("fbdbn", "trues");
        mytags = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if(customKeys.isChecked()&standartKeys.isChecked()) {CommonTask.check=3;}
        else if(customKeys.isChecked()) {CommonTask.check=2;}
        else if(standartKeys.isChecked()) {CommonTask.check=1;}
        else CommonTask.check=3;
        Intent activinf = new Intent(this, dumps.class);
        if (action.equals(NfcAdapter.ACTION_TAG_DISCOVERED) ||
                action.equals(NfcAdapter.ACTION_TECH_DISCOVERED)) {
            //names = getTagInfo(mytags);
            //  info.setText(names + " \n Reading...");


            // infcard.getTagInfo(mytags);
            if (r == 1) {
                dialogProgress posts = new dialogProgress();
                posts.execute();
                /*toast = Toast.makeText(getApplicationContext(),
                        "Сканирование...", Toast.LENGTH_LONG);
                toast.show();
               // dats = getTagInfo(mytags);
                if (dats[1].indexOf("errors") == -1) {
                    Log.i("fbdbn", "true");
                    r = 0;
                    datatemp.setText(dats[1]);
                    getSupportActionBar().setTitle(dats[8]);
                    nameTag.setVisibility(View.VISIBLE);
                    nameTag.setText(dats[8]);
                    standartKeys.setVisibility(View.INVISIBLE);
                    customKeys.setVisibility(View.INVISIBLE);
                    info.setVisibility(View.INVISIBLE);
                    return;
                } else {
                    info.setText("Попробуйте снова, приложить NFC метку к устройству");
                    r = 1;
                    Log.i("fbdbn", "errors");

                }*/
            }

        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAdapter != null) mAdapter.disableForegroundDispatch(this);
    }

    static public String[] getTagInfo(Tag tag) {
        techs="";
        NfcA nfca = NfcA.get(tag);
        byte[] atqaBytes = nfca.getAtqa();
        atqaBytes = new byte[] {atqaBytes[1], atqaBytes[0]};
        String atqa = CommonTask.byte2HexString(atqaBytes);
        // SAK in big endian.
        byte[] sakBytes = new byte[] {
                (byte)((nfca.getSak() >> 8) & 0xFF),
                (byte)(nfca.getSak() & 0xFF)};
        String sak;
        // Print the first SAK byte only if it is not 0.
        if (sakBytes[0] != 0) {
            sak = CommonTask.byte2HexString(sakBytes);
        } else {
            sak = CommonTask.byte2HexString(new byte[] {sakBytes[1]});
        }
        String[] info = new String[120];
        String[] techList = tag.getTechList();
       for (int f = 0; f < techList.length; f++) {
            if (techList[f].equals(MifareUltralight.class.getName())) {
              techs+="MifareUltralight; ";
            } if (techList[f].equals(MifareClassic.class.getName())) {
                techs+="MifareClassic; ";
            } if (techList[f].equals(IsoDep.class.getName())) {
                techs+="IsoDep; ";
            }  if (techList[f].equals(Ndef.class.getName())) {
                techs+="Ndef; ";
            } if (techList[f].equals(NdefFormatable.class.getName())) {
                techs+="NdefFormatable; ";
            }  if (techList[f].equals(NfcA.class.getName())) {
                techs+="NfcA; ";
            } if (techList[f].equals(NfcB.class.getName())) {
                techs+="NfcB; ";
            }  if (techList[f].equals(NfcF.class.getName())) {
                techs+="NfcF; ";
            }  if (techList[f].equals(NfcV.class.getName())) {
                techs+="NfcV; ";

            }

        }

            if (techs.indexOf("MifareUltralight")!=-1) {
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                switch (mifareUlTag.getType()) {
                    case MifareUltralight.TYPE_ULTRALIGHT:

                        info[9] = MifareUltra.readTag(tag);
                        info[8] = "MifareUltralight";
                        break;
                    case MifareUltralight.TYPE_ULTRALIGHT_C:

                        info[9] = MifareUltra.readTag(tag);
                        info[8] = "MifareUltralight C";
                        break;
                }
            } else if (techs.indexOf("MifareClassic")!=-1) {
                MifareClassic mifareClassicTag = MifareClassic.get(tag);
                switch (mifareClassicTag.getType()) {
                    case MifareClassic.TYPE_CLASSIC:

                        info = MifareClass.readTag(tag);
                        info[8] = "MifareClassic";
                        break;
                    case MifareClassic.TYPE_PLUS:

                        info = MifareClass.readTag(tag);
                        info[8] = "MifarePlus";
                        break;
                    case MifareClassic.TYPE_PRO:

                        info = MifareClass.readTag(tag);
                        info[8] = "MifarePro";
                        break;
                }
            } else if (techs.indexOf("IsoDep")!=-1) {
                Log.i("Wri", "knjnk");
                info = isodepclass.read(tag);
                info[8] = "IsoDep";

            } else if (techs.indexOf("Ndef")!=-1) {
                Ndef.get(tag);

                info[1] = Ndefclass.read(tag);
                info[8] = "Ndef";
            } else if (techs.indexOf("NdefFormatable")!=-1) {
                // info="Ndef format";
                // NdefFormatable ndefFormatableTag = NdefFormatable.get(tag);
            } else if (techs.indexOf("NfcA")!=-1) {
                Log.i("classic", techList.toString());


                info = Nfcaclass.readTag(tag);
                info[8] = "NFCA";
            } else if (techs.indexOf("NfcB")!=-1) {

                info[1] = nfcbclass.read(tag);
                info[8] = "NFC B";

            } else if (techs.indexOf("NfcF")!=-1) {
                info[8] = "NFC F";

            } else if (techs.indexOf("NfcV")!=-1) {
                info[8] = "NFC V";


            }

        info[3]= "ATQA: " + atqa + " SAK: " + sak;
        return info;
    }

    public void OnClicks(View view) {
        Log.i("Wri", String.valueOf(mytags));
        r = 0;
        names = "errors";
        Log.i("Wri", "button2");
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

class dialogProgress extends AsyncTask<Void, Void, Void> {
    public String datsAsync[] = new String[120];
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.i("fbdbn", "asynctusk");
        toast = Toast.makeText(getApplicationContext(),
                "Сканирование...", Toast.LENGTH_SHORT);
        toast.show();
        image.setVisibility(View.INVISIBLE);
        standartKeys.setVisibility(View.INVISIBLE);
        customKeys.setVisibility(View.INVISIBLE);
        //centerProgres.setVisibility(View.VISIBLE);
        dialog = ProgressDialog.show(rwnfc.this, "",
                "Сканирование NFC... Пожалуйста, будьте терпиливыми!", true);
        dialog.getWindow().setLayout(900, 260);
        info.setText("");
    }

    @Override
    protected Void doInBackground(Void... params) {
        datsAsync= getTagInfo(mytags);
       return null;
    }



    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        info.setVisibility(View.INVISIBLE);
        dialog.dismiss();
        if (datsAsync[1].indexOf("errors") == -1) {
            r = 0;
            catNames = new String[] {
                    datsAsync[8], datsAsync[2], datsAsync[3], datsAsync[4], "Mifare Classic"
            };
            catNames2 = new String[] {
                    "Тип тега:", "UID:", "Информация о секторах:", "Размер метки:", "Mifare Classic"
            };
            datsAsync2=datsAsync;
            itemlist.setEnabled(true);
            itemlist.setVisibility(View.VISIBLE);

            //datatemp.setText(datsAsync[1]);
            if (getSupportActionBar()!=null){
                getSupportActionBar().setTitle(datsAsync[2]);}
           /* nameTag.setVisibility(View.VISIBLE);

            uid.setVisibility(View.VISIBLE);

            Sectors.setVisibility(View.VISIBLE);

            size.setVisibility(View.VISIBLE);

            tech.setVisibility(View.VISIBLE);

            info.setVisibility(View.INVISIBLE);*/
            /*nameTag.setText(datsAsync[8]);
            uid.setText(datsAsync[2]);
            Sectors.setText(datsAsync[3]);
            size.setText(datsAsync[4]);
            tech.setText("Mifare Classic ");//datsAsync[5]*/
         //  ArrayAdapter<String> adapter = new ArrayAdapter<String>(rwnfc.this, R.layout.list_item, R.id.firstLine, catNames2);
            String[] from = { "name", "purpose" };
            ArrayList<HashMap<String, String>> myArrList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;

// Досье на первого кота
            map = new HashMap<String, String>();
            map.put("Name", "Тип тега");
            map.put("Tel", datsAsync[8]);
            myArrList.add(map);
// Досье на второго кота
            map = new HashMap<String, String>();
            map.put("Name", "UID");
            map.put("Tel", datsAsync[2]);
            myArrList.add(map);
// Досье на третьего кота
            map = new HashMap<String, String>();
            map.put("Name", "Информация");
            map.put("Tel", datsAsync[3]);
            myArrList.add(map);

            map = new HashMap<String, String>();
            map.put("Name", "Размер метки");
            map.put("Tel", datsAsync[4]);
            myArrList.add(map);

            map = new HashMap<String, String>();
            map.put("Name", "Технологии");
            map.put("Tel", techs);
            myArrList.add(map);
            int[] to = { R.id.secondLine, R.id.firstLine };
            int[] tos = { android.R.id.text1, android.R.id.text2 };
            SimpleAdapter adapter = new SimpleAdapter(rwnfc.this, myArrList, R.layout.list_item, new String[] {"Name", "Tel"},
                    new int[] {R.id.firstLine,R.id.secondLine});
            fab2.setVisibility(View.VISIBLE);
            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(rwnfc.this, R.layout.list_item, R.id.secondLine, catNames);
            itemlist.setAdapter(adapter);


            if (getSupportActionBar()!=null){
                getSupportActionBar().setTitle(datsAsync[8]);}
        } else {
            fab2.setVisibility(View.INVISIBLE);
            info.setVisibility(View.VISIBLE);
            image.setVisibility(View.VISIBLE);
            builder = new SpannableStringBuilder();
            SpannableString str1 = new SpannableString("Попробуйте снова, приложить NFC метку");
            str1.setSpan(new ForegroundColorSpan(Color.RED), 0, str1.length(), 0);
            builder.append(str1);
            info.setText(str1, EditText.BufferType.SPANNABLE);
            itemlist.setVisibility(View.INVISIBLE);
            itemlist.setEnabled(false);
            r = 1;
            standartKeys.setVisibility(View.VISIBLE);
            customKeys.setVisibility(View.VISIBLE);
        }
    }
}

}