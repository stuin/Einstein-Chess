package com.stuin.instant_chess;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class Piece {
    int x;
    int y;
    private int i;
    int type = 6;
    boolean black = false;

    private boolean moved = false;
    private TextView[][] board;
    private Game game;

    Piece(Game nGame) {
        game = nGame;
    }

    void showPosition(boolean end) {
        board = game.board;
        TextView t = board[x][y];
        t.setText(Display.pieces[type - 1]);
        t.setTypeface(Typeface.MONOSPACE);
        t.setTextSize(4);

        if(type != 0) {
            if(black) {
                t.setBackgroundColor(Color.GRAY);
                t.setRotation(180);
                t.setTextColor(Color.BLACK);
            } else {
                t.setBackgroundColor(Color.GRAY);
                t.setTextColor(Color.WHITE);
            }

            i = t.getId();
            if(!end) t.setOnClickListener(selectListener);
        }
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
                while(dir != 0) {
                    int nX = x;
                    int nY = y;
                    switch(type) {
                        case 1:
                            //king
                            moves.add(new Point(nX + dir, nY + dir));
                            moves.add(new Point(nX - dir, nY + dir));
                            moves.add(new Point(nX + dir, nY));
                            moves.add(new Point(nX, nY + dir));
                            break;
                        case 2:
                        case 3:
                            //queen & rook
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
                            if(type == 3) break;
                        case 4:
                            //bishop
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
                            moving = true;
                            break;
                        case 5:
                            //knight
                            moves.add(new Point(x + 2 * dir, y + dir));
                            moves.add(new Point(x + 2 * dir, y - dir));
                            moves.add(new Point(x + dir, y + 2 * dir));
                            moves.add(new Point(x - dir, y + 2 * dir));
                            break;
                        case 6:
                            //pawn
                            if(black) nY++;
                            else nY--;
                            if(contains(x, nY)) {
                                if(board[nX][nY].length() == 0 && dir == 1) {
                                    moves.add(new Point(nX, nY));
                                    if(!moved && black && board[nX][nY + 1].length() == 0)
                                        moves.add(new Point(nX, nY + 1));
                                    if(!moved && !black && board[nX][nY - 1].length() == 0)
                                        moves.add(new Point(nX, nY - 1));
                                }
                                if(contains(nX + dir, nY) && board[nX + dir][nY].length() != 0)
                                    moves.add(new Point(nX + dir, nY));
                            }
                            break;
                    }
                    if(dir == 1) dir = -1;
                    else dir = 0;
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

                if(type == 1 && !moved) {
                    i = view.getId();
                    if(black) i--;
                    if(board[1][y].length() == 0 && board[2][y].length() == 0 && (!black || (black && board[3][y].length() == 0))) for(Piece p : game.set) if(p.i == i - 3 && !p.moved) {
                        board[1][y].setBackgroundColor(Color.BLUE);
                        board[1][y].setOnClickListener(castleListener);
                    }
                    if(board[6][y].length() == 0 && board[5][y].length() == 0 && (black || (!black && board[4][y].length() == 0))) for(Piece p : game.set) if(p.i == i + 4 && !p.moved) {
                        board[6][y].setBackgroundColor(Color.BLUE);
                        board[6][y].setOnClickListener(castleListener);
                    }
                }

                if(type == 6 && ((y == 7 && black) || (y == 0 && !black))) {
                    view.setOnClickListener(promoteListener);
                    view.setBackgroundColor(Color.CYAN);
                }
            }
        }
    };

    private TextView.OnClickListener moveListener = new TextView.OnClickListener() {
        @Override
        public void onClick(View view) {
            i = view.getId();
            x = i % 8;
            y = i / 8;
            moved = true;

            if(board[x][y].length() != 0) {
                for(int j = 0; j < game.set.length; j++)
                    if(game.set[j].i == i && game.set[j].black != black) game.set[j].type = 0;
                game.win = board[x][y].getText().equals("K");
            }

            if(!game.win) game.blackTurn = !game.blackTurn;
            game.setBoard();
        }
    };

    private TextView.OnClickListener castleListener = new TextView.OnClickListener() {
        @Override
        public void onClick(View view) {
            i = view.getId();
            int nX = i % 8;

            if(nX < x) for(Piece p : game.set) if(p.i == i - 1) {
                p.x = 2;
                moved = true;
            }

            if(nX > x) for(Piece p : game.set) if(p.i == i + 1) {
                p.x = 5;
                moved = true;
            }

            moveListener.onClick(view);
        }
    };

    private TextView.OnClickListener promoteListener = new TextView.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(type > 1) {
                if(type > 2) type--;
                else type = 6;
            }
            game.setBoard();
            selectListener.onClick(view);
        }
    };
}
