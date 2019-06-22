package com.android.nfcexclusive.nfcexclusive;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

public class select extends AppCompatActivity {
    ArrayList<HashMap<String, String>> myArrList;
    ListView itemlist;
    Button button;
    FloatingActionButton fab;
    File my;
    static int blocksd = 1;
    static ArrayList<HashMap<String, String>> myArrList2;
    static ArrayList<HashMap<String, String>> myArrList3 = new ArrayList<HashMap<String, String>>();;
    int positionf = 0;
    Menu menus;
    static String filemy;
    static String filemypatch;
    File[] comp2;
    static int start = 0;
    static boolean dumpWr = false;
    File[] comp;
    int comps = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        itemlist = (ListView) findViewById(R.id.listView);
        TextView text = (TextView) findViewById(R.id.textView4);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select");
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filemypatch = comp2[positionf].getPath();
                filemy = comp2[positionf].getName();
                parse(comp2[positionf].getName());
            }
        });
        fab.setVisibility(View.INVISIBLE);
        ArrayList<File> files = new ArrayList<File>();
        String paths;
        paths = getApplicationContext().getFilesDir().getParent() + "/files";
        File file = new File(paths);
        files = CommonTask.listFilesWithSubFolders(file);
        File[] fSorted = files.toArray(new File[files.size()]);
        comp2 = new File[fSorted.length];
        comp = new File[fSorted.length];
        myArrList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;
        map = new HashMap<String, String>();
        for (int c = 0; c != files.size(); c++) {
            if (fSorted[c].getName().indexOf("dump") != -1) {
                comp[comps] = fSorted[c];
                comps++;
            }
        }
        for (int m = 0; m != comps; m++) {
            map = new HashMap<String, String>();

            comp2[m] = compare(comp);
            Log.i("bs", comp2[m].getName() + "n,");
            map.put("Name", comp2[m].getName().substring(0, comp2[m].getName().length() - 14));
            Date lastModDate = new Date(comp2[m].lastModified());
String date=lastModDate.toString();
            date=date.substring(0,date.length()-14)+date.substring(date.length()-4,date.length());
            map.put("Tel", date);
            myArrList.add(map);

        }

