package com.example.qrcodescanner;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MailSender extends BaseActivity{

    EditText mail_id,subject,message;
    Button send;
    String data;
    String email_id;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_sender);
        getSupportActionBar().setTitle("Send mail");

        mail_id = (EditText)findViewById(R.id.to_mail_address);
        subject = (EditText)findViewById(R.id.subject_field);
        message = (EditText)findViewById(R.id.message_field);

        email_id = mail_id.getText().toString().trim();

        data = getIntent().getStringExtra("mail");
        mail_id.setText(data);

        send = (Button)findViewById(R.id.send_button);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail();
            }
        });
    }

    public void sendMail(){
        String[] reciepent = email_id.split(",");
        String email_subject = subject.getText().toString().trim();
        String mail = message.getText().toString().trim();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL,reciepent);
        intent.putExtra(Intent.EXTRA_SUBJECT,email_subject);
        intent.putExtra(Intent.EXTRA_TEXT,mail);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent,"Choose an application to send mail:"));
    }
}
