package com.android.nfcexclusive.nfcexclusive;

import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.util.Log;

import java.io.IOException;

/**
 * Created by c0de on 21.05.2017.
 */

public class isodepclass {
    public static String[] read(Tag tag) {
        String str = "";
        int secetor=0;
        String[] metaInfo = new String[120];
        int sectorinf = 9;
        int temp = 0;
        int m = 0;
        byte[] cmd;
        metaInfo[1] = "ok";

        byte[] APDUCommand = {
                (byte) 0x00, // CLA Class
                (byte) 0xA4, // INS Instruction
                (byte) 0x04, // P1  Parameter 1
                (byte) 0x00, // P2  Parameter 2
                (byte) 0x07, // Length
                //(byte) 0xA0, 0x00, 0x00, 0x00, 0x04, 0x10, 0x10 // AID
        };
        final String[] APP_AIDS = {
                "00A4040007A00000002501", "00A4040007A000000025", "00A4040007A0000000250107",
                "00A4040007A000000025010701",
                "00A4040007A0000003241010",
                "00A4040007A0000000041010",
                "00A4040007A0000000042203",
                "00A4040007F07465737420414944", "00A4040007F07465737420414944",
                "00A4040007A0000000031010", "00A4040007A000000003101001", "00A4040007A000000003101002",
        };
        byte[] result;
        IsoDep isoDep = IsoDep.get(tag);
        try {


            isoDep.connect();

            metaInfo[2] = CommonTask.byte2HexString(isoDep.getTag().getId());
            metaInfo[3] = "ATQA: "+ " SAK: ";
            metaInfo[7] = String.valueOf(1);
            metaInfo[sectorinf] = "Sector " + secetor;
            sectorinf++;
            while (str.length() < 7 & temp != APP_AIDS.length) {



                result = isoDep.transceive(CommonTask.hexStringToByteArray(APP_AIDS[temp]));
                metaInfo[sectorinf] = CommonTask.byte2HexString(result);
                str=metaInfo[sectorinf];

                Log.i("aaaaabbbsfippppppppppp", temp + " temp");
                temp++;
            }
            for (int sfi = 1; sfi <= 31; sfi++) {

                for (int rec = 1; rec <= 16; rec++) {
                    cmd = new byte[]{(byte) 0x00, // CLA Class
                            (byte) 0xB2, // INS Instruction
                            (byte) (rec & 0x0ff), // P1 Parameter 1
                            (byte) ((sfi << 3) | 0x04), // P2 Parameter 2
                            (byte) 0x00, // Le
                    };

                    result = isoDep.transceive(cmd);
                    if (str.indexOf(CommonTask.byte2HexString(result)) == -1 & result.length > 4) {



                        if (m == sfi) {
                            metaInfo[sectorinf] += CommonTask.byte2HexString(result);
                            str+=metaInfo[sectorinf];
                            Log.i("aaaaabbbrecif", CommonTask.byte2HexString(result)+" sfi" + sfi + " rec " + rec + " m "+m+ " sector " + secetor);
                        } else {
                            secetor++;
                            sectorinf++;
                            metaInfo[sectorinf] = "Sector " + secetor;
                            sectorinf++;
                            metaInfo[sectorinf] = CommonTask.byte2HexString(result);
                            str+=metaInfo[sectorinf];


                            Log.i("aaaaabbbrecelse", CommonTask.byte2HexString(result)+" sfi" + sfi + " rec " + rec + " m "+m+ " sector " + secetor);
                            m = sfi;
                        }




                    }
                    Log.i("aaaaabbbsfi", sfi + " sfi");
                    Log.i("aaaaabbbrec", rec + " rec");

                }
            }
           //  String tr = CommonTask.hexToAscii(str);
            Log.i("aaaaabbb", str + "df");

            isoDep.close();
        } catch (Exception e) {

        }
        metaInfo[5] = String.valueOf(secetor+1);
        metaInfo[6] = String.valueOf(secetor+1);
        metaInfo[4] = String.valueOf(secetor+1) + " секторов";
        return metaInfo;
    }
}
