package com.example.erpproject.Admin.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.erpproject.Admin.ViewClasses.LoanViewClass;
import com.example.erpproject.R;


import java.util.List;
public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.LoanViewHolder>{
    private final AsyncListDiffer<LoanViewClass> differ;
    public interface OnLoanClickListener {
        void onLoanClicked(int loanId);
    }

    private LoanAdapter.OnLoanClickListener listener;

    public void setOnLoanClickListener(LoanAdapter.OnLoanClickListener listener) {
        this.listener = listener;
    }

    public LoanAdapter() {
        DiffUtil.ItemCallback<LoanViewClass> diffCallback = new DiffUtil.ItemCallback<LoanViewClass>() {
            @Override
            public boolean areItemsTheSame(@NonNull LoanViewClass oldItem, @NonNull LoanViewClass newItem) {
                // Compare unique IDs
                return (String.valueOf(oldItem.getLoanId()).equals(String.valueOf(newItem.getLoanId())));
            }
            @Override
            public boolean areContentsTheSame(@NonNull LoanViewClass oldItem, @NonNull LoanViewClass newItem) {
                // Compare entire content
                return oldItem.getWorkerName().equals(newItem.getWorkerName())
                        &&oldItem.getWorkerId().equals(newItem.getWorkerId())
                        &&oldItem.getLoanAmount().equals(newItem.getLoanAmount());
            }
        };
        differ = new AsyncListDiffer<>(this, diffCallback);
    }

    public void submitList(List<LoanViewClass> loans) {
        differ.submitList(loans);
    }

    @NonNull
    @Override
    public LoanAdapter.LoanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loan,parent,false);
        return new LoanAdapter.LoanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoanAdapter.LoanViewHolder holder, int position) {

        LoanViewClass loan = differ.getCurrentList().get(position);
        holder.name.setText(loan.getWorkerName());
        holder.status.setText(loan.getStatus());
        holder.date.setText(loan.getDateApplied());
        holder.amount.setText("$"+loan.getLoanAmount());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onLoanClicked(loan.getLoanId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    static class LoanViewHolder extends RecyclerView.ViewHolder {
        TextView name,status,date,amount;

        public LoanViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvWorkerName);
            status = itemView.findViewById(R.id.tvLoanStatus);
            date = itemView.findViewById(R.id.tvLoanDate);
            amount = itemView.findViewById(R.id.tvLoanAmount);
        }
    }
}
