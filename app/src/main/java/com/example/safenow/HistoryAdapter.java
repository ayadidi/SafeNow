package com.example.safenow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.safenow.models.AlertEvent;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<AlertEvent> alertEvents;

    public HistoryAdapter(List<AlertEvent> alertEvents) {
        this.alertEvents = alertEvents;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AlertEvent event = alertEvents.get(position);
        holder.tvDate.setText(event.getDate());
        holder.tvTime.setText(event.getHeure());
        holder.tvPosition.setText(event.getPosition());
    }

    @Override
    public int getItemCount() {
        return alertEvents.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvTime, tvPosition;
        public ViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvPosition = itemView.findViewById(R.id.tvPosition);
        }
    }
}