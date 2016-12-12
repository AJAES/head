package com.example.new_commer.missandmatch;


import com.example.new_commer.missandmatch.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collections;

public class Multi_room extends AppCompatActivity implements View.OnClickListener{

    private int[] index_array = new int[3];     //clicked buttons array
    private int click_count=0;                   //count how many times clicked
    private int score1 = 0;                       //user1's score
    private int score2 = 0;
    private int temp_index = 0;
    private int finishcount = 0;                 //number of matches
    private int finish_check_count = 0;         //number of remain matches
    private int start_flag = 0;                   //if clicked start button
    private int my_turn = 0;                      //my turn number
    private int opp_turn = 0;
    private int position = 0;                     //for checking button
    private int matchflag = 0;                    //if clicked match button
    int value = -1;                                 //time
    int ver=1;                                       //option
    int vibe =0;                                     //vibration

    ImageButton back_button;
    ImageButton miss_button;
    ImageButton match_button;
    ImageButton start_button;
    TextView t_view;                                   //time view
    Vibrator vibrator;
    TextView user1_view;
    TextView user2_view;
    TextView dummy_view;                              // " : "
    TextView connect_view;
    TextView score1_view;
    TextView score2_view;

    private stage i_stage = new stage();
    private stage_block[] button_block = new stage_block[9];

    // Debugging
    private static final String TAG = "Multi_room";
    private static final boolean D = true;

    // Message types sent from the BluetoothService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    // Name of the connected device
    private String mConnectedDeviceName = null;
    // Array adapter for the conversation thread
    private ArrayAdapter<String> mConversationArrayAdapter;
    // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BluetoothService mChatService = null;

    @TargetApi(5)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(D) Log.e(TAG, "+++ ON CREATE +++");
        setContentView(R.layout.activity_multi_room);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        RelativeLayout main = (RelativeLayout) findViewById(R.id.activity_multi_room);
        Resources res = getResources(); //resource handle

        Intent intent = getIntent();
        ver = (int)intent.getSerializableExtra("VER");
        vibe = intent.getIntExtra("VIBE", 0);

        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "������� ���Ұ�", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

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

        score1_view = (TextView) findViewById(R.id.m_score1_view);
        score1_view.setText(String.format("%d ", score1));
        score2_view = (TextView) findViewById(R.id.m_score2_view);
        score2_view.setText(String.format(" %d", score2));
        dummy_view = (TextView) findViewById(R.id.textView);
        dummy_view.setText("  :  ");
        connect_view = (TextView) findViewById(R.id.con_state);
        connect_view.setText(".....");

        t_view = (TextView) findViewById(R.id.m_timer);

        back_button = (ImageButton )findViewById(R.id.b_back_multi);
        back_button.setBackgroundColor(00000000);

        miss_button = (ImageButton)findViewById(R.id.b_miss_multi);
        miss_button.setBackgroundColor(00000000);
        miss_button.setOnClickListener(this);
        miss_button.setEnabled(false);

        match_button = (ImageButton)findViewById(R.id.b_match_multi);
        match_button.setBackgroundColor(00000000);
        match_button.setOnClickListener(this);
        match_button.setEnabled(false);

        start_button = (ImageButton)findViewById(R.id.start);
        start_button.setBackgroundColor(00000000);
        start_button.setOnClickListener(this);
        start_button.setEnabled(true);


