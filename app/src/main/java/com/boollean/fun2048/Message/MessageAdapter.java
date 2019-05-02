package com.boollean.fun2048.Message;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boollean.fun2048.Entity.MessageEntity;
import com.boollean.fun2048.R;
import com.boollean.fun2048.Utils.HttpUtils;
import com.boollean.fun2048.Utils.MyPhotoFactory;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 留言板界面处理留言信息的Adapter。
 *
 * @author Boollean
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private DownloadImage<ViewHolder> mDownloadImage;
    private List<MessageEntity> mList;

    public MessageAdapter(List<MessageEntity> list) {
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
        date.replace(10,12,"  ");
        Log.i("Message",date.toString());
        holder.dateTextView.setText(date.toString());
        holder.messageTextView.setText(messageEntity.getMessage());
        if (mList.get(position).getGender() == 1) {
            holder.genderImageView.setImageResource(R.mipmap.ic_male);
        } else if (mList.get(position).getGender() == 2) {
            holder.genderImageView.setImageResource(R.mipmap.ic_female);
        } else {
            holder.genderImageView.setImageResource(0);
        }
        mDownloadImage = new DownloadImage(holder,messageEntity.getName());
        mDownloadImage.execute();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class DownloadImage<T> extends AsyncTask<Void, Void, Bitmap>{
        private String mName;
        private ViewHolder mViewHolder;

        public DownloadImage(T target,String name) {
            mName = name;
            mViewHolder = (ViewHolder) target;
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

        public void bindBitmap(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
