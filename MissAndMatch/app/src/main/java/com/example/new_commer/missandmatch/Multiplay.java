package com.example.new_commer.missandmatch;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Multiplay extends AppCompatActivity {

    private String name;
    ImageButton makeroom_button;
    ImageButton joinroom_button;
    ImageButton back_button;
    ImageView t_multi;

    int ver =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplay);
        final EditText NameEditor = (EditText)findViewById(R.id.Name_text);
        RelativeLayout main = (RelativeLayout) findViewById(R.id.activity_multiplay);
        Resources res = getResources(); //resource handle

        t_multi= (ImageView) findViewById(R.id.t_multi);
        t_multi.setBackgroundColor(00000000);

        back_button = (ImageButton )findViewById(R.id.back_button);
        back_button.setBackgroundColor(00000000);

        makeroom_button = (ImageButton)findViewById(R.id.make_button);
        makeroom_button.setBackgroundColor(00000000);

        joinroom_button = (ImageButton)findViewById(R.id.join_button);
        joinroom_button.setBackgroundColor(00000000);

        Intent intent = new Intent(Multiplay.this.getIntent());
        ver = intent.getIntExtra("VER", 0);

        switch (ver) {
            case 1: {
                makeroom_button.setImageResource(R.drawable.b1_enter);
                joinroom_button.setImageResource(R.drawable.b1_enter);
                t_multi.setImageResource(R.drawable.t_multiplay);
                back_button.setImageResource(R.drawable.b1_back);
                Drawable drawable = res.getDrawable(R.drawable.b1_multiplay_back); //new Image that was added to the res folder
                main.setBackground(drawable);
                break;
            }
            case 2: {
                makeroom_button.setImageResource(R.drawable.b2_enter);
                joinroom_button.setImageResource(R.drawable.b2_enter);
                back_button.setImageResource(R.drawable.b2_back);
                Drawable drawable = res.getDrawable(R.drawable.b2_multiroom_back); //new Image that was added to the res folder
                main.setBackground(drawable);
                break;
            }
        }

        makeroom_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the user's name
                if(NameEditor.getText().toString().length() == 0){
                    Toast.makeText(Multiplay.this, "Enter your name", Toast.LENGTH_SHORT).show();
                }else{
                    name = NameEditor.getText().toString();

                    //store data in an Intent
                    Intent makeroom_intent = new Intent(Multiplay.this, Multi_room.class);
                    makeroom_intent.putExtra("User1", new Client(name, 0, 0));
                    makeroom_intent.putExtra("VER",ver);

                    startActivity(makeroom_intent);
                }
            }
        });

        joinroom_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the user's name
                if(NameEditor.getText().toString().length() == 0){
                    Toast.makeText(Multiplay.this, "Enter your name", Toast.LENGTH_SHORT).show();
                }else {
                    name = NameEditor.getText().toString();
                    //store data in an Intent
                    Intent joinroom_intent = new Intent(Multiplay.this, Room_List.class);
                    joinroom_intent.putExtra("User2", new Client(name, 0, -1));
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