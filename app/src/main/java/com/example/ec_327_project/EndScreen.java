package com.example.ec_327_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class EndScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.endscreen);

        // Get the reference to the "Back to Start" button
        Button backToStartButton = findViewById(R.id.backToStartButton);

        // Set a click listener for the button
        backToStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToStartScreen();
            }
        });
    }

    // Method to navigate back to the StartScreen
    private void goToStartScreen() {
        Intent intent = new Intent(this, StartScreen.class);
        startActivity(intent);
        finish(); // Close the current activity
    }
}
