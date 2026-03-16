package com.example.safenow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.safenow.models.ContactUrgence;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class EmergencyContactAdapter extends RecyclerView.Adapter<EmergencyContactAdapter.ContactViewHolder> {

    public interface OnContactActionListener {
        void onEdit(ContactUrgence contact);
        void onDelete(ContactUrgence contact);
    }

    private List<ContactUrgence> contacts = new ArrayList<>();
    private final OnContactActionListener listener;

    public EmergencyContactAdapter(List<ContactUrgence> contacts, OnContactActionListener listener) {
        if (contacts != null) {
            this.contacts = contacts;
        }
        this.listener = listener;
    }

    public void updateData(List<ContactUrgence> newContacts) {
        this.contacts = newContacts != null ? newContacts : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        ContactUrgence contact = contacts.get(position);

        holder.tvName.setText(contact.getNom_contact());
        holder.tvPhone.setText(contact.getNumero_contact());
        holder.tvRelationship.setText(contact.getRelation_contact());

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(contact);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(contact);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPhone, tvRelationship;
        MaterialButton btnEdit, btnDelete;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvRelationship = itemView.findViewById(R.id.tvRelationship);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}