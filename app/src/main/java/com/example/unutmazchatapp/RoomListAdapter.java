package com.example.unutmazchatapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.ViewHolder> {
    private ArrayList<Chatroom> chatrooms = new ArrayList<>();
    private String currentUsername;

    private onItemClickListener mListener;

    public interface onItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chatroom_item, parent, false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getRoomName().setText(chatrooms.get(position).getName());
        holder.getRoomDesc().setText(chatrooms.get(position).getDesc());

        switch (chatrooms.get(position).getIconName()){
            case "icon_general":
                holder.getRoomImage().setImageResource(R.drawable.ic_baseline_chat_24);
                break;
            case "icon_game":
                holder.getRoomImage().setImageResource(R.drawable.game_icon);
                break;
            case "icon_family":
                holder.getRoomImage().setImageResource(R.drawable.family_icon);
                break;
            case "icon_food":
                holder.getRoomImage().setImageResource(R.drawable.food_icon);
                break;
            case "icon_school":
                holder.getRoomImage().setImageResource(R.drawable.school_icon);
                break;
            case "icon_music":
                holder.getRoomImage().setImageResource(R.drawable.music_icon);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return chatrooms.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView roomName;
        private final TextView roomDesc;
        private final ImageView roomImage;
        private final Button enterButton;

        public ViewHolder(View v, onItemClickListener listener) {
            super(v);
            roomImage = v.findViewById(R.id.chatroomImage);
            roomName = v.findViewById(R.id.chatroomName);
            roomDesc = v.findViewById(R.id.roomDesc);
            enterButton = v.findViewById(R.id.enterButton);

            enterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }

        public ImageView getRoomImage() { return roomImage; }
        public TextView getRoomName() { return roomName; }
        public TextView getRoomDesc() { return roomDesc; }
        public Button getEnterButton() { return enterButton; }
    }

    public RoomListAdapter(ArrayList<Chatroom> chatrooms, String currentUsername){
        this.chatrooms = chatrooms;
        this.currentUsername = currentUsername;
    }

    public void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }
}
