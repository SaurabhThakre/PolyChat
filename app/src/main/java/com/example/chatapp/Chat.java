package com.example.chatapp;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import java.util.HashMap;
import java.util.Map;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.Locale;

//import static com.example.chatapp.Translate.*;

public class Chat extends AppCompatActivity {
    LinearLayout layout;
    RelativeLayout layout_2;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;
    Button Text_to_Speech;
    TextToSpeech t1;
    String ttospeech;
    String  mTranslatedText;
    String translateoption;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Log.e("User Get",sharedPreferences.getString("english","not found"));
         translateoption=sharedPreferences.getString("english","not found");
        layout = findViewById(R.id.layout1);
        layout_2 = findViewById(R.id.layout2);
        sendButton = findViewById(R.id.sendButton);
        messageArea = findViewById(R.id.messageArea);
        scrollView = findViewById(R.id.scrollView);
        Text_to_Speech=findViewById(R.id.text_to_speech);

        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://fir-chat-app-18c3d-default-rtdb.firebaseio.com/messages/" + UserDetails.username + "_" + UserDetails.chatWith);
        reference2 = new Firebase("https://fir-chat-app-18c3d-default-rtdb.firebaseio.com/messages/" + UserDetails.chatWith + "_" + UserDetails.username);

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    if(translateoption.equals("hindi")){
                        t1.setLanguage(new Locale("hi"));
                    }
                    else{
                        t1.setLanguage(Locale.ENGLISH);
                    }
                }
            }
        });

        Text_to_Speech.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
               // Convert_text_To_Speech(ttospeech);
                Toast.makeText(getApplicationContext(), ttospeech,Toast.LENGTH_SHORT).show();
                t1.speak(ttospeech, TextToSpeech.QUEUE_FLUSH, null);

            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", UserDetails.username);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    messageArea.setText("");
                }
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if(userName.equals(UserDetails.username)){
                    addMessageBox(message, 1);

                }
                else{
                    //addMessageBox(superMethod(message), 2);
                     if(translateoption.equals("hindi")){
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
                                 translator.translate(message).addOnSuccessListener(new OnSuccessListener<String>() {
                                     //                    @Override
                                     public void onSuccess(String s) {
                                         mTranslatedText=s;
                                         addMessageBox(s.toString(), 2);
                                         ttospeech=s;
                                     }
                                 });
                             }
                         });
                         Log.e("Output text",s);

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
                                 translator.translate(message).addOnSuccessListener(new OnSuccessListener<String>() {
                                     //                    @Override
                                     public void onSuccess(String s) {
                                         mTranslatedText=s;
                                         addMessageBox(s.toString(), 2);
                                         ttospeech=s;
                                     }
                                 });
                             }
                         });
                         Log.e("Output text",s);

                     }


                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void addMessageBox(String message, int type){
        TextView textView = new TextView(Chat.this);
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 7.0f;

        if(type == 1) {
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.bubble_in);
        }
        else{
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.bubble_out);
        }
        textView.setLayoutParams(lp2);
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void Convert_text_To_Speech(String s){

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    if(translateoption.equals("hindi")){
                        t1.setLanguage(new Locale("hi"));
                    }
                    else{
                        t1.setLanguage(Locale.ENGLISH);
                    }


                }
            }
        });
        String toSpeak =  s;
        Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
        t1.speak(s, TextToSpeech.QUEUE_FLUSH, null,null);
        //t1.speak(toSpeak);
    }
}