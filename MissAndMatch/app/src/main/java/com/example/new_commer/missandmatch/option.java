package com.example.new_commer.missandmatch;

/**
 * Created by Changgil on 2016-12-06.
 */

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class option extends AppCompatActivity {

    int ver = 0;
    int out=0;
    ImageButton option;
    ImageButton back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        RelativeLayout main = (RelativeLayout) findViewById(R.id.act_option);
        Resources res = getResources(); //resource handle

        option = (ImageButton) findViewById(R.id.b_opt);
        option.setBackgroundColor(000000);

        back = (ImageButton) findViewById(R.id.b_back_option);
        back.setBackgroundColor(000000);

        Intent intent = new Intent(option.this.getIntent());
        ver = intent.getIntExtra("VER", 0);

        switch (ver) {
            case 1: {
                option.setImageResource(R.drawable.b1_option);
                back.setImageResource(R.drawable.b1_back);
                Drawable drawable = res.getDrawable(R.drawable.b1_main_back); //new Image that was added to the res folder
                main.setBackground(drawable);
                break;
            }
            case 2: {
                option.setImageResource(R.drawable.b2_option);
                back.setImageResource(R.drawable.b2_back);
                Drawable drawable = res.getDrawable(R.drawable.b2_main_back); //new Image that was added to the res folder
                main.setBackground(drawable);
                break;
            }
        }

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
        option.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ver++;
                if(ver%2 ==0)
                    out = 2;
                else
                    out =1;
                Intent intent = new Intent(option.this,MainActivity.class);
                intent.putExtra("VER",out);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
