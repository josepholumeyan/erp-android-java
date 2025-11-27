package com.example.erpproject.Admin.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.example.erpproject.Admin.ViewClasses.VacationViewClass;
import com.example.erpproject.R;

import java.util.List;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {
    private final AsyncListDiffer<VacationViewClass> differ;
    public interface OnVacationClickListener {
        void onVacationClicked(int vacationId);
    }

    private VacationAdapter.OnVacationClickListener listener;

    public void setOnVacationClickListener(VacationAdapter.OnVacationClickListener listener) {
        this.listener = listener;
    }

    public VacationAdapter() {
        DiffUtil.ItemCallback<VacationViewClass> diffCallback = new DiffUtil.ItemCallback<VacationViewClass>() {
            @Override
            public boolean areItemsTheSame(@NonNull VacationViewClass oldItem, @NonNull VacationViewClass newItem) {
                // Compare unique IDs
                return (String.valueOf(oldItem.getVacationId()).equals(String.valueOf(newItem.getVacationId())));
            }
            @Override
            public boolean areContentsTheSame(@NonNull VacationViewClass oldItem, @NonNull VacationViewClass newItem) {
                // Compare entire content
                return oldItem.getWorkerName().equals(newItem.getWorkerName())
                        &&oldItem.getWorkerId().equals(newItem.getWorkerId())
                        &&oldItem.getDateApplied().equals(newItem.getDateApplied());
            }
        };
        differ = new AsyncListDiffer<>(this, diffCallback);
    }

    public void submitList(List<VacationViewClass> vacations) {
        differ.submitList(vacations);
    }

    @NonNull
    @Override
    public VacationAdapter.VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vacation,parent,false);
        return new VacationAdapter.VacationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationAdapter.VacationViewHolder holder, int position) {

        VacationViewClass vacation = differ.getCurrentList().get(position);
        holder.name.setText(vacation.getWorkerName());
        holder.status.setText(vacation.getStatus());
        holder.date.setText(vacation.getDateApplied());
        holder.duration.setText(vacation.getVacationDuration()+"Days");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onVacationClicked(vacation.getVacationId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    static class VacationViewHolder extends RecyclerView.ViewHolder {
        TextView name,status,date,duration;

        public VacationViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvWorkerName);
            status = itemView.findViewById(R.id.tvVacationStatus);
            date = itemView.findViewById(R.id.tvVacationDate);
            duration = itemView.findViewById(R.id.tvVacationPeriod);
        }
    }
}
