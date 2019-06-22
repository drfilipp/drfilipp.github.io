package com.android.nfcexclusive.nfcexclusive;

import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;

import java.io.IOException;

/**
 * Created by c0de on 21.05.2017.
 */

public class nfcbclass {
    public static String read(Tag tag) {
        NfcB nfcB = NfcB.get(tag);
    byte[] responses = {(byte)0x30,  // READ
        (byte)(0 & 0x0ff)};

    try {
        nfcB.connect();
        for (int i = 0; i < 10; i++) {
            responses = nfcB.transceive(new byte[] {
                    (byte)0x30,  // READ
                    (byte)(0 & 0x0ff)
            });
        }
        nfcB.close();
    } catch (IOException e) {
    }

    return CommonTask.byte2HexString(responses);
}
}
