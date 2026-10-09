package com.example.mad3new;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Act4Activity extends AppCompatActivity {
    private Button rollButton;
    private Button nextButton;
    private TextView resultText;
    private Spinner diceSpinner;
    private GridLayout diceGrid;

    private LinearLayout historyLayout;
    private ArrayList<UserData> players; // Your list of players
    private int currentPlayerIndex = 0; // Tracks the current player

    private TextView name;
    private DatabaseHelper databaseHelper;

    private Random random = new Random();

    private List<DiceView> diceViews = new ArrayList<>();
    private int numberOfDice = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_act4);
        name = findViewById(R.id.playername);
        rollButton = findViewById(R.id.rollButton);
        nextButton = findViewById(R.id.nextButton);
        resultText = findViewById(R.id.resultText);
        diceSpinner = findViewById(R.id.diceSpinner);
        diceGrid = findViewById(R.id.diceGrid);

        rollButton = findViewById(R.id.rollButton);
        resultText = findViewById(R.id.resultText);
        Intent NOD = getIntent();

        // Retrieve the integer value (0 is the default value if "USER_SCORE" doesn't exist)
        int numberOfDice = NOD.getIntExtra("totaldice", 1);


        players = (ArrayList<UserData>) getIntent().getSerializableExtra("PLAYER_LIST");


        startGame(numberOfDice);

    }


    // Call this method to kick off the game after 'players' is populated
    private void startGame(int numberOfDice) {
            createDice(numberOfDice);

            setupNextButton(numberOfDice); // Set up the click listener once
            rollButton.setOnClickListener(v -> rollDice());
            showPlayerTurn();
    }



    private void setupNextButton(int numberOfDice) {

        nextButton.setOnClickListener(v -> {
            // Move to the next player index and update the UI
            currentPlayerIndex++;
            startGame(numberOfDice);

        });

    }

    private void showPlayerTurn() {
        // 1. Check if all players have taken their turn BEFORE processing
        if (currentPlayerIndex >= players.size()) {
            Log.d("GameFlow", "All players have taken their turn.");

            return; // Stop execution so we don't crash on an out-of-bounds index
        }

        // 2. Get the current player data
        UserData player = players.get(currentPlayerIndex);

        // 3. Update the UI for this specific player
        name.setText(player.getName());
        Log.d("PlayerData", "Displaying turn for ID: " + player.getId() + ", Name: " + player.getName());
//        diceView.setBackgroundColor(player.getColor());
        // 4. Increment the index so the NEXT call to this method loads the next player
//        currentPlayerIndex++;
        return;
    }







    private void createDice(int numberOfDice) {

        // Remove old dice
        diceGrid.removeAllViews();
        UserData player = players.get(currentPlayerIndex);

        // Clear the list
        diceViews.clear();

        for (int i = 0; i < numberOfDice; i++) {

            DiceView diceView = new DiceView(this);

            // 90dp
            int size = (int) (
                    90 * getResources().getDisplayMetrics().density
            );

            GridLayout.LayoutParams params =
                    new GridLayout.LayoutParams();

            params.width = size;
            params.height = size;

            params.setMargins(10, 10, 10, 10);

            /*
             * kung naay 3 ka dice, dapat 2 sa babaw 1 sa ubos centered
             */

            if (numberOfDice == 3 && i == 2) {

                params.columnSpec =
                        GridLayout.spec(
                                0,
                                2,
                                GridLayout.CENTER
                        );
            }

            diceView.setLayoutParams(params);

            // Sugod sa 1
            diceView.setNumber(1);


            diceGrid.addView(diceView);


            diceViews.add(diceView);
        }

        // Reset result
        resultText.setText("Result: -");
    }


    private void rollDice() {
        int numberOfDice = (int) getIntent().getSerializableExtra("totaldice");

        rollButton.setEnabled(false);

        resultText.setText("Rolling...");


        // Store results for every die
        List<Integer> results = new ArrayList<>();


        // Generate random result for every die
        for (int i = 0; i < numberOfDice; i++) {

            int result = random.nextInt(6) + 1;

            results.add(result);
        }


        // Animate every die
        List<Animator> animations = new ArrayList<>();


        for (DiceView diceView : diceViews) {

            ObjectAnimator rotationX =
                    ObjectAnimator.ofFloat(
                            diceView,
                            "rotationX",
                            0f,
                            360f,
                            720f,
                            1080f
                    );


            ObjectAnimator rotationY =
                    ObjectAnimator.ofFloat(
                            diceView,
                            "rotationY",
                            0f,
                            360f,
                            720f,
                            1080f
                    );


            AnimatorSet diceAnimation = new AnimatorSet();

            diceAnimation.playTogether(
                    rotationX,
                    rotationY
            );

            diceAnimation.setDuration(1200);

            diceAnimation.setInterpolator(
                    new DecelerateInterpolator()
            );


            animations.add(diceAnimation);
        }


        // Combine all dice animations
        AnimatorSet allAnimations = new AnimatorSet();

        allAnimations.playTogether(animations);


        allAnimations.addListener(
                new Animator.AnimatorListener() {

                    @Override
                    public void onAnimationStart(
                            Animator animation) {

                        resultText.setText("Rolling...");
                    }


                    @Override
                    public void onAnimationEnd(
                            Animator animation) {

                        StringBuilder resultString =
                                new StringBuilder();
                        int totalResult = 0;
                        // Set each die's result
                        for (int i = 0; i < diceViews.size(); i++) {

                            DiceView diceView =
                                    diceViews.get(i);

                            int result =
                                    results.get(i);

                            diceView.setNumber(result);


                            if (i > 0) {
                                resultString.append(", ");
                            }
                            totalResult += result;
                            resultString.append(result);
                        }
                        resultString.append("\n Total: " + totalResult);

                        // Display result
                        resultText.setText(
                                "You rolled: " +
                                        resultString.toString()
                        );


                        // Result animation
                        resultText.setScaleX(0.7f);
                        resultText.setScaleY(0.7f);
                        resultText.setAlpha(0f);


                        resultText.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .alpha(1f)
                                .setDuration(300)
                                .start();


                        rollButton.setEnabled(true);
                    }


                    @Override
                    public void onAnimationCancel(
                            Animator animation) {

                        rollButton.setEnabled(true);
                    }


                    @Override
                    public void onAnimationRepeat(
                            Animator animation) {

                    }
                }
        );


        allAnimations.start();
    }







}