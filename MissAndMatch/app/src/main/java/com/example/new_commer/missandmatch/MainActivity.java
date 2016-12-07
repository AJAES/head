package com.example.new_commer.missandmatch;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
    int ver_sig=0;

    RelativeLayout main;
    Resources res;
    Drawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
                ghmain_intent.putExtra("VER_SIG",ver_sig);
                startActivity(ghmain_intent);
            }
        });
        multieplay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent multi_intent = new Intent(MainActivity.this, Multiplay.class);
                multi_intent.putExtra("VER",ver);
                multi_intent.putExtra("VER_SIG",ver_sig);
                startActivity(multi_intent);
            }
        });
        HTP_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent HTP_intent = new Intent(MainActivity.this, howto.class);
                startActivity(HTP_intent);
                //Intent HTP_intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://serviceapi.nmv.naver.com/flash/convertIframeTag.nhn?vid=33D3A3196A64C8C09178A1F9C4B3F1CB3996&outKey=V126304d30c9ef7dd19f156e9be4b1ee33e3d451d4289d9abf61d56e9be4b1ee33e3d&width=720&height=438"));
               // HTP_intent.putExtra("VER",ver);
                //HTP_intent.putExtra("VER_SIG",ver_sig);
            }
        });
        creater_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent Creater_intent = new Intent(MainActivity.this, creater.class);
                Creater_intent.putExtra("VER",ver);
                Creater_intent.putExtra("VER_SIG",ver_sig);
                startActivity(Creater_intent);
            }
        });
       option_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent option_intent = new Intent(MainActivity.this, option.class);
                option_intent.putExtra("VER",ver);
                option_intent.putExtra("VER_SIG",ver_sig);
                //startActivity(option_intent);
                startActivityForResult(option_intent,1);
               // finish();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                ver = data.getIntExtra("VER", 0);
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
