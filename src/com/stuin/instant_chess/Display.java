package com.stuin.instant_chess;

import android.content.Context;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by Stuart on 5/22/2017.
 */
class Display {
    static String[] pieces;

    static void load(Context context) {
        pieces = new String[6];
        InputStream[] inputStream = new InputStream[6];

        inputStream[0] = context.getResources().openRawResource(R.raw.king);
        inputStream[1] = context.getResources().openRawResource(R.raw.queen);
        inputStream[2] = context.getResources().openRawResource(R.raw.rook);
        inputStream[3] = context.getResources().openRawResource(R.raw.bishop);
        inputStream[4] = context.getResources().openRawResource(R.raw.knight);
        inputStream[5] = context.getResources().openRawResource(R.raw.pawn);

        for(int i = 0; i < inputStream.length; i++) {
            Scanner s = new Scanner(inputStream[i]).useDelimiter("\\A");
            pieces[i] = s.hasNext() ? s.next() : "";
            s.close();
        }
    }
}
