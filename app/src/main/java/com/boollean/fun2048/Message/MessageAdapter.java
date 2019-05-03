package com.boollean.fun2048.Message;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.boollean.fun2048.Entity.MessageEntity;
import com.boollean.fun2048.R;
import com.boollean.fun2048.Utils.HttpUtils;
import com.boollean.fun2048.Utils.MyPhotoFactory;

import java.util.List;

/**
 * 留言板界面处理留言信息的Adapter。
 *
 * @author Boollean
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private List<MessageEntity> mList;

    MessageAdapter(List<MessageEntity> list) {
        mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MessageEntity messageEntity = mList.get(position);

        holder.nameTextView.setText(messageEntity.getName());
        StringBuilder date = new StringBuilder(messageEntity.getDate());
        date.replace(10, 12, "  ");

        holder.dateTextView.setText(date.toString());
        holder.messageTextView.setText(messageEntity.getMessage());
        if (mList.get(position).getGender() == 1) {
            holder.genderImageView.setImageResource(R.mipmap.ic_male);
        } else if (mList.get(position).getGender() == 2) {
            holder.genderImageView.setImageResource(R.mipmap.ic_female);
        } else {
            holder.genderImageView.setImageResource(0);
        }
        DownloadImage downloadImage = new DownloadImage(holder, messageEntity.getName());
        downloadImage.execute();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * 下载头像的线程类
     */
    private class DownloadImage extends AsyncTask<Void, Void, Bitmap> {
        private String mName;
        private ViewHolder mViewHolder;

        DownloadImage(ViewHolder holder, String name) {
            mName = name;
            mViewHolder = holder;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap bitmap = HttpUtils.getImage(mName);
            return MyPhotoFactory.toRoundBitmap(bitmap);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mViewHolder.bindBitmap(bitmap);
        }
    }

    /**
     * 留言板每个对象的容器
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView nameTextView;
        TextView dateTextView;
        TextView messageTextView;
        ImageView genderImageView;
        View view;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.message_box_image_view);
            nameTextView = itemView.findViewById(R.id.message_box_name_text_view);
            dateTextView = itemView.findViewById(R.id.message_box_date_text_view);
            messageTextView = itemView.findViewById(R.id.message_box_message_text_view);
            genderImageView = itemView.findViewById(R.id.message_gender_image_view);
            view = itemView.findViewById(R.id.message_box_bottom_line);
        }

        /**
         * 绑定Bitmap到ImageView里面
         *
         * @param bitmap 需要绑定的bitmap对象
         */
        void bindBitmap(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
