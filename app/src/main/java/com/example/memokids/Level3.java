package com.example.memokids;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
// import android.media.MediaPlayer; // REMOVED: No longer needed if no audio is played
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
import java.util.Collections; // Import for Collections.shuffle
import java.util.ArrayList; // Import for ArrayList

public class Level3 extends AppCompatActivity {
    private static final String TAG = "Level3Debug"; // TAG for debugging

    ImageView curView = null;
    private int countPair = 0;
    private final int TOTAL_PAIRS = 8; // Adjust for Level 3 difficulty (e.g., 8 pairs for 16 cards)

    private TextView timerTextView;
    private TextView movesTextView;
    private CountDownTimer gameTimer;
    private long timeLeftInMillis = 30000; // 20 seconds for Level 3
    private int movesCount = 0;
    private final int MAX_MOVES = 40; // 18 moves for Level 3

    // IMPORTANT: For Level 3, consider using a different set of images or more images
    // if you increase TOTAL_PAIRS.
    // Ensure you have 8 unique images for 8 pairs.
    final int[] drawable = new int[]{
            R.drawable.cartoon1, R.drawable.cartoon2,
            R.drawable.cartoon3, R.drawable.cartoon5,
            R.drawable.cartoon6, R.drawable.cartoon7, R.drawable.cartoon8,  R.drawable.cartoon10
            // If you increase TOTAL_PAIRS, ADD NEW R.drawable resources here!
            // Example for 10 pairs: , R.drawable.new_animal1, R.drawable.new_animal2
    };

    // This 'pos' array MUST be shuffled for a proper game.
    // Ensure its size is TOTAL_PAIRS * 2 (e.g., 16 elements for 8 pairs).
    int[] pos = new int[TOTAL_PAIRS * 2]; // Initialize array with correct size

    int currentPos = -1;
    private boolean isGameActive = true;

    private LottieAnimationView congratulationsAnimationView;
    // private MediaPlayer congratulationsMediaPlayer; // REMOVED: No longer needed
    // private MediaPlayer gameOverMediaPlayer; // REMOVED: No longer needed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level3); // Make sure activity_level3.xml exists

        // Initialize 'pos' array with pairs and then shuffle
        initializeAndShufflePosArray();

        GridView gridView = findViewById(R.id.gridView);
        // Ensure ImageAdapter takes necessary parameters (Context, card count, drawables, positions)
        ImageAdapter imageAdapter = new ImageAdapter(this, TOTAL_PAIRS * 2, drawable, pos);
        gridView.setAdapter(imageAdapter);

        timerTextView = findViewById(R.id.timerTextView);
        movesTextView = findViewById(R.id.movesTextView);
        congratulationsAnimationView = findViewById(R.id.congratulationsAnimationView);
        // Make sure congratulationsAnimationView is initially GONE if you only want to show it on completion
        congratulationsAnimationView.setVisibility(View.GONE);


        updateTimerText();
        updateMovesText();
        startGameTimer();

        Log.d(TAG, "Game initialized. isGameActive: " + isGameActive);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick fired for position: " + position + ", isGameActive: " + isGameActive);

                if (!isGameActive) {
                    Log.d(TAG, "Game is not active, ignoring click.");
                    return;
                }

                // Prevent clicking already matched/disabled cards, or during pair-check delay
                if (!view.isEnabled()) {
                    Log.d(TAG, "Card is disabled, ignoring click.");
                    return;
                }

                // If same card clicked twice in a row, flip back
                if (currentPos != -1 && currentPos == position) {
                    ((ImageView) view).setImageResource(R.drawable.images3); // Flip back to card back
                    Toast.makeText(getApplicationContext(), "Same card clicked!", Toast.LENGTH_SHORT).show();
                    currentPos = -1; // Reset selection
                    curView = null;
                    return; // Don't count as a move if same card clicked
                }


                movesCount++;
                updateMovesText();
                Log.d(TAG, "Moves count: " + movesCount);

