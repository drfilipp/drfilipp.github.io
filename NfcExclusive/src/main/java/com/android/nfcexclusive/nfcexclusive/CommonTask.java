package com.android.nfcexclusive.nfcexclusive;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by c0de on 18.05.2017.
 */

public class CommonTask {
    public static ArrayList<byte[]> mKeysWithOrder;
    public static final byte[] KEY_B =
            {(byte) 0xE3, (byte) 0x51, (byte) 0x73, (byte) 0x49, (byte) 0x4A, (byte) 0x81};
    public static final byte[] KEY_A =
            {(byte) 0xA7, (byte) 0x3F, (byte) 0x5D, (byte) 0xC1, (byte) 0xD3, (byte) 0x33};

    public static final byte[] KEY_0 =
            {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
    static byte[][] datas;
    static String def = "";

    static int check = 3;
    static String my = "";

    public static byte[] getKeys(int n) {
        byte[][] data = new byte[10][10];


        data[0] = hexStringToByteArray("a0a1a2a3a4a5");
        data[1] = hexStringToByteArray("000000000001");
        data[2] = hexStringToByteArray("ffffffffffff");
        data[3] = hexStringToByteArray("a73f5dc1d333"); //73068f118c13
        data[4] = hexStringToByteArray("000000000001"); //2b7f3253fac5
        data[5] = hexStringToByteArray("73068f118c13");//0f1c63013dba
        data[6] = hexStringToByteArray("0f1c63013dba"); //12 ->6
        data[7] = hexStringToByteArray("ae3d65a3dad4");
        return data[n];
    }

    public static byte[] getKeyssize(int n) {
        if (n == 0) {
            getKeyssize2();
        }
        if (n > getKeyssizer()) {
            return datas[0];
        }
        return datas[n];
    }

    public static byte[][] getKeyssize2() {
        byte[][] data;
        int size = 0;
        int sub = 0;
        int def1 = 0;
        def = readFromFile("defkey.keys");
        my = readFromFile("mykey.keys");
        if (check == 3) {
            size = (my.length() / 12) + (def.length() / 12);
        }
        if (check == 2) {
            size = (my.length() / 12);
        }
        if (check == 1) {
            size = (def.length() / 12);
        }
        datas = new byte[size][size];
        data = new byte[size][size];
        if (check == 3 | check == 1) {
            for (def1 = 0; def1 != def.length() / 12; def1++) {
                data[def1] = CommonTask.hexStringToByteArray(def.substring(sub, sub + 12));
                sub = sub + 12;
            }
        }
        sub = 0;
        if (check == 3 | check == 2) {
            for (int def12 = 0; def12 != my.length() / 12; def12++) {

                data[def1] = CommonTask.hexStringToByteArray(my.substring(sub, sub + 12));
                sub = sub + 12;
                def1++;
            }
        }
        Log.i("check", check + "");
        sub = 0;
        datas = data;
        return datas;
    }

    public static int getKeyssizer() {
        int def1 = 0;
        if (check == 3) {
            def1 = (my.length() / 12) + (def.length() / 12);
        }
        if (check == 2) {
            def1 = (my.length() / 12);
        }
        if (check == 1) {
            def1 = (def.length() / 12);
        }
        return def1 - 1;
    }

    public static int getKeyssizer1() {
        int def1 = 0;
        def1 = (def.length() / 12);
        return def1 - 1;
    }

    public static String readFromFile(String patchm) {
        Context context = MainActivity.context2;
        String ret = "";
        Log.i("key", "readfile" + patchm);
        try {
            InputStream inputStream = context.openFileInput(patchm);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    public static ArrayList<File> listFilesWithSubFolders(File dir) {
        ArrayList<File> files = new ArrayList<File>();
        for (File file : dir.listFiles()) {
            if (file.isDirectory())
                files.addAll(listFilesWithSubFolders(file));
            else
                files.add(file);
        }
        return files;
    }

    public static String byte2HexString(byte[] bytes) {
        String ret = "";
        if (bytes != null) {
            for (Byte b : bytes) {
                ret += String.format("%02X", b.intValue() & 0xFF);
            }
        }
        return ret;
    }

    public static final byte[] HEX_CHAR_TABLE = {
            (byte) '0', (byte) '1', (byte) '2', (byte) '3',
            (byte) '4', (byte) '5', (byte) '6', (byte) '7',
            (byte) '8', (byte) '9', (byte) 'a', (byte) 'b',
            (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f'
    };

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        try {
            for (int i = 0; i < len; i += 2) {
                data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                        + Character.digit(s.charAt(i + 1), 16));
            }
        } catch (Exception e) {

        }
        return data;
    }

    public static String hexToAscii(String hexStr) {
        hexStr = hexStr.replaceAll("\\[NOT__KEY_A]", "");
        hexStr = hexStr.replaceAll("\\[NOT__KEY_B]", "");
        hexStr = hexStr.replaceAll("\n", "");
        StringBuilder output = new StringBuilder("");

        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            Log.i("gd", str);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }

    public static String hexToAscii2(String hexStr) {
        String out = "";
        hexStr = hexStr.replaceAll("\\[NOT__KEY_A]", "");
        hexStr = hexStr.replaceAll("\\[NOT__KEY_B]", "");
        hexStr = hexStr.replaceAll("\n", "");
        StringBuilder output = new StringBuilder("");
        int sub = 0;
        int subplus = 32;
        if (hexStr.length() == 8) {
            subplus = 8;
            hexToAscii(hexStr);
        } else for (int m = 0; m != (hexStr.length() / 32); m++) {
            out += hexToAscii(hexStr.substring(sub, sub += subplus));
            out.replaceAll("\\s+", ".");
            out += "\n";
        }
        ;
        return out;
    }

    public static String getHexString(byte[] raw)
            throws UnsupportedEncodingException {
        byte[] hex = new byte[2 * raw.length];
        int index = 0;

        for (byte b : raw) {
            int v = b & 0xFF;
            hex[index++] = HEX_CHAR_TABLE[v >>> 4];
            hex[index++] = HEX_CHAR_TABLE[v & 0xF];
        }
        return new String(hex, "ASCII");
    }

    public static boolean isHexAnd16Byte(String hexString, Context context, int nomer, int num) {
        try {
            String newhex = "";
            Log.i("wtf", hexString);
            if (hexString != null) {
                if (hexString.length() != 0) {
                    newhex = hexString.replaceAll("\\[NOT__KEY_A]", "");
                    newhex = newhex.replaceAll("\\[NOT__KEY_B]", "");
                    newhex = newhex.replaceAll("\n", "");
                    //Log.i("Exception", hexString.length()+"File write failed:"+ hexString);
                    if ((!newhex.matches("[0-9A-Fa-f]+"))) {
                        // Error, not hex.
                        Toast.makeText(context, "Строка не имеет формата HEX в секторе " + nomer,
                                Toast.LENGTH_LONG).show();
                        return false;
                    }

                    hexString = hexString.replaceAll("\n", "");
                    if (hexString.length() != num) {
                        // Error, not 16 byte (32 chars).
                        if (num == 8) {
                            Toast.makeText(context, "Блок в секторе " + nomer + " не из " + 4 + " байтов ( " + num + " символов)",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Блок в секторе " + nomer + " не из 16 байтов (32 символа)",
                                    Toast.LENGTH_LONG).show();
                        }
                        return false;
                    }
                }
            }

        } catch (Exception e) {
        }
        return true;
    }

    public static boolean isHexAnd12Byte(String hexString, Context context, int nomer, int num) {
        try {
            String newhex = "";
            if (hexString != null) {
                if (hexString.length() != 0) {
                    newhex = hexString.replaceAll("\\-", "");
                    //Log.i("Exception", hexString.length()+"File write failed:"+ hexString);
                    if ((!newhex.matches("[0-9A-Fa-f]+"))) {
                        // Error, not hex.
                        Toast.makeText(context, "Строка не имеет формата HEX в строке " + nomer,
                                Toast.LENGTH_LONG).show();
                        return false;
                    }

                    if (hexString.length() != num) {
                        // Error, not 16 byte (32 chars).
                        Toast.makeText(context, "Строка" + nomer + " не 12 символов",
                                Toast.LENGTH_LONG).show();
                        return false;
                    }
                }
            }

        } catch (Exception e) {
        }
        return true;
    }

    public static boolean isHexAnd1Byte(String hexString, Context context) {
        try {
            String newhex = "";
            if (hexString != null) {
                if (hexString.length() != 0) {
                    newhex = hexString.replaceAll("\\-", "");
                    //Log.i("Exception", hexString.length()+"File write failed:"+ hexString);
                    if ((!newhex.matches("[0-9A-Fa-f]+"))) {
                        // Error, not hex.
                        Toast.makeText(context, "Строка не имеет формата HEX",
                                Toast.LENGTH_LONG).show();
                        return false;
                    }

                }
            }

        } catch (Exception e) {
        }
        return true;
    }

}
