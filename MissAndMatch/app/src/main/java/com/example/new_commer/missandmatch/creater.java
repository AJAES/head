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

public class creater extends AppCompatActivity {

   ImageButton back_button;
    ImageView im_creater1;
    ImageView im_creater2;
    ImageView im_creater3;
    ImageView im_creater4;

    int ver =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creater);

        RelativeLayout main = (RelativeLayout) findViewById(R.id.act_create);
        Resources res = getResources(); //resource handle

        back_button = (ImageButton)findViewById(R.id.b_back_create);
        back_button.setBackgroundColor(00000000);

        im_creater1= (ImageView) findViewById(R.id.im_creater1);
        im_creater1.setBackgroundColor(00000000);
        im_creater1.setImageResource(R.drawable.creater1);

        im_creater2= (ImageView) findViewById(R.id.im_creater2);
        im_creater2.setBackgroundColor(00000000);
        im_creater2.setImageResource(R.drawable.creater2);

        im_creater3= (ImageView) findViewById(R.id.im_creater3);
        im_creater3.setBackgroundColor(00000000);
        im_creater3.setImageResource(R.drawable.creater3);

        im_creater4= (ImageView) findViewById(R.id.im_creater4);
        im_creater4.setBackgroundColor(00000000);
        im_creater4.setImageResource(R.drawable.creater4);

        Intent intent = new Intent(creater.this.getIntent());
        ver = intent.getIntExtra("VER", 0);

        switch (ver) {
            case 1: {
                back_button.setImageResource(R.drawable.b1_back);
                Drawable drawable = res.getDrawable(R.drawable.b1_madeby_back); //new Image that was added to the res folder
                main.setBackground(drawable);
                break;
            }
            case 2: {
                back_button.setImageResource(R.drawable.b2_back);
                Drawable drawable = res.getDrawable(R.drawable.b2_madeby_back); //new Image that was added to the res folder
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
    }


}
