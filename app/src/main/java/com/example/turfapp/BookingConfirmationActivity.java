package com.example.turfapp;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class BookingConfirmationActivity extends AppCompatActivity {

    private TextView bookingDetailsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirmation);

        // Initialize views
        bookingDetailsTextView = findViewById(R.id.bookingDetailsTextView);

        // Retrieve booking object from intent
        Booking booking = (Booking) getIntent().getSerializableExtra("booking");

        // Check if booking details are available and display them
        if (booking != null) {
            displayBookingDetails(booking);
        } else {
            Toast.makeText(this, "Booking details not found.", Toast.LENGTH_SHORT).show();
            // Optional: Close the activity if no booking details were passed
            finish();
        }
    }

    // Helper method to display booking details in the TextView
    private void displayBookingDetails(Booking booking) {
        String bookingInfo = "Booking Details:\n" +
                "Turf: " + booking.getTurfId() + "\n" +
                "Date: " + booking.getDate() + "\n" +
                "Time: " + booking.getTimeSlot() + "\n" +
                "Price: ₹" + booking.getPrice();

        bookingDetailsTextView.setText(bookingInfo);
    }
}
