package com.example.simpletodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    public interface OnLongClickListener {
        void onItemLongClicked(int position);
    }

    public interface OnClickListener {
        void OnItemClicked(int position);
    }

    List<String> items;
    OnLongClickListener longClickListener;
    OnClickListener onClickListener;

    public ItemsAdapter(List<String> items, OnLongClickListener longClickListener, OnClickListener onClickListener) {
        this.items = items;
        this.longClickListener = longClickListener;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflates view, wraps inside view holder, and returns
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(todoView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // grabs item at position & binds to view holder
        String item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // container - easy access to views representing each row of list
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        // updates view inside view holder
        public void bind(String item) {
            tvItem.setText(item);
            tvItem.setOnLongClickListener(v -> {
                // remove item from recycler view
                longClickListener.onItemLongClicked(getAdapterPosition());
                return true;
            });
            tvItem.setOnClickListener(v -> {
                // edit item in recycler view
                onClickListener.OnItemClicked(getAdapterPosition());
            });
        }
    }
}
