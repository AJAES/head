package com.example.new_commer.missandmatch;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Room_List extends AppCompatActivity {

    Client user2;
    ImageButton back_button;
    ImageButton enter_button;
    ImageView t_multi;
    TextView v_roomnum;
    EditText e_enter_roomnum;
    String input = null;
    int enter_roomN = 0;

    int ver = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room__list);

        Intent intent = getIntent();
        user2 = (Client)intent.getSerializableExtra("User2");
        ver = (int)intent.getSerializableExtra("VER");
        Toast.makeText(this, user2.name, Toast.LENGTH_SHORT).show();

        RelativeLayout main = (RelativeLayout) findViewById(R.id.activity_room__list);
        Resources res = getResources(); //resource handle

        t_multi= (ImageView) findViewById(R.id.t_multi);
        t_multi.setBackgroundColor(00000000);

        back_button = (ImageButton )findViewById(R.id.b_back_multi);
        back_button.setBackgroundColor(00000000);

        enter_button = (ImageButton)findViewById(R.id.b_enter);
        enter_button.setBackgroundColor(00000000);


        switch (ver) {
            case 1: {
                enter_button.setImageResource(R.drawable.b1_enter);
                t_multi.setImageResource(R.drawable.t_multiplay);
                back_button.setImageResource(R.drawable.b1_back);
                Drawable drawable = res.getDrawable(R.drawable.b1_multiplay_back); //new Image that was added to the res folder
                main.setBackground(drawable);
                break;
            }
            case 2: {
                enter_button.setImageResource(R.drawable.b2_enter);
                back_button.setImageResource(R.drawable.b2_back);
                Drawable drawable = res.getDrawable(R.drawable.b2_multiroom_back); //new Image that was added to the res folder
                main.setBackground(drawable);
                break;
            }
        }
        v_roomnum= (TextView) findViewById(R.id.v_roomnum);
        v_roomnum.setBackgroundColor(00000000);
        e_enter_roomnum=(EditText) findViewById(R.id.e_enter_roomnum);
        e_enter_roomnum.setBackgroundColor(00000000);

        enter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(e_enter_roomnum.getText().toString().length() == 0){
                    Toast.makeText(Room_List.this, "Enter the room number", Toast.LENGTH_SHORT).show();
                }else {
                    input = e_enter_roomnum.getText().toString();
                    enter_roomN = Integer.parseInt(input);
                    //store data in an Intent
                    Intent joinroom_intent = new Intent(Room_List.this, Join_room.class);
                    joinroom_intent.putExtra("User2", new Client(user2.name, enter_roomN, -1));
                    joinroom_intent.putExtra("VER",ver);
                    startActivity(joinroom_intent);
                }
            }
        });
        back_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
    }
}