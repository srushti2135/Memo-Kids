package com.example.memokids; // Make sure this matches your project's package name

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper; // Import Looper for Handler constructor
import androidx.appcompat.app.AppCompatActivity;

public class splash_activity extends AppCompatActivity {

    // Duration for which the splash screen will be displayed (in milliseconds)
    private static final int SPLASH_DISPLAY_LENGTH = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to your splash screen layout
        // IMPORTANT: Ensure you have a file named 'activity_splash.xml' in your res/layout/ folder
        setContentView(R.layout.activity_splash);

        // Use a Handler to delay the transition to the next activity
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the LevelSelectionActivity. */
                Intent mainIntent = new Intent(splash_activity.this, LevelSelectionActivity.class);
                splash_activity.this.startActivity(mainIntent);

                /* Close this splash activity so the user cannot go back to it
                   by pressing the back button. */
                splash_activity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}