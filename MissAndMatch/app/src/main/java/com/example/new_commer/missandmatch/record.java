package com.example.new_commer.missandmatch;

/**
 * Created by Changgil on 2016-11-30.
 */

import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class record extends AppCompatActivity {

    ImageButton back_button;
    ImageView t_record;
    TextView t_result;

    DataBase db_open;
    SQLiteDatabase db;

    int score;
    int round;
    int time;
    int record_sig;
    int ver =1;

    @Override
    protected  void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        RelativeLayout main = (RelativeLayout) findViewById(R.id.act_record);
        Resources res = getResources(); //resource handle

        back_button = (ImageButton)findViewById(R.id.b_back_record);
        back_button.setBackgroundColor(00000000);

        t_record= (ImageView) findViewById(R.id.t_record);
        t_record.setBackgroundColor(00000000);

        Intent intent1 = new Intent(record.this.getIntent());
        ver = intent1.getIntExtra("VER", 0);

        switch (ver) {
            case 1: {
                back_button.setImageResource(R.drawable.b1_back);
                t_record.setImageResource(R.drawable.t_record);
                Drawable drawable = res.getDrawable(R.drawable.b1_singleplay_back); //new Image that was added to the res folder
                main.setBackground(drawable);
                break;
            }

            case 2: {
                back_button.setImageResource(R.drawable.b2_back);
                t_record.setImageResource(R.drawable.t_record);
                Drawable drawable = res.getDrawable(R.drawable.b2_singleplay_back); //new Image that was added to the res folder
                main.setBackground(drawable);
                break;
            }
        }


        back_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        t_result = (TextView) findViewById(R.id.record_view);

        DataBase dbHelper = new DataBase(getApplicationContext(), "db_record.db",null,1);
        dbHelper.getWritableDatabase();

        Intent intent = new Intent(record.this.getIntent());
        score = intent.getIntExtra("SCORE",0);
        round= intent.getIntExtra("ROUND",0);
        time = intent.getIntExtra("TIME",0);
        record_sig = intent.getIntExtra("RECORD_SIG",0);

        if(record_sig ==1) {
            dbHelper.insert(score, round, time);
        }
        t_result.setText(dbHelper.getResult());
    }
}