if(comps==0){text.setVisibility(View.VISIBLE);}
        MySimpleAdapter adapter = new MySimpleAdapter(select.this, myArrList, R.layout.list_select, new String[]{"Name", "Tel"},
                new int[]{R.id.firstLine, R.id.secondLine});
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(rwnfc.this, R.layout.list_item, R.id.secondLine, catNames);
        itemlist.setAdapter(adapter);

    }
    @Override
    public void onBackPressed() {
       Intent activinf = new Intent(select.this, MainActivity.class);
        activinf.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(activinf);
    }
    public boolean dialog() {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        final String  currentTime = df.format(c.getTime());
        final EditText edittext = new EditText(this);
        ad.setTitle("Delete");  // заголовок
        ad.setMessage("Do you want to delete this dump?"); // сообщение
       // ad.setView(edittext);
       // edittext.setText(m);
        ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                comp2[positionf].delete();
                myArrList.remove(positionf);
                Toast.makeText(getBaseContext(), "Dump Removed", Toast.LENGTH_SHORT).show();
                Intent activinf = new Intent(select.this, select.class);
                activinf.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(activinf);


            }
        });
        ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
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

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.a) {
            dialog();

        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select, menu);
        menus=menu;

        return true;
    }
    public void parse(String p) {
        int end = 0;
        int sub = 0;
        start = 0;
        int bl=0;
        myArrList2 = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;

        String msg;
        String temp = "";
        msg = CommonTask.readFromFile(p);


        while (msg.length() != 1) {
            map = new HashMap<String, String>();
            Log.i("bsstart", msg.length() + "tr");
            map.put("Name", msg.substring(msg.indexOf("{") + 1, msg.indexOf("}")));
            msg = msg.substring(msg.indexOf("}") + 1, msg.length());
            if (((msg.substring(msg.indexOf("{") + 1, msg.indexOf("}")).indexOf("null")) == -1) & (msg.substring(msg.indexOf("{") + 1, msg.indexOf("}")).length() != 0)) {
                if ((msg.substring(msg.indexOf("{") + 1, msg.indexOf("}")).length()) > 6) {
                    blocksd = msg.substring(msg.indexOf("{") + 1, msg.indexOf("}")).length();
                     bl=blocksd;
                    Log.i("fbdbnkkkk", blocksd + "mife");
                    if (bl== 32*4) {
                        blocksd = 4;
                    } else blocksd = 1;
                }
                if (blocksd == 4) {
                    temp = msg.substring(msg.indexOf("{") + 1, msg.indexOf("}")).substring(sub, sub + 32);
                    sub = sub + 32;

                    for (int k = 1; k != blocksd; k++) {
                        Log.i("fbdbnkllllllkkk","mifelllllll"+temp);
                        temp += "\n"+msg.substring(msg.indexOf("{") + 1, msg.indexOf("}")).substring(sub, sub + 32);
                        sub = sub + 32;

                    }map.put("Tel", temp);
                } else map.put("Tel", msg.substring(msg.indexOf("{") + 1, msg.indexOf("}")));
            }
            sub = 0;
            Log.i("fbdbnkllllllkkk", blocksd + "mife");

            msg = msg.substring(msg.indexOf("}") + 1, msg.length());
            Log.i("bss", msg.length() + "tr");
            start++;
            myArrList2.add(map);
        }
        if (myArrList2 != null) {
            MainActivity.select_CHECK=true;
            if (!MainActivity.select) dumpWr = true;
            Intent activinf = new Intent(select.this, dump.class);
            startActivity(activinf);
        }
    }

    public File compare(File[] files) {
        int i = 0;
        int iif = 0;
        File lastModifiedFile = files[0];
        if (files[0] == null) {
            for (i = 1; i < files.length; i++) {
                if (files[i] != null) {
                    lastModifiedFile = files[i];
                }
            }
        }
        for (i = 1; i < comps; i++) {

            if (files[i] != null) {
                if (lastModifiedFile.lastModified() < files[i].lastModified()) {
                    Log.i("bss", files[i].getName());
                    lastModifiedFile = files[i];
                    Log.i("iif", i + "k");
                    iif = i;
                }
            }
        }
        Log.i("ii", i + "k");
        comp[iif] = null;
        return lastModifiedFile;
    }

    public class MySimpleAdapter extends SimpleAdapter {
        private ArrayList<HashMap<String, String>> myArrList;
        private RadioButton mSelectedRB;
        private int mSelectedPosition = -1;

        public MySimpleAdapter(Context context, ArrayList<HashMap<String, String>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            this.myArrList = data;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final MySimpleAdapter.ViewHolder holder;
            if (convertView == null)

            {

                holder = new MySimpleAdapter.ViewHolder();
                LayoutInflater inflater = select.this.getLayoutInflater();
                convertView = inflater.inflate(R.layout.list_select, null);
                holder.textView1 = (TextView) convertView.findViewById(R.id.firstLine);
                holder.textView2 = (TextView) convertView.findViewById(R.id.secondLine);
                holder.button = (RadioButton) convertView.findViewById(R.id.radioButton);
                RadioButton button1 = (RadioButton) convertView.findViewById(R.id.radioButton);
                button1.setSelected(false);
                convertView.setTag(holder);

            } else

            {

                holder = (MySimpleAdapter.ViewHolder) convertView.getTag();
            }
            RadioButton r = (RadioButton) convertView.findViewById(R.id.radioButton);
            holder.ref = position;
            holder.textView1.setText(myArrList.get(position).get("Name"));
            holder.textView2.setText(myArrList.get(position).get("Tel"));
            holder.button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (position != mSelectedPosition && mSelectedRB != null) {
                        mSelectedRB.setChecked(false);
                        holder.button.setChecked(true);
                        positionf = position;

                    } else {
                        holder.button.setChecked(true);
                        positionf = position;
                    }
                    fab.setVisibility(View.VISIBLE);
                    menus.setGroupVisible(R.id.groupVsbl, true);

                    mSelectedPosition = position;
                    mSelectedRB = (RadioButton) v;
                }

            });
            holder.textView1.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    if (position != mSelectedPosition && mSelectedRB != null) {
                        mSelectedRB.setChecked(false);
                        holder.button.setChecked(true);
                        positionf = position;
                    } else {
                        holder.button.setChecked(true);
                        positionf = position;
                    }
                    fab.setVisibility(View.VISIBLE);
                    menus.setGroupVisible(R.id.groupVsbl, true);
                    mSelectedPosition = position;
                    mSelectedRB = holder.button;

                }
            });

            holder.textView2.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    Log.i("bs", "truessss1");


                    if (position != mSelectedPosition && mSelectedRB != null) {
                        mSelectedRB.setChecked(false);
                        holder.button.setChecked(true);
                        positionf = position;
                    } else {
                        holder.button.setChecked(true);
                        positionf = position;
                    }
                    fab.setVisibility(View.VISIBLE);
                    menus.setGroupVisible(R.id.groupVsbl, true);
                    mSelectedPosition = position;
                    mSelectedRB = holder.button;

                }

            });
            if (mSelectedPosition != position) {
                holder.button.setChecked(false);
            } else {
                positionf = position;
                holder.button.setChecked(true);
                fab.setVisibility(View.VISIBLE);
                menus.setGroupVisible(R.id.groupVsbl, true);
                if (mSelectedRB != null && holder.button != mSelectedRB) {
                    mSelectedRB = holder.button;
                }
            }
            return convertView;
        }

        private class ViewHolder {
            TextView textView1;
            TextView textView2;
            RadioButton button;
            int ref;
        }
    }
}
