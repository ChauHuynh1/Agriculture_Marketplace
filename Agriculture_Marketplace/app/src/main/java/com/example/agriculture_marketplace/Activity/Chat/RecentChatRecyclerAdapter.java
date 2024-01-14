package com.example.agriculture_marketplace.Activity.Chat;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agriculture_marketplace.Forum.Model.Forum;
import com.example.agriculture_marketplace.MemberForum.MemberForum;
import com.example.agriculture_marketplace.Message.Model.ChatroomModel;
import com.example.agriculture_marketplace.R;
import com.example.agriculture_marketplace.User.Model.User;
import com.example.agriculture_marketplace.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.sql.Timestamp;

public class RecentChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatroomModel, RecentChatRecyclerAdapter.ChatroomModelViewHolder> {
    private Context context;

    public RecentChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatroomModel> options,Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public ChatroomModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_chat_recycler_row, parent, false);
        return new ChatroomModelViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatroomModelViewHolder holder, int position, @NonNull ChatroomModel model) {
        String forumId = model.getForumId();
        if (forumId != null) {
            FirebaseUtil.getForumDetails(forumId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            boolean lastMessageSentByMe = model.getLastMessageSenderId().equals(FirebaseUtil.currentUserId());

                            Forum forum = task.getResult().toObject(Forum.class);
                            if (forum != null) {
                                holder.forum_detail_name.setText(forum.getName());
                                if (lastMessageSentByMe)
                                    holder.last_message.setText("You : " + model.getLastMessage());
                                else
                                    holder.last_message.setText(model.getLastMessage());

                                holder.last_message_time.setText(FirebaseUtil.timestampToString(model.getLastMessageTimestamp()));

                                holder.itemView.setOnClickListener(v -> {
                                    Intent intent = new Intent(context, Chat.class);
                                    intent.putExtra("forumId", model.getForumId());
                                    intent.putExtra("forumName", forum.getName());
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                });
                            }
                        }
                    });
        } else {
            Log.e("RecentChatRecyclerAdapter", "ForumId is null for position: " + position);
        }
    }






    class ChatroomModelViewHolder extends RecyclerView.ViewHolder {
        TextView forum_detail_name;
        TextView last_message;
        TextView last_message_time;
        ImageView profilePic;

        public ChatroomModelViewHolder(@NonNull View itemView) {
            super(itemView);
            forum_detail_name = itemView.findViewById(R.id.forum_detail_name);
            profilePic = itemView.findViewById(R.id.forumImageView);
            last_message = itemView.findViewById(R.id.last_message);
            last_message_time = itemView.findViewById(R.id.last_message_time);

        }
    }
}
