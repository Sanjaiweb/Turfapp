package com.example.turfapp;

import android.os.Bundle;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class ViewBookingHistoryActivity extends AppCompatActivity {

    private ListView bookingsListView;
    private BookingAdapter bookingAdapter;
    private List<Booking> bookingList;
    private DatabaseReference bookingsRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_booking_history);

        bookingsListView = findViewById(R.id.bookingsListView);
        bookingList = new ArrayList<>();
        bookingAdapter = new BookingAdapter(this, bookingList);
        bookingsListView.setAdapter(bookingAdapter);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        bookingsRef = FirebaseDatabase.getInstance().getReference("bookings");

        fetchBookings();
    }

    private void fetchBookings() {
        bookingsRef.orderByChild("userId").equalTo(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookingList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Booking booking = dataSnapshot.getValue(Booking.class);
                    bookingList.add(booking);
                }
                bookingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
    }
}
