package com.stuin.einstein_chess;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

public class Game extends Activity {
    TextView[][] board = new TextView[8][8];
    Piece[] set;
    boolean blackTurn = false;
    boolean win = false;
    boolean einstein = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        if(getActionBar() != null) getActionBar().hide();
    }

    void setBoard() {
        TextView textView = (TextView) findViewById(R.id.Turn);
        String[] s = getResources().getStringArray(R.array.game_point);
        if(blackTurn) textView.setText(s[0]);
        else textView.setText(s[1]);
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
        if(win) {
            if(blackTurn) textView.setText(s[2]);
            else textView.setText(s[3]);
        }
        for(Piece p : set) p.showPosition(win);
    }

    @Override
    public void onBackPressed() {
        einstein = false;
        win = false;
        blackTurn = false;

        findViewById(R.id.Normal).setVisibility(View.VISIBLE);
        findViewById(R.id.Einstein).setVisibility(View.VISIBLE);

        GridLayout gridLayout = (GridLayout) findViewById(R.id.ChessBoard);
        gridLayout.removeAllViewsInLayout();

        String s = getResources().getString(R.string.app_name);
        TextView textView = (TextView) findViewById(R.id.Turn);
        textView.setText(s);
    }

    public void makeBoard(View view) {
        if(view.getId() == R.id.Einstein) einstein = true;
        findViewById(R.id.Normal).setVisibility(View.GONE);
        findViewById(R.id.Einstein).setVisibility(View.GONE);

        int scale = findViewById(R.id.Relative).getHeight();
        if(scale < findViewById(R.id.Relative).getWidth()) scale = findViewById(R.id.Relative).getWidth();
        scale = scale / 14;

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
            p.game = this;
        }
        setBoard();
    }
}
