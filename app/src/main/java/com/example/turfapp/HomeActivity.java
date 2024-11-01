package com.example.turfapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TurfAdapter turfAdapter;
    private List<Turf> turfList;
    private FloatingActionButton addTurfButton;
    private ImageButton viewProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize buttons
        addTurfButton = findViewById(R.id.addTurfBtn);
        viewProfileButton = findViewById(R.id.viewProfileButton);

        // Set onClick listeners for buttons
        addTurfButton.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, AddTurfActivity.class);
            startActivity(intent);
        });

        viewProfileButton.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });

        // Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.turfRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        turfList = new ArrayList<>();
        turfAdapter = new TurfAdapter(this, turfList);
        recyclerView.setAdapter(turfAdapter);

        // Fetch data from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("turfs")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Turf turf = document.toObject(Turf.class);
                        if (turf != null) { // Null check for safety
                            turf.setId(document.getId()); // Set the document ID as turf ID
                            turfList.add(turf);
                        }
                    }
                    turfAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    // Log the error for debugging
                    Log.e("HomeActivity", "Firestore Error: ", e);
                });

        // Set listener for adapter item clicks
        turfAdapter.setOnItemClickListener(new TurfAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Handle general item click if needed
                Toast.makeText(HomeActivity.this, "Item clicked at position: " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBookClick(int position) {
                Turf selectedTurf = turfList.get(position);
                if (selectedTurf.getId() != null) {
                    Intent intent = new Intent(HomeActivity.this, TurfBookingActivity.class);
                    intent.putExtra("turfId", selectedTurf.getId()); // Pass the turfId to TurfBookingActivity
                    startActivity(intent);
                } else {
                    Toast.makeText(HomeActivity.this, "Invalid turf ID", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onMoreClick(int position) {
                Turf selectedTurf = turfList.get(position);
                if (selectedTurf.getId() != null) {
                    Intent intent = new Intent(HomeActivity.this, TurfDetailsActivity.class);
                    intent.putExtra("turfId", selectedTurf.getId()); // Pass the turfId to TurfDetailsActivity
                    startActivity(intent);
                } else {
                    Toast.makeText(HomeActivity.this, "Invalid turf ID", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
