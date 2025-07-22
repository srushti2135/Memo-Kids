package com.example.memokids;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private int numberOfCards; // Total number of cards in the grid
    private int[] cardDrawables; // Array of actual image drawables (e.g., R.drawable.cartoon)
    private int[] cardPositions; // Array representing the shuffled positions of image drawables

    // Corrected Constructor to match the call in Level3.java
    public ImageAdapter(Context context, int numberOfCards, int[] cardDrawables, int[] cardPositions) {
        this.context = context;
        this.numberOfCards = numberOfCards;
        this.cardDrawables = cardDrawables;
        this.cardPositions = cardPositions;
    }

    @Override
    public int getCount() {
        return numberOfCards; // Return the total number of cards in the grid
    }

    @Override
    public Object getItem(int position) {
        return null; // Not typically used for simple image adapters
    }

    @Override
    public long getItemId(int position) {
        return position; // Return the position as the ID
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(this.context);
            // Set layout parameters for the ImageView within the GridView
            // You might need to adjust these dimensions based on your desired card size
            imageView.setLayoutParams(new GridView.LayoutParams(250, 250)); // Example size: 250x250 pixels
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP); // Adjust scale type as needed
            imageView.setPadding(8, 8, 8, 8); // Add some padding around the image
        } else {
            imageView = (ImageView) convertView;
        }

        // Initially, show the backside of the card (images3.png)
        // The game logic in Level3.java will change this to the actual image on click.
        imageView.setImageResource(R.drawable.images3);

        return imageView;
    }
}