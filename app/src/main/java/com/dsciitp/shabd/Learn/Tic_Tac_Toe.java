package com.dsciitp.shabd.Learn;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dsciitp.shabd.R;

import static com.dsciitp.shabd.Learn.TIC_TAC_TOE_GAME.board;
import static com.dsciitp.shabd.Learn.TIC_TAC_TOE_GAME.getComputerMove;
import static com.dsciitp.shabd.Learn.TIC_TAC_TOE_GAME.getGameStatus;
import static com.dsciitp.shabd.Learn.TIC_TAC_TOE_GAME.getUserMove;
import static com.dsciitp.shabd.Learn.TIC_TAC_TOE_GAME.initializeBoard;
import static com.dsciitp.shabd.Learn.TIC_TAC_TOE_GAME.isWinning;
import static com.dsciitp.shabd.Learn.TIC_TAC_TOE_GAME.markMoveOnBoard;

public class Tic_Tac_Toe extends Activity {

    char[][] check = new char[3][3];
    final Button[][] buttons = new Button[3][3];
    private Handler mHandler = new Handler();
    private Handler mHandler1 = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tictactoeactivity);


        buttons[0][0] = (Button) findViewById(R.id.box00);
        buttons[0][1] = (Button) findViewById(R.id.box01);
        buttons[0][2] = (Button) findViewById(R.id.box02);
        buttons[1][0] = (Button) findViewById(R.id.box10);
        buttons[1][1] = (Button) findViewById(R.id.box11);
        buttons[1][2] = (Button) findViewById(R.id.box12);
        buttons[2][0] = (Button) findViewById(R.id.box20);
        buttons[2][1] = (Button) findViewById(R.id.box21);
        buttons[2][2] = (Button) findViewById(R.id.box22);

        initializeBoard();


        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                check[i][j] = '-';
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int I = i;
                final int J = j;
                initializeBoard();
                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (check[I][J] == '-') {
                            check[I][J] = 'X';
                            buttons[I][J].setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));

                            buttons[I][J].setText("X");

                            String userMove = getUserMove(I, J);
                            markMoveOnBoard('X', userMove);
                            String gameStatus = getGameStatus(check);
                            if (!gameStatus.equals("InProgress")) {
                                Toast.makeText(getApplicationContext(), gameStatus, Toast.LENGTH_LONG).show();

                                initializeBoard();
                                for (int i = 0; i < 3; i++) {
                                    for (int j = 0; j < 3; j++) {
                                        check[i][j] = '-';
                                    }
                                }

                                for (int i = 0; i < 3; i++) {
                                    for (int j = 0; j < 3; j++) {
                                        buttons[i][j].setText(" ");
                                    }
                                }

                            }else {
                                String computerMove = getComputerMove();
                                markMoveOnBoard('O', computerMove);
                                final int row = Character.getNumericValue(computerMove.charAt(1));
                                final int column = Character.getNumericValue(computerMove.charAt(4));

                                buttons[row][column].setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));

                                mHandler.postDelayed(new Runnable() {
                                    public void run() {

                                        buttons[row][column].setText("O");
                                        mHandler1.postDelayed(new Runnable() {
                                                                  @Override
                                                                  public void run() {
                                                                      String gameStatus = getGameStatus(board);
                                                                      if (!gameStatus.equals("InProgress")) {
                                                                          Toast.makeText(getApplicationContext(), gameStatus, Toast.LENGTH_LONG).show();

                                                                          initializeBoard();
                                                                          for (int i = 0; i < 3; i++) {
                                                                              for (int j = 0; j < 3; j++) {
                                                                                  check[i][j] = '-';
                                                                              }
                                                                          }

                                                                          for (int i = 0; i < 3; i++) {
                                                                              for (int j = 0; j < 3; j++) {
                                                                                  buttons[i][j].setText(" ");
                                                                              }
                                                                          }
                                                                      }
                                                                  }
                                                              },500);

                                    }
                                }, 500);


                            }
                        }
                    }
                });
            }
        }


    }


}
