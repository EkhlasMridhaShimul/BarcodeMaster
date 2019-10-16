package com.example.qrcodescanner;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

public class CopyMaster {
    private Context context;

    public CopyMaster(Context context){
        this.context = context;
    }

    public void copy(String key,String value){
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(key,value);
        clipboardManager.setPrimaryClip(clipData);
    }
}
