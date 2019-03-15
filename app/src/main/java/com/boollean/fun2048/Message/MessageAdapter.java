package com.boollean.fun2048.Message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boollean.fun2048.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 留言板界面处理留言信息的Adapter。
 * Created by Boollean on 2019/3/6.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    //private ArrayList<Bitmap> mImageList;
    private ArrayList<String> mNameList;
    private ArrayList<String> mDateList;
    private ArrayList<String> mMessageList;
    private ArrayList<Integer> mGenderList;


    public MessageAdapter(ArrayList<String> nameList, ArrayList<String> dateList, ArrayList<String> messageList, ArrayList<Integer> genderList) {
        mNameList = nameList;
        mDateList = dateList;
        mMessageList = messageList;
        mGenderList = genderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = mNameList.get(position);
        String date = mDateList.get(position);
        String message = mMessageList.get(position);
        Integer gender = mGenderList.get(position);
        holder.nameTextView.setText(name);
        holder.dateTextView.setText(date);
        holder.messageTextView.setText(message);
        if (gender == 1) {
            holder.genderImageView.setImageResource(R.mipmap.ic_male);
        } else if (gender == 2) {
            holder.genderImageView.setImageResource(R.mipmap.ic_female);
        } else {
            holder.genderImageView.setImageResource(0);
        }
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
        ImageView genderImageView;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.message_box_image_view);
            nameTextView = itemView.findViewById(R.id.message_box_name_text_view);
            dateTextView = itemView.findViewById(R.id.message_box_date_text_view);
            messageTextView = itemView.findViewById(R.id.message_box_message_text_view);
            genderImageView = itemView.findViewById(R.id.message_gender_image_view);
            view = itemView.findViewById(R.id.message_box_bottom_line);
        }
    }
}
