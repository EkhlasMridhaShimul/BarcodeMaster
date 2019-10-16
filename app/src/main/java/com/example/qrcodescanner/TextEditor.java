package com.example.qrcodescanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TextEditor extends BaseActivity {

    EditText textEditor;
    String info;
    Button back,exec;
    ActionManager actionManager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_editor);
        getSupportActionBar().setTitle("Editor");

        textEditor = (EditText)findViewById(R.id.editor_field);
        back = (Button)findViewById(R.id.back_button);
        exec= (Button)findViewById(R.id.execute_button);
        final CopyMaster copyMaster = new CopyMaster(this);

        info = getIntent().getStringExtra("info");

        textEditor.setText(info);

        exec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = getText();
                actionManager = new ActionManager(TextEditor.this,text);
                actionManager.action();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),UnknownResultHandler.class);
                intent.putExtra("result",info);
                startActivity(intent);
                finish();
            }
        });
    }

    public String getText(){
        return textEditor.getText().toString().trim();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,UnknownResultHandler.class);
        startActivity(intent);
        finish();
    }
}
