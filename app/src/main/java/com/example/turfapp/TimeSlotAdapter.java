package com.example.turfapp;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder> {
    private List<TimeSlot> timeSlotList;
    private int selectedPosition = RecyclerView.NO_POSITION; // To track the selected position

    // Constructor
    public TimeSlotAdapter(Context context, List<TimeSlot> timeSlotList) {
        this.timeSlotList = timeSlotList;
    }

    @NonNull
    @Override
    public TimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each time slot item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_slot_item, parent, false);
        return new TimeSlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotViewHolder holder, int position) {
        TimeSlot slot = timeSlotList.get(position); // Get the time slot at the current position

        // Set the time text in the TextView
        holder.timeTextView.setText(slot.getTime()); // Assuming TimeSlot has a getTime() method

        // Highlight the selected item
        holder.itemView.setSelected(selectedPosition == holder.getAdapterPosition());
        holder.itemView.setOnClickListener(v -> {
            // Update the selected position
            notifyItemChanged(selectedPosition); // Clear previous selection
            selectedPosition = holder.getAdapterPosition(); // Set new selected position
            notifyItemChanged(selectedPosition); // Highlight new selection

            // Optional: show a Toast or perform any other action
            Toast.makeText(v.getContext(), "Selected: " + slot.getTime(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return timeSlotList.size(); // Return the total number of items
    }

    // Method to get the selected TimeSlot
    public TimeSlot getSelectedTimeSlot() {
        if (selectedPosition != RecyclerView.NO_POSITION) {
            return timeSlotList.get(selectedPosition);
        }
        return null; // No selection
    }

    // ViewHolder class to hold the view references
    static class TimeSlotViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView;

        public TimeSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.timeTextView); // Update with your actual TextView ID
        }
    }
}
