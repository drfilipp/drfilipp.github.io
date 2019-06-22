package com.android.nfcexclusive.nfcexclusive;

import android.content.Context;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import static java.util.Arrays.copyOf;

/**
 * Created by c0de on 18.05.2017.
 */

public class MifareClass {
    public static int writeBlock(Tag tag, int sectorinf, int block, String[][] datsAsyncWrite) {
        boolean auth = false;
        MifareClassic mfc = MifareClassic.get(tag);

        try {
            //Enable I/O operations to the tag from this TagTechnology object.
            mfc.connect();
            int type = mfc.getType();
            int sectorCount = mfc.getSectorCount();
            String typeS = "";
            Log.i("WtFFFFFFF", "jutm");
         /*   if (mfc.getBlockCountInSector(sectorIndex) - 1 < blockIndex) {
                Log.i("WtFFFFFFF", "2");
                return 2;
            }*/


        /*if (!authenticate(sectorIndex, key, useAsKeyB)) {
            return 4;
        }*/
            for (int wr = 0; wr != sectorinf; wr++) {

                //Authenticate a sector with key A.
                auth = mfc.authenticateSectorWithKeyB(wr,
                        CommonTask.getKeyssize(0));
                int bCount;
                int bIndex;
                int cik = 1;
                Log.i("Wri", String.valueOf(auth));
                while (!auth) {
                    auth = mfc.authenticateSectorWithKeyB(wr,
                            CommonTask.getKeyssize(cik));

                    if (cik >= CommonTask.getKeyssizer()) break;
                    cik++;
                    Log.i("KEYBBBBBBBBBBBBBBBBBBBB", String.valueOf(auth));
                }
                cik=0;
                while (!auth) {
                    auth = mfc.authenticateSectorWithKeyA(wr,
                            CommonTask.getKeyssize(cik));
                    Log.i("KEYBAAAAAAAAAA", String.valueOf(auth)+"dta"+cik);
                    if (cik >= CommonTask.getKeyssizer()) break;
                    cik++;

                    

                }
                if (auth) {
                    // Write block.
                    for (int bl = 0; bl != block - 1; bl++) {
                        try {
                            mfc.writeBlock(mfc.sectorToBlock(wr) + bl, CommonTask.hexStringToByteArray(datsAsyncWrite[wr][bl]));
                            Log.i("KEYBBBBBBBBSuccess", String.valueOf(auth));
                        } catch (IOException e) {
                            Log.i("KEYBBBBBBBBSuccess", "error");


                        }
                    }
                }
            }
        } catch (Exception e) {

        }
        return 0;
    }


