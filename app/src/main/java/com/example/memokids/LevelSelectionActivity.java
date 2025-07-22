package com.example.memokids;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.res.ColorStateList;
import android.widget.ImageView;

public class LevelSelectionActivity extends AppCompatActivity {

    private Button level1Button;
    private Button level2Button;
    private Button level3Button;
    private ImageView level1Checkmark;
    private ImageView level2Checkmark;
    private ImageView level3Checkmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_selection1);

        level1Button = findViewById(R.id.buttonLevel1);
        level2Button = findViewById(R.id.buttonLevel2);
        level3Button = findViewById(R.id.buttonLevel3);

        level1Checkmark = findViewById(R.id.checkLevel1);
        level2Checkmark = findViewById(R.id.checkLevel2);
        level3Checkmark = findViewById(R.id.checkLevel3);

        // Set up click listeners
        level1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLevelCompleted(1)) {
                    Toast.makeText(LevelSelectionActivity.this, "Level 1 already completed! Try other levels.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(LevelSelectionActivity.this, MainActivity.class);
                    intent.putExtra("level", 1);
                    startActivity(intent);
                }
            }
        });

        if (level2Button != null) {
            level2Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isLevelCompleted(1)) {
                        if (isLevelCompleted(2)) {
                            Toast.makeText(LevelSelectionActivity.this, "Level 2 already completed! Try other levels.", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(LevelSelectionActivity.this, Level2.class);
                            intent.putExtra("level", 2);
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(LevelSelectionActivity.this, "Please complete Level 1 first!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if (level3Button != null) {
            level3Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isLevelCompleted(2)) {
                        if (isLevelCompleted(3)) {
                            // All levels are completed, go to CongratulationsActivity
                            Intent intent = new Intent(LevelSelectionActivity.this, CongratulationsActivity.class);
                            startActivity(intent);
                            // Finish LevelSelectionActivity so user doesn't come back here with back button
                            // This depends on your desired flow. If you want them to be able to go back to level selection, remove finish().
                            finish();
                        } else {
                            // If Level 3 is not completed, start Level 3
                            Intent intent = new Intent(LevelSelectionActivity.this, Level3.class);
                            intent.putExtra("level", 3);
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(LevelSelectionActivity.this, "Please complete Level 2 first!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLevelButtonStates();
    }

    private void updateLevelButtonStates() {
        boolean level1Completed = isLevelCompleted(1);
        boolean level2Completed = isLevelCompleted(2);
        boolean level3Completed = isLevelCompleted(3);

        // Check if ALL levels are completed
        if (level1Completed && level2Completed && level3Completed) {
            // All levels are done, go to congratulations screen immediately
            Intent intent = new Intent(LevelSelectionActivity.this, CongratulationsActivity.class);
            startActivity(intent);
            finish(); // Finish LevelSelectionActivity
            return; // Stop further updates of buttons
        }


        // --- Level 1 Button and Checkmark Logic ---
        level1Button.setEnabled(true);
        if (level1Completed) {
            level1Checkmark.setVisibility(View.VISIBLE);
            level1Button.setBackgroundTintList(getResources().getColorStateList(R.color.level_completed_color, getTheme()));
            level1Button.setTextColor(getResources().getColor(android.R.color.white, getTheme()));
        } else {
            level1Checkmark.setVisibility(View.GONE);
            level1Button.setBackgroundTintList(getResources().getColorStateList(R.color.level1_unlocked_color, getTheme()));
            level1Button.setTextColor(getResources().getColor(android.R.color.white, getTheme()));
        }


        // --- Level 2 Button and Checkmark Logic ---
        if (level2Button != null) {
            level2Button.setEnabled(level1Completed);
            if (level2Completed) {
                level2Checkmark.setVisibility(View.VISIBLE);
                level2Button.setBackgroundTintList(getResources().getColorStateList(R.color.level_completed_color, getTheme()));
                level2Button.setTextColor(getResources().getColor(android.R.color.white, getTheme()));
            } else if (!level1Completed) {
                level2Checkmark.setVisibility(View.GONE);
                level2Button.setBackgroundTintList(getResources().getColorStateList(R.color.level_locked_color, getTheme()));
                level2Button.setTextColor(getResources().getColor(android.R.color.darker_gray, getTheme()));
            } else {
                level2Checkmark.setVisibility(View.GONE);
                level2Button.setBackgroundTintList(getResources().getColorStateList(R.color.level2_unlocked_color, getTheme()));
                level2Button.setTextColor(getResources().getColor(android.R.color.white, getTheme()));
            }
        }

        // --- Level 3 Button and Checkmark Logic ---
        if (level3Button != null) {
            level3Button.setEnabled(level2Completed);
            if (level3Completed) {
                level3Checkmark.setVisibility(View.VISIBLE);
                level3Button.setBackgroundTintList(getResources().getColorStateList(R.color.level_completed_color, getTheme()));
                level3Button.setTextColor(getResources().getColor(android.R.color.white, getTheme()));
            } else if (!level2Completed) {
                level3Checkmark.setVisibility(View.GONE);
                level3Button.setBackgroundTintList(getResources().getColorStateList(R.color.level_locked_color, getTheme()));
                level3Button.setTextColor(getResources().getColor(android.R.color.darker_gray, getTheme()));
            } else {
                level3Checkmark.setVisibility(View.GONE);
                level3Button.setBackgroundTintList(getResources().getColorStateList(R.color.level3_unlocked_color, getTheme()));
                level3Button.setTextColor(getResources().getColor(android.R.color.white, getTheme()));
            }
        }
    }

    private boolean isLevelCompleted(int level) {
        SharedPreferences sharedPref = getSharedPreferences("GameProgress", Context.MODE_PRIVATE);
        return sharedPref.getBoolean("level_" + level + "_completed", false);
    }
}