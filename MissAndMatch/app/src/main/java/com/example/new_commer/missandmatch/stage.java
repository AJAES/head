package com.example.new_commer.missandmatch;

import java.util.ArrayList;

/**
 * Created by hskim7341 on 2015-11-02.
 */
public class stage{
    protected class init_image {
        public int image;
        public int shape;
        public int back_color;
        public int shape_color;
        init_image(){
            this.image = 0;
            this.shape = 0;
            this.back_color = 0;
            this.shape_color = 0;
        }
    }

   public static final int imagearray1[]= new int[]{
           R.drawable.b1_mbg, R.drawable.b1_mbo, R.drawable.b1_mbp,R.drawable.b1_mgg, R.drawable.b1_mgo, R.drawable.b1_mgp, R.drawable.b1_mwg, R.drawable.b1_mwo, R.drawable.b1_mwp,
           R.drawable.b1_sbg, R.drawable.b1_sbo, R.drawable.b1_sbp,R.drawable.b1_sgg, R.drawable.b1_sgo, R.drawable.b1_sgp, R.drawable.b1_swg, R.drawable.b1_swo, R.drawable.b1_swp,
           R.drawable.b1_sunbg, R.drawable.b1_sunbo, R.drawable.b1_sunbp,R.drawable.b1_sungg, R.drawable.b1_sungo, R.drawable.b1_sungp, R.drawable.b1_sunwg, R.drawable.b1_sunwo, R.drawable.b1_sunwp};


    ArrayList<Integer> ranNumber = new ArrayList<Integer>();

    public int match(stage_block button1, stage_block button2, stage_block button3){
        if((button1.b_shape + button2.b_shape + button3.b_shape) % 3 == 0){
            if((button1.b_shapecolor + button2.b_shapecolor + button3.b_shapecolor) % 3 == 0){
                if((button1.b_backcolor + button2.b_backcolor + button3.b_backcolor) % 3 == 0){
                    return 1;
                }
            }
        }
        return -1;
    }

    int[][] hab_array = new int[84][4]; //[0]check_flag
    public init_image[] block_array = new init_image[27];
    //public init_image[] block_array2 = new init_image[27];

    stage() {
        for (int a = 0; a < 27; a++) {
            ranNumber.add(a);
            block_array[a] = new init_image();
            //block_array2[a] = new init_image();
        }


        for (int temp = 0; temp < 27; temp++) {
            block_array[temp].image = imagearray1[temp];
            //block_array2[temp].image = imagearray2[temp];
            if (temp / 9 == 0) {//background color is black
                block_array[temp].back_color = 0; //back_color == 0 is black
                //block_array2[temp].back_color = 0; //back_color == 0 is black
            } else if (temp / 9 == 1) {//background color is gray
                block_array[temp].back_color = 1; //back_color == 1 is gray
                //block_array2[temp].back_color = 1; //back_color == 1 is gray
            } else if (temp / 9 == 2) {//background color is white
                block_array[temp].back_color = 2; //back_color == 2 is white
                //block_array2[temp].back_color = 2; //back_color == 2 is white
            }

            if (temp % 9 == 0 || temp % 9 == 1 || temp % 9 == 2) {//shape color is blue
                block_array[temp].shape_color = 0;
                //block_array2[temp].shape_color = 0;
            } else if (temp % 9 == 3 || temp % 9 == 4 || temp % 9 == 5) {//shape color is red
                block_array[temp].shape_color = 1;
                //block_array2[temp].shape_color = 1;
            } else if (temp % 9 == 6 || temp % 9 == 7 || temp % 9 == 8) {//shape color is yellow
                block_array[temp].shape_color = 2;
                //block_array2[temp].shape_color = 2;
            }

            if (temp % 3 == 0) {//shape is circle
                block_array[temp].shape = 0;
                //block_array2[temp].shape = 0;
            } else if (temp % 3 == 1) {//shape is square
                block_array[temp].shape = 1;
                //block_array2[temp].shape = 1;
            } else if (temp % 3 == 2) {//shape is triangle
                block_array[temp].shape = 2;
                //block_array2[temp].shape = 2;
            }
        }
    }

    int check_finishcount(stage_block[] b_block){
        int count = 0;
        int a = 0;
        int b = 0;
        int c = 0;
        int[] temp = new int[3];
        for(a = 0; a < 9; a++){
            for( b = a+1; b < 9; b++){
                for(c = b+1; c <  9; c++){
                    if(match(b_block[a], b_block[b], b_block[c]) == 1){
                        temp[0] = a;    temp[1] = b;    temp[2] = c;
                        uppersort(temp);
                        this.hab_array[count][0] = 0;
                        this.hab_array[count][1] = temp[0];
                        this.hab_array[count][2] = temp[1];
                        this.hab_array[count][3] = temp[2];
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public void uppersort(int[] x){
        int temp = 0;
        for(int y =0; y < x.length; y++){
            for(int z=y+1; z < x.length; z++){
                if(x[y] > x[z]){
                    temp = x[y];    x[y] = x[z];    x[z]=temp;
                }
            }
        }
    }

}