        switch (ver) {
            case 1: {
                start_button.setImageResource(R.drawable.b1_start);
                back_button.setImageResource(R.drawable.b1_back);
                miss_button.setImageResource(R.drawable.b1_miss);
                match_button.setImageResource(R.drawable.b1_match);
                Drawable drawable = res.getDrawable(R.drawable.b1_multiplay_back); //new Image that was added to the res folder
                main.setBackground(drawable);
                break;
            }

            case 2: {
                start_button.setImageResource(R.drawable.b2_start);
                back_button.setImageResource(R.drawable.b2_back);
                miss_button.setImageResource(R.drawable.b2_miss);
                match_button.setImageResource(R.drawable.b2_match);
                Drawable drawable = res.getDrawable(R.drawable.b2_multi_back); //new Image that was added to the res folder
                main.setBackground(drawable);
                break;
            }
        }
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(vibe*100);
                finish();
            }
        });


        //Collections.shuffle(i_stage.ranNumber);
        for (int i = 0; i < 9; i++) {
            button_block[i].b_button.setTag(i);
            button_block[i].b_button.setOnClickListener(this);
            button_block[i].b_button.setBackgroundColor(00000000);
            button_block[i].b_position = 0;
        }
    }
    private Handler cHandler = new Handler()    {
        public void handleMessage(Message msg){
            if(value > 0) {
                value--;
                t_view.setText("" + value);
                cHandler.sendEmptyMessageDelayed(0, 1000);
            }else if(value == 0){
                sndtimesup();
                value--;
            }
            else if(value < 0){

            }
        }
    };

    @TargetApi(5)
    @Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "++ ON START ++");

        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else {
            if (mChatService == null) setupChat();
        }
    }
    @Override
    public synchronized void onResume() {
        super.onResume();
        if(D) Log.e(TAG, "+ ON RESUME +");

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }
    }
    @TargetApi(3)
    private void setupChat() {
        Log.d(TAG, "setupChat()");

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothService(this, mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }
    @Override
    public synchronized void onPause() {
        super.onPause();
        if(D) Log.e(TAG, "- ON PAUSE -");
    }
    @Override
    public void onStop() {
        super.onStop();
        if(D) Log.e(TAG, "-- ON STOP --");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (mChatService != null) mChatService.stop();
        if(D) Log.e(TAG, "--- ON DESTROY ---");
    }
    @TargetApi(5)
    private void ensureDiscoverable() {
        if(D) Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    // The action listener for the EditText widget, to listen for the return key
    private TextView.OnEditorActionListener mWriteListener =
            new TextView.OnEditorActionListener() {
                public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                    // If the action is a key-up event on the return key, send the message
                    if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                        String message = view.getText().toString();
                        sendMessage(message);
                    }
                    if(D) Log.i(TAG, "END onEditorAction");
                    return true;
                }
            };

    /**
     * Sends a message.
     * @param message  A string of text to send.
     */

    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            //mOutEditText.setText(mOutStringBuffer);
        }
    }

    // The Handler that gets information back from the BluetoothService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            connect_view.setText(R.string.title_connected_to);
                            connect_view.append(mConnectedDeviceName);
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            connect_view.setText(R.string.title_connecting);
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            connect_view.setText(R.string.title_not_connected);
                            break;
                    }
                case MESSAGE_WRITE:

                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    int readint = Integer.parseInt(readMessage);
                    int[] buttons = new int[3];
                    int temp = readint / 1000000;
                    switch (temp){
                        case 1:
                        case 2:
                        case 3: //setting for start
                            readint = readint % 1000000;
                            buttons[0] = (readint / 10000) - 50;
                            buttons[1] = ((readint % 10000) / 100) - 50;
                            buttons[2] = (readint % 100) - 50;
                            do_start(temp, buttons);
                            break;
                        case 4://click 3 buttons
                            readint = readint % 1000000;
                            buttons[0] = (readint / 10000) - 50;
                            buttons[1] = ((readint % 10000) / 100) - 50;
                            buttons[2] = (readint % 100) - 50;
                            play_user(buttons);
                            break;
                        case 5://click miss button
                            readint = readint % 1000000;
                            buttons[0] = (readint / 10000) - 50;
                            buttons[1] = ((readint % 10000) / 100) - 50;
                            buttons[2] = (readint % 100) - 50;
                            ifmiss(buttons);
                            break;
                        case 6: //time's up
                            rcvtimesup();
                            break;
                        default:
                            break;
                    }
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occured
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }
    @TargetApi(5)
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BLuetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent serverIntent = null;
        switch (item.getItemId()) {
            case R.id.secure_connect_scan:
                // Launch the DeviceListActivity to see devices and do scan
                serverIntent = new Intent(this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                return true;
            case R.id.insecure_connect_scan:
                // Launch the DeviceListActivity to see devices and do scan
                serverIntent = new Intent(this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
                return true;
            case R.id.discoverable:
                // Ensure this device is discoverable by others
                ensureDiscoverable();
                return true;
        }
        return false;
    }

    private void do_start(int flag, int[] ints){ // start as user2
        switch (flag) {
            case 1:
                value = -1;
                cHandler.sendEmptyMessage(0);
                this.start_flag = 1;

                this.opp_turn = 1;
                this.user1_view.setText(mConnectedDeviceName);
                this.score1 = 0;
                this.score1_view.setText(String.format("%d ", this.score1));

                this.my_turn = 2;
                this.user2_view.setText("  ME  ");
                this.score2 = 0;
                this.score2_view.setText(String.format(" %d", this.score2));

                this.start_button.setEnabled(false);
                this.match_button.setEnabled(false);
                this.miss_button.setEnabled(false);

                for (int i = 0; i < 9; i++) {
                    button_block[i].b_button.setEnabled(false);
                    button_block[i].b_button.setTag(i);
                    button_block[i].b_button.setOnClickListener(Multi_room.this);
                    button_block[i].b_button.setBackgroundColor(00000000);
                    button_block[i].b_position = 0;
                }
                for (int j = 0; j < 3; j++) {
                    temp_index = ints[j];
                    switch(ver){
                        case 2:
                            button_block[j].b_button.setImageResource(i_stage.block_array2[temp_index].image);
                            button_block[j].b_backcolor = i_stage.block_array2[temp_index].back_color;
                            button_block[j].b_shapecolor = i_stage.block_array2[temp_index].shape_color;
                            button_block[j].b_shape = i_stage.block_array2[temp_index].shape;
                            break;
                        case 1:
                            button_block[j].b_button.setImageResource(i_stage.block_array[temp_index].image);
                            button_block[j].b_backcolor = i_stage.block_array[temp_index].back_color;
                            button_block[j].b_shapecolor = i_stage.block_array[temp_index].shape_color;
                            button_block[j].b_shape = i_stage.block_array[temp_index].shape;
                        default:
                    }
                }
                break;
            case 2:
                for (int j = 0; j < 3; j++) {
                    temp_index = ints[j];
                    switch(ver){
                        case 2:
                            button_block[j+3].b_button.setImageResource(i_stage.block_array2[temp_index].image);
                            button_block[j+3].b_backcolor = i_stage.block_array2[temp_index].back_color;
                            button_block[j+3].b_shapecolor = i_stage.block_array2[temp_index].shape_color;
                            button_block[j+3].b_shape = i_stage.block_array2[temp_index].shape;
                            break;
                        case 1:
                            button_block[j+3].b_button.setImageResource(i_stage.block_array[temp_index].image);
                            button_block[j+3].b_backcolor = i_stage.block_array[temp_index].back_color;
                            button_block[j+3].b_shapecolor = i_stage.block_array[temp_index].shape_color;
                            button_block[j+3].b_shape = i_stage.block_array[temp_index].shape;
                        default:
                    }
                }
                break;
            case 3:
                for (int j = 0; j < 3; j++) {
                    temp_index = ints[j];
                    switch(ver){
                        case 2:
                            button_block[j+6].b_button.setImageResource(i_stage.block_array2[temp_index].image);
                            button_block[j+6].b_backcolor = i_stage.block_array2[temp_index].back_color;
                            button_block[j+6].b_shapecolor = i_stage.block_array2[temp_index].shape_color;
                            button_block[j+6].b_shape = i_stage.block_array2[temp_index].shape;
                            break;
                        case 1:
                            button_block[j+6].b_button.setImageResource(i_stage.block_array[temp_index].image);
                            button_block[j+6].b_backcolor = i_stage.block_array[temp_index].back_color;
                            button_block[j+6].b_shapecolor = i_stage.block_array[temp_index].shape_color;
                            button_block[j+6].b_shape = i_stage.block_array[temp_index].shape;
                        default:
                    }
                }
                this.finishcount = i_stage.check_finishcount(button_block);
                this.finish_check_count = finishcount;
                default:
                    break;
        }
    }
    private void play_user(int[] ints){
        if(ints[0] == ints[1]){
            Toast.makeText(this, "The opponent gave turn to you", Toast.LENGTH_SHORT).show();
        }else{
            switch (this.my_turn){
                case 1:
                    if(i_stage.match(button_block[ints[0]-1], button_block[ints[1]-1], button_block[ints[2]-1]) == 1){
                        for(int t = 0; t < this.finish_check_count; t ++){
                            if(this.i_stage.hab_array[t][1] == ints[0] - 1){
                                if(this.i_stage.hab_array[t][2] == ints[1] - 1) {
                                    if(this.i_stage.hab_array[t][3] == ints[2] - 1){
                                        //first chosen
                                        if(i_stage.hab_array[t][0] == 0){
                                            Toast.makeText(this, "Opponent clicked "+ ints[0] + ", " + ints[1] + ", " + ints[2] + " Button\n" + "are correct match +1 point", Toast.LENGTH_SHORT).show();
                                            this.finishcount--;
                                            this.score2++;
                                            this.i_stage.hab_array[t][0] = 1;
                                            score2_view.setText(String.format(" %d",this.score2));
                                        }
                                        //already chosen
                                        else{
                                            Toast.makeText(this, "Opponent clicked "+ ints[0] + ", " + ints[1] + ", " + ints[2] + " Button\n" + "are already chose -1 point", Toast.LENGTH_SHORT).show();
                                            this.score2--;
                                            score2_view.setText(String.format(" %d", this.score2));
                                        }
                                    }else continue;
                                }else continue;
                            }else continue;
                        }
                    }
                    else{
                        //incorrect
                        Toast.makeText(this, "Opponent clicked "+ ints[0] + ", " + ints[1] + ", " + ints[2] + " Button\n"+ "are NOT matched!! -1 point", Toast.LENGTH_SHORT).show();
                        this.score2--;
                        score2_view.setText(String.format(" %d",this.score2));
                    }
                    break;
                case 2:
                    if(i_stage.match(button_block[ints[0]-1], button_block[ints[1]-1], button_block[ints[2]-1]) == 1){
                        for(int t = 0; t < this.finish_check_count; t ++){
                            if(this.i_stage.hab_array[t][1] == ints[0] - 1){
                                if(this.i_stage.hab_array[t][2] == ints[1] - 1) {
                                    if(this.i_stage.hab_array[t][3] == ints[2] - 1){
                                        //first chosen
                                        if(i_stage.hab_array[t][0] == 0){
                                            Toast.makeText(this, "Opponent clicked "+ ints[0] + ", " + ints[1] + ", " + ints[2] + " Button\n" + "are correct match +1 point", Toast.LENGTH_SHORT).show();
                                            this.finishcount--;
                                            this.score1++;
                                            this.i_stage.hab_array[t][0] = 1;
                                            score1_view.setText(String.format("%d ",this.score1));
                                        }
                                        //already chosen
                                        else{
                                            Toast.makeText(this, "Opponent clicked "+ ints[0] + ", " + ints[1] + ", " + ints[2] + " Button\n" + "are already chose -1 point", Toast.LENGTH_SHORT).show();
                                            this.score1--;
                                            score1_view.setText(String.format("%d ", this.score1));
                                        }
                                    }else continue;
                                }else continue;
                            }else continue;
                        }
                    }
                    else{
                        //incorrect
                        Toast.makeText(this, "Opponent clicked "+ ints[0] + ", " + ints[1] + ", " + ints[2] + " Button\n"+ "are NOT matched!! -1 point", Toast.LENGTH_SHORT).show();
                        this.score1--;
                        score1_view.setText(String.format("%d ",this.score1));
                    }
                    break;

            }

        }
        match_button.setBackgroundColor(00000000);
        miss_button.setEnabled(true);
        match_button.setEnabled(true);
        click_count = 0;
        matchflag = 0;
        value = 30;
        cHandler.sendEmptyMessage(0);

    }
    private void ifmiss(int[] ints){
        value = 30;
        cHandler.sendEmptyMessage(0);
        matchflag = 0;
        click_count = 0;
        switch(this.my_turn){
            case 1:
                if (finishcount == 0) {
                    Toast.makeText(this, "Opponent Miss. +3 points", Toast.LENGTH_SHORT).show();
                    for(int i =0; i<9; i++){
                        button_block[i].b_button.setEnabled(false);
                        button_block[i].b_button.setTag(i);
                        button_block[i].b_button.setOnClickListener(this);
                        button_block[i].b_button.setBackgroundColor(Color.RED);
                        button_block[i].b_position = 0;
                    }
                    value = -1;
                    this.score2 = this.score2 + 3;
                    score2_view.setText(String.format("%d ", this.score2));
                } else {
                    Toast.makeText(this, "Opponent clicked miss button\n not miss yet -1 point", Toast.LENGTH_SHORT).show();
                    for(int i =0; i<9; i++){
                        button_block[i].b_button.setEnabled(false);
                        button_block[i].b_button.setTag(i);
                        button_block[i].b_button.setOnClickListener(this);
                        button_block[i].b_button.setBackgroundColor(00000000);
                        button_block[i].b_position = 0;
                    }
                    this.score2--;
                    score2_view.setText(String.format("%d ", this.score2));
                }
                break;
            case 2:
                if (finishcount == 0) {
                    Toast.makeText(this, "Opponent Miss. +3 points", Toast.LENGTH_SHORT).show();
                    for(int i =0; i<9; i++){
                        button_block[i].b_button.setEnabled(false);
                        button_block[i].b_button.setTag(i);
                        button_block[i].b_button.setOnClickListener(this);
                        button_block[i].b_button.setBackgroundColor(Color.LTGRAY);
                        button_block[i].b_position = 0;
                    }
                    value = -1;
                    this.score1 = this.score1 + 3;
                    score1_view.setText(String.format("%d ", this.score1));
                } else {
                    Toast.makeText(this, "Opponent clicked miss button\n not miss yet -1 point", Toast.LENGTH_SHORT).show();
                    for(int i =0; i<9; i++){
                        button_block[i].b_button.setEnabled(false);
                        button_block[i].b_button.setTag(i);
                        button_block[i].b_button.setOnClickListener(this);
                        button_block[i].b_button.setBackgroundColor(00000000);
                        button_block[i].b_position = 0;
                    }
                    this.score1--;
                    score1_view.setText(String.format("%d ", this.score1));
                }
                break;
        }
    }
    private void sndtimesup(){
        int c = 111111;
        String q = Integer.toString(c);
        c = 0;
        String message6 = "6"+q;
        sendMessage(message6);
        waitforsend();
    }
    private void rcvtimesup(){
        Toast.makeText(this, "Opponent time is up!!", Toast.LENGTH_SHORT).show();
        match_button.setEnabled(true);
        miss_button.setEnabled(true);
        matchflag = 0;
        value = 30;
        cHandler.sendEmptyMessage(0);
    }


    public void onClick(View v) {
        ImageButton newButton = (ImageButton) v;
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); //진동을위해

        if(newButton == start_button){ //click start ; start as user1
            start_flag = 1;
            my_turn = 1;
            opp_turn = 2;
            score1 = 0;
            score2 = 0;

            Collections.shuffle(i_stage.ranNumber);
            for (int i = 0; i < 9; i++) {
                button_block[i].b_button.setEnabled(false);
                button_block[i].b_button.setTag(i);
                button_block[i].b_button.setOnClickListener(this);
                temp_index = i_stage.ranNumber.get(i);
                button_block[i].b_button.setBackgroundColor(00000000);
                button_block[i].b_position = 0;
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
            }
            finishcount = i_stage.check_finishcount(button_block);
            finish_check_count = finishcount;

            value = 30;
            cHandler.sendEmptyMessage(0);
            user1_view.setText("  ME  ");
            score1_view.setText(String.format("%d ", score1));
            user2_view.setText(mConnectedDeviceName);
            score2_view.setText(String.format(" %d", score2));
            start_button.setEnabled(false);
            match_button.setEnabled(true);
            miss_button.setEnabled(true);


            int z = i_stage.ranNumber.get(0) + 50;
            String q = Integer.toString(z);
            z = 0;
            int x = i_stage.ranNumber.get(1) + 50;
            String w = Integer.toString(x);
            x = 0;
            int c = i_stage.ranNumber.get(2) + 50;
            String e = Integer.toString(c);
            c = 0;
            String message1 = "1"+q+w+e;
            sendMessage(message1);
            waitforsend();

            z = i_stage.ranNumber.get(3) + 50;
            q = Integer.toString(z);
            z = 0;
            x = i_stage.ranNumber.get(4) + 50;
            w = Integer.toString(x);
            x = 0;
            c = i_stage.ranNumber.get(5) + 50;
            e = Integer.toString(c);
            c = 0;
            String message2 = "2"+q+w+e;
            sendMessage(message2);
            waitforsend();

            z = i_stage.ranNumber.get(6) + 50;
            q = Integer.toString(z);
            z = 0;
            x = i_stage.ranNumber.get(7) + 50;
            w = Integer.toString(x);
            x = 0;
            c = i_stage.ranNumber.get(8) + 50;
            e = Integer.toString(c);
            c = 0;
            String message3 = "3"+q+w+e;
            sendMessage(message3);
            waitforsend();
        }

        if(start_flag == 1 && my_turn == 1){///////////play as user1
            ImageButton tempButton;
            if(newButton == match_button){
                if (matchflag == 0) {
                    int color = getResources().getColor(R.color.match_color);
                    match_button.setBackgroundColor(color);
                    for(int i =0; i<9; i++) {
                        button_block[i].b_button.setEnabled(true);
                    }
                    miss_button.setEnabled(false);
                    matchflag = 1;
                }else if(matchflag == 1){
                    match_button.setBackgroundColor(00000000);
                    for(int i =0; i<9; i++) {
                        button_block[i].b_button.setEnabled(false);
                    }
                    Toast.makeText(this, "Give turn to the opponent", Toast.LENGTH_SHORT).show();
                    value = -1;
                    match_button.setEnabled(false);
                    miss_button.setEnabled(false);
                    matchflag = 0;
                    int z = 10+50;
                    String q = Integer.toString(z);
                    z = 0;
                    int x = 10+50;
                    String w = Integer.toString(x);
                    x = 0;
                    int c = 10+50;
                    String e = Integer.toString(c);
                    c = 0;
                    String message4 = "4"+q+w+e;
                    sendMessage(message4);
                    waitforsend();
                }
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
                                                    score1++;
                                                    finishcount--;
                                                    i_stage.hab_array[t][0] = 1;
                                                    score1_view.setText(String.format("%d ",score1));
                                                }
                                                //already chosen
                                                else{
                                                    Toast.makeText(this, "clicked "+ index_array[0] + ", " + index_array[1] + ", " + index_array[2] + " Button\n" + "are already chose -1 point", Toast.LENGTH_SHORT).show();
                                                    score1--;
                                                    score1_view.setText(String.format("%d ", score1));
                                                }
                                            }else continue;
                                        }else continue;
                                    }else continue;
                                }
                            }
                            else{
                                //incorrect
                                Toast.makeText(this, "clicked "+ index_array[0] + ", " + index_array[1] + ", " + index_array[2] + " Button\n"+ "are NOT matched!! -1 point", Toast.LENGTH_SHORT).show();
                                score1--;
                                score1_view.setText(String.format("%d ",score1));
                            }
                            for(int t=0; t<3; t++){
                                button_block[index_array[t]-1].b_button.setBackgroundColor(00000000);
                                button_block[index_array[t]-1].b_position = 0;
                            }
                            match_button.setBackgroundColor(00000000);
                            match_button.setEnabled(false);
                            miss_button.setEnabled(false);
                            value = -1;
                            click_count = 0;
                            matchflag = 0;

                            int z = index_array[0] + 50;
                            String q = Integer.toString(z);
                            z = 0;
                            int x = index_array[1] + 50;
                            String w = Integer.toString(x);
                            x = 0;
                            int c = index_array[2] + 50;
                            String e = Integer.toString(c);
                            c = 0;
                            String message4 = "4"+q+w+e;
                            sendMessage(message4);
                            waitforsend();
                        }
                    }
                }
            }
            if(newButton == miss_button){
                matchflag = 0;
                click_count = 0;
                if (finishcount == 0) {
                    Toast.makeText(this, "Miss. +3 points", Toast.LENGTH_SHORT).show();
                    score1 = score1 + 3;

                    for(int i =0; i<9; i++){
                        button_block[i].b_button.setEnabled(false);
                        button_block[i].b_button.setTag(i);
                        button_block[i].b_button.setOnClickListener(this);
                        button_block[i].b_button.setBackgroundColor(getResources().getColor(R.color.match_color));
                        button_block[i].b_position = 0;
                    }
                    score1_view.setText(String.format("%d ", score1));

                } else {
                    Toast.makeText(this, "not miss yet -1 point", Toast.LENGTH_SHORT).show();
                    for(int i =0; i<9; i++){
                        button_block[i].b_button.setEnabled(false);
                        button_block[i].b_button.setTag(i);
                        button_block[i].b_button.setOnClickListener(this);
                        button_block[i].b_button.setBackgroundColor(00000000);
                        button_block[i].b_position = 0;
                    }
                    score1--;
                    score1_view.setText(String.format("%d ", score1));
                }
                match_button.setBackgroundColor(00000000);
                match_button.setEnabled(false);
                miss_button.setEnabled(false);
                value = -1;

                int z = 11 + 50;
                String q = Integer.toString(z);
                z = 0;
                int x = 11 + 50;
                String w = Integer.toString(x);
                x = 0;
                int c = 11 + 50;
                String e = Integer.toString(c);
                c = 0;
                String message5 = "5"+q+w+e;
                sendMessage(message5);
                waitforsend();
            }

        }else if(start_flag == 1 && my_turn == 2) {///////////play as user2
            ImageButton tempButton;
            if(newButton == match_button){
                if (matchflag == 0) {
                    int color = getResources().getColor(R.color.match_color);
                    match_button.setBackgroundColor(color);
                    for(int i =0; i<9; i++) {
                        button_block[i].b_button.setEnabled(true);
                    }
                    miss_button.setEnabled(false);
                    matchflag = 1;
                }else if(matchflag == 1){
                    match_button.setBackgroundColor(00000000);
                    for(int i =0; i<9; i++) {
                        button_block[i].b_button.setEnabled(false);
                    }
                    Toast.makeText(this, "Give turn to the opponent", Toast.LENGTH_SHORT).show();
                    value = -1;
                    match_button.setEnabled(false);
                    miss_button.setEnabled(false);
                    matchflag = 0;
                    int z = 10+50;
                    String q = Integer.toString(z);
                    z = 0;
                    int x = 10+50;
                    String w = Integer.toString(x);
                    x = 0;
                    int c = 10+50;
                    String e = Integer.toString(c);
                    c = 0;
                    String message4 = "4"+q+w+e;
                    sendMessage(message4);
                    waitforsend();
                }
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
                                                    score2++;
                                                    finishcount--;
                                                    i_stage.hab_array[t][0] = 1;
                                                    score2_view.setText(String.format(" %d",score2));
                                                }
                                                //already chosen
                                                else{
                                                    Toast.makeText(this, "clicked "+ index_array[0] + ", " + index_array[1] + ", " + index_array[2] + " Button\n" + "are already chose -1 point", Toast.LENGTH_SHORT).show();
                                                    score2--;
                                                    score2_view.setText(String.format(" %d", score2));
                                                }
                                            }else continue;
                                        }else continue;
                                    }else continue;
                                }
                            }
                            else{
                                //incorrect
                                Toast.makeText(this, "clicked "+ index_array[0] + ", " + index_array[1] + ", " + index_array[2] + " Button\n"+ "are NOT matched!! -1 point", Toast.LENGTH_SHORT).show();
                                score2--;
                                score2_view.setText(String.format(" %d",score2));
                            }
                            for(int t=0; t<3; t++){
                                button_block[index_array[t]-1].b_button.setBackgroundColor(00000000);
                                button_block[index_array[t]-1].b_position = 0;
                            }
                            match_button.setBackgroundColor(00000000);
                            match_button.setEnabled(false);
                            miss_button.setEnabled(false);
                            value = -1;
                            click_count = 0;
                            matchflag = 0;

                            int z = index_array[0] + 50;
                            String q = Integer.toString(z);
                            z = 0;
                            int x = index_array[1] + 50;
                            String w = Integer.toString(x);
                            x = 0;
                            int c = index_array[2] + 50;
                            String e = Integer.toString(c);
                            c = 0;
                            String message4 = "4"+q+w+e;
                            sendMessage(message4);
                            waitforsend();
                        }
                    }
                }
            }
            if(newButton == miss_button){
                matchflag = 0;
                click_count = 0;
                if (finishcount == 0) {
                    Toast.makeText(this, "Miss. +3 points", Toast.LENGTH_SHORT).show();
                    for(int i =0; i<9; i++){
                        button_block[i].b_button.setEnabled(false);
                        button_block[i].b_button.setTag(i);
                        button_block[i].b_button.setOnClickListener(this);
                        button_block[i].b_button.setBackgroundColor(getResources().getColor(R.color.match_color));
                        button_block[i].b_position = 0;
                    }
                    score2 = score2 + 3;
                    score2_view.setText(String.format(" %d", score2));

                } else {
                    Toast.makeText(this, "not miss yet -1 point", Toast.LENGTH_SHORT).show();
                    for(int i =0; i<9; i++){
                        button_block[i].b_button.setEnabled(false);
                        button_block[i].b_button.setTag(i);
                        button_block[i].b_button.setOnClickListener(this);
                        button_block[i].b_button.setBackgroundColor(00000000);
                        button_block[i].b_position = 0;
                    }
                    score2--;
                    score2_view.setText(String.format(" %d", score2));
                }
                match_button.setBackgroundColor(00000000);
                match_button.setEnabled(false);
                miss_button.setEnabled(false);
                value = -1;

                int z = 11 + 50;
                String q = Integer.toString(z);
                z = 0;
                int x = 11 + 50;
                String w = Integer.toString(x);
                x = 0;
                int c = 11 + 50;
                String e = Integer.toString(c);
                c = 0;
                String message5 = "5"+q+w+e;
                sendMessage(message5);
                waitforsend();
            }


        }
    }

    private void waitforsend(){
        try{
            Thread.sleep(50);
        }catch(InterruptedException e){
            System.out.println(e.getMessage());
        }
    }
}