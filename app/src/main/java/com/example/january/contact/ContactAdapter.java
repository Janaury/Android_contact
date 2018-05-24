package com.example.january.contact;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by january on 18-5-9.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private List<ContactDetails> contacts;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View contact_item_view;
        TextView firstChar_view;
        TextView name_view;

        public ViewHolder(View view){
            super(view);
            contact_item_view = view;
            firstChar_view = (TextView) view.findViewById(R.id.first_char);
            name_view = (TextView) view.findViewById(R.id.name);
        }
    }

    public ContactAdapter(List<ContactDetails> _contacts){
        contacts = _contacts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_item_view, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Toast.makeText(view.getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ContactDetails contact = contacts.get(position);
        holder.firstChar_view.setText(contact.getFirstChar());
        holder.name_view.setText(contact.getName());
        if((position == 0) || !(contacts.get(position - 1).getFirstChar().equals(contact.getFirstChar()))){
            holder.firstChar_view.setVisibility(View.VISIBLE);
        }else{
            holder.firstChar_view.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return contacts.size();
    }


}
