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
import android.widget.RelativeLayout;

/**
 * Created by Changgil on 2016-12-07.
 */

public class howto extends AppCompatActivity implements View.OnClickListener{

    private ImageButton back_button;
    private  ImageButton next_button;

    Drawable drawable;
    RelativeLayout main;
    Resources res;//resource handle

    private int change = 0;
    private int ver=1;
    private int vibe=0;

    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howto);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        back_button = (ImageButton) findViewById(R.id.b_back_howto);
        back_button.setBackgroundColor(000000);


        next_button = (ImageButton) findViewById(R.id.b_next_howto);
        next_button.setBackgroundColor(000000);

        main = (RelativeLayout) findViewById(R.id.act_howto);
        res = getResources();

        Intent intent = new Intent(howto.this.getIntent());
        ver = intent.getIntExtra("VER", 0);
        vibe = intent.getIntExtra("VIBE", 0);

        if(ver==1) {
            drawable = res.getDrawable(R.drawable.page_1); //new Image that was added to the res folder
            back_button.setImageResource(R.drawable.b1_back);
            next_button.setImageResource(R.drawable.b1_next);
            main.setBackground(drawable);
        }
        else
        {
            drawable = res.getDrawable(R.drawable.b2_page1); //new Image that was added to the res folder
            back_button.setImageResource(R.drawable.b2_back);
            next_button.setImageResource(R.drawable.b2_next);
            main.setBackground(drawable);
        }

        back_button.setOnClickListener(this);
        next_button.setOnClickListener(this);
    }

    public void onClick(View v) {

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); //진동을위해
        vibrator.vibrate(vibe*100);

        ImageButton newButton = (ImageButton) v;
        if (newButton == back_button) {
            if (change % 6 == 0)
                finish();
            else
                change--;
        } else {
            change++;
        }
        if (ver == 1) {
            switch (change % 6) {
                case 0:
                    drawable = res.getDrawable(R.drawable.page_1); //new Image that was added to the res folder
                    main.setBackground(drawable);
                    break;
                case 1:
                    drawable = res.getDrawable(R.drawable.page_2); //new Image that was added to the res folder
                    main.setBackground(drawable);
                    break;
                case 2:
                    drawable = res.getDrawable(R.drawable.page_3); //new Image that was added to the res folder
                    main.setBackground(drawable);
                    break;
                case 3:
                    drawable = res.getDrawable(R.drawable.page_4); //new Image that was added to the res folder
                    main.setBackground(drawable);
                    break;
                case 4:
                    drawable = res.getDrawable(R.drawable.page_5); //new Image that was added to the res folder
                    main.setBackground(drawable);
                    break;
                case 5:
                    drawable = res.getDrawable(R.drawable.page_6); //new Image that was added to the res folder
                    main.setBackground(drawable);
                    break;
            }
        }
        else if(ver==2)
        {
            switch (change % 6) {
                case 0:
                    drawable = res.getDrawable(R.drawable.b2_page1); //new Image that was added to the res folder
                    main.setBackground(drawable);
                    break;
                case 1:
                    drawable = res.getDrawable(R.drawable.b2_page2); //new Image that was added to the res folder
                    main.setBackground(drawable);
                    break;
                case 2:
                    drawable = res.getDrawable(R.drawable.b2_page3); //new Image that was added to the res folder
                    main.setBackground(drawable);
                    break;
                case 3:
                    drawable = res.getDrawable(R.drawable.b2_page4); //new Image that was added to the res folder
                    main.setBackground(drawable);
                    break;
                case 4:
                    drawable = res.getDrawable(R.drawable.b2_page5); //new Image that was added to the res folder
                    main.setBackground(drawable);
                    break;
                case 5:
                    drawable = res.getDrawable(R.drawable.b2_page6); //new Image that was added to the res folder
                    main.setBackground(drawable);
                    break;
            }
        }
    }
}
