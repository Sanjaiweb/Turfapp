package com.example.turfapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddTurfActivity extends AppCompatActivity {

    private EditText turfNameEditText, locationEditText, priceEditText;
    private Spinner availabilitySpinner;
    private Button addTurfButton, selectImageButton;
    private ImageView turfImageView;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    // Define the predefined timeslots with booking status
    private static final List<Map<String, Object>> PREDEFINED_TIMESLOTS = new ArrayList<>();

    static {
        // Initialize timeslots with time and booking status
        addTimeslot("9:00 AM - 10:00 AM");
        addTimeslot("4:00 PM - 5:00 PM");
        addTimeslot("5:00 PM - 6:00 PM");
        addTimeslot("6:00 PM - 7:00 PM");
        addTimeslot("7:00 PM - 8:00 PM");
    }

    private static void addTimeslot(String time) {
        Map<String, Object> timeslot = new HashMap<>();
        timeslot.put("time", time);
        timeslot.put("isBooked", false); // All slots start as unbooked
        PREDEFINED_TIMESLOTS.add(timeslot);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_turf);

        // Initialize Firestore and Firebase Storage
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        // Find views by ID
        turfNameEditText = findViewById(R.id.turfNameEditText);
        locationEditText = findViewById(R.id.locationEditText);
        priceEditText = findViewById(R.id.priceEditText);
        availabilitySpinner = findViewById(R.id.availabilitySpinner);
        addTurfButton = findViewById(R.id.addTurfButton);
        selectImageButton = findViewById(R.id.selectImageButton);
        turfImageView = findViewById(R.id.turfImageView);

        // Set up the availability spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.availability_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        availabilitySpinner.setAdapter(adapter);

        // Set click listeners
        selectImageButton.setOnClickListener(v -> openFileChooser());
        addTurfButton.setOnClickListener(v -> addTurf());
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            turfImageView.setImageURI(imageUri); // Display the selected image
        }
    }

    private void addTurf() {
        String turfName = turfNameEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();
        String priceString = priceEditText.getText().toString().trim();
        String availability = availabilitySpinner.getSelectedItem().toString();

        // Validate inputs
        if (turfName.isEmpty() || location.isEmpty() || priceString.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceString);
        String imagePath = "turfs/" + System.currentTimeMillis() + ".jpg"; // Create a unique path for the image

        // Upload the image to Firebase Storage
        StorageReference imageRef = storageRef.child(imagePath);
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Create a map for the turf data
                    Map<String, Object> turfData = new HashMap<>();
                    turfData.put("name", turfName);
                    turfData.put("location", location);
                    turfData.put("price", price);
                    turfData.put("availability", availability.equals("Available"));
                    turfData.put("imageUrl", uri.toString()); // Save the image URL
                    turfData.put("timeslots", PREDEFINED_TIMESLOTS); // Add the predefined timeslots with booking status

                    // Add turf to Firestore
                    db.collection("turfs")
                            .add(turfData)
                            .addOnSuccessListener(documentReference -> {
                                Toast.makeText(AddTurfActivity.this, "Turf added successfully!", Toast.LENGTH_SHORT).show();
                                clearFields(); // Clear input fields after successful addition
                            })
                            .addOnFailureListener(e -> Toast.makeText(AddTurfActivity.this, "Error adding turf", Toast.LENGTH_SHORT).show());
                }))
                .addOnFailureListener(e -> Toast.makeText(AddTurfActivity.this, "Error uploading image", Toast.LENGTH_SHORT).show());
    }

    private void clearFields() {
        turfNameEditText.setText("");
        locationEditText.setText("");
        priceEditText.setText("");
        availabilitySpinner.setSelection(0); // Reset to the first item
        turfImageView.setImageURI(null); // Clear the ImageView
        imageUri = null; // Reset the image URI
    }
}

