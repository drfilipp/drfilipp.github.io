package com.android.nfcexclusive.nfcexclusive;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.PendingIntent.getActivity;

public class dump extends AppCompatActivity {
    ArrayList<String> listItems = new ArrayList<String>();
    ListView itemlist;
    ProgressBar progress;
    private PendingIntent mPendingIntent;
    static Tag mytags2;
    private IntentFilter[] mFilters;
    TextView text1;

    int back=0;
    int i;
    String[] datsAsync3 = new String[120];
    String[] datsAsync3temp = new String[120];
    int bs;
    private String[][] mTechLists;
    private NfcAdapter mAdapter;
    String paths = "";
    int arr = 0;
    public ImageView image;
    int r = 0;
    ProgressDialog dialog;
    SpannableStringBuilder builder ;
    String puth;
    int arrt = 0;
    String[][] datsAsyncWrite;
    boolean mn = false;
    int sectorinf = 0;
    Switch soundSwitch;
    ArrayList<HashMap<String, String>> myArrList2= new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> myArrListtemp= new ArrayList<HashMap<String, String>>();
    int formul;
    int m = 0;
    int blocks;
    FloatingActionButton fab2;
    int sw=0;
    ArrayList<HashMap<String, String>> myArrList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dump);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        text1 = (TextView) findViewById(R.id.textView);
        itemlist = (ListView) findViewById(R.id.listView2);
        datsAsync3 = wrnfc.datsAsync2;
        HashMap<String, String> map2;
        map2 = new HashMap<String, String>();

        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        fab2.setVisibility(View.INVISIBLE);
        image=(ImageView) findViewById(R.id.imageView3);
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
        if (select.dumpWr) {
            writeDump(select.myArrList2, select.start, select.blocksd);
        } else if (MainActivity.select& MainActivity.select_CHECK) {
            fab2.setVisibility(View.INVISIBLE);
            MainActivity.select_CHECK=false;
            myArrList = select.myArrList2;
            bs = select.start;
            Log.i("fbdbnkkkkff", MainActivity.select+"t");
            blocks = select.blocksd;
            sectorinf = select.start;
            if (blocks > 1) {
                datsAsync3[8] = "MifareClassic";
                Log.i("fbdbnkkkk", blocks + "mifare");
            } else {
                datsAsync3[8] = "NFC A";
                if(myArrList.get(0).get("Tel").length()>8){
                    datsAsync3[8] = "IsoDep";
                }
            }

        } else {
            MainActivity.select=false;
            MainActivity.select_CHECK=false;
            myArrList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;
            map = new HashMap<String, String>();
            Log.i("fbdbnkkkk", blocks + "mifare");
            try {
                bs = Integer.parseInt(datsAsync3[5]);
                Log.i("bs", String.valueOf(bs));
                formul = Integer.parseInt(datsAsync3[6]);
                blocks = Integer.parseInt(datsAsync3[7]);
                //bs+(formul*blocks)
                Log.i("formu", datsAsync3[6]);
                Log.i("blocks", datsAsync3[7]);
                for (i = 9; i != 9 + bs + (formul * blocks); i++) {
                    map = new HashMap<String, String>();
                    try {

                        if (datsAsync3[i] == null) {
                            break;
                        }

                        if ((datsAsync3[i].indexOf("Sector") != -1)) {
                            map.put("Name", datsAsync3[i]);
                            arrt++;
                            if (datsAsync3[i].indexOf("fail") == -1) {
                                for (int block = 0; block != blocks; block++) {
                                    i++;
                                    if (block == 0) {
                                        datsAsync3temp[1] = datsAsync3[i];

                                    } else {
                                        datsAsync3temp[1] += "\n"+datsAsync3[i];
                                    }
                                }
                                map.put("Tel", datsAsync3temp[1]);
                                arr++;
                            }
                        } else {


                            arrt++;
                            map.put("Name", datsAsync3[i]);
                        }            // Log.i(String.valueOf(bs), datsAsync3[i]);
                    } catch (Exception e) { //map.put("Name", datsAsync3[i]);
                        arrt++;
                        map.put("Name", datsAsync3[i]);
                    }

                    sectorinf++;
                    myArrList.add(map);
                }
            } catch (Exception e) {
            }
        }

        ImageButton buttoni = new ImageButton(this);
        soundSwitch = new Switch(this);
        for(int h = 0; h != sectorinf; h++){
            map2 = new HashMap<String, String>();
            map2.put("Name", myArrList.get(h).get("Name"));
            if(myArrList.get(h).get("Tel")!=null){map2.put("Tel", CommonTask.hexToAscii2(myArrList.get(h).get("Tel")));} else map2.put("Tel", myArrList.get(h).get("Tel"));

            myArrList2.add(map2);

        }
        AnimationDrawable myFrameAnimation;
        buttoni.setImageResource(R.drawable.okay);
        int bottom = 0;
        buttoni.setPadding(bottom, bottom, bottom , bottom);
        int top = buttoni.getPaddingTop();
        int right = buttoni.getPaddingRight();
        int left = buttoni.getPaddingLeft();
