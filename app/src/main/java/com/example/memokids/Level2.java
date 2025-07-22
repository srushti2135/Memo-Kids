package com.example.memokids; // Make sure this matches your project's package name

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer; // Import for audio
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView; // Import for Lottie

import java.util.concurrent.TimeUnit;

public class Level2 extends AppCompatActivity {
    private static final String TAG = "MainActivityDebug"; // TAG for debugging specific to Level 1

    ImageView curView = null; // Stores the currently flipped first card's ImageView
    private int countPair = 0; // Counts successfully matched pairs
    private final int TOTAL_PAIRS = 8; // Total number of pairs to match for Level 1 completion

    private TextView timerTextView;
    private TextView movesTextView;
    private CountDownTimer gameTimer;
    private long timeLeftInMillis = 60000; // 1 minute = 60000 milliseconds for Level 1 (adjust as needed)
    private int movesCount = 0;
    private final int MAX_MOVES = 30; // Maximum allowed moves for Level 1 (adjust as needed)

    // IMPORTANT: Make sure these drawables exist in your res/drawable folder.
    final int[] drawable = new int[]{
            R.drawable.dog1, R.drawable.dog2,
            R.drawable.dog8, R.drawable.dog7, R.drawable.dog6,
            R.drawable.dog5, R.drawable.dog4, R.drawable.dog3    };

    // Card positions for the 4x4 grid (16 cards).
    // Ensure this array correctly shuffles pairs of your `drawable` indices.
    int[] pos = { 1, 3, 0, 2, 4, 5, 7, 6, 2, 0, 1, 4, 5, 3, 6, 7};
    int currentPos = -1; // Stores the position of the first flipped card, -1 if no card is flipped
    private boolean isGameActive = true; // Flag to control game state (prevent clicks when game over/paused)

    private LottieAnimationView congratulationsAnimationView; // For success animation
    private MediaPlayer mediaPlayer; // For success sound effect

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Set the layout for Level 1

        GridView gridView = findViewById(R.id.gridView);
        // Make sure you define these variables in your MainActivity, similar to Level3
// For example:
// private final int TOTAL_PAIRS = 6; // Or whatever is appropriate for Level 1
// final int[] drawable = new int[]{R.drawable.img1, R.drawable.img2, ...}; // Your Level 1 images
// int[] pos = new int[TOTAL_PAIRS * 2]; // Needs to be initialized and shuffled

// Assuming these are defined and 'pos' is shuffled:
        ImageAdapter imageAdapter = new ImageAdapter(this, TOTAL_PAIRS * 2, drawable, pos); // Your custom adapter for the grid
        gridView.setAdapter(imageAdapter);

        timerTextView = findViewById(R.id.timerTextView);
        movesTextView = findViewById(R.id.movesTextView);
        congratulationsAnimationView = findViewById(R.id.congratulationsAnimationView);

        // Initialize and start game elements
        updateTimerText();
        updateMovesText();
        startGameTimer();

        Log.d(TAG, "Level 1 game initialized. isGameActive: " + isGameActive);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick fired for position: " + position + ", isGameActive: " + isGameActive);

                // Prevent interaction if game is not active (e.g., game over, animation playing)
                if (!isGameActive) {
                    Log.d(TAG, "Game is not active, ignoring click.");
                    return;
                }

                // If a first card is already flipped
                if (currentPos != -1) {
                    movesCount++; // Increment moves only after a second card is picked
                    updateMovesText();
                    Log.d(TAG, "Moves count: " + movesCount);

                    // Check for moves limit
                    if (movesCount >= MAX_MOVES) {
                        isGameActive = false; // Game over
                        gameTimer.cancel(); // Stop timer
                        Log.d(TAG, "Game over via moves limit!");
                        showGameOverDialog("Moves Limit Reached!", "You ran out of moves!");
                        return; // Exit click handler
                    }
                }

