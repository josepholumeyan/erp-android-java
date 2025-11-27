package com.example.erpproject.Admin.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.erpproject.R;
import com.example.erpproject.data.entity.Worker;

import java.util.List;

public class WorkerAdapter extends RecyclerView.Adapter<WorkerAdapter.WorkerViewHolder>{


    private final AsyncListDiffer<Worker> differ;
    public interface OnWorkerClickListener {
        void onWorkerClicked(String workerId);
    }

    private OnWorkerClickListener listener;

    public void setOnWorkerClickListener(OnWorkerClickListener listener) {
        this.listener = listener;
    }

    public WorkerAdapter() {
        DiffUtil.ItemCallback<Worker> diffCallback = new DiffUtil.ItemCallback<Worker>() {
            @Override
            public boolean areItemsTheSame(@NonNull Worker oldItem, @NonNull Worker newItem) {
                // Compare unique IDs
                return oldItem.getId().equals(newItem.getId());
            }
            @Override
            public boolean areContentsTheSame(@NonNull Worker oldItem, @NonNull Worker newItem) {
                // Compare entire content
                return oldItem.getName().equals(newItem.getName())
                        &&oldItem.getDepartment().equals(newItem.getDepartment())
                        &&oldItem.getRole().equals(newItem.getRole());
            }
        };
        differ = new AsyncListDiffer<>(this, diffCallback);
    }

    public void submitList(List<Worker> workers) {
        differ.submitList(workers);
    }

    @NonNull
    @Override
    public WorkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_worker,parent,false);
        return new WorkerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkerViewHolder holder, int position) {

        Worker worker = differ.getCurrentList().get(position);
        holder.name.setText(worker.getName());
        holder.id.setText(worker.getId());
        holder.department.setText(worker.getDepartment());
        holder.role.setText(worker.getRole());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onWorkerClicked(worker.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
            return differ.getCurrentList().size();
    }

    static class WorkerViewHolder extends RecyclerView.ViewHolder {
        TextView name,id,department,role;

        public WorkerViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvWorkerName);
            id = itemView.findViewById(R.id.tvWorkerId);
            department = itemView.findViewById(R.id.tvWorkerDept);
            role = itemView.findViewById(R.id.tvWorkerRole);
        }
    }
}
