package com.example.chatapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

public class SettingsActivity extends AppCompatActivity {
    private static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);



        }
    }
    public static class downloadModel{
        public void getmodel(){

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            if((sharedPreferences.getString("english","not found")).equals("hindi")){
                FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                        //from language
                        .setSourceLanguage(FirebaseTranslateLanguage.EN)
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
                        Log.e("Success","Hindi model downloaded");
                    }
                });



            }
            else{
                FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                        //from language
                        .setSourceLanguage(FirebaseTranslateLanguage.HI)
                        // to language
                        .setTargetLanguage(FirebaseTranslateLanguage.EN)
                        .build();

                final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance()
                        .getTranslator(options);

                FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                        .build();
                translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                    //            @Override
                    public void onSuccess(Void aVoid) {
                        Log.e("Success","English model downloaded");
                    }
                });
            }

        }
    }

}