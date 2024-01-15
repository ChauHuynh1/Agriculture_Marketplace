package com.example.agriculture_marketplace.Activity.Chat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.agriculture_marketplace.Message.Model.ChatMessageModel;
import com.example.agriculture_marketplace.R;
import com.example.agriculture_marketplace.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatMessageModel, ChatRecyclerAdapter.ChatModelViewHolder> {
    private Context context;

    public ChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatMessageModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatModelViewHolder holder, int position, @NonNull ChatMessageModel model) {
        boolean isCurrentUser = model.getSenderId().equals(FirebaseUtil.currentUserId());

        // Set up text message layouts
        if (isCurrentUser) {
            holder.leftChatLayout.setVisibility(View.GONE);
            holder.rightChatLayout.setVisibility(View.VISIBLE);
            holder.rightChatTextview.setText(model.getMessage());
        } else {
            holder.rightChatLayout.setVisibility(View.GONE);
            holder.leftChatLayout.setVisibility(View.VISIBLE);
            holder.leftChatTextview.setText(model.getMessage());
        }

        // Set sender's username
        if (model.getSenderUsername() != null && !model.getSenderUsername().isEmpty()) {
            holder.senderTextView.setVisibility(View.VISIBLE);
            holder.senderTextView.setText(model.getSenderUsername());
        } else {
            holder.senderTextView.setVisibility(View.GONE);
        }

        // Check if there is an image URL
        if (model.getImageUrl() != null && !model.getImageUrl().isEmpty()) {
            // If there's an image URL, hide text message layouts
            holder.rightChatLayout.setVisibility(View.GONE);
            holder.leftChatLayout.setVisibility(View.GONE);

            // Display the appropriate ImageView based on the sender
            if (isCurrentUser) {
                holder.messageSenderImgView.setVisibility(View.VISIBLE);
                Glide.with(context).load(model.getImageUrl()).into(holder.messageSenderImgView);
            } else {
                holder.messageReceiverImgView.setVisibility(View.VISIBLE);
                Glide.with(context).load(model.getImageUrl()).into(holder.messageReceiverImgView);
            }
        } else {
            // If no image URL, hide image views
            holder.messageSenderImgView.setVisibility(View.GONE);
            holder.messageReceiverImgView.setVisibility(View.GONE);
        }
    }



    @NonNull
    @Override
    public ChatModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_recycler_row, parent, false);
        return new ChatModelViewHolder(view);
    }

    class ChatModelViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftChatLayout, rightChatLayout;
        TextView leftChatTextview, rightChatTextview;
        ImageView messageReceiverImgView, messageSenderImgView;
        TextView senderTextView;

        public ChatModelViewHolder(@NonNull View itemView) {
            super(itemView);
            leftChatLayout = itemView.findViewById(R.id.left_chat_layout);
            rightChatLayout = itemView.findViewById(R.id.right_chat_layout);
            leftChatTextview = itemView.findViewById(R.id.left_chat_textview);
            rightChatTextview = itemView.findViewById(R.id.right_chat_textview);
            messageReceiverImgView = itemView.findViewById(R.id.message_receiver_img_view);
            messageSenderImgView = itemView.findViewById(R.id.message_sender_img_view);
            senderTextView = itemView.findViewById(R.id.sender_text_view);
        }
    }



}
