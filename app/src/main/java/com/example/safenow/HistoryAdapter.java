package com.example.safenow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.safenow.models.SOS; // Plus besoin d'AlertEvent

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    // 1. La liste doit être de type SOS
    private List<SOS> alertEvents;

    // 2. Le constructeur doit AUSSI recevoir une liste de SOS
    public HistoryAdapter(List<SOS> alertEvents) {
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
        SOS event = alertEvents.get(position);

        if (event.getTimestamp() != null) {
            SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            holder.tvDate.setText(sdfDate.format(event.getTimestamp()));

            SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
            holder.tvTime.setText(sdfTime.format(event.getTimestamp()));
        }

        // 3. Vérifie que tvType est bien déclaré dans ton ViewHolder plus bas
        holder.tvType.setText(event.getType());
        holder.tvPosition.setText(event.getLocalisation());
    }

    @Override
    public int getItemCount() {
        return alertEvents.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Ajoute tvType ici pour correspondre au onBindViewHolder
        TextView tvDate, tvTime, tvPosition, tvType;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvPosition = itemView.findViewById(R.id.tvPosition);
            tvType = itemView.findViewById(R.id.tvType); // Assure-toi que cet ID existe dans item_history.xml
        }
    }
}