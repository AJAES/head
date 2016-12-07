package com.example.new_commer.missandmatch;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collections;

import static java.lang.String.valueOf;

public class Join_room extends AppCompatActivity implements View.OnClickListener{

    Client user1;
    Client user2;

    private int[] index_array = new int[3];
    private int click_count=0;
    private int score = 0;
    private int temp_index = 0;
    private int finishcount = 0;
    private int finish_check_count = 0;
    int position = 0;
    int matchflag = 0;
    int value = 30;
    int ver=1;

    ImageButton back_button;
    ImageButton miss_button;
    ImageButton match_button;
    TextView t_view;
    TextView user1_view;
    TextView user2_view;
    TextView roomNum_view;

    stage i_stage = new stage();

    public stage_block[] button_block = new stage_block[9];
    public TextView s_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_room);

        RelativeLayout main = (RelativeLayout) findViewById(R.id.activity_join_room);
        Resources res = getResources(); //resource handle

        Intent intent = getIntent();
        user2 = (Client)intent.getSerializableExtra("User2");
        ver = (int)intent.getSerializableExtra("VER");
        Toast.makeText(this, user2.name, Toast.LENGTH_SHORT).show();

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

        user1_view = (TextView)findViewById(R.id.m_user1);
        user2_view = (TextView)findViewById(R.id.m_user2);
        user2_view.setText(user2.name);
        roomNum_view = (TextView)findViewById(R.id.m_roomnum);
        s_view = (TextView) findViewById(R.id.m_score_view);
        t_view = (TextView) findViewById(R.id.m_timer);

        s_view = (TextView)findViewById(R.id.m_score_view);
        t_view = (TextView) findViewById(R.id.m_timer);

        back_button = (ImageButton )findViewById(R.id.b_back_multi);
        back_button.setBackgroundColor(00000000);

        miss_button = (ImageButton)findViewById(R.id.b_miss_multi);
        miss_button.setBackgroundColor(00000000);

        match_button = (ImageButton)findViewById(R.id.b_match_multi);
        match_button.setBackgroundColor(00000000);


        switch (ver) {
            case 1: {
                back_button.setImageResource(R.drawable.b1_back);
                miss_button.setImageResource(R.drawable.b1_miss);
                match_button.setImageResource(R.drawable.b1_match);
                Drawable drawable = res.getDrawable(R.drawable.b1_multiplay_back); //new Image that was added to the res folder
                main.setBackground(drawable);
                break;
            }

            case 2: {
                back_button.setImageResource(R.drawable.b2_back);
                miss_button.setImageResource(R.drawable.b2_miss);
                match_button.setImageResource(R.drawable.b2_match);
                Drawable drawable = res.getDrawable(R.drawable.b2_multiplay_back); //new Image that was added to the res folder
                main.setBackground(drawable);
                break;
            }
        }

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        miss_button.setOnClickListener(this);

        Collections.shuffle(i_stage.ranNumber);

        for (int i = 0; i < 9; i++) {
            button_block[i].b_button.setTag(i);
            button_block[i].b_button.setOnClickListener(this);
            temp_index = i_stage.ranNumber.get(i);
            button_block[i].b_button.setImageResource(i_stage.block_array[temp_index].image);
            button_block[i].b_button.setBackgroundColor(00000000);
            button_block[i].b_backcolor = i_stage.block_array[temp_index].back_color;
            button_block[i].b_shapecolor = i_stage.block_array[temp_index].shape_color;
            button_block[i].b_shape = i_stage.block_array[temp_index].shape;
            button_block[i].b_position = 0;
        }
        mHandler.sendEmptyMessage(0);
        s_view.setText(String.format("Score : %d점", score));
        finishcount = i_stage.check_finishcount(button_block);
        finish_check_count = finishcount;
    }
    private Handler mHandler = new Handler()    {
        public void handleMessage(Message msg){
            value++;
            t_view.setText(""+value);
            mHandler.sendEmptyMessageDelayed(0,1000);
        }
    };

    public void onClick(View v) {
        ImageButton newButton = (ImageButton) v;
        ImageButton tempButton;
        if(newButton == match_button){
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
                            button_block[position].b_button.setBackgroundColor(Color.LTGRAY);
                        } else if (button_block[position].b_position == 1) {
                            button_block[position].b_position = 0;
                            click_count--;
                            button_block[position].b_button.setBackgroundColor(Color.WHITE);
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
                            button_block[index_array[t]-1].b_button.setBackgroundColor(Color.WHITE);
                            button_block[index_array[t]-1].b_position = 0;
                            index_array[t] = 0;
                        }
                        miss_button.setEnabled(true);
                        click_count = 0;
                        matchflag = 0;
                    }
                }
            }
        }
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
                    button_block[i].b_button.setImageResource(i_stage.block_array[temp_index].image);
                    button_block[i].b_button.setBackgroundColor(Color.WHITE);
                    button_block[i].b_backcolor = i_stage.block_array[temp_index].back_color;
                    button_block[i].b_shapecolor = i_stage.block_array[temp_index].shape_color;
                    button_block[i].b_shape = i_stage.block_array[temp_index].shape;
                    button_block[i].b_position = 0;
                }
                s_view.setText(String.format("Score : %d", score));
                finishcount = i_stage.check_finishcount(button_block);
                finish_check_count = finishcount;
            } else {
                Toast.makeText(this, "not miss yet -1 point", Toast.LENGTH_SHORT).show();
                for(int i =0; i<9; i++){
                    button_block[i].b_button.setEnabled(false);
                    button_block[i].b_button.setTag(i);
                    button_block[i].b_button.setOnClickListener(this);
                    button_block[i].b_button.setBackgroundColor(Color.WHITE);
                    button_block[i].b_position = 0;
                }
                score--;
                s_view.setText(String.format("Score : %d", score));
            }
        }
    }
}