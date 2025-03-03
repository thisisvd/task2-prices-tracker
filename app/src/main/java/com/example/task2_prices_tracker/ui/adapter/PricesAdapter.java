package com.example.task2_prices_tracker.ui.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task2_prices_tracker.databinding.PricesItemBinding;
import com.example.task2_prices_tracker.domain.Prices;

import org.jspecify.annotations.NonNull;

import java.util.List;

public class PricesAdapter extends RecyclerView.Adapter<PricesAdapter.PricesViewHolder> {

    private final AsyncListDiffer<Prices> differ;

    public static class PricesViewHolder extends RecyclerView.ViewHolder {
        private final PricesItemBinding binding;

        public PricesViewHolder(PricesItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public PricesAdapter() {
        DiffUtil.ItemCallback<Prices> differCallback = new DiffUtil.ItemCallback<>() {
            @Override
            public boolean areItemsTheSame(@NonNull Prices oldItem, @NonNull Prices newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @SuppressLint("DiffUtilEquals")
            @Override
            public boolean areContentsTheSame(@NonNull Prices oldItem, @NonNull Prices newItem) {
                return oldItem.equals(newItem);
            }
        };
        differ = new AsyncListDiffer<>(this, differCallback);
    }

    @NonNull
    @Override
    public PricesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PricesItemBinding binding = PricesItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PricesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PricesViewHolder holder, int position) {
        Prices transaction = differ.getCurrentList().get(position);
        holder.binding.coinId.setText(String.valueOf(transaction.getId()));
        holder.binding.coinName.setText(transaction.getName());
        holder.binding.price.setText(String.valueOf(transaction.getPrice()));
        double change = transaction.getChange();
        holder.binding.changePercent.setText(String.valueOf(change) + "%");
        holder.binding.changePercent.setTextColor(change > 0d ? Color.GREEN : Color.RED);
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    public void submitList(List<Prices> newList) {
        differ.submitList(newList);
    }
}