package com.example.memokids;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class CongratulationsActivity extends AppCompatActivity {

    private LottieAnimationView celebrationAnimation;
    private MediaPlayer mediaPlayer; // For the celebration sound
    private Button restartGameButton;
    private Button exitAppButton;

    // Removed: private Handler handler; // No longer needed for auto-close
    // Removed: private Runnable runnable; // No longer needed for auto-close
    // Removed: private final long AUTO_CLOSE_DELAY_MS = 5000; // No longer needed for auto-close

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulations);

        celebrationAnimation = findViewById(R.id.celebrationAnimation);
        restartGameButton = findViewById(R.id.restartGameButton);
        exitAppButton = findViewById(R.id.exitAppButton);

        // Play Lottie animation
        celebrationAnimation.playAnimation();
        celebrationAnimation.loop(true); // Loop the animation until the activity is closed by user action

        // Play celebratory music/sound
        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.level_complete_confetti);
            if (mediaPlayer != null) {
                mediaPlayer.setLooping(false); // Play once
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // Optional: You can stop animation here if you want it to stop after sound
                        // celebrationAnimation.cancelAnimation();
                        // celebrationAnimation.setVisibility(View.GONE);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Removed: handler and runnable initialization and postDelayed call


        // Set up button listeners - these will now be the ONLY way to exit this activity
        restartGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Removed: handler.removeCallbacks(runnable);
                clearGameProgress();
                Intent intent = new Intent(CongratulationsActivity.this, LevelSelectionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear back stack
                startActivity(intent);
                finishAndReleaseResources(); // Finish this activity and release resources
            }
        });

        exitAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Removed: handler.removeCallbacks(runnable);
                finishAffinity(); // Close all activities and exit app
                releaseResources(); // Release resources just in case
            }
        });
    }

    // Removed: navigateToLevelSelection() method as it was part of the auto-close logic

    private void clearGameProgress() {
        getSharedPreferences("GameProgress", Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply();
        Toast.makeText(this, "Game progress reset!", Toast.LENGTH_SHORT).show();
    }

    private void finishAndReleaseResources() {
        releaseResources(); // Release MediaPlayer and Lottie resources
        finish(); // Finish the activity
    }

    private void releaseResources() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (celebrationAnimation != null) {
            celebrationAnimation.cancelAnimation();
            celebrationAnimation.removeAllAnimatorListeners();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Removed: handler.removeCallbacks(runnable);
        releaseResources(); // Still essential to release resources when activity is destroyed
    }

    @Override
    public void onBackPressed() {
        // This behavior is now the default when no auto-close is present.
        // It will either:
        // 1. Just show a toast (as currently implemented for this case)
        // 2. Or, if you remove the toast and call super.onBackPressed(), it might go back to LevelSelectionActivity.
        //    However, usually on a "Congratulations" screen, you want explicit user choice.
        Toast.makeText(this, "Please choose an option!", Toast.LENGTH_SHORT).show();
    }
}