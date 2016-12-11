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

/**
 * Created by hskim7341 on 2015-11-10.
 */


public class Ghmain extends AppCompatActivity {

    ImageButton ghstart_button;
    ImageButton record_button;
    ImageButton back_button;
    ImageView t_single;

    int ver=1;
    int vibe=0;

    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghmain);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        RelativeLayout main = (RelativeLayout) findViewById(R.id.act_singleroom);
        Resources res = getResources(); //resource handle

        ghstart_button = (ImageButton)findViewById(R.id.b_start_single);
        ghstart_button.setBackgroundColor(000000);
        record_button = (ImageButton)findViewById(R.id.b_record);
        record_button.setBackgroundColor(000000);
        back_button = (ImageButton)findViewById(R.id.b_back_single);
        back_button.setBackgroundColor(000000);
        t_single= (ImageView) findViewById(R.id.t_single);
        t_single.setBackgroundColor(00000000);

        Intent intent = new Intent(Ghmain.this.getIntent());
        ver = intent.getIntExtra("VER", 0);
        vibe = intent.getIntExtra("VIBE", 0);

        switch (ver) {
            case 1: {
                ghstart_button.setImageResource(R.drawable.b1_play);
                record_button.setImageResource(R.drawable.b1_record);
                back_button.setImageResource(R.drawable.b1_back);
                t_single.setImageResource(R.drawable.t_singleplay);
                Drawable drawable = res.getDrawable(R.drawable.b1_singleplay_back); //new Image that was added to the res folder
                main.setBackground(drawable);
                break;
            }

            case 2: {
                ghstart_button.setImageResource(R.drawable.b2_play);
                record_button.setImageResource(R.drawable.b2_record);
                back_button.setImageResource(R.drawable.b2_back);
                t_single.setImageResource(R.drawable.b2_invisi);
                Drawable drawable = res.getDrawable(R.drawable.b2_singleroom_back); //new Image that was added to the res folder
                main.setBackground(drawable);
                break;
            }
        }

        ghstart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Ghstart_intent = new Intent(Ghmain.this, Singleplay.class);
                Ghstart_intent .putExtra("VER",ver);
                Ghstart_intent .putExtra("VIBE",vibe);
                vibrator.vibrate(vibe*100);
                startActivity(Ghstart_intent);
            }
        });
        record_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent record_intent = new Intent(Ghmain.this, record.class);
                record_intent .putExtra("VER",ver);
                record_intent .putExtra("VIBE",vibe);
                vibrator.vibrate(vibe*100);
                startActivity(record_intent);
            }
        });
        back_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                vibrator.vibrate(vibe*100);
                finish();
            }
        });
    }

}
