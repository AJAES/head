package com.example.new_commer.missandmatch;

/**
 * Created by new_commer on 2016-12-13.
 */

public class stage2 extends stage {
    public init_image[] block_array2 = new init_image[27];

    public static final int imagearray2[]= new int[]{
            R.drawable.b2_bbg, R.drawable.b2_bbp, R.drawable.b2_bby,R.drawable.b2_bgg, R.drawable.b2_bgp, R.drawable.b2_bgy, R.drawable.b2_bwg, R.drawable.b2_bwp, R.drawable.b2_bwy,
            R.drawable.b2_cbg, R.drawable.b2_cbp, R.drawable.b2_cby,R.drawable.b2_cgg, R.drawable.b2_cgp, R.drawable.b2_cgy, R.drawable.b2_cwg, R.drawable.b2_cwp, R.drawable.b2_cwy,
            R.drawable.b2_dbg, R.drawable.b2_dbp, R.drawable.b2_dby,R.drawable.b2_dgg, R.drawable.b2_dgp, R.drawable.b2_dgy, R.drawable.b2_dwg, R.drawable.b2_dwp, R.drawable.b2_dwy};

    public stage2()  {
        new stage();
        for (int a = 0; a < 27; a++) {
            //ranNumber.add(a);
            block_array2[a] = new init_image();
        }
        for (int temp = 0; temp < 27; temp++) {
            block_array2[temp].image = imagearray2[temp];
            if (temp / 9 == 0) {//background color is black
                block_array2[temp].back_color = 0; //back_color == 0 is black
            } else if (temp / 9 == 1) {//background color is gray
                block_array2[temp].back_color = 1; //back_color == 1 is gray
            } else if (temp / 9 == 2) {//background color is white
                block_array2[temp].back_color = 2; //back_color == 2 is white
            }

            if (temp % 9 == 0 || temp % 9 == 1 || temp % 9 == 2) {//shape color is blue
                block_array2[temp].shape_color = 0;
            } else if (temp % 9 == 3 || temp % 9 == 4 || temp % 9 == 5) {//shape color is red
                block_array2[temp].shape_color = 1;
            } else if (temp % 9 == 6 || temp % 9 == 7 || temp % 9 == 8) {//shape color is yellow
                block_array2[temp].shape_color = 2;
            }

            if (temp % 3 == 0) {//shape is circle
                block_array2[temp].shape = 0;
            } else if (temp % 3 == 1) {//shape is square
                block_array2[temp].shape = 1;
            } else if (temp % 3 == 2) {//shape is triangle
                block_array2[temp].shape = 2;
            }
        }
    }
}
