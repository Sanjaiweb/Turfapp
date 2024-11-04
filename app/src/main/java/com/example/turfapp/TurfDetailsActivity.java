package com.example.turfapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class TurfDetailsActivity extends AppCompatActivity {

    private TextView turfNameTextView, turfLocationTextView, turfPriceTextView, turfDescriptionTextView;
    private ImageView turfImageView;
    private Button bookButton;
    private Turf turf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turf_details);

        // Initialize views
        turfNameTextView = findViewById(R.id.turfNameTextView);
        turfLocationTextView = findViewById(R.id.turfLocationTextView);
        turfPriceTextView = findViewById(R.id.turfPriceTextView);
        turfDescriptionTextView = findViewById(R.id.turfDescriptionTextView);

        turfImageView = findViewById(R.id.turfImageView);
        bookButton = findViewById(R.id.bookButton);

        // Retrieve the Turf object from the intent
        turf = (Turf) getIntent().getSerializableExtra("turf");

        // Check if turf data exists and populate fields
        if (turf != null) {
            turfNameTextView.setText(turf.getName() != null ? turf.getName() : "Name not available");
            turfLocationTextView.setText(turf.getLocation() != null ? turf.getLocation() : "Location not available");
            turfPriceTextView.setText("₹" + turf.getPrice());
            turfDescriptionTextView.setText(turf.getDescription() != null ? turf.getDescription() : "Description not available");


            // Load the turf image with Glide and placeholder in case of missing image URL
            if (turf.getImageUrl() != null) {
                Glide.with(this)
                        .load(turf.getImageUrl())
                        .placeholder(R.drawable.turf) // Placeholder image in case of loading delay
                        .into(turfImageView);
            } else {
                turfImageView.setImageResource(R.drawable.turf); // Fallback image if URL is missing
            }
        } else {
            Toast.makeText(this, "Turf details not available", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if there's no valid turf data
            return;
        }

        // Set onClickListener for the booking button
        bookButton.setOnClickListener(v -> {
            if (turf.isAvailable()) {
                Intent intent = new Intent(TurfDetailsActivity.this, TurfBookingActivity.class);
                intent.putExtra("turf", turf);  // Pass the Turf object to the booking activity
                startActivity(intent);
            } else {
                Toast.makeText(this, "Turf is currently unavailable for booking", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
