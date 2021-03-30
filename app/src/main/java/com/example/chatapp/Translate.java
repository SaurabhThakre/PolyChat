package com.example.chatapp;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import androidx.appcompat.app.AppCompatActivity;

public class Translate {
    static String mSourceLang="";
    static String mTranslatedText="";
    static String sourceText="";
    static String sm;
    static int langCode;
    public static String superMethod(String mSourcetext) {
        String tt="";
        String s="";
        int lc=0;
        s = identifyLanguage(mSourcetext);
        //langCode=getLanguageCode(s);
        tt = translateText(FirebaseTranslateLanguage.HI);
        return tt;

    }
    private static String identifyLanguage(String mSourcetext) {
        sourceText = mSourcetext;

        FirebaseLanguageIdentification identifier = FirebaseNaturalLanguage.getInstance()
                .getLanguageIdentification();

        //mSourceLang.setText("Detecting..");
        identifier.identifyLanguage(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
            //           /
            public void onSuccess(String s) {
                if (s.equals("und")) {
                  //  Toast.makeText(getApplicationContext(), "Language Not Identified", Toast.LENGTH_SHORT).show();

                } else {
                    sm =s;
                }
            }
        });
        return sm;
    }

    private static int getLanguageCode(String language) {

        switch (language) {
            case "hi":
                langCode = FirebaseTranslateLanguage.HI;
                mSourceLang="Hindi";
                break;
            case "en":
                langCode = FirebaseTranslateLanguage.AR;
                mSourceLang="English";

                break;
            case "ur":
                langCode = FirebaseTranslateLanguage.UR;
                mSourceLang="Urdu";

                break;
            default:
                langCode = 0;
        }
        return langCode;
    }
    private static String translateText(int langCode){
        //mTranslatedText.setText("Translating..");
        FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                //from language
                .setSourceLanguage(langCode)
                // to language
                .setTargetLanguage(FirebaseTranslateLanguage.HI)
                .build();

        final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance()
                .getTranslator(options);

        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                .build();


        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            //            @Override
            public void onSuccess(Void aVoid) {
                translator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                    //                    @Override
                    public void onSuccess(String s) {
                        mTranslatedText=s;
                    }
                });
            }
        });
        return mTranslatedText;
    }
}
