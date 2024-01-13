package com.example.agriculture_marketplace.Activity.Chat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agriculture_marketplace.Forum.Model.Forum;
import com.example.agriculture_marketplace.MemberForum.MemberForum;
import com.example.agriculture_marketplace.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SearchForumRecyclerAdapter extends FirestoreRecyclerAdapter<Forum, SearchForumRecyclerAdapter.ForumModelViewHolder> {
    private Context context;

    public SearchForumRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Forum> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ForumModelViewHolder holder, int position, @NonNull Forum model) {
        holder.forum_detail_name.setText(model.getName());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Chat.class);
            intent.putExtra("forumId", model.getId());
            intent.putExtra("forumName", model.getName());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        });
    }

    @NonNull
    @Override
    public ForumModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_forum_recycler_row, parent, false);
        return new ForumModelViewHolder(view);
    }

    class ForumModelViewHolder extends RecyclerView.ViewHolder {
        TextView forum_detail_name;
        ImageView profilePic;

        public ForumModelViewHolder(@NonNull View itemView) {
            super(itemView);
            forum_detail_name = itemView.findViewById(R.id.forum_detail_name);
            profilePic = itemView.findViewById(R.id.forumImageView);
        }
    }
}
