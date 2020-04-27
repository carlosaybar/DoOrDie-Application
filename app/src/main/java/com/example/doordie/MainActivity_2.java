package com.example.doordie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import java.io.*;

public class MainActivity_2 extends AppCompatActivity implements View.OnClickListener{

    private int size = 4;
    private Button[][] buttons = new Button[size][size];
    public int x = 0;

    private boolean Turn = true;

    private int roundCount;

    public static int Points = MainActivity.Points+1;
    // private int player2Points;

    private TextView textViewPlayer1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);

        textViewPlayer1 = findViewById(R.id.text_view_p1);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
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


        Button next_Activity_button = findViewById(R.id.five_by_five_button);
        next_Activity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity_2.this, MainActivity_3.class);

                // start the activity connect to the specified class
                startActivity(intent);
            }
        });




    }

    @Override
    public void onClick(View v) {
        if (!((Button)v).getText().toString().equals("")) {
            return;
        }

        //int x = 0;

        if (Turn) {
            x++;
            // ((Button) v).setText((x));
            ((Button)v).setText(String.valueOf(x));
            // ((Button) v).setText("X");
        }
        else {
            x++;
            ((Button)v).setText(String.valueOf(x));

            //((Button) v).setText(String.valueOf("O"));
        }


        roundCount++;
        if ((roundCount == size * size) && (win())) {
            player1Wins();

            // x = 0;
        }
        else if ((roundCount == size * size) && (!win()))
        {
            lose();
            x = 0;
        }

    }

    private boolean win() {



        String[][] tempGrid = new String[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tempGrid[i][j] = (buttons[i][j].getText().toString());
            }
        }

        int[][] grid = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = Integer.parseInt(tempGrid[i][j]);
            }
        }




        // calculate the sum of the first diagonal
        int diag1Sum = 0;
        for (int i = 0; i < size; i++) {
            diag1Sum = diag1Sum + grid[i][i];
        }


        // calculate the sum of the second diagonal
        int diag2Sum = 0;
        for (int i = 0; i < size; i++)
        {
            diag2Sum = diag2Sum + grid[i][size - 1 - i];
        }


        //compare the sum of the two diagonals
        if (diag1Sum != diag2Sum)
        {
            return false;
        }


        // this for loop sums all the cells in a row and sotores them in rowSum
        for (int i = 0; i < size; i++) {
            int rowSum = 0;
            for (int j = 0; j < size; j++)
            {
                rowSum += grid[i][j]; //iterates through the cells and adds them
            }

            //compare the sum of the rows with that of the first diagonal to make sure they are the same
            if (rowSum != diag1Sum)
                return false;
        }

        // For sums of Columns
        for (int i = 0; i < size; i++) {

            int colSum = 0;
            for (int j = 0; j < size; j++)
                colSum += grid[j][i];

            //compare the sum of the colums with that of the diagonal to make sure they are the same
            if (diag1Sum != colSum)
                return false;
        }

        Intent intent = new Intent(MainActivity_2.this, MainActivity_3.class);

        // start the activity connect to the specified class
        startActivity(intent);
        return true;


    }


    private void player1Wins() {
        Points++;
        Toast.makeText(this, "Next Level", Toast.LENGTH_SHORT).show();

        try
        {
            FileOutputStream fos = new FileOutputStream("Scores.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject("Current Level: " + Points); //writes the serialized contacts to the file
            oos.close();
            fos.close();

        }
        catch (FileNotFoundException FNFE)
        {
            System.out.print("Error: file not found");
        }
        catch (IOException IOE)
        {
            System.out.print("Error: cannot write to the file");
        }

        updatePoints();
        clearBoard();
    }


    private void lose() {
        Toast.makeText(this, "Try again!", Toast.LENGTH_SHORT).show();
        clearBoard();
    }

    private void updatePoints() {
        textViewPlayer1.setText("Current Level: " + Points);
    }

    private void clearBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                buttons[i][j].setText("");
            }
        }

        roundCount = 0;
        Turn = true;
    }

    private void resetGame() {
        //player1Points = 0;
        updatePoints();
        clearBoard();
        x = 0;
    }

    //when we rotate the device
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("roundCount", roundCount);
        outState.putInt("player1Points", Points);
        outState.putBoolean("player1Turn", Turn);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        roundCount = savedInstanceState.getInt("roundCount");
        Points = savedInstanceState.getInt("player1Points");
        Turn = savedInstanceState.getBoolean("player1turn");
    }


}
