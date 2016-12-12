package com.example.new_commer.missandmatch;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;

public class Singleplay extends AppCompatActivity implements View.OnClickListener{

    private int[] index_array = new int[3];
    private int click_count=0;
    private int score = 0;
    private int temp_index = 0;
    private int finishcount = 0;
    private int finish_check_count = 0;
    int position = 0;
    int round = 1;
    int value = 300;
    int save_value=value;
    int total_time=0;
    int record_sig=0;
    int matchflag = 0;
    int ver=1;
    int vibe=0;
    int hintcount=1;
    Vibrator vibrator;

    ImageButton back_button;
    ImageButton miss_button;
    ImageButton match_button;
    ImageButton hint_button;
    TextView t_view;

    stage i_stage = new stage();

    public stage_block[] button_block = new stage_block[9];
    public TextView s_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singleplay);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        RelativeLayout main = (RelativeLayout) findViewById(R.id.act_singleplay);
        Resources res = getResources(); //resource handle

        Intent intent = new Intent(Singleplay.this.getIntent());
        ver = intent.getIntExtra("VER", 0);
        vibe = intent.getIntExtra("VIBE", 0);

        for(int temp = 0; temp<9; temp++){
            button_block[temp] = new stage_block();
        }

        button_block[0].b_button = (ImageButton)findViewById(R.id.block1);
        button_block[1].b_button = (ImageButton)findViewById(R.id.block2);
        button_block[2].b_button = (ImageButton)findViewById(R.id.block3);

        button_block[3].b_button = (ImageButton)findViewById(R.id.block4);
        button_block[4].b_button = (ImageButton)findViewById(R.id.block5);
        button_block[5].b_button = (ImageButton)findViewById(R.id.block6);

        button_block[6].b_button = (ImageButton)findViewById(R.id.block7);
        button_block[7].b_button = (ImageButton)findViewById(R.id.block8);
        button_block[8].b_button = (ImageButton)findViewById(R.id.block9);

        s_view = (TextView)findViewById(R.id.score_view);
        t_view = (TextView) findViewById(R.id.s_timer);

        back_button = (ImageButton)findViewById(R.id.b_back_sigleroom);
        back_button.setBackgroundColor(00000000);

        miss_button = (ImageButton)findViewById(R.id.b_miss_single);
        miss_button.setBackgroundColor(00000000);
        miss_button.setOnClickListener(this);
        miss_button.setEnabled(false);

        match_button = (ImageButton)findViewById(R.id.b_match_single);
        match_button.setBackgroundColor(00000000);
        match_button.setOnClickListener(this);
        match_button.setEnabled(true);

        hint_button = (ImageButton)findViewById(R.id.b_hint);
        hint_button.setBackgroundColor(00000000);
        hint_button.setEnabled(true);
        hint_button.setOnClickListener(this);

        switch (ver) {
            case 1: {
                back_button.setImageResource(R.drawable.b1_back);
                miss_button.setImageResource(R.drawable.b1_miss);
                match_button.setImageResource(R.drawable.b1_match);
                hint_button.setImageResource(R.drawable.b1_hint);
                Drawable drawable = res.getDrawable(R.drawable.b1_singleplay_back); //new Image that was added to the res folder
                main.setBackground(drawable);
                break;
            }

            case 2: {
                back_button.setImageResource(R.drawable.b2_back);
                miss_button.setImageResource(R.drawable.b2_miss);
                match_button.setImageResource(R.drawable.b2_match);
                hint_button.setImageResource(R.drawable.b2_hint);
                Drawable drawable = res.getDrawable(R.drawable.b2_singleplay_back); //new Image that was added to the res folder
                main.setBackground(drawable);
                break;
            }
        }


        back_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                vibrator.vibrate(vibe*100);
                total_time = (save_value-value);
                record_sig=1;
                Intent intent = new Intent(Singleplay.this,record.class);
                intent.putExtra("TIME",total_time);
                intent.putExtra("ROUND",round);
                intent.putExtra("SCORE",score);
                intent.putExtra("RECORD_SIG",record_sig);
                intent.putExtra("VER",ver);
                intent.putExtra("VIBE",vibe);
                startActivity(intent);
                record_sig=0;
                value = -1;
                finish();
            }
        });
        Collections.shuffle(i_stage.ranNumber);

        for(int i =0; i<9; i++){
            //button_block[i].b_button.setEnabled(true);
            button_block[i].b_button.setTag(i);
            button_block[i].b_button.setOnClickListener(this);
            temp_index = i_stage.ranNumber.get(i);
            button_block[i].b_button.setBackgroundColor(00000000);
            switch(ver){
                case 2:
                    button_block[i].b_button.setImageResource(i_stage.block_array2[temp_index].image);
                    button_block[i].b_backcolor = i_stage.block_array2[temp_index].back_color;
                    button_block[i].b_shapecolor = i_stage.block_array2[temp_index].shape_color;
                    button_block[i].b_shape = i_stage.block_array2[temp_index].shape;
                    break;
                case 1:
                    button_block[i].b_button.setImageResource(i_stage.block_array[temp_index].image);
                    button_block[i].b_backcolor = i_stage.block_array[temp_index].back_color;
                    button_block[i].b_shapecolor = i_stage.block_array[temp_index].shape_color;
                    button_block[i].b_shape = i_stage.block_array[temp_index].shape;
                default:
            }
            button_block[i].b_position = 0;
        }
        mHandler.sendEmptyMessage(0);
        s_view.setText(String.format("Score : %d점", score));
        finishcount = i_stage.check_finishcount(button_block);
        finish_check_count = finishcount;
    }


    public Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg){
            if(value>0) {
                value--;
                t_view.setText("" + value);
                mHandler.sendEmptyMessageDelayed(0, 1000);
            }
            else if(value==0){
                total_time+= (save_value-value);
                record_sig=1;
                Intent intent = new Intent(Singleplay.this,record.class);
                intent.putExtra("TIME",total_time);
                intent.putExtra("ROUND",round);
                intent.putExtra("SCORE",score);
                intent.putExtra("RECORD_SIG",record_sig);
                intent .putExtra("VER",ver);
                intent.putExtra("VIBE",vibe);
                Singleplay.this.startActivity(intent);
                finish();
                record_sig=0;
            }else if(value < 0){

            }
        }
    };

    public void onClick(View v) {
        ImageButton newButton = (ImageButton) v;
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); //진동을위해
        vibrator.vibrate(vibe*100);

        ImageButton tempButton;

        if(newButton == hint_button)
        {
            if(hintcount!=0) {
                Toast.makeText(this, "remain number of \"MATCH\" " + finishcount, Toast.LENGTH_SHORT).show();
                hintcount=0;
            }
            else
            {
                Toast.makeText(this, "you already show hint", Toast.LENGTH_SHORT).show();
            }
        }
        if(newButton == match_button){
            int color = getResources().getColor(R.color.match_color);
            match_button.setBackgroundColor(color);
            for(int i =0; i<9; i++) {
                button_block[i].b_button.setEnabled(true);
            }
            miss_button.setEnabled(false);
            matchflag = 1;
        }

        if(matchflag == 1){
            for (int i =0; i<9; i++) {
                tempButton = button_block[i].b_button;
                if (tempButton == newButton) {
                    position = (Integer) v.getTag();
                    if (click_count < 3) {
                        index_array[click_count] = position + 1;
                        if (button_block[position].b_position == 0) {
                            click_count++;
                            button_block[position].b_position = 1;
                            button_block[position].b_button.setBackgroundColor(Color.RED);
                        } else if (button_block[position].b_position == 1) {
                            button_block[position].b_position = 0;
                            click_count--;
                            button_block[position].b_button.setBackgroundColor(00000000);
                        }
                    }

                    if (click_count == 3) {
                        this.i_stage.uppersort(index_array);
                        if(i_stage.match(button_block[index_array[0]-1], button_block[index_array[1]-1], button_block[index_array[2]-1]) == 1){
                            for(int t = 0; t < finish_check_count; t ++){
                                if(i_stage.hab_array[t][1] == index_array[0] - 1){
                                    if(i_stage.hab_array[t][2] == index_array[1] - 1) {
                                        if(i_stage.hab_array[t][3] == index_array[2] - 1){
                                            //first chosen
                                            if(i_stage.hab_array[t][0] == 0){
                                                Toast.makeText(this, "clicked "+ index_array[0] + ", " + index_array[1] + ", " + index_array[2] + " Button\n" + "are correct match +1 point", Toast.LENGTH_SHORT).show();
                                                score++;
                                                finishcount--;
                                                i_stage.hab_array[t][0] = 1;
                                                s_view.setText(String.format("Score : %d",score));
                                            }
                                            //already chosen
                                            else{
                                                Toast.makeText(this, "clicked "+ index_array[0] + ", " + index_array[1] + ", " + index_array[2] + " Button\n" + "are already chose -1 point", Toast.LENGTH_SHORT).show();
                                                score--;
                                                s_view.setText(String.format("Score : %d", score));
                                            }
                                        }else continue;
                                    }else continue;
                                }else continue;
                            }
                        }
                        else{
                            //incorrect
                            Toast.makeText(this, "clicked "+ index_array[0] + ", " + index_array[1] + ", " + index_array[2] + " Button\n"+ "are NOT matched!! -1 point", Toast.LENGTH_SHORT).show();
                            score--;
                            s_view.setText(String.format("Score : %d",score));
                        }
                        for(int t=0; t<3; t++){
                            button_block[index_array[t]-1].b_button.setBackgroundColor(000000);
                            button_block[index_array[t]-1].b_position = 0;
                            index_array[t] = 0;
                        }
                        match_button.setBackgroundColor(00000000);
                        miss_button.setEnabled(true);
                        click_count = 0;
                        matchflag = 0;
                    }
                }
            }
        }
        else if(matchflag == 0){
            if(newButton == miss_button){
                matchflag = 0;
                click_count = 0;
                if (finishcount == 0) {
                    Toast.makeText(this, "Miss. +3 points", Toast.LENGTH_SHORT).show();
                    score = score + 3;
                    Collections.shuffle(i_stage.ranNumber);

                    for(int i =0; i<9; i++){
                        button_block[i].b_button.setEnabled(false);
                        button_block[i].b_button.setTag(i);
                        button_block[i].b_button.setOnClickListener(this);
                        temp_index = i_stage.ranNumber.get(i);
                        button_block[i].b_button.setBackgroundColor(00000000);
                        switch(ver){
                            case 2:
                                button_block[i].b_button.setImageResource(i_stage.block_array2[temp_index].image);
                                button_block[i].b_backcolor = i_stage.block_array2[temp_index].back_color;
                                button_block[i].b_shapecolor = i_stage.block_array2[temp_index].shape_color;
                                button_block[i].b_shape = i_stage.block_array2[temp_index].shape;
                                break;
                            case 1:
                                button_block[i].b_button.setImageResource(i_stage.block_array[temp_index].image);
                                button_block[i].b_backcolor = i_stage.block_array[temp_index].back_color;
                                button_block[i].b_shapecolor = i_stage.block_array[temp_index].shape_color;
                                button_block[i].b_shape = i_stage.block_array[temp_index].shape;
                            default:
                        }
                        button_block[i].b_position = 0;
                    }
                    total_time+= (save_value-value);
                    value = 300-(((round/10)*60)+((round%10)*20));
                    save_value = value;
                    t_view.setText(""+value);
                    s_view.setText(String.format("Score : %d", score));
                    finishcount = i_stage.check_finishcount(button_block);
                    finish_check_count = finishcount;
                    hintcount = 1;
                } else {
                    Toast.makeText(this, "not miss yet -1 point", Toast.LENGTH_SHORT).show();
                    for(int i =0; i<9; i++){
                        button_block[i].b_button.setEnabled(false);
                        button_block[i].b_button.setTag(i);
                        button_block[i].b_button.setOnClickListener(this);
                        button_block[i].b_button.setBackgroundColor(000000);
                        button_block[i].b_position = 0;
                    }
                    score--;
                    s_view.setText(String.format("Score : %d", score));
                }
            }
        }
    }
}