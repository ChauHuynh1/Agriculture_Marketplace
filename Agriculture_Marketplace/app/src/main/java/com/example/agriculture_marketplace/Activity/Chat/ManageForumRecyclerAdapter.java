package com.example.agriculture_marketplace.Activity.Chat;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agriculture_marketplace.Forum.Model.ManageForumModel;
import com.example.agriculture_marketplace.Message.Model.ChatroomModel;
import com.example.agriculture_marketplace.R;

import java.util.List;

// ManageForumRecyclerAdapter.java
public class ManageForumRecyclerAdapter extends RecyclerView.Adapter<ManageForumRecyclerAdapter.ManageForumViewHolder> {

    private List<ManageForumModel> manageForumList;


    // Constructor to receive the data list
    public ManageForumRecyclerAdapter(List<ManageForumModel> manageForumList) {
        this.manageForumList = manageForumList;
    }
    public void setData(List<ManageForumModel> newData) {
        manageForumList.clear();
        manageForumList.addAll(newData);
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ManageForumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_forum_recycler_row, parent, false);
        return new ManageForumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageForumViewHolder holder, int position) {
        ManageForumModel currentForum = manageForumList.get(position);
        // Set data to views
        holder.forum_detail_name.setText(currentForum.getForumName());
        // Set other data as needed

        // Handle checkbox state
        holder.checkBox.setChecked(currentForum.isSelected());

        // Handle item click listener if needed
        holder.itemView.setOnClickListener(v -> {
            // Toggle the checkbox state
            currentForum.setSelected(!currentForum.isSelected());
            holder.checkBox.setChecked(currentForum.isSelected());
        });

    }
    @Override
    public int getItemCount() {
        return manageForumList.size();
    }

    // ViewHolder class
    public static class ManageForumViewHolder extends RecyclerView.ViewHolder {
        TextView forum_detail_name;
        ImageView profilePic;
        CheckBox checkBox;

        public ManageForumViewHolder(@NonNull View itemView) {
            super(itemView);
            forum_detail_name = itemView.findViewById(R.id.forum_detail_name);
            profilePic = itemView.findViewById(R.id.forumImageView);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }

    // Method to get the selected items
    public List<ManageForumModel> getSelectedItems() {

        return null;
    }
}
