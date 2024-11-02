package com.example.turfapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TurfBookingActivity extends AppCompatActivity {

    private ImageView turfImageView;
    private TextView turfTitleTextView, turfDescriptionTextView, turfPriceTextView;
    private Spinner timeSlotSpinner;
    private Button confirmBookingButton;
    private String turfId;
    private String selectedSlot;
    private FirebaseFirestore db;
    private List<String> availableSlots = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turf_booking);

        db = FirebaseFirestore.getInstance();
        turfId = getIntent().getStringExtra("turfId");
        if (turfId == null || turfId.isEmpty()) {
            Toast.makeText(this, "Invalid Turf ID.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize UI components
        turfImageView = findViewById(R.id.taskImageView);
        turfTitleTextView = findViewById(R.id.taskTitleTextView);
        turfDescriptionTextView = findViewById(R.id.taskDescriptionTextView);
        turfPriceTextView = findViewById(R.id.taskPriceTextView);
        timeSlotSpinner = findViewById(R.id.timeSlotSpinner);
        confirmBookingButton = findViewById(R.id.confirmBookingButton);

        loadTurfDetails();

        confirmBookingButton.setOnClickListener(v -> {
            selectedSlot = (String) timeSlotSpinner.getSelectedItem();
            if (selectedSlot != null) {
                checkAndBookSlot();
            } else {
                Toast.makeText(this, "Please select a valid time slot.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTurfDetails() {
        db.collection("turfs").document(turfId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot document = task.getResult();
                        String imageUrl = document.getString("imageUrl");
                        if (imageUrl != null) {
                            Glide.with(this).load(imageUrl).into(turfImageView);
                        }
                        turfTitleTextView.setText(document.getString("name"));
                        turfDescriptionTextView.setText(document.getString("location"));
                        turfPriceTextView.setText("₹" + document.getLong("price"));
                        loadAvailableSlots(document);
                    } else {
                        Toast.makeText(this, "Error loading turf details.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadAvailableSlots(DocumentSnapshot document) {
        List<Map<String, Object>> timeSlots = (List<Map<String, Object>>) document.get("timeslots");
        if (timeSlots != null) {
            for (Map<String, Object> slot : timeSlots) {
                Boolean isBooked = (Boolean) slot.get("isBooked");
                String time = (String) slot.get("time");
                if (time != null && (isBooked == null || !isBooked)) {
                    availableSlots.add(time);
                }
            }
            if (availableSlots.isEmpty()) {
                Toast.makeText(this, "No available slots.", Toast.LENGTH_SHORT).show();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, availableSlots);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            timeSlotSpinner.setAdapter(adapter);
        }
    }

    private void checkAndBookSlot() {
        db.collection("turfs").document(turfId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot document = task.getResult();
                        List<Map<String, Object>> timeSlots = (List<Map<String, Object>>) document.get("timeslots");
                        for (Map<String, Object> slot : timeSlots) {
                            String time = (String) slot.get("time");
                            Boolean isBooked = (Boolean) slot.get("isBooked");
                            if (time.equals(selectedSlot) && Boolean.TRUE.equals(isBooked)) {
                                Toast.makeText(this, "This slot is already booked.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        bookTurf();
                    } else {
                        Toast.makeText(this, "Error checking slot.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void bookTurf() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String bookingId = turfId + "_" + selectedSlot;
        Map<String, Object> bookingData = new HashMap<>();
        bookingData.put("turf_id", turfId);
        bookingData.put("user_id", userId);
        bookingData.put("time_slot", selectedSlot);
        bookingData.put("status", "Confirmed");

        db.collection("bookings").document(bookingId).set(bookingData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                updateTurfSlots();
                Toast.makeText(this, "Booking confirmed!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to book slot", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTurfSlots() {
        db.collection("turfs").document(turfId).update("timeslots", FieldValue.arrayUnion(new HashMap<String, Object>() {{
            put("time", selectedSlot);
            put("isBooked", true);
        }})).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Toast.makeText(this, "Failed to update slots", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
