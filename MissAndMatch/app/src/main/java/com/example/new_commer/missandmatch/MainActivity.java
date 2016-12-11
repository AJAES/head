package com.example.new_commer.missandmatch;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

   ImageButton singleplay_button;
    ImageButton multieplay_button;
    ImageButton HTP_button;
    ImageButton creater_button;
    ImageButton option_button;
    ImageView title;

    int ver=1;
    int vibe=0;
    int ver_sig=0;

    RelativeLayout main;
    Resources res;
    Drawable drawable;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        title= (ImageView) findViewById(R.id.title);
        title.setBackgroundColor(00000000);

        singleplay_button = (ImageButton)findViewById(R.id.b_single);
        singleplay_button.setBackgroundColor(00000000);
        multieplay_button = (ImageButton)findViewById(R.id.b_multi);
        multieplay_button.setBackgroundColor(00000000);
        HTP_button = (ImageButton)findViewById(R.id.b_htp);
        HTP_button.setBackgroundColor(00000000);
        creater_button = (ImageButton)findViewById(R.id.b_made);
        creater_button.setBackgroundColor(00000000);
        option_button = (ImageButton)findViewById(R.id.b_option);
        option_button.setBackgroundColor(00000000);

       title.setImageResource(R.drawable.title);
        singleplay_button.setImageResource(R.drawable.b1_single);
        multieplay_button.setImageResource(R.drawable.b1_multi);
        HTP_button.setImageResource(R.drawable.b1_howto);
        creater_button.setImageResource(R.drawable.b1_madeby);
        option_button.setImageResource(R.drawable.b1_option);
        main = (RelativeLayout) findViewById(R.id.act_main);
        res = getResources(); //resource handl
        drawable = res.getDrawable(R.drawable.b1_main_back); //new Image that was added to the res folder
        main.setBackground(drawable);


        singleplay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ghmain_intent = new Intent(MainActivity.this, Ghmain.class);
                ghmain_intent.putExtra("VER",ver);
                ghmain_intent.putExtra("VIBE",vibe);
                vibrator.vibrate(vibe*100);
                startActivity(ghmain_intent);
            }
        });
        multieplay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent multi_intent = new Intent(MainActivity.this, Multi_room.class);
                multi_intent.putExtra("VER",ver);
                multi_intent.putExtra("VIBE",vibe);
                vibrator.vibrate(vibe*100);
                startActivity(multi_intent);
            }
        });
        HTP_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent HTP_intent = new Intent(MainActivity.this, howto.class);
                HTP_intent.putExtra("VER",ver);
                HTP_intent.putExtra("VIBE",vibe);
                vibrator.vibrate(vibe*100);
                startActivity(HTP_intent);

            }
        });
        creater_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent Creater_intent = new Intent(MainActivity.this, creater.class);
                Creater_intent.putExtra("VER",ver);
                Creater_intent.putExtra("VIBE",vibe);
                vibrator.vibrate(vibe*100);
                startActivity(Creater_intent);
            }
        });
        option_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent option_intent = new Intent(MainActivity.this, option.class);
                option_intent.putExtra("VER",ver);
                option_intent.putExtra("VIBE",vibe);
                vibrator.vibrate(vibe*100);
                startActivityForResult(option_intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                ver = data.getIntExtra("VER", 0);
                vibe = data.getIntExtra("VIBE", 0);
            }
        }
        switch (ver) {
            case 1: {
                title.setImageResource(R.drawable.title);
                singleplay_button.setImageResource(R.drawable.b1_single);
                multieplay_button.setImageResource(R.drawable.b1_multi);
                HTP_button.setImageResource(R.drawable.b1_howto);
                creater_button.setImageResource(R.drawable.b1_madeby);
                option_button.setImageResource(R.drawable.b1_option);
                drawable = res.getDrawable(R.drawable.b1_main_back); //new Image that was added to the res folder
                main.setBackground(drawable);
                break;
            }

            case 2: {
                title.setImageResource(R.drawable.b2_invisi);
                singleplay_button.setImageResource(R.drawable.b2_single);
                multieplay_button.setImageResource(R.drawable.b2_multi);
                HTP_button.setImageResource(R.drawable.b2_howto);
                creater_button.setImageResource(R.drawable.b2_madeby);
                option_button.setImageResource(R.drawable.b2_option);
                drawable = res.getDrawable(R.drawable.b2_main_back); //new Image that was added to the res folder
                main.setBackground(drawable);
                break;
            }
        }

    }
}