                // Logic for handling card clicks
                if (currentPos < 0) { // No card is currently flipped (this is the first card of a pair)
                    currentPos = position;
                    curView = (ImageView) view;
                    ((ImageView) view).setImageResource(drawable[pos[position]]); // Show the image
                } else { // A card is already flipped (this is the second card of a pair)
                    if (currentPos == position) { // Same card clicked again
                        ((ImageView) view).setImageResource(R.drawable.images3); // Flip back to default
                        Toast.makeText(getApplicationContext(), "Same card clicked!", Toast.LENGTH_SHORT).show();
                    } else if (pos[currentPos] != pos[position]) { // Mismatch
                        final ImageView firstCard = curView;
                        final ImageView secondCard = (ImageView) view;
                        secondCard.setImageResource(drawable[pos[position]]); // Temporarily show second card's image

                        // Delay to let the user see the second card before flipping both back
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                firstCard.setImageResource(R.drawable.images3); // Flip first back
                                secondCard.setImageResource(R.drawable.images3); // Flip second back
                                Toast.makeText(getApplicationContext(), "OPPS!!!", Toast.LENGTH_SHORT).show();
                            }
                        }, 700); // 0.7 second delay

                    } else { // Match!
                        ((ImageView) view).setImageResource(drawable[pos[position]]); // Keep second card's image shown
                        view.setEnabled(false); // Disable matched cards
                        curView.setEnabled(false); // Disable matched cards

                        countPair++; // Increment matched pairs
                        Log.d(TAG, "Pair matched! Total pairs: " + countPair);

                        // Check for Level Completion
                        if (countPair == TOTAL_PAIRS) {
                            isGameActive = false; // Game over
                            gameTimer.cancel(); // Stop timer
                            Toast.makeText(getApplicationContext(), "GOOD !!!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Level 2 completed!");
                            playCongratulationsEffect(); // Play animation and audio
                            // --- CRITICAL LINE FOR LEVEL 1 COMPLETION ---
                            // Save Level 1 completion status. Pass '1' for Level 1.
                            saveLevelCompletionStatus(2, true);
                        }
                    }
                    currentPos = -1; // Reset for next pair selection
                }
            }
        });
    }

    // Method to play the Lottie animation and associated audio
    private void playCongratulationsEffect() {
        congratulationsAnimationView.setVisibility(View.VISIBLE);
        congratulationsAnimationView.playAnimation();

        try {
            // Ensure R.raw.congratulations_animation exists and is an audio file (e.g., MP3, WAV)
            mediaPlayer = MediaPlayer.create(this, R.raw.level_complete_confetti);
            if (mediaPlayer != null) {
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // Optionally release media player once sound finishes
                        if (mediaPlayer != null) {
                            mediaPlayer.release();
                            mediaPlayer = null;
                        }
                    }
                });
            }
        } catch (Exception e) {
            Log.e(TAG, "Error playing audio: " + e.getMessage());
        }

        // Listener for Lottie animation completion
        congratulationsAnimationView.addAnimatorListener(new android.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(android.animation.Animator animation) {
                // Optionally disable input during animation
            }
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                congratulationsAnimationView.setVisibility(View.GONE); // Hide animation after it finishes
                showCongratulationsDialog(); // Then show the completion dialog
            }
            @Override
            public void onAnimationCancel(android.animation.Animator animation) {
                congratulationsAnimationView.setVisibility(View.GONE);
                showCongratulationsDialog(); // Show dialog even if animation is cancelled
            }
            @Override
            public void onAnimationRepeat(android.animation.Animator animation) {}
        });
    }

    // Handle back button press
    @Override
    public void onBackPressed() {
        // If game is not active (already won/lost) or completed, allow normal back press
        if (!isGameActive || countPair == TOTAL_PAIRS) {
            super.onBackPressed();
        } else {
            // Show confirmation dialog if game is still ongoing
            new AlertDialog.Builder(this)
                    .setTitle("Quit Level?")
                    .setMessage("You haven't completed the level yet. Are you sure you want to go back? Your progress will be lost!")
                    .setPositiveButton("Yes, Quit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Stop timer and release media player
                            if (gameTimer != null) {
                                gameTimer.cancel();
                            }
                            if (mediaPlayer != null) {
                                mediaPlayer.release();
                                mediaPlayer = null;
                            }
                            // Navigate back to LevelSelectionActivity
                            Intent intent = new Intent(Level2.this, LevelSelectionActivity.class);
                            startActivity(intent);
                            finish(); // Finish current activity
                        }
                    })
                    .setNegativeButton("No, Stay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss(); // Dismiss dialog and stay in game
                        }
                    })
                    .setCancelable(false) // User must choose an option
                    .show();
        }
    }

    // Starts the countdown timer for the game
    private void startGameTimer() {
        gameTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText(); // Update TextView every second
                Log.d(TAG, "Timer Tick! Time left: " + timeLeftInMillis / 1000 + " seconds");
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateTimerText();
                isGameActive = false; // Game over
                Log.d(TAG, "Game over via time's up!");
                showGameOverDialog("Time's Up!", "The time limit has ended!");
            }
        }.start();
    }

    // Updates the timer TextView with formatted time
    private void updateTimerText() {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeLeftInMillis);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeLeftInMillis) -
                TimeUnit.MINUTES.toSeconds(minutes);
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        timerTextView.setText("Time: " + timeFormatted);
    }

    // Updates the moves TextView
    private void updateMovesText() {
        movesTextView.setText("Moves: " + movesCount + "/" + MAX_MOVES);
    }

    // Displays the congratulations dialog after winning
    private void showCongratulationsDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Congratulations!")
                .setMessage("You have completed Level 2!") // Message specific for Level 1
                .setPositiveButton("Next Level", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // This will typically go to LevelSelectionActivity to see Level 2 unlocked
                        Intent intent = new Intent(Level2.this, LevelSelectionActivity.class);
                        startActivity(intent);
                        finish(); // Finish Level 1
                    }
                })
                .setNegativeButton("Exit App", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity(); // Exit the entire app
                    }
                })
                .setCancelable(false) // User must make a choice
                .show();
    }

    // Displays the game over dialog (time's up or moves limit)
    private void showGameOverDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Restart Level 1
                        Intent intent = new Intent(Level2.this, Level2.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Back to Levels", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Go back to LevelSelectionActivity
                        Intent intent = new Intent(Level2.this, LevelSelectionActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }

    // Saves the completion status of a given level in SharedPreferences
    private void saveLevelCompletionStatus(int level, boolean completed) {
        SharedPreferences sharedPref = getSharedPreferences("GameProgress", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("level_" + level + "_completed", completed);
        editor.apply(); // Use apply() for asynchronous save to avoid blocking UI
        Log.d(TAG, "Level " + level + " completion status saved: " + completed);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cancel timer to prevent memory leaks and crashes if activity is destroyed
        if (gameTimer != null) {
            gameTimer.cancel();
            Log.d(TAG, "Game timer cancelled in onDestroy.");
        }
        // Release MediaPlayer resources to prevent leaks
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            Log.d(TAG, "MediaPlayer released in onDestroy.");
        }
    }
}