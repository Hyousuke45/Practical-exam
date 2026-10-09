package com.example.mad3new;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;










public class dicepanel extends AppCompatActivity {

    private Button goButton;

    private Spinner diceSpinnerPlayer;
    private Spinner diceSpinnerDice;
    private LinearLayout playerSelection;

    private Random random = new Random();



    private int numberOfDice = 1;
    private int numberOfPlayer = 1;



    private List<View> rowViewList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.dicepanel);
        diceSpinnerPlayer = findViewById(R.id.diceSpinnerPlayer);
        diceSpinnerDice = findViewById(R.id.diceSpinnerDice);
        playerSelection = findViewById(R.id.playerSelection);


        goButton = findViewById(R.id.goButton);


        String[] diceOptions = {
                "1",
                "2",
                "3",
                "4"
        };

        String[] playerOptions = {
                "1",
                "2",
                "3",
                "4",
                "5"
        };





        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                diceOptions
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        diceSpinnerDice.setAdapter(adapter);


        // When dropdown selection changes
        diceSpinnerDice.setOnItemSelectedListener(
                new android.widget.AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            android.widget.AdapterView<?> parent,
                            View view,
                            int position,
                            long id) {

                        numberOfDice = position + 1;

                    }


                    @Override
                    public void onNothingSelected(
                            android.widget.AdapterView<?> parent) {

                    }
                }
        );










        ArrayAdapter<String> adapterplayer = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                playerOptions
        );

        adapterplayer.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        diceSpinnerPlayer.setAdapter(adapterplayer);


        // When dropdown selection changes
        diceSpinnerPlayer.setOnItemSelectedListener(
                new android.widget.AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            android.widget.AdapterView<?> parent,
                            View view,
                            int position,
                            long id) {

                        numberOfPlayer = position + 1;
                        generateFormFields(numberOfPlayer);
                    }


                    @Override
                    public void onNothingSelected(
                            android.widget.AdapterView<?> parent) {

                    }
                }
        );













        goButton.setOnClickListener(v -> goAction());
    }



    private void goAction() {

        // Inside your "Next" button click listener:

            ArrayList<UserData> savedPlayers = new ArrayList<>();

            // 1. Loop through your tracked row views
            for (View rowView : rowViewList) {
                EditText etName = rowView.findViewById(R.id.et_name);
                Button btnPickColor = rowView.findViewById(R.id.btn_pick_color);
                TextView tvId = rowView.findViewById(R.id.playerId);

                // 2. Extract values
                String playerName = etName.getText().toString().trim();
                String playerId = tvId.getText().toString();

                // Retrieve the color from the tag (fallback to Gray if null)
                int playerColor = Color.GRAY;
                if (btnPickColor.getTag() != null) {
                    playerColor = (int) btnPickColor.getTag();
                }

                // 3. Add to your list
                savedPlayers.add(new UserData(playerName, playerColor, playerId));
            }

            // 4. Pass the data to the next intent
            Intent intent = new Intent(dicepanel.this, Act4Activity.class);

            intent.putExtra("PLAYER_LIST", savedPlayers);
            intent.putExtra("totaldice", numberOfDice);
            startActivity(intent);
        };















    private void generateFormFields(int count) {
        playerSelection.removeAllViews();
        rowViewList.clear();

        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < count; i++) {
            // Inflate row_input.xml layout into parent container
            View rowView = inflater.inflate(R.layout.row_input, playerSelection, false);

            EditText etName = rowView.findViewById(R.id.et_name);

            Button btnPickColor = rowView.findViewById(R.id.btn_pick_color);

            TextView id = rowView.findViewById(R.id.playerId);
            // Default color configuration tag attached to button (e.g., Gray)
            final int[] selectedColor = {Color.GRAY};

            // Color picker dialog trigger
            btnPickColor.setOnClickListener(v -> {
                final int[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA};
                final String[] colorNames = {"Red", "Green", "Blue", "Yellow", "Magenta"};




                new AlertDialog.Builder(dicepanel.this)
                        .setTitle("Pick a Color")
                        .setItems(colorNames, (dialog, which) -> {
                            selectedColor[0] = colors[which];
                            btnPickColor.setBackgroundColor(colors[which]);
                            // Store selected color temporarily inside the view's Tag
                            btnPickColor.setTag(colors[which]);
                        })
                        .show();
            });

            // Set initial tag to prevent null values
            btnPickColor.setTag(selectedColor[0]);


            id.setText("Player " + (i + 1));
            // Add the constructed row view to layout container and global array
            playerSelection.addView(rowView);
            rowViewList.add(rowView);






        }
    }









}





