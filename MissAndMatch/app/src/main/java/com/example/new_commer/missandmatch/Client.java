package com.example.new_commer.missandmatch;

import java.io.Serializable;

/**
 * Created by new_commer on 2016-12-07.
 */

public class Client implements Serializable{
    String name;
    int score;
    int roomNumber;
    int turn;

    public Client(String name, int roomNumber, int opponent){
        this.name = name;
        this.roomNumber = roomNumber;
        this.turn = opponent;

    }
    @Override
    public boolean equals(Object obj) {
        if(((Client)obj).turn != this.turn && ((Client)obj).roomNumber == this.roomNumber){
            return true;
        }else{
            return false;
        }
    }
}