Log.i("uooi0",left+ top+ right+ bottom+"");
        buttoni.setBackgroundResource(R.drawable.spin_animation);
        buttoni.setPadding(bottom, bottom, bottom , bottom);
        buttoni.setLayoutParams(new LayoutParams(35, 20));
       // buttoni.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(soundSwitch, new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER_VERTICAL | Gravity.END));

        getSupportActionBar().setTitle(datsAsync3[8]);
        builder = new SpannableStringBuilder();
        SpannableString str1 = new SpannableString("ASCII");
        str1.setSpan(new ForegroundColorSpan(Color.rgb(255,255,255)), 0, str1.length(), 0);
        builder.append(str1);


        soundSwitch.setText(str1,TextView.BufferType.SPANNABLE);
        MyListAdapter myListAdapter = new MyListAdapter();
        itemlist.setItemsCanFocus(true);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(rwnfc.this, R.layout.list_item, R.id.secondLine, catNames);
        itemlist.setAdapter(myListAdapter);
        soundSwitch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("kbg","sbsjnbkjn");
                if(sw==0){
                    myArrListtemp=myArrList;
                    myArrList=myArrList2;
                    MyListAdapter myListAdapter = new MyListAdapter();
                    itemlist.setItemsCanFocus(true);
                    //ArrayAdapter<String> adapter = new ArrayAdapter<String>(rwnfc.this, R.layout.list_item, R.id.secondLine, catNames);
                    itemlist.setAdapter(myListAdapter); // TODO Auto-generated method stub

                    sw=1;
                }else  {
                    myArrList2=myArrList;
                    myArrList=myArrListtemp;MyListAdapter myListAdapter = new MyListAdapter();
                    itemlist.setItemsCanFocus(true);
                    //ArrayAdapter<String> adapter = new ArrayAdapter<String>(rwnfc.this, R.layout.list_item, R.id.secondLine, catNames);
                    itemlist.setAdapter(myListAdapter); // TODO Auto-generated method stub
                    sw=0;} }
        });

    }

    public void OnClickd(View view) {

    }

    public boolean dialog(String m, final String msg) {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);

        final Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        final String currentTime = df.format(c.getTime());

        final EditText edittext = new EditText(this);
        ad.setTitle("Save");
        ad.setIcon(R.drawable.ic_save_pink_48dp);// заголовок
        ad.setMessage("Enter name file:"); // сообщение
        ad.setView(edittext);
        edittext.setText(m);


        ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {


                String YouEditTextValue = edittext.getText().toString();
                paths = YouEditTextValue + "-" + currentTime;
                writeData(msg);
                Toast.makeText(getBaseContext(), "File Saved", Toast.LENGTH_SHORT).show();
                mn = true;
            }
        });
        ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

            }
        });
        ad.setCancelable(true);
        ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {

            }
        });
        ad.show();
        return mn;
    }

    public boolean dialogrepl(final String msg) {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);


        final TextView edittext = new TextView(this);
        ad.setTitle("File already exists");  // заголовок
        ad.setIcon(R.drawable.ic_save_pink_48dp);
        ad.setMessage("This file already exists:"); // сообщение
        ad.setView(edittext);
        edittext.setText("Please select");
        ad.setPositiveButton("REPLACE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

                paths = select.filemy.substring(0, select.filemy.length()-5);
                Log.i("kljk",paths+" size");
                writeData(msg);
                Toast.makeText(getBaseContext(), "File Saved", Toast.LENGTH_SHORT).show();
                mn = true;
            }
        });
        ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

            }
        });
        ad.setCancelable(true);
        ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {

            }
        });
        ad.show();
        return mn;
    }

    public boolean dialog2(final String m, final String msg) {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);


        final EditText edittext = new EditText(this);
        ad.setTitle("Save");  // заголовок
        ad.setIcon(R.drawable.ic_save_pink_48dp);
        ad.setMessage("Enter name file:"); // сообщение
        ad.setView(edittext);
        edittext.setText(m.substring(0, m.length() - 14));
        ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {


                String YouEditTextValue = edittext.getText().toString();
                paths = YouEditTextValue + m.substring(m.length() - 14, m.length() - 5);
                puth = select.filemypatch.substring(0, select.filemypatch.length() - select.filemy.length()) + paths + ".dump";
                Log.i("fbdbn", puth);
                File filet = new File(puth);
                Log.i("fbdbn", puth);
                if (filet.exists()) {
                    dialogrepl(msg);
                }
                writeData(msg);
                Toast.makeText(getBaseContext(), "File Saved", Toast.LENGTH_SHORT).show();
                mn = true;
            }
        });
        ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

            }
        });
        ad.setCancelable(true);
        ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {

            }
        });
        ad.show();
        return mn;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null) mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters,
                mTechLists);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAdapter != null) mAdapter.disableForegroundDispatch(this);
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
        mytags2 = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (action.equals(NfcAdapter.ACTION_TAG_DISCOVERED) ||
                action.equals(NfcAdapter.ACTION_TECH_DISCOVERED)) {
            if (r == 1) {
                dump.dialogProgress posts = new dump.dialogProgress();
                posts.execute();
            }

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        datsAsyncWrite = new String[sectorinf][blocks];
        String namefile = "";
        int sub = 0;
        int le=0;
String msga="";
        int id = item.getItemId();
        String msg = "";
        int num = blocks * 32;
        //noinspection SimplifiableIfStatement
        if (id == R.id.w) {

            for (int h = 0; h != sectorinf; h++) {
                Log.i("jbks", "ololonull");
                if ((datsAsync3[8].indexOf("MifareClassic") != -1)) {
                    Log.i("jbkp", "ololonull");
                    if ((CommonTask.isHexAnd16Byte(myArrList.get(h).get("Tel"), getBaseContext(), h, num))) {
                        Log.i("jbk", myArrList.get(h).get("Tel") + "ololo:" + h);
                        if ((myArrList.get(h).get("Tel") != null)) {
                            if ((myArrList.get(h).get("Tel").length() != 0)) {
                                for (int lm = 0; lm != blocks; lm++) {
                                    datsAsyncWrite[h][lm] = myArrList.get(h).get("Tel").substring(sub, sub + 32);
                                    sub = sub + 32;
                                    Log.i("jbk", datsAsyncWrite[h][lm] + "ololo");
                                }
                                sub = 0;
                            }
                        }
                    } else datsAsyncWrite = null;


                } else if ((datsAsync3[8].indexOf("NFC A") != -1)) {
                    if (myArrList.get(h).get("Tel") != null) {
                        if (!(CommonTask.isHexAnd16Byte(myArrList.get(h).get("Tel"), getBaseContext(), h, 8))) {
                            datsAsyncWrite = null;
                            break;
                        }
                        Log.i("jbktt", "ololonull");
                        if (myArrList.get(h).get("Tel").length() != 0) {

                            if (myArrList.get(h).get("Tel").length() == 8) {  Log.i("jbkl", sectorinf+"y"+h);
                                datsAsyncWrite[h][0] = myArrList.get(h).get("Tel");
                                for(int y=h+1;y!=h+4;y++){

                                    if(y==sectorinf){le=0;} else if (y>=sectorinf){le++;} else le=y;
                                    datsAsyncWrite[h][0] += myArrList.get(le).get("Tel");
                                    Log.i("jbktt", datsAsyncWrite[h][0]+" :"+y+":"+h);
                                }

                            } else datsAsyncWrite = null;
                        }
                    }
                } else datsAsyncWrite = null;
            }
            if (datsAsyncWrite != null) {
                Log.i("jbk", "uraa!!");
                text1.setVisibility(View.VISIBLE);
                text1.setText("Приложите NFC метку для записи данных ");
                itemlist.setVisibility(View.INVISIBLE);
                image.setVisibility(View.VISIBLE);
                r = 1;
                back=1;
            }
        }
        if (id == R.id.a) {
            for (int h = 0; h != sectorinf; h++) {
                msg += "{" + (myArrList.get(h).get("Name")) + "} ";
                msg += "{" + (myArrList.get(h).get("Tel")) + "} ";

                if ((datsAsync3[8].indexOf("MifareClassic") != -1)) {
                    namefile = "Mifare";
                    if (!(CommonTask.isHexAnd16Byte(myArrList.get(h).get("Tel"), getBaseContext(), h, num))) {
                        msg = null;
                        break;
                    }
                } else if((datsAsync3[8].indexOf("NFC A") != -1)) {
                    Log.i("jbk", h + "troololo" + h);
                    if (!(CommonTask.isHexAnd16Byte(myArrList.get(h).get("Tel"), getBaseContext(), h, 8))) {
                        msg = null;
                        break;
                    }
                }
            }
            if (msg != null) {
                if (!MainActivity.select) {
                    namefile += "Tag-" + datsAsync3[2].substring(0, 6);
                    dialog(namefile, msg);
                } else dialog2(select.filemy, msg);
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
                // define the animation for rotation

   //ImageButton b item.getActionView().findViewById(R.id.w);


        return true;
    }
    @Override
    public void onBackPressed() {
     if(back==1){text1.setVisibility(View.INVISIBLE);
         itemlist.setVisibility(View.VISIBLE);
         image.setVisibility(View.INVISIBLE); back=0;} else finish();

    }
    public void writeDump(ArrayList<HashMap<String, String>> myArrListd, int sectorin, int blocksd) {
        datsAsyncWrite = new String[sectorin][blocksd];

        int sub = 0;
        for (int h = 0; h != sectorin; h++) {
            if (blocksd >= 4) {
                Log.i("jbk", h + "troololo" + sectorin);
                if ((myArrListd.get(h).get("Tel") != null)) {
                    if ((myArrListd.get(h).get("Tel").indexOf("null")) == -1) {
                        if ((myArrListd.get(h).get("Tel").length() != 0)) {
                            for (int lm = 0; lm != blocksd; lm++) {
                                datsAsyncWrite[h][lm] = myArrListd.get(h).get("Tel").substring(sub, sub + 32);
                                sub = sub + 32;
                                Log.i("jbk", datsAsyncWrite[h][lm] + "ololo");
                            }
                            sub = 0;
                        }
                    }
                }
            }
        }
        if (datsAsyncWrite != null) {

            text1.setVisibility(View.VISIBLE);
            itemlist.setVisibility(View.INVISIBLE);
            image.setVisibility(View.VISIBLE);
            text1.setText("Приложите NFC метку, чтобы записать данные ");
            r = 1;
            select.dumpWr = false;
            back=1;
            Log.i("jbk", "uraas!!"+back);
        }

    }

    public void writeData(String data) {
        try {
            Context context = getApplicationContext();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(paths + ".dump", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (Exception e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if (sectorinf != 0) {
                return sectorinf;
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return myArrList.get(position).get("Name");
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //ViewHolder holder = null;
            final ViewHolder holder;
            if (convertView == null) {

                holder = new ViewHolder();
                LayoutInflater inflater = dump.this.getLayoutInflater();
                convertView = inflater.inflate(R.layout.list_dumpp, null);
                holder.textView1 = (TextView) convertView.findViewById(R.id.firstLine);
                holder.editText1 = (EditText) convertView.findViewById(R.id.secondLine);
                convertView.setTag(holder);

            } else {

                holder = (ViewHolder) convertView.getTag();
            }
            holder.ref = position;
            builder = new SpannableStringBuilder();
            try {
                //Log.i("vnmjf",myArrList.get(holder.ref).get("Tel").length()+"j,");
                if ((myArrList.get(holder.ref).get("Name")).length() >10) {
                    SpannableString str1 = new SpannableString(myArrList.get(position).get("Name"));
                    str1.setSpan(new RelativeSizeSpan(0.8f), 0,str1.length(), 0);
                    str1.setSpan(new ForegroundColorSpan(Color.RED), 0, str1.length(), 0);
                    builder.append(str1);
                    holder.textView1.setText(str1, EditText.BufferType.SPANNABLE);
                    holder.editText1.setEnabled(false);
                    holder.editText1.setText(myArrList.get(position).get("Tel"));
                } else {
                    if((myArrList.get(position).get("Tel")).length()>=32*4&(myArrList.get(position).get("Tel")).length()<=32*4+5) {
                        holder.textView1.setText(myArrList.get(position).get("Name"));
                        SpannableString str1 = new SpannableString(myArrList.get(position).get("Tel"));
                        str1.setSpan(new ForegroundColorSpan(Color.rgb(0,0,139)), str1.length() - 12, str1.length(), 0);
                    str1.setSpan(new ForegroundColorSpan(Color.rgb(0,81,230)), str1.length() - 32, str1.length()-20, 0);
                        str1.setSpan(new ForegroundColorSpan(Color.rgb(54,154,0)), str1.length() - 20, str1.length()-12, 0);
                        builder.append(str1);
                        holder.editText1.setEnabled(true);
                        holder.editText1.setText(str1, EditText.BufferType.SPANNABLE);
                    } else {holder.editText1.setText(myArrList.get(position).get("Tel")); holder.textView1.setText(myArrList.get(position).get("Name"));}
                }
            } catch (Exception e) {
            }





            holder.editText1.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    try {
                        Log.i("vnmjf",myArrList.get(holder.ref).get("Name").length()+"j,");
                        if (((myArrList.get(holder.ref).get("Name")).length() >10)) {
                            holder.editText1.setEnabled(false);
                        } else {
                            holder.editText1.setEnabled(true);
                        }
                    } catch (Exception e) {
                    }
                    // TODO Auto-generated method stub
                    Log.i("Exception", "onTextChanged " + myArrList.get(holder.ref).get("Name"));
                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                              int arg3) {
                    Log.i("vnmjf",myArrList.get(holder.ref).get("Name").length()+"j,");
                    Log.i("Exception", "beforeTextChanged " + myArrList.get(holder.ref).get("Name"));
                    try {
                        if (((myArrList.get(holder.ref).get("Name")).length() > 10)) {
                            holder.editText1.setEnabled(false);
                        } else {
                            holder.editText1.setEnabled(true);
                        }
                    } catch (Exception e) {
                    }// TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub
                    Log.i("vnmjf",myArrList.get(holder.ref).get("Name").length()+"j,");
                    Log.i("Exception", "AfterTextChanged" + myArrList.get(holder.ref).get("Name"));
                    try {
                        if (((myArrList.get(holder.ref).get("Name")).length() > 10)) {
                            holder.editText1.setEnabled(false);
                        } else {
                            holder.editText1.setEnabled(true);
                        }
                    } catch (Exception e) {
                    }
                    myArrList.get(holder.ref).put("Tel", arg0.toString());
                }
            });


            return convertView;
        }

        private class ViewHolder {
            TextView textView1;
            EditText editText1;
            int ref;
        }

    }

    class dialogProgress extends AsyncTask<Void, Void, Void> {
       // AlertDialog alertDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //alertDialog = new AlertDialog.Builder(dump.this);
            text1.setVisibility(View.INVISIBLE);
            image.setVisibility(View.INVISIBLE);
            dialog = ProgressDialog.show(dump.this, "",
                    "Запись данных... Пожалуйста, будьте терпиливыми!", true);
            dialog.getWindow().setLayout(900, 260);
            Log.i("fbdbn", "asynctusk");

        }

        @Override
        protected Void doInBackground(Void... params) {
            // MifareClass.writeBlock(mytags2,sectorinf,blocks,datsAsyncWrite);
            if ((datsAsync3[8].indexOf("MifareClassic") != -1)) {
                MifareClass.writeBlock(mytags2, sectorinf, blocks, datsAsyncWrite);
                Log.i("fbdbkn", "pp" + datsAsyncWrite[7][2]);
            } else if ((datsAsync3[8].indexOf("NFC A") != -1)) {
                Log.i("fbdbn", "ppd" + "NFCA");
                Nfcaclass.writeTag(mytags2,sectorinf,0,datsAsyncWrite);
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            dialog.dismiss();
            image.setVisibility(View.INVISIBLE);
            r = 0;
            builder = new SpannableStringBuilder();
            SpannableString str1 = new SpannableString("Запись данных завершена!");
            str1.setSpan(new RelativeSizeSpan(1.34f), 0,str1.length(), 0);
            str1.setSpan(new ForegroundColorSpan(Color.WHITE), 0, str1.length(), 0);
            builder.append(str1);
            AlertDialog.Builder builder = new AlertDialog.Builder(dump.this);
            builder.setTitle(str1)
                    .setMessage("Проверьте изменения. Если запись не произошла добавьте ключ для редактирования и повторите попытку")
                    .setIcon(R.drawable.ic_launcher)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent activinf = new Intent(dump.this, wrnfc.class);
                            activinf.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(activinf);
                            dialog.cancel();
                        }
                    });
            builder.create();
            builder.show();
        }
    }
}