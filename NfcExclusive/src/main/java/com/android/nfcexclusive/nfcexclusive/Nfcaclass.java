package com.android.nfcexclusive.nfcexclusive;

import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.util.Log;

import java.io.IOException;
import java.nio.charset.Charset;

import static java.lang.Thread.sleep;

/**
 * Created by c0de on 18.05.2017.
 */

public class Nfcaclass {
    public static boolean writeTag(Tag tag, int sectorinf, int block, String[][] datsAsyncWrite ) {
        byte[] DEF;
boolean success=false;
int p=0;
        NfcA nfca = NfcA.get(tag);



        try {
            nfca.connect();

            while (!success) {
                for (int t = p; t != sectorinf; t++) {
                    Log.i("aaaaa", t + "sect" + datsAsyncWrite[t][0]);
                    DEF = CommonTask.hexStringToByteArray(datsAsyncWrite[t][0]);
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                    nfca.transceive(new byte[]{
                            (byte) 0xA2,  // WRITE
                            (byte) (t & 0x0ff),
                            DEF[0], DEF[1], DEF[2], DEF[3]

                    });
                        success=true;
                    }

                    catch (IOException e) {
                            e.printStackTrace();
                            success=false;
                        break;
                        }


                }
                p++;
            }

            nfca.close();
        }
            catch (IOException e) {
                e.printStackTrace();
            }




return success;
    }
    public static String[] readTag(Tag tag) {
        String atqa="";
        String temp;
        int infhard = 8;
        int secetor = 0;
       final  byte CMD_PWD_AUTH = (byte)0x1B;
        String[] metaInfo = new String[120];
        byte[] DEFALT_PASSWORD = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        NfcA nfca = NfcA.get(tag);
        byte[] READ_COMMAND = {(byte) 0xFF, (byte) 0xCA, 0x00, 0x00, 0x00};
        byte[] commands = new byte[]{(byte) 0xAA, 00, (byte) 0x01, (byte) 0x86, (byte) 0x87, (byte) 0xBB};
        byte[] readedData;

        try {

            nfca.connect();
            int s = nfca.getSak();
            byte[] a = nfca.getAtqa();
            byte[] UID = nfca.getTag().getId();
Log.i("atqa",a+"");
            metaInfo[3] = "ATQA: " + CommonTask.byte2HexString(a) + " SAK: " + s;
            int p = 0;
            metaInfo[1] = "ok";
            metaInfo[2] = CommonTask.byte2HexString(UID);
            metaInfo[7] = String.valueOf(1);
            while (true) {
                infhard++;
                secetor++;
                readedData = nfca.transceive(new byte[]{
                        (byte) 0x30,  // READ
                        (byte) (p & 0x0ff)
                });

                metaInfo[infhard] = "Sector " + p;
                p++;
                infhard++;
                temp = CommonTask.byte2HexString(readedData);
                Log.i("aaaaa",temp+p);
                for (int d = 0; d < 8; d++) {

                    atqa += temp.charAt(d);
                }
                // Log.i("KEYBBBB", String.valueOf(infhard));
                metaInfo[infhard] = atqa;
                atqa = "";
                if (readedData == null) break;
                metaInfo[5] = String.valueOf(secetor);
                metaInfo[6] = String.valueOf(secetor);
                metaInfo[4] = String.valueOf(secetor) + " сектор";
            }
            metaInfo[5] = String.valueOf(secetor);
            metaInfo[6] = String.valueOf(secetor);
            metaInfo[4] = String.valueOf(secetor) + " сектор";
            nfca.close();
        } catch (Exception e) {

              //  metaInfo[1] = "errors";

        }

        return metaInfo;
    }
}
