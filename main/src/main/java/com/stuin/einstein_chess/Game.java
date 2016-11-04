package com.stuin.einstein_chess;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

public class Game extends Activity {
    TextView[][] board = new TextView[8][8];
    private Piece[] set;
    boolean blackTurn = false;
    boolean win = false;
    boolean einstein = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }

    void setBoard() {
        TextView textView = (TextView) findViewById(R.id.Turn);
        if(blackTurn) textView.setText("Black's Turn");
        else textView.setText("White's Turn");
        boolean black = true;
        for(TextView[] l : board) {
            black = !black;
            for (TextView t : l) {
                black = !black;
                if (black) t.setBackgroundColor(Color.BLACK);
                else t.setBackgroundColor(Color.WHITE);
                t.setText("");
                t.setOnClickListener(null);
            }
        }
        for(Piece p : set) p.showPosition();
        blackTurn = !blackTurn;
    }

    public void makeBoard(View view) {
        if(view.getId() == R.id.Einstein) einstein = true;

        int scale = findViewById(R.id.Relative).getHeight();
        if(scale < findViewById(R.id.Relative).getWidth()) scale = findViewById(R.id.Relative).getWidth();
        scale = scale / 9;

        boolean black = true;
        GridLayout gridLayout = (GridLayout) findViewById(R.id.ChessBoard);
        int x = 0;
        int y = 0;
        int i = 0;
        for(TextView[] l : board) {
            black = !black;
            for(TextView t : l) {
                t = new TextView(this);
                t.setHeight(scale);
                t.setWidth(scale);
                t.setTextSize(scale / 4);
                t.setId(i);
                black = !black;
                gridLayout.addView(t);
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
            p = new Piece();
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
                        p.type = 2;
                        break;
                    case 4:
                        p.type = 1;
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
            p.game = this;
        }
        setBoard();
    }
}
