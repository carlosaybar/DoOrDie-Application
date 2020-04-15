package com.example.doordie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[][] buttons = new Button[3][3];
    public int x = 0;

    private boolean player1Turn = true;

    private int roundCount;

    public static int player1Points;
    // private int player2Points;

    private TextView textViewPlayer1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewPlayer1 = findViewById(R.id.text_view_p1);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }

        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetGame();
            }
        });
/*
        if(player1Points > 0){
            Intent intent = new Intent(MainActivity.this, MainActivity_2.class);

            // start the activity connect to the specified class
            startActivity(intent);
        }

 */

        Button next_Activity_button = findViewById(R.id.four_by_four_button);
        next_Activity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, MainActivity_2.class);

                // start the activity connect to the specified class
                startActivity(intent);
            }
        });


    }

    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }

        //int x = 0;

        if (player1Turn) {
            x++;
            // ((Button) v).setText((x));
            ((Button) v).setText(String.valueOf(x));
            // ((Button) v).setText("X");
        } else {
            x++;
            ((Button) v).setText(String.valueOf(x));

            //((Button) v).setText(String.valueOf("O"));
        }


        roundCount++;
        if ((roundCount == 9) && (checkForWin())) {
            player1Wins();
            // x = 0;
        }
        else if((roundCount == 9) &&(!checkForWin()))
        {
            draw();
            x = 0;
        }

    }

    private boolean checkForWin() {



        String[][] tempGrid = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tempGrid[i][j] = (buttons[i][j].getText().toString());
            }
        }

        int [] [] grid = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                grid[i][j] = Integer.parseInt(tempGrid[i][j]);
            }
        }


        if( grid [0][0] + grid[1][0] + grid[2][0] == grid [0][1] + grid[1][1] + grid[2][1] &&
                grid [0][1] + grid[1][1] + grid[2][1] == grid [0][2] + grid[1][2] + grid[2][2] &&
                grid [0][0] + grid[0][1] + grid[0][2] ==  grid [1][0] + grid[1][1] + grid[1][2] &&
                grid [1][0] + grid[1][1] + grid[1][2] == grid [2][0] + grid[2][1] + grid[2][2] &&
                grid [0][0] + grid[1][1] + grid[2][2] == grid [2][0] + grid[1][1] + grid[0][2])
        {
            Intent intent = new Intent(MainActivity.this, MainActivity_2.class);

            // start the activity connect to the specified class
            startActivity(intent);
            return true;

        }

        return false;
    }


    private void player1Wins() {
        player1Points++;
        Toast.makeText(this, "Next Level", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }


    private void draw() {
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        resetBoard();
    }

    private void updatePointsText() {
        textViewPlayer1.setText("Current Level: " + player1Points);
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }

        roundCount = 0;
        player1Turn = true;
    }

    private void resetGame(){
        //player1Points = 0;
        updatePointsText();
        resetBoard();
        x = 0;
    }

    //when we rotate the device
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt("roundCount", roundCount);
        outState.putInt("player1Points", player1Points);
        outState.putBoolean("player1Turn", player1Turn);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        roundCount = savedInstanceState.getInt("roundCount");
        player1Points = savedInstanceState.getInt("player1Points");
        player1Turn = savedInstanceState.getBoolean("player1turn");
    }


}