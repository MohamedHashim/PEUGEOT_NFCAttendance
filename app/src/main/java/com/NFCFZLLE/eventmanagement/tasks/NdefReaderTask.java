package com.NFCFZLLE.eventmanagement.tasks;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.NFCFZLLE.eventmanagement.interfaces.NFCListener;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created by Muhammad Tahir Ashraf on 23/05/2016.
 * Email: tahir90.webdeveloper@gmail.com
 * Phone: +971569709138 , +923214745779
 * Skype: tahir90_webdeveloper
 */
public class NdefReaderTask<T> extends AsyncTask<T, Void, String> {

    NFCListener<T> mListener;
    T readData;
    Class mClass;
    boolean isJson;

    public NdefReaderTask(Class<T> toClass, NFCListener<T> mListener,boolean isJsonResponse) {
        this.mListener = mListener;
        mClass = toClass;
        isJson = isJsonResponse;
    }

    @Override
    protected String doInBackground(T... params) {
        Tag tag = (Tag) params[0];


        Ndef ndef = Ndef.get(tag);
        if (ndef == null) {
            // NDEF is not supported by this Tag.
            return null;
        }

        NdefMessage ndefMessage = ndef.getCachedNdefMessage();

        NdefRecord[] records = ndefMessage.getRecords();
        for (NdefRecord ndefRecord : records) {
            if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                try {
                    return readText(ndefRecord);
                } catch (UnsupportedEncodingException e) {
                }
            }
        }

        return null;
    }

    private String readText(NdefRecord record) throws UnsupportedEncodingException {
			/*
			 * See NFC forum specification for "Text Record Type Definition" at 3.2.1
			 *
			 * http://www.nfc-forum.org/specs/
			 *
			 * bit_7 defines encoding
			 * bit_6 reserved for future use, must be 0
			 * bit_5..0 length of IANA language code
			 */

        byte[] payload = record.getId();

        // Get the Text Encoding
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

        // Get the Language Code
        int languageCodeLength = payload[0] & 0063;

        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
        // e.g. "en"

        // Get the Text
        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
    }



    @Override
    protected void onPostExecute(String result) {
        if (result != null) {

            if(isJson)
            readData = (T) new Gson().fromJson(result, mClass);
            else
            readData = (T) result;

            if(readData!=null){
                Log.d("",""+readData.toString());
                mListener.onRead(readData);
            }else
                mListener.onReadError("Unable to read Data");
        }
    }
}
