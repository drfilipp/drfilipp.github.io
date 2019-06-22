package com.android.nfcexclusive.nfcexclusive;

import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.util.Log;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by c0de on 21.05.2017.
 */

public class Ndefclass {
    public static void write(Tag tag, String text) {
        try {
            NdefRecord[] records = {
                    new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], text.getBytes("US-ASCII"))
            };
            NdefMessage message = new NdefMessage(records);
            Ndef ndef = Ndef.get(tag);
            ndef.connect();
            ndef.writeNdefMessage(message);
            ndef.close();
        } catch(IOException | FormatException e) {
            e.printStackTrace();
        }
    }
    public static String read(Tag tag) {
        try {
            Ndef ndef = Ndef.get(tag);
            ndef.connect();
            NdefRecord[] records = ndef.getNdefMessage().getRecords();
            String text = "";
            for(NdefRecord record : records) {
                text += new String(record.getPayload());
            }
            ndef.close();
            return text;
        } catch(IOException |FormatException e) {
            e.printStackTrace();
        }
        return null;
    }
}
