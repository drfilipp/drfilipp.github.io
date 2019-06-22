package com.android.nfcexclusive.nfcexclusive;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class dumps extends AppCompatActivity {
    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems = new ArrayList<String>();
    ListView itemlist;
    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;
    int i;
    Switch soundSwitch;
    String paths="";
    String[] datsAsync3 = new String[120];
    String[] datsAsync3temp = new String[120];
    int bs;
    SpannableStringBuilder builder;
    int sectorinf = 0;
    int formul;
    int sw=0;
    int blocks;
    ArrayList<HashMap<String, String>> myArrList;
    ArrayList<HashMap<String, String>> myArrList2= new ArrayList<HashMap<String, String>>();
    //RECORDING HOW MANY TIMES THE BUTTON HAS BEEN CLICKED
    int clickCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dumps);
        itemlist = (ListView) findViewById(R.id.listView2);
        datsAsync3 = rwnfc.datsAsync2;
        myArrList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;
        HashMap<String, String> map2;
        map2 = new HashMap<String, String>();
        map = new HashMap<String, String>();
        //map.put("Name", datsAsync3[9]);
        //map.put("Tel", datsAsync3[10]);
        // Log.i("mmmmm","datsAsyn");

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
                }
            } else {
                map.put("Name", datsAsync3[i]);
            }            // Log.i(String.valueOf(bs), datsAsync3[i]);
        } catch (Exception e) { //map.put("Name", datsAsync3[i]);
            map.put("Name", datsAsync3[i]);
        }

            sectorinf++;
            myArrList.add(map);
}
    } catch (Exception e) {}
         soundSwitch = new Switch(this);
        for(int h = 0; h != sectorinf; h++){
            map2 = new HashMap<String, String>();
            map2.put("Name", myArrList.get(h).get("Name"));
            if(myArrList.get(h).get("Tel")!=null){map2.put("Tel", CommonTask.hexToAscii2(myArrList.get(h).get("Tel")));} else map2.put("Tel", myArrList.get(h).get("Tel"));

            myArrList2.add(map2);

        }
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
        MySimpleAdapter adapter = new MySimpleAdapter(dumps.this, myArrList, R.layout.list_dump, new String[]{"Name", "Tel"},
                new int[]{R.id.firstLine, R.id.secondLine});
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(rwnfc.this, R.layout.list_item, R.id.secondLine, catNames);
        itemlist.setAdapter(adapter);
        soundSwitch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("kbg","sbsjnbkjn");
                if(sw==0){
                MySimpleAdapter adapter = new MySimpleAdapter(dumps.this, myArrList2, R.layout.list_dump, new String[]{"Name", "Tel"},
                        new int[]{R.id.firstLine, R.id.secondLine});
                //ArrayAdapter<String> adapter = new ArrayAdapter<String>(rwnfc.this, R.layout.list_item, R.id.secondLine, catNames);
                itemlist.setAdapter(adapter); // TODO Auto-generated method stub
                    sw=1;
            }else  {MySimpleAdapter adapter = new MySimpleAdapter(dumps.this, myArrList, R.layout.list_dump, new String[]{"Name", "Tel"},
                        new int[]{R.id.firstLine, R.id.secondLine});
                //ArrayAdapter<String> adapter = new ArrayAdapter<String>(rwnfc.this, R.layout.list_item, R.id.secondLine, catNames);
                itemlist.setAdapter(adapter); // TODO Auto-generated method stub
                sw=0;} }
        });

    }
    public boolean dialog(String m, final String msg) {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        final String  currentTime = df.format(c.getTime());
        final EditText edittext = new EditText(this);
        ad.setTitle("Save");
        ad.setIcon(R.drawable.ic_save_pink_48dp);// заголовок
        ad.setMessage("Enter name file:"); // сообщение
        ad.setView(edittext);
        edittext.setText(m);
        ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {


                String YouEditTextValue = edittext.getText().toString();
                paths=YouEditTextValue+ "-" + currentTime;
                writeData(msg);
                Toast.makeText(getBaseContext(), "File Saved", Toast.LENGTH_SHORT).show();
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
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        String namefile ="";
        final ArrayList<HashMap<String, String>> myArrList2= new ArrayList<HashMap<String, String>>();;
        Calendar c = Calendar.getInstance();
        int id = item.getItemId();
        String msg = "";
        int num =blocks*32;
        HashMap<String, String> map;
        map = new HashMap<String, String>();
        final HashMap<String, String> map2;
        map2 = new HashMap<String, String>();
                /*for(int h = 0; h != sectorinf; h++){
                    map2.put("Name", myArrList.get(h).get("Name"));
                    map2.put("Tel", CommonTask.hexToAscii(myArrList.get(h).get("Tel")));
                    myArrList2.add(map2);

                }
                MySimpleAdapter adapter = new MySimpleAdapter(dumps.this, myArrList2, R.layout.list_dump, new String[]{"Name", "Tel"},
                        new int[]{R.id.firstLine, R.id.secondLine});
                //ArrayAdapter<String> adapter = new ArrayAdapter<String>(rwnfc.this, R.layout.list_item, R.id.secondLine, catNames);
                itemlist.setAdapter(adapter); // TODO Auto-generated method stub*/
        //noinspection SimplifiableIfStatement
        if (id == R.id.a) {

            for (int h = 0; h != sectorinf; h++) {
                msg += "{" + (myArrList.get(h).get("Name")) + "} ";
                msg += "{" + (myArrList.get(h).get("Tel")) + "} ";
                if((datsAsync3[8].indexOf("MifareClassic")!=-1)) {
                    namefile="Mifare";
                    if (!(CommonTask.isHexAnd16Byte(myArrList.get(h).get("Tel"), getBaseContext(), h,num))) {
                        msg = null;
                        break;
                    }
                }
            }
            if (msg != null) {
                namefile +="Tag-" +datsAsync3[2].substring(0, 6);
                dialog(namefile,msg);
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_read, menu);
       // Switch switch1= (Switch)menu.findItem(R.id.myswitch).getActionView().findViewById(R.id.switch1);

        return true;
    }

    public void writeData(String data) {
        try {
            Context context = getApplicationContext();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(paths+".dump", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (Exception e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    public class MySimpleAdapter extends SimpleAdapter {
        private ArrayList<HashMap<String, String>> myArrList;

        public MySimpleAdapter(Context context, ArrayList<HashMap<String, String>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            this.myArrList = data;
        }
int sel=0;
        int pos1=0;
        int pos2=0;
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null)

            {

                holder = new ViewHolder();
                LayoutInflater inflater = dumps.this.getLayoutInflater();
                convertView = inflater.inflate(R.layout.list_dump, null);
                holder.textView1 = (TextView) convertView.findViewById(R.id.firstLine);
                holder.textView2 = (TextView) convertView.findViewById(R.id.secondLine);
                holder.textView2.setTextIsSelectable(true);
                convertView.setTag(holder);

            } else

            {

                holder = (ViewHolder) convertView.getTag();
            }

            holder.ref = position;
            if ((myArrList.get(position).get("Tel")) !=null) {
                holder.textView1.setText(myArrList.get(position).get("Name"));
                if((myArrList.get(position).get("Tel")).length()>=32*4&(myArrList.get(position).get("Tel")).length()<=32*4+5) {
                    builder = new SpannableStringBuilder();
                    SpannableString str1 = new SpannableString(myArrList.get(position).get("Tel"));
                    Log.i("Exception", myArrList.get(position).get("Tel") + "dt");
                    str1.setSpan(new ForegroundColorSpan(Color.rgb(0,0,139)), str1.length() - 12, str1.length(), 0);
                    str1.setSpan(new ForegroundColorSpan(Color.rgb(0,81,230)), str1.length() - 32, str1.length()-20, 0);
                    str1.setSpan(new ForegroundColorSpan(Color.rgb(54,154,0)), str1.length() - 20, str1.length()-12, 0);
                    builder.append(str1);
                    holder.textView2.setText(str1, EditText.BufferType.SPANNABLE);
                } else { holder.textView2.setText(myArrList.get(position).get("Tel"));}
            } else {
                builder = new SpannableStringBuilder();
                SpannableString str1 = new SpannableString(myArrList.get(position).get("Name"));
                str1.setSpan(new RelativeSizeSpan(0.8f), 0,str1.length(), 0);
                str1.setSpan(new ForegroundColorSpan(Color.RED), 0, str1.length(), 0);
                builder.append(str1);
                holder.textView1.setText(str1,EditText.BufferType.SPANNABLE);
                holder.textView2.setText(myArrList.get(position).get("Tel"));
            }
            holder.textView2.setTextIsSelectable(false);
            holder.textView2.setFocusable(false);
            holder.textView2.setFocusableInTouchMode(false);
            holder.textView2.setTextIsSelectable(true);
            holder.textView2.setFocusable(true);
            holder.textView2.setFocusableInTouchMode(true);
            holder.textView2.clearFocus();
            holder.textView1.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    holder.textView2.setTextIsSelectable(false);
                    holder.textView2.setFocusable(false);
                    holder.textView2.setFocusableInTouchMode(false);
                    holder.textView2.setTextIsSelectable(true);
                    holder.textView2.setFocusable(true);
                    holder.textView2.setFocusableInTouchMode(true);
                    holder.textView2.clearFocus();
                }
            });

            holder.textView2.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    Log.i("bs", "truessss1");

                    holder.textView2.setTextIsSelectable(false);
                    holder.textView2.setFocusable(false);
                    holder.textView2.setFocusableInTouchMode(false);
                    holder.textView2.setTextIsSelectable(true);
                    holder.textView2.setFocusable(true);
                    holder.textView2.setFocusableInTouchMode(true);
                    holder.textView2.clearFocus();
                }

            });

            return convertView;
        }

        private class ViewHolder {
            TextView textView1;
            TextView textView2;
            int ref;
        }
    }
    }