                if (movesCount >= MAX_MOVES) {
                    isGameActive = false;
                    if (gameTimer != null) gameTimer.cancel(); // Ensure timer is cancelled
                    Log.d(TAG, "Game over via moves limit!");
                    showGameOverDialog("Moves Limit Reached!", "You ran out of moves!");
                    return; // Stop processing further
                }


                if (currentPos < 0) { // First card of a pair selected
                    currentPos = position;
                    curView = (ImageView) view;
                    ((ImageView) view).setImageResource(drawable[pos[position]]);
                } else { // Second card of a pair selected
                    final ImageView firstCard = curView; // Store first card reference
                    final ImageView secondCard = (ImageView) view; // Store second card reference
                    secondCard.setImageResource(drawable[pos[position]]); // Show second card's image

                    if (pos[currentPos] != pos[position]) { // No match
                        // Use a Handler to delay flipping cards back
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (isGameActive) { // Only flip back if game is still active
                                    firstCard.setImageResource(R.drawable.images3);
                                    secondCard.setImageResource(R.drawable.images3);
                                    Toast.makeText(getApplicationContext(), "OPPS!!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, 700); // 0.7 second delay
                    } else { // Match found
                        view.setEnabled(false); // Disable matched cards (current card clicked)
                        curView.setEnabled(false); // Disable matched cards (first card clicked)

                        countPair++;
                        Log.d(TAG, "Pair matched! Total pairs: " + countPair);

                        if (countPair == TOTAL_PAIRS) {
                            isGameActive = false;
                            if (gameTimer != null) gameTimer.cancel(); // Cancel timer
                            Toast.makeText(getApplicationContext(), "GOOD !!!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Level completed!");
                            saveLevelCompletionStatus(3, true); // IMPORTANT: Save Level 3 completion status
                            playCongratulationsEffect(); // Play Lottie animation, but no sound now
                        }
                    }
                    currentPos = -1; // Reset selection for next pair
                    curView = null; // Clear current view reference
                }
            }
        });
    }

    // Helper method to initialize 'pos' array with pairs and then shuffle
    private void initializeAndShufflePosArray() {
        ArrayList<Integer> temp = new ArrayList<>();
        for (int i = 0; i < TOTAL_PAIRS; i++) {
            temp.add(i);
            temp.add(i);
        }
        Collections.shuffle(temp); // Shuffle the ArrayList

        for (int i = 0; i < pos.length; i++) {
            pos[i] = temp.get(i); // Copy back to int array
        }
    }


    private void playCongratulationsEffect() {
        congratulationsAnimationView.setVisibility(View.VISIBLE);
        congratulationsAnimationView.playAnimation();

        // REMOVED ALL MEDIAPLAYER RELATED CODE FOR CONGRATULATIONS
        /*
        try {
            if (congratulationsMediaPlayer != null) {
                congratulationsMediaPlayer.release();
            }
            congratulationsMediaPlayer = MediaPlayer.create(this, R.raw.final_celebration_animation); // Assuming this was your audio
            if (congratulationsMediaPlayer != null) {
                congratulationsMediaPlayer.start();
                congratulationsMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                        congratulationsMediaPlayer = null;
                    }
                });
            }
        } catch (Exception e) {
            Log.e(TAG, "Error playing audio for congratulations: " + e.getMessage());
        }
        */

        congratulationsAnimationView.addAnimatorListener(new android.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(android.animation.Animator animation) {}
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                congratulationsAnimationView.setVisibility(View.GONE);
                showCongratulationsDialog(); // Call dialog AFTER animation completes
            }
            @Override
            public void onAnimationCancel(android.animation.Animator animation) {}
            @Override
            public void onAnimationRepeat(android.animation.Animator animation) {}
        });
    }


    @Override
    public void onBackPressed() {
        // If game is over or completed, allow normal back press (to LevelSelection)
        if (!isGameActive || countPair == TOTAL_PAIRS) {
            super.onBackPressed();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Quit Level?")
                    .setMessage("You haven't completed Level 3 yet. Are you sure you want to go back? Your progress will be lost!")
                    .setPositiveButton("Yes, Quit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (gameTimer != null) {
                                gameTimer.cancel();
                            }
                            // REMOVED MEDIAPLAYER RELEASE CODE FOR CONGRATULATIONS
                            /*
                            if (congratulationsMediaPlayer != null) {
                                congratulationsMediaPlayer.release();
                                congratulationsMediaPlayer = null;
                            }
                            if (gameOverMediaPlayer != null) {
                                gameOverMediaPlayer.release();
                                gameOverMediaPlayer = null;
                            }
                            */
                            Intent intent = new Intent(Level3.this, LevelSelectionActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("No, Stay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    }


    private void startGameTimer() {
        gameTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
                Log.d(TAG, "Timer Tick! Time left: " + timeLeftInMillis / 1000 + " seconds");
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateTimerText();
                isGameActive = false; // Set game to inactive
                Log.d(TAG, "Game over via time's up!");
                showGameOverDialog("Time's Up!", "The time limit has ended!");
            }
        }.start();
    }

    private void updateTimerText() {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeLeftInMillis);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeLeftInMillis) -
                TimeUnit.MINUTES.toSeconds(minutes);
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        timerTextView.setText("Time: " + timeFormatted);
    }

    private void updateMovesText() {
        movesTextView.setText("Moves: " + movesCount + "/" + MAX_MOVES);
    }

    private void showCongratulationsDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Congratulations!")
                .setMessage("You have completed Level 3!")
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Navigate to CongratulationsActivity after Level 3 completion
                        Intent intent = new Intent(Level3.this, CongratulationsActivity.class);
                        startActivity(intent);
                        finish(); // Finish Level3 activity
                    }
                })
                .setCancelable(false) // Keeps the dialog on screen until "Continue" is pressed
                .show();
    }

    private void showGameOverDialog(String title, String message) {
        // REMOVED ALL MEDIAPLAYER RELATED CODE FOR GAME OVER
        /*
        try {
            if (gameOverMediaPlayer != null) {
                gameOverMediaPlayer.release();
            }
            gameOverMediaPlayer = MediaPlayer.create(this, R.raw.final_celebration_animation); // Assuming this was your audio
            if (gameOverMediaPlayer != null) {
                gameOverMediaPlayer.setLooping(false);
                gameOverMediaPlayer.start();
                gameOverMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                        gameOverMediaPlayer = null;
                    }
                });
            }
        } catch (Exception e) {
            Log.e(TAG, "Error playing game over audio: " + e.getMessage());
        }
        */

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // REMOVED MEDIAPLAYER RELEASE CODE FOR GAME OVER
                        /*
                        if (gameOverMediaPlayer != null) {
                            gameOverMediaPlayer.release();
                            gameOverMediaPlayer = null;
                        }
                        */
                        Intent intent = new Intent(Level3.this, Level3.class); // Restart Level 3
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Back to Levels", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // REMOVED MEDIAPLAYER RELEASE CODE FOR GAME OVER
                        /*
                        if (gameOverMediaPlayer != null) {
                            gameOverMediaPlayer.release();
                            gameOverMediaPlayer = null;
                        }
                        */
                        Intent intent = new Intent(Level3.this, LevelSelectionActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void saveLevelCompletionStatus(int level, boolean completed) {
        SharedPreferences sharedPref = getSharedPreferences("GameProgress", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("level_" + level + "_completed", completed);
        editor.apply();
        Log.d(TAG, "Level " + level + " completion status saved: " + completed);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gameTimer != null) {
            gameTimer.cancel();
            Log.d(TAG, "Game timer cancelled in onDestroy.");
        }
        // REMOVED MEDIAPLAYER RELEASE CODE IN ONSTROY
        /*
        if (congratulationsMediaPlayer != null) {
            congratulationsMediaPlayer.release();
            congratulationsMediaPlayer = null;
            Log.d(TAG, "Congratulations MediaPlayer released in onDestroy.");
        }
        if (gameOverMediaPlayer != null) {
            gameOverMediaPlayer.release();
            gameOverMediaPlayer = null;
            Log.d(TAG, "Game Over MediaPlayer released in onDestroy.");
        }
        */
    }
}