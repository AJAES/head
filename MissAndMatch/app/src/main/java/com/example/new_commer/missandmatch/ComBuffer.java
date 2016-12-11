package com.example.new_commer.missandmatch;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by new_commer on 2016-12-08.
 */

public class ComBuffer {
    int start = 0;
    int turn = 0;
    int score = 0;
    int flag = -1;//0:setting, 1:click 2: miss
    int set[] = new int[9];

    public ComBuffer(byte[] buf){
        for(int i = 0; i < 13; i++){
            byte[] temp = new byte[4];
            for(int j =0; j <4; j++) {
                temp[j] = buf[(4 * i) + j];
            }
            switch (i){
                case 0:
                    this.start = 1;
                    break;
                case 1:
                    this.turn = byteArrayToInt(temp);
                    break;
                case 2:
                    this.score = byteArrayToInt(temp);
                    break;
                case 3:
                    this.flag = byteArrayToInt(temp);
                    break;
                default:
                    if(this.flag == 1){
                        this.set[i-4] = byteArrayToInt(temp);
                    }else if(this.flag == 0){
                        if(i > 6) {break;}
                        this.set[i-4] = byteArrayToInt(temp);
                    }
                    break;
            }
        }
    }
    public ComBuffer(){

    }

    private  byte[] IntToByteArray(final int integer){
        ByteBuffer buffer = ByteBuffer.allocateDirect(Integer.SIZE / 8);
        buffer.putInt(integer);
        buffer.order(ByteOrder.BIG_ENDIAN);
        return buffer.array();
    }
    private static int byteArrayToInt(byte[] bytes){
        final int size = Integer.SIZE / 8;
        ByteBuffer buffer = ByteBuffer.allocate(size);
        final byte[] newBytes = new byte[size];
        for(int i = 0; i < size; i++){
            if(i + bytes.length < size){
                newBytes[i] = (byte) 0x00;
            }else{
                newBytes[i] = bytes[i+bytes.length - size];
            }
        }
        buffer = ByteBuffer.wrap(newBytes);
        buffer.order(ByteOrder.BIG_ENDIAN);
        return buffer.getInt();
    }
}
