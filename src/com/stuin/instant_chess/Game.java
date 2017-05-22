package com.stuin.instant_chess;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.TextView;

public class Game extends Activity {
    TextView[][] board = new TextView[8][8];
    Piece[] set;
    boolean blackTurn = false;
    boolean win = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Display.load(this);

        findViewById(R.id.Relative).post(new Runnable() {
            @Override
            public void run() {
                makeBoard();
            }
        });
    }

    void setBoard() {
        if(blackTurn) findViewById(R.id.Relative).setBackgroundColor(Color.BLACK);
        else findViewById(R.id.Relative).setBackgroundColor(Color.WHITE);
        boolean black = true;
        for(TextView[] l : board) {
            black = !black;
            for (TextView t : l) {
                black = !black;
                t.setText("");
                t.setOnClickListener(null);
                t.setBackgroundColor(0);
            }
        }
        for(Piece p : set) p.showPosition(win);
    }

    @Override
    public void onBackPressed() {

    }

    private void makeBoard() {
        int scale = findViewById(R.id.Relative).getHeight();
        if(scale > findViewById(R.id.Relative).getWidth()) scale = findViewById(R.id.Relative).getWidth();
        scale = scale / 8;

        boolean black = true;
        GridLayout gridLayout = (GridLayout) findViewById(R.id.ChessBoard);
        int x = 0;
        int y = 0;
        int i = 0;
        for(TextView[] l : board) {
            black = !black;
            for(TextView t : l) {
                t = new TextView(this);
                t.setTextSize(scale / 4);
                t.setId(i);
                t.setWidth(scale);
                t.setHeight(scale);
                black = !black;

                FrameLayout frameLayout = new FrameLayout(this);
                frameLayout.setMinimumHeight(scale);
                frameLayout.setMinimumWidth(scale);
                frameLayout.addView(t);

                if (black) frameLayout.setBackgroundColor(Color.BLACK);
                else frameLayout.setBackgroundColor(Color.WHITE);

                gridLayout.addView(frameLayout);
                board[x][y] = t;
                x++;
                i++;
            }
            y++;
            x = 0;
        }

        y = 0;
        i = 0;
        set = new Piece[32];
        for(Piece p : set) {
            p = new Piece(this);
            p.x = x;
            p.y = y;
            if(y < 2) p.black = true;
            if(y == 0 || y == 7) {
                switch(x) {
                    case 0:case 7:
                        p.type = 3;
                        break;
                    case 1:case 6:
                        p.type = 5;
                        break;
                    case 2:case 5:
                        p.type = 4;
                        break;
                    case 3:
                        if(y == 7) p.type = 1;
                        else p.type = 2;
                        break;
                    case 4:
                        if(y == 0) p.type = 1;
                        else p.type = 2;
                        break;
                }
            }

            x++;
            //Next Row
            if(x == 8) {
                x = 0;
                y++;
                if(y == 2) y = 6;
            }

            set[i] = p;
            i++;
        }
        setBoard();
    }
}
