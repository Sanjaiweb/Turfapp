package com.example.turfapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class TurfBrowsingActivity extends AppCompatActivity {

    private RecyclerView turfsRecyclerView;
    private TurfAdapter turfAdapter;
    private List<Turf> turfList = new ArrayList<>();
    private DatabaseReference turfsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turf_browsing);

        turfsRecyclerView = findViewById(R.id.turfsRecyclerView);
        turfsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter with empty list
        turfAdapter = new TurfAdapter(this, turfList);
        turfsRecyclerView.setAdapter(turfAdapter);

        // Firebase reference to "turfs"
        turfsRef = FirebaseDatabase.getInstance().getReference("turfs");
        fetchAvailableTurfs();
    }

    private void fetchAvailableTurfs() {
        turfsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                turfList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Turf turf = snapshot.getValue(Turf.class);
                    turfList.add(turf);
                }
                turfAdapter.notifyDataSetChanged();  // Notify adapter about data changes
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TurfBrowsingActivity.this, "Failed to fetch turfs", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
