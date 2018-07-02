package com.example.connecfour.connect_four;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyGameActivity extends AppCompatActivity {

    private int mColumn;
    private int p1 = 1, p2 = 2, turn = 1;
    private int[][] board = new int [6][7];

    private ImageView mRed, mYellow;

    private MediaPlayer mPlayer, mWinMusic;

    private GridView mBoardView, mChipsView;
    private ImageButton nSound, Sound;
    private TextView nDisplayTurn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_game);

        mPlayer = MediaPlayer.create(this, R.raw.bake_ost);
        mWinMusic = MediaPlayer.create(this, R.raw.win_sfx);

        //Filling board with zeros
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                board[i][j] = 0;
            }
        }

        mRed = (ImageView) findViewById(R.id.red_chip);
        mYellow = (ImageView) findViewById(R.id.yellow_chip);
        mBoardView = (GridView)findViewById(R.id.gridViewBoard);
        mChipsView = (GridView)findViewById(R.id.gridViewChips);
        Sound = (ImageButton)findViewById(R.id.sound_btn);
        nSound = (ImageButton)findViewById(R.id.no_sound_btn);
        nDisplayTurn = (TextView)findViewById(R.id.textView1);

        mBoardView.setAdapter(new BoardAdapter(this));
        mChipsView.setAdapter(new ChipsAdapter(this));


        mBoardView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub

                if (actualTurn() == 1) {
                    nDisplayTurn.setText("P2 Moves");
                    mYellow.setVisibility(View.VISIBLE);
                    mRed.setVisibility(View.INVISIBLE);
                } else {
                    nDisplayTurn.setText("P1 Moves");
                    mRed.setVisibility(View.VISIBLE);
                    mYellow.setVisibility(View.INVISIBLE);
                }

                mColumn = position % 7;

                if (isFull(0) &&
                        isFull(1) &&
                        isFull(2) &&
                        isFull(3) &&
                        isFull(4) &&
                        isFull(5) &&
                        isFull(6)) {
                    //It's a Draw! Show Draw Dialog
                    alertDialogBox(-1);
                }
                if (isFull(mColumn)) {
                    Toast.makeText(MyGameActivity.this,
                                   "this column is full",
                                    Toast.LENGTH_SHORT).show();
                    /*Do Nothing*/
                } else {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce_from_top);
                    ImageView chipImg = (ImageView) mChipsView.getItemAtPosition(boardPos(mColumn));

                    if (actualTurn() == 1) {
                        chipImg.setImageResource(R.drawable.red);
                        chipImg.startAnimation(animation);
                    } else {
                        chipImg.setImageResource(R.drawable.yellow);
                        chipImg.startAnimation(animation);
                    }

                    chipImg.setVisibility(View.VISIBLE);
                    toggleTurn();
                }
            }
        });

        if ( checkWin(board) != 0 ) {
            //Show Button Dialog Message
            mWinMusic.start();
            alertDialogBox(checkWin(board));
        }
        
        Sound.setOnClickListener(view -> {
            Sound.setVisibility(View.INVISIBLE);
            nSound.setVisibility(View.VISIBLE);
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
            }
        });

        nSound.setOnClickListener(view -> {
            nSound.setVisibility(View.INVISIBLE);
            Sound.setVisibility(View.VISIBLE);
            mPlayer.start();
        });
    }

    /**
     * Function to return the person who's playing at the moment
     * @return 1 if Player1 or 2 if Player2
     **/
    public int actualTurn() { return turn == 1? p1 : p2; }

    /**
     * Function to change the turn of the player
     * @return the player who's playing next
     **/
    public int toggleTurn() {
        turn = turn == 1? 2 : 1;
        return turn == 1? p1 : p2;
    }

    /**
     * Function to return the person who's playing at the moment
     * @param col -- column that the chip was placed
     * @return row that the chip is going to be put on
     **/
    public int boardPos(int col) {

        for (int i = 5 ; i >= 0 ; i--) {
            if (board[i][col] == 0) {
                fillBoard(i, col);
                return col + (i * 7);
            }
        }
        return -1;
    }

    /**
     * Function fill the board with the player identifier
     * 1 if Player1 or 2 if Player2 // 0 if slot is empty.
     * @param row -- row that the chip was placed in.
     * @param column -- column that the chip was placed in.
     **/
    public void fillBoard (int row, int column){ board[row][column] = actualTurn(); }

    /**
     * Function to check if the column that was touch is full.
     * @param col -- column that the chip was intended to fall.
     * @return false if slot is empty or true if slot has a chip.
     **/
    public boolean isFull (int col) {

        for (int i = 5; i >= 0; i--) {
            if (board[i][col] == 0) return false;
        }

        return true;
    }

    /**
     * Function to check if one of the players has won in vertical, or
     * horizontal, or diagonal manner
     * @param board -- the condition of the board at the moment the chip was placed.
     * @return 1 if Player1 won, 2 if Player2 won or 0 if no one has won.
     **/
    public static int checkWin(int[][] board) {
        final int HEIGHT = board.length;
        final int WIDTH = board[0].length;

        int player;

        for (int row = 0; row < HEIGHT; row++) { // iterate rows, bottom to top
            for (int col = 0; col < WIDTH; col++) { // iterate columns, left to right
                player = board[row][col];

                // don't check empty slots
                if (player == 0) continue;

                // check horizontal
                if (col + 3 < WIDTH &&
                        player == board[row][col+1] &&
                        player == board[row][col+2] &&
                        player == board[row][col+3]) {

                    return player;
                }
                // check vertical
                if (row + 3 < HEIGHT) {
                    if (player == board[row+1][col] &&
                            player == board[row+2][col] &&
                            player == board[row+3][col]) {

                        return player;
                    }
                    // check diagonal right
                    if (col + 3 < WIDTH &&
                            player == board[row+1][col+1] &&
                            player == board[row+2][col+2] &&
                            player == board[row+3][col+3]) {

                        return player;
                    }
                    // check diagonal left
                    if (col - 3 >= 0 &&
                            player == board[row+1][col-1] &&
                            player == board[row+2][col-2] &&
                            player == board[row+3][col-3]) {

                        return player;
                    }
                }
            }
        }
        return 0; // no winner found
    }

    /**
     * Function to display a message if users want to play another game.
     * @param player -- player who has won the game, or if the game ended in a draw.
     **/
    public void alertDialogBox(int player) {
        String message;

        if (player == 1) message = "PLAYER ONE HAS WON!";
        else if (player == 2) message = "PLAYER TWO HAS WON!";
        else message = "IT'S A DRAW!";

        AlertDialog alert = new AlertDialog.Builder(MyGameActivity.this)
                .setTitle(message)
                .setMessage("DO YOU WISH TO PLAY AGAIN?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent gameActivity = new Intent(MyGameActivity.this, MyGameActivity.class);
                        startActivity(gameActivity);
                        mPlayer.stop();
                        finish();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent mainActivity = new Intent(MyGameActivity.this, MainActivity.class);
                        startActivity(mainActivity);
                        mPlayer.stop();
                        finish();
                    }
                }).setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Do Nothing
            }
        }).create();

        alert.show();

    }

    /**
     * Function to display alert message when back button has been pressed
     **/
    public void onBackPressed() {
        backButtonHandler();
    }


    /**
     * Function to handle what's displayed in the alert message when back button has been pressed
     **/
    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MyGameActivity.this);

        alertDialog.setTitle("Leave game?");
        alertDialog.setMessage("Are you sure you want to leave the this game?");
        alertDialog.setIcon(R.mipmap.ic_launcher);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mPlayer.stop();
                        finish();
                    }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        dialog.cancel();
                    }
        });

        alertDialog.show();
    }
}