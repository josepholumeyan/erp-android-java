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
import com.example.erpproject.data.entity.Attendance;


import java.util.List;


public class AttendanceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final AsyncListDiffer<Attendance> differ;


    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;


    public interface OnAttendanceClickListener {
        void onAttendanceClicked(String workerId);
    }


    private OnAttendanceClickListener listener;


    public void setOnAttendanceClickListener(OnAttendanceClickListener listener) {
        this.listener = listener;
    }


    public AttendanceAdapter() {
        DiffUtil.ItemCallback<Attendance> diffCallback = new DiffUtil.ItemCallback<Attendance>() {
            @Override
            public boolean areItemsTheSame(@NonNull Attendance oldItem, @NonNull Attendance newItem) {
                return oldItem.getId() == newItem.getId();
            }


            @Override
            public boolean areContentsTheSame(@NonNull Attendance oldItem, @NonNull Attendance newItem) {
                return oldItem.getCheckOutTime().equals(newItem.getCheckOutTime())
                        && oldItem.getWorkerId().equals(newItem.getWorkerId())
                        && oldItem.getCheckInTime().equals(newItem.getCheckInTime());
            }
        };


        differ = new AsyncListDiffer<>(this, diffCallback);
    }


    public void submitList(List<Attendance> attendances) {
        differ.submitList(attendances);
    }


    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_ITEM;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_HEADER) {
            View headerView = inflater.inflate(R.layout.item_attendance_header, parent, false);
            return new HeaderViewHolder(headerView);
        } else {
            View itemView = inflater.inflate(R.layout.item_attendance, parent, false);
            return new ItemViewHolder(itemView);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            Attendance attendance = differ.getCurrentList().get(position - 1); // subtract 1 for header
            ItemViewHolder itemHolder = (ItemViewHolder) holder;
            itemHolder.workerId.setText(attendance.getWorkerId());
            itemHolder.checkInTime.setText(attendance.getCheckInTime());
            itemHolder.checkOutTime.setText(attendance.getCheckOutTime());
            itemHolder.status.setText(attendance.getStatus());


            itemHolder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAttendanceClicked(attendance.getWorkerId());
                }
            });
        }
        // No binding needed for header since text is static in XML
    }


    @Override
    public int getItemCount() {
        return differ.getCurrentList().size() + 1; // +1 for header
    }


    // --- ViewHolders ---
    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView workerId, checkInTime, checkOutTime, status;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            workerId = itemView.findViewById(R.id.workerId_tvView);
            checkInTime = itemView.findViewById(R.id.Checkin_time_tvView);
            checkOutTime = itemView.findViewById(R.id.Checkout_time_tvView);
            status = itemView.findViewById(R.id.Status_tvView);
        }
    }
}