    public static String[] readTag(Tag tag) {
        String[] metaInfo = new String[120];
        int infhard = 8;
        int valid = 0;
        int razmetka = 0;
        boolean auth = false;
        boolean authB = false;
        byte[] data = null;
        int complete = 0;
        int sectorCount = 5;
        ArrayList<byte[]> mKeysWithOrde;
        MifareClassic mfc = MifareClassic.get(tag);
        metaInfo[2] = CommonTask.byte2HexString(mfc.getTag().getId());
        metaInfo[5] = String.valueOf(mfc.getSectorCount());
        metaInfo[3] = String.valueOf(mfc.getSectorCount()) + " секторов по " + mfc.getBlockCountInSector(0) + " блока ";
        metaInfo[4] = "NFC метка на " + String.valueOf(mfc.getSize()) + " байт \n"+metaInfo[3];

        //  Log.i("WtFFFFFFF","juts");
        try {
            //Enable I/O operations to the tag from this TagTechnology object.
            mfc.connect();
            int type = mfc.getType();
            sectorCount = mfc.getSectorCount();
            String typeS = "";
            switch (type) {
                case MifareClassic.TYPE_CLASSIC:
                    typeS = "TYPE_CLASSIC";
                    break;
                case MifareClassic.TYPE_PLUS:
                    typeS = "TYPE_PLUS";
                    break;
                case MifareClassic.TYPE_PRO:
                    typeS = "TYPE_PRO";
                    break;
                case MifareClassic.TYPE_UNKNOWN:
                    typeS = "TYPE_UNKNOWN";
                    break;
            }

            metaInfo[7] = String.valueOf(mfc.getBlockCountInSector(0));
            metaInfo[1] += "Card type：" + typeS + "n with" + sectorCount + " Sectorsn, "
                    + mfc.getBlockCount() + " BlocksnStorage Space: " + mfc.getSize() + "Bn";
            Log.i("sector", String.valueOf(sectorCount));
            for (int j = 0; j < sectorCount; j++) {
                //Authenticate a sector with key A.
                auth=false;
                authB=false;
                int cik = 0;
                int cikB = 0;
                auth = mfc.authenticateSectorWithKeyA(j,
                        CommonTask.getKeyssize(cik));
               // authB = mfc.authenticateSectorWithKeyB(j, CommonTask.getKeys(cikB));
                int bCount;
                int bIndex;

                // Log.i("Wtf", String.valueOf(auth));
                while (!auth) {
                    cik++;
                    auth = mfc.authenticateSectorWithKeyA(j, CommonTask.getKeyssize(cik));

                    if (cik >= CommonTask.getKeyssizer()) break;

                    //  Log.i("Wtf", String.valueOf(auth));
                }
                if (!authB&!auth) {
                    authB = mfc.authenticateSectorWithKeyB(j, CommonTask.getKeyssize(cikB));
                }
                while (!authB&!auth) {
                    cikB++;
                    authB = mfc.authenticateSectorWithKeyB(j,
                            CommonTask.getKeyssize(cikB));

                    // Log.i("KEYBBBBBBBBBBBBBBBBBBBB", String.valueOf(auth));
                    if (cikB >= CommonTask.getKeyssizer()) break;

                }

                if (auth || authB) {

                     Log.i("Wtf", String.valueOf(j));
                    valid++;
                    razmetka++;
                    infhard++;
                    metaInfo[infhard] = "Sector " + j;
                    bCount = mfc.getBlockCountInSector(j);
                    bIndex = mfc.sectorToBlock(j);
                    Log.i("sector", String.valueOf(j));
                    for (int i = 0; i < bCount; i++) {
                        data = mfc.readBlock(bIndex);
                        Log.i("js", String.valueOf(bIndex));
 Log.i("KEllllllllllll",data.length+"length"+data);
                        infhard++;
                        Log.i("jsm", String.valueOf(infhard));
                        if (data.length < 16) {
                            throw new IOException();
                        }
                        if (data.length > 16) {
                            byte[] blockBytesTmp = copyOf(data,16);
                            data = blockBytesTmp;
                        }
                        //if(infhard==19) {   Log.i("19",metaInfo[infhard]); }
                        metaInfo[infhard] = CommonTask.byte2HexString(data);
                        Log.i("KEYBBBBBBBBBBBBBBBBBBBB", metaInfo[infhard]);
                        bIndex++;
                    }
                    while (!authB) {
                        cikB++;
                        authB = mfc.authenticateSectorWithKeyB(j,
                                CommonTask.getKeyssize(cikB));

                         Log.i("KEYBBBBBBBBBBBBBBBnew", String.valueOf(authB));
                        if (cikB >= CommonTask.getKeyssizer()) break;

                    }
                    if (auth & authB) {
                        metaInfo[infhard] = CommonTask.byte2HexString(CommonTask.getKeyssize(cik)) + metaInfo[infhard].substring(12, 20) + CommonTask.byte2HexString(CommonTask.getKeyssize(cikB));
                    } else if (auth) {
                        metaInfo[infhard] = CommonTask.byte2HexString(CommonTask.getKeyssize(cik)) + metaInfo[infhard].substring(12, 20) + "[NOT__KEY_B]";
                    } else if (authB) {
                        metaInfo[infhard] = "[NOT__KEY_A]" + metaInfo[infhard].substring(12, 20) + CommonTask.byte2HexString(CommonTask.getKeyssize(cikB));
                    }
                } else {
                    infhard++;
                    Log.i("jstt", String.valueOf(j));
                    Log.i("jsm", String.valueOf(infhard));

                    metaInfo[infhard] = "Sector " + j + ": Verified failure";
                    //if(infhard==19) {   Log.i("19",metaInfo[infhard]); }
                }
                complete = j;
                Log.i("j", String.valueOf(j));
                // Log.i("19",metaInfo[infhard]);
            }
            Log.i("19", metaInfo[19]);
        } catch (Exception e) {
            e.printStackTrace();
            if ((complete + 1) != sectorCount) {
                metaInfo[1] = "errors";
                return metaInfo;
            }
        }
        metaInfo[6] = String.valueOf(valid);
        Log.i("Blocks", metaInfo[11]);


        return metaInfo;
    }
}