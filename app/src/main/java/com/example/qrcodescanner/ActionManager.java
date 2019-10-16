package com.example.qrcodescanner;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Patterns;

import com.google.zxing.Result;

public class ActionManager {
    private Context context;
    private String code;
    private CharSequence charCode;

    public ActionManager(Context context, String  code){
        this.code = code;
        this.context = context;
    }

    public void action(){
        String mailPrefix = "MAILTO:";
        charCode = code;
        if(isMailAddress(code) || (code.toLowerCase().startsWith(mailPrefix.toLowerCase()))){
            if(code.toLowerCase().startsWith(mailPrefix.toLowerCase())){
                code = code.substring(mailPrefix.length());
                Intent intent = new Intent(context,MailSender.class);
                intent.putExtra("mail",code);
                context.startActivity(intent);
            }
            else {
                Intent intent = new Intent(context,MailSender.class);
                intent.putExtra("mail",code);
                context.startActivity(intent);
            }

        }
        else if(isUrl(charCode)){
            if((!code.startsWith("http://")) && (!code.startsWith("https://"))){
                code = "http://" + code;
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(code));
                context.startActivity(intent);
            }
            else {
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(code));
                context.startActivity(intent);
            }
        }
        else{
            Intent intent = new Intent(context, UnknownResultHandler.class);
            intent.putExtra("result",code);
            context.startActivity(intent);
        }
    }

    public boolean isMailAddress(CharSequence mail){
        return Patterns.EMAIL_ADDRESS.matcher(mail).matches();
    }

    public boolean isUrl(CharSequence url){
       return Patterns.WEB_URL.matcher(url).matches();
    }
}
