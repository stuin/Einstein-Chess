package com.stuin.einstein_chess;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class Piece {
    int x;
    int y;
    int type = 6;
    boolean moved = false;
    boolean black = false;
    private TextView[][] board;
    Game game;

    void showPosition() {
        board = game.board;
        TextView t = board[x][y];
        switch(type) {
            case 1:
                t.setText("K");
                break;
            case 2:
                t.setText("Q");
                break;
            case 3:
                t.setText("R");
                break;
            case 4:
                t.setText("B");
                break;
            case 5:
                t.setText("N");
                break;
            case 6:
                t.setText("P");
                break;
        }

        if(black) {
            t.setBackgroundColor(Color.GRAY);
            t.setTextColor(Color.BLACK);
        } else {
            t.setBackgroundColor(Color.GRAY);
            t.setTextColor(Color.WHITE);
        }

        t.setOnClickListener(selectListener);
    }

    private boolean contains(int x, int y) {
        return (x < 8 && x >= 0 && y < 8 && y >= 0);
    }

    private TextView.OnClickListener selectListener = new TextView.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(black == game.blackTurn) {
                List<Point> moves = new ArrayList<>();
                boolean moving = true;
                int dir = 1;
                int nX = x;
                int nY = y;
                switch(type) {
                    case 1:
                        while(dir != 0) {
                            moves.add(new Point(nX + dir, nY + dir));
                            moves.add(new Point(nX - dir, nY + dir));
                            moves.add(new Point(nX + dir, nY));
                            moves.add(new Point(nX, nY + dir));
                            if(dir == 1) dir = -1;
                            else dir = 0;
                        }
                        break;
                    case 2:
                        while(dir != 0) {
                            while(contains(nX, nY) && moving) {
                                nX += dir;
                                moves.add(new Point(nX, nY));
                                if(contains(nX, nY) && board[nX][nY].getText().length() != 0) moving = false;
                            }
                            nX = x;
                            moving = true;
                            while(contains(nX, nY) && moving) {
                                nY += dir;
                                moves.add(new Point(nX, nY));
                                if(contains(nX, nY) && board[nX][nY].length() != 0) moving = false;
                            }
                            nY = y;
                            moving = true;
                            if(dir == 1) dir = -1;
                            else dir = 0;
                        }
                        dir = 1;
                        if(type == 3 || type == 9) break;
                    case 4:
                        while(dir != 0) {
                            while(contains(nX, nY) && moving) {
                                nX += dir;
                                nY += dir;
                                moves.add(new Point(nX, nY));
                                if(contains(nX, nY) && board[nX][nY].length() != 0) moving = false;
                            }
                            nX = x;
                            nY = y;
                            moving = true;
                            while(contains(nX, nY) && moving) {
                                nX -= dir;
                                nY += dir;
                                moves.add(new Point(nX, nY));
                                if(contains(nX, nY) && board[nX][nY].length() != 0) moving = false;
                            }
                            nX = x;
                            nY = y;
                            moving = true;
                            if(dir == 1) dir = -1;
                            else dir = 0;
                        }
                        break;
                    case 5:
                        while(dir != 0) {
                            moves.add(new Point(x + 2 * dir, y + dir));
                            moves.add(new Point(x + 2 * dir, y - dir));
                            moves.add(new Point(x + dir, y + 2 * dir));
                            moves.add(new Point(x - dir, y + 2 * dir));
                            if(dir == 1) dir = -1;
                            else dir = 0;
                        }
                        break;
                    case 6:
                        if(black) {
                            if(nY == 1) moves.add(new Point(nX, nY + 2));
                            moves.add(new Point(nX, nY + 1));
                            while(dir != 0) {
                                if(contains(nX + dir, nY + 1) && board[nX + dir][nY + 1].length() != 0)
                                    moves.add(new Point(nX + dir, nY + 1));
                                if(dir == 1) dir = -1;
                                else dir = 0;
                            }
                        } else {
                            if(nY == 6) moves.add(new Point(nX, nY - 2));
                            moves.add(new Point(nX, nY - 1));
                            while(dir != 0) {
                                if(contains(nX + dir, nY - 1) && board[nX + dir][nY - 1].length() != 0)
                                    moves.add(new Point(nX + dir, nY - 1));
                                if(dir == 1) dir = -1;
                                else dir = 0;
                            }
                        }
                        break;
                }

                List<Point> remove = new ArrayList<>();
                for(Point p : moves)
                    if(contains(p.x, p.y)) {
                        if(black == (board[p.x][p.y].getCurrentTextColor() == Color.BLACK) && board[p.x][p.y].length() != 0)
                            remove.add(p);
                        if(p.x == x && p.y == y) remove.add(p);
                    } else remove.add(p);
                moves.removeAll(remove);

                game.setBoard();
                for(Point p : moves) {
                    if(board[p.x][p.y].length() == 0) board[p.x][p.y].setBackgroundColor(Color.GREEN);
                    else board[p.x][p.y].setBackgroundColor(Color.RED);
                    board[p.x][p.y].setOnClickListener(moveListener);
                }
            }
        }
    };

    private TextView.OnClickListener moveListener = new TextView.OnClickListener() {
        @Override
        public void onClick(View view) {
            int i = view.getId();
            x = i % 8;
            y = i / 8;
            moved = true;

            if(game.einstein && type > 1) {
                if(type < 6) type++;
                else type = 2;
            }

            game.win = board[x][y].getText().equals("K");
            game.setBoard();
        }
    };
}
