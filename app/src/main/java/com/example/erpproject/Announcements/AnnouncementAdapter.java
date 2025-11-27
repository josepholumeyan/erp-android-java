package com.example.erpproject.Announcements;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.example.erpproject.R;
import com.example.erpproject.data.entity.Announcement;


import java.util.List;
public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder>{
    private final AsyncListDiffer<Announcement> differ;
    public interface OnAnnouncementClickListener {
        void onAnnouncementClicked(int announcementId);
    }

    private AnnouncementAdapter.OnAnnouncementClickListener listener;

    public void setOnAnnouncementClickListener(AnnouncementAdapter.OnAnnouncementClickListener listener) {
        this.listener = listener;
    }

    public AnnouncementAdapter() {
        DiffUtil.ItemCallback<Announcement> diffCallback = new DiffUtil.ItemCallback<Announcement>() {
            @Override
            public boolean areItemsTheSame(@NonNull Announcement oldItem, @NonNull Announcement newItem) {
                // Compare unique IDs
                return (String.valueOf(oldItem.getId()).equals(String.valueOf(newItem.getId())));
            }
            @Override
            public boolean areContentsTheSame(@NonNull Announcement oldItem, @NonNull Announcement newItem) {
                // Compare entire content
                return oldItem.getTitle().equals(newItem.getTitle())
                        &&oldItem.getContent().equals(newItem.getContent())
                        &&oldItem.getAdminId().equals(newItem.getAdminId());
            }
        };
        differ = new AsyncListDiffer<>(this, diffCallback);
    }

    public void submitList(List<Announcement> announcements) {
        differ.submitList(announcements);
    }

    @NonNull
    @Override
    public AnnouncementAdapter.AnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_announcement,parent,false);
        return new AnnouncementAdapter.AnnouncementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementAdapter.AnnouncementViewHolder holder, int position) {

        Announcement announcement = differ.getCurrentList().get(position);
        holder.title.setText(announcement.getTitle());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAnnouncementClicked(announcement.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    static class AnnouncementViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public AnnouncementViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.announcement_title_tv);

        }
    }
}
