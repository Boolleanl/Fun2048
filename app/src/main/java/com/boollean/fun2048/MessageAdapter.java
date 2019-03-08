package com.boollean.fun2048;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    //private ArrayList<Bitmap> mImageList;
    private ArrayList<String> mNameList;
    private ArrayList<String> mDateList;
    private ArrayList<String> mMessageList;


    public MessageAdapter(ArrayList<String> nameList,ArrayList<String> dateList,ArrayList<String> messageList) {
        mNameList = nameList;
        mDateList = dateList;
        mMessageList = messageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = mNameList.get(position);
        String date = mDateList.get(position);
        String message = mMessageList.get(position);
        holder.nameTextView.setText(name);
        holder.dateTextView.setText(date);
        holder.messageTextView.setText(message);
    }

    @Override
    public int getItemCount() {
        return mNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView dateTextView;
        TextView messageTextView;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.message_box_image_view);
            nameTextView = itemView.findViewById(R.id.message_box_name_text_view);
            dateTextView = itemView.findViewById(R.id.message_box_date_text_view);
            messageTextView = itemView.findViewById(R.id.message_box_message_text_view);
            view = itemView.findViewById(R.id.message_box_bottom_line);
        }
    }
}
