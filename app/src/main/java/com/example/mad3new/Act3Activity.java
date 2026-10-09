package com.example.mad3new;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Act3Activity extends AppCompatActivity {

    private EditText weightInput;
    private EditText heightInput;

    private Button calculateButton;

    private TextView bmiResult;
    private TextView bmiCategory;

    private LinearLayout bmiHistoryLayout;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_act3);

        weightInput = findViewById(R.id.weightInput);
        heightInput = findViewById(R.id.heightInput);

        calculateButton = findViewById(R.id.calculateButton);

        bmiResult = findViewById(R.id.bmiResult);
        bmiCategory = findViewById(R.id.bmiCategory);



        databaseHelper = new DatabaseHelper(this);


        calculateButton.setOnClickListener(v -> calculateBMI());
    }

    private void calculateBMI() {
        calculateButton.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(80)
                .withEndAction(() -> {

                    calculateButton.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(80)
                            .start();

                })
                .start();

        String weightText = weightInput.getText().toString().trim();
        String heightText = heightInput.getText().toString().trim();

        if (weightText.isEmpty() || heightText.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please enter your weight and height.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        double weight = Double.parseDouble(weightText);
        double heightCm = Double.parseDouble(heightText);

        if (weight <= 0 || heightCm <= 0) {

            Toast.makeText(
                    this,
                    "Please enter valid values.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Convert height from centimeters to meters
        double heightMeters = heightCm / 100;

        // BMI formula
        double bmi = weight / (heightMeters * heightMeters);

        String category;

        if (bmi < 18.5) {

            category = "Underweight";

        } else if (bmi < 25) {

            category = "Normal weight";

        } else if (bmi < 30) {

            category = "Overweight";

        } else {

            category = "Obese";
        }

        bmiResult.setText(
                String.format(
                        Locale.getDefault(),
                        "BMI: %.2f",
                        bmi
                )
        );

        bmiCategory.setText(
                "Category: " + category
        );

        bmiResult.setScaleX(0.7f);
        bmiResult.setScaleY(0.7f);
        bmiResult.setAlpha(0f);

        bmiResult.animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(300)
                .start();

        bmiCategory.setAlpha(0f);

        bmiCategory.animate()
                .alpha(1f)
                .setDuration(400)
                .start();



    }
}