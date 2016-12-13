package com.example.new_commer.missandmatch;

/**
 * Created by Changgil on 2016-12-06.
 */

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class option extends AppCompatActivity {

    private int ver = 0;
    private  int out=0;
    private  int vibe=0;
    private ImageButton option;
    private ImageButton back_button;
    private ImageButton b_vibe;

    Vibrator vibrator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        RelativeLayout main = (RelativeLayout) findViewById(R.id.act_option);
        Resources res = getResources(); //resource handle

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        option = (ImageButton) findViewById(R.id.b_opt);
        option.setBackgroundColor(000000);

        back_button = (ImageButton) findViewById(R.id.b_back_option);
        back_button.setBackgroundColor(000000);

        b_vibe = (ImageButton) findViewById(R.id.b_vive);
        b_vibe.setBackgroundColor(000000);

        Intent intent = new Intent(option.this.getIntent());
        ver = intent.getIntExtra("VER", 0);
        vibe = intent.getIntExtra("VIBE", 0);

        switch (ver) {
            case 1: {
                option.setImageResource(R.drawable.b1_change);
                back_button.setImageResource(R.drawable.b1_back);
                b_vibe.setImageResource(R.drawable.b1_vibe);
                Drawable drawable = res.getDrawable(R.drawable.b1_main_back); //new Image that was added to the res folder
                main.setBackground(drawable);
                break;
            }
            case 2: {
                option.setImageResource(R.drawable.b2_change);
                back_button.setImageResource(R.drawable.b2_back);
                b_vibe.setImageResource(R.drawable.b2_vibe);
                Drawable drawable = res.getDrawable(R.drawable.b2_main_back); //new Image that was added to the res folder
                main.setBackground(drawable);
                break;
            }
        }

        back_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                vibrator.vibrate(vibe*100);
                finish();
            }
        });
        option.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                vibrator.vibrate(vibe*100);
                ver++;
                if(ver%2 ==0)
                    out = 2;
                else
                    out =1;
                Intent intent = new Intent(option.this,MainActivity.class);
                intent.putExtra("VER",out);
                intent.putExtra("VIBE",vibe);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        b_vibe.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                vibe++;
                if(vibe%2 ==0)
                    out = 0;
                else
                {
                    out = 1;
                    vibrator.vibrate(vibe*100);
                }
                Intent intent = new Intent(option.this,MainActivity.class);
                intent.putExtra("VIBE",out);
                intent.putExtra("VER",ver);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
