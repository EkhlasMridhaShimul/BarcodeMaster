package com.example.qrcodescanner;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UnknownResultHandler extends BaseActivity {

    Button copy,text,viewWeb;
    TextView resultText;

    private String scanInfo;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unknown_result);
        getSupportActionBar().hide();

        resultText = (TextView)findViewById(R.id.scan_result_field);
        text = (Button)findViewById(R.id.text_editor_button);
        copy = (Button)findViewById(R.id.copy_button);
        viewWeb = (Button)findViewById(R.id.browser_button);

        scanInfo = getIntent().getStringExtra("result");
        if(scanInfo!=null){
            resultText.setText(scanInfo);
        }

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyItem();
            }
        });
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTextEditor();
            }
        });

        viewWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewOnWeb();
            }
        });
    }

    public void openTextEditor(){
        Intent intent = new Intent(this,TextEditor.class);
        intent.putExtra("info",scanInfo);
        startActivity(intent);
    }

    public void viewOnWeb(){
        try {
            String searchItem = URLEncoder.encode(scanInfo,"UTF-8");
            Uri uri = Uri.parse("http://www.google.com/#q="+searchItem);
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            startActivity(intent);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void copyItem(){
        CopyMaster copyMaster = new CopyMaster(this);
        copyMaster.copy("scaninfo",scanInfo);
        Toast.makeText(getApplicationContext(),"Text copied",Toast.LENGTH_SHORT).show();
        finish();
    }
}
