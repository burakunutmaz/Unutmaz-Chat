package com.example.unutmazchatapp;


import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private ArrayList<Message> localDataset;
    private String currentUsername;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getTextView().setText(localDataset.get(position).getData());
        holder.getMessageBox().setBackgroundResource(R.drawable.other_message);
        holder.getUsernameOnMessage().setText(localDataset.get(position).getSentBy());
        holder.getMessageHour().setText(localDataset.get(position).getHour());

        if (localDataset.get(position).getSentBy().equals(currentUsername)){
            holder.getMessageBox().removeAllViews();
            holder.getMessageBox().addView(holder.getMessageHour());
            holder.getMessageBox().addView(holder.getTextView());
            holder.getUsernameOnMessage().setVisibility(View.GONE);
            holder.getMessageBox().setBackgroundResource(R.drawable.message);
            holder.getLinearLayout().setLayoutParams(holder.params);
        }
    }

    @Override
    public int getItemCount() {
        return localDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textView;
        private final TextView usernameOnMessage;
        private final TextView messageHour;
        private final LinearLayout linearLayout;
        private final LinearLayout messageBox;
        private FrameLayout.LayoutParams params;

        public ViewHolder(View v){
            super(v);
            textView = v.findViewById(R.id.textView);
            usernameOnMessage = v.findViewById(R.id.usernameOnMessage);
            messageHour = v.findViewById(R.id.messageHour);
            linearLayout = v.findViewById(R.id.messagesLayout);
            messageBox = v.findViewById(R.id.messageBox);
            params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.END;
            params.setMargins(0,10,0,0);
        }

        public TextView getTextView(){
            return textView;
        }
        public TextView getUsernameOnMessage() { return usernameOnMessage; }
        public LinearLayout getLinearLayout() { return linearLayout; }
        public LinearLayout getMessageBox() { return messageBox; }
        public TextView getMessageHour() { return messageHour; }
    }

    public CustomAdapter(ArrayList<Message> dataset, String currentUsername){
        localDataset = dataset;
        this.currentUsername = currentUsername;
    }

    public void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }
}
