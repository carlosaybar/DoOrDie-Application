package com.example.doordie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.*;

/**
 *this class declares, sets the layout, and implements level 3
 * of my do or die game.
 */
public class MainActivity_3 extends AppCompatActivity implements View.OnClickListener{

    private int size = 5;
    private Button[][] buttons = new Button[size][size];
    public int x = 0;

    private boolean Turn = true;

    private int roundCount;

    public static int Points = MainActivity_2.Points; //Points is inherited from MainActivity_2
    // private int player2Points;

    private TextView textViewPlayer1;

    /**
     *this method calls the activity layout
     * launches the app, and creates all the buttons
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_3);

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
    }

    /**
     * this method writes the numbers to the grid each time
     * the player clicks one of the grids buttons.
     * it keeps track of the round count so that when the grid is full
     * it can validate the magic square
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (!((Button)v).getText().toString().equals("")) {
            return;
        }

       // int x = 0;

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
            playerWins();

            // x = 0;
        }
        else if ((roundCount == size * size) && (!win()))
        {
            lose();
            x = 0;
        }

    }

    /**
     *this method contains the logic and rules for a player to win this game
     * it checks to see if the numbers inputted are a magic square
     * if the magic square is valid, the player will advance to the next level
     * @return true or false, depending on whether the player wins or not
     */
    private boolean win() {



        String[][] tempGrid = new String[size][size];

        try{
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    tempGrid[i][j] = (buttons[i][j].getText().toString());
                }
            }
        }catch(NullPointerException NPE)
        {
            Toast.makeText(this, "Cell is empty!", Toast.LENGTH_SHORT).show();
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


        return true;


    }


    /**
     *updates the points and writes the scorte to
     * the dat file
     */
    private void playerWins() {
        Points++;
        Toast.makeText(this, "Next Level", Toast.LENGTH_SHORT).show();
        updateScore();
        clearGrid();
    }


    /**
     * this method displays a "Try again" message to the user
     * and clears the board for the user to play again
     */
    private void lose() {
        Toast.makeText(this, "Try again!", Toast.LENGTH_SHORT).show();
        clearGrid();
    }

    /**
     * updates the points whenever the player wins a round
     */
    private void updateScore() {
        textViewPlayer1.setText("Current Level: " + Points);
    }

    /**
     * erases any numbers in the grid
     * this is called whenever the player wins, loses, or restarts the game
     */
    private void clearGrid() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                buttons[i][j].setText("");
            }
        }

        roundCount = 0;
        Turn = true;
    }

    /**
     * whenver the user hits the restart button
     * this method will be called to clear the board
     * and to update the points
     */
    private void resetGame() {
        //player1Points = 0;
        updateScore();
        clearGrid();
        x = 0;
    }

    /**
     * This method makes sure you do not lose any of the game information
     * whenever you rotate your device. you can rotate your device at any point
     * during the game and your progress will not be lost
     * @param outState
     */
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("roundCount", roundCount);
        outState.putInt("Points", Points);
        outState.putBoolean("Turn", Turn);
    }

    /**
     * saves the information when the device is rotated
     * @param savedInstanceState
     */
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        roundCount = savedInstanceState.getInt("roundCount");
        Points = savedInstanceState.getInt("Points");
        Turn = savedInstanceState.getBoolean("Turn");
    }

    /**
     * The SaveScores method creates a file called
     * Scores.dat, where the scores/level completed will be saved
     * it uses recursion to write the information to the file
     * @param file the name fo the file (Scores.dat)
     * @param score the string conversion of points to be written to the file
     */
    private void SaveScores(ObjectOutputStream file, String score)
    {
        try {
            FileOutputStream fos = new FileOutputStream("Scores.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            score = String.valueOf( Points );
            this.SaveScores(oos, "Current level"); //writes this message to the file
            this.SaveScores(oos, score); //uses recursion to save the scores
            fos.close();
            oos.close();
        }catch(FileNotFoundException FNE)
        {
            Toast.makeText(this, "Scores file does not exist!", Toast.LENGTH_SHORT).show();
        }catch(IOException IOE)
        {
            Toast.makeText(this, "Try again!", Toast.LENGTH_SHORT).show();
        }

    }

}
