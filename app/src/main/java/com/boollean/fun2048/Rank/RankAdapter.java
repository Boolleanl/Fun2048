package com.boollean.fun2048.Rank;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boollean.fun2048.Entity.RankUserEntity;
import com.boollean.fun2048.R;
import com.boollean.fun2048.Utils.HttpUtils;
import com.boollean.fun2048.Utils.MyPhotoFactory;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 排行榜界面每条排行信息的适配器。
 *
 * @author Boollean
 */
public class RankAdapter extends RecyclerView.Adapter<RankAdapter.RankViewHolder> {
    private DownloadRankImage<RankViewHolder> mDownloadRankImage;
    private List<RankUserEntity> mList;

    public RankAdapter(List<RankUserEntity> list) {
        mList = list;
    }

    @NonNull
    @Override
    public RankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_item, parent, false);
        return new RankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankViewHolder holder, int position) {
        RankUserEntity rankUserEntity = mList.get(position);
        holder.positionTextView.setText(rankUserEntity.getPosition() + "");
        holder.nameTextView.setText(rankUserEntity.getName());
        holder.scoreTextView.setText(rankUserEntity.getScore() + "");
        if (mList.get(position).getGender() == 1) {
            holder.genderImageView.setImageResource(R.mipmap.ic_male);
        } else if (mList.get(position).getGender() == 2) {
            holder.genderImageView.setImageResource(R.mipmap.ic_female);
        } else {
            holder.genderImageView.setImageResource(0);
        }
        mDownloadRankImage = new DownloadRankImage<RankViewHolder>(holder,rankUserEntity.getName());
        mDownloadRankImage.execute();
    }

    @Override
    public long getItemId(int position) {
        return mList.get(position).getPosition();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class DownloadRankImage<T> extends AsyncTask<Void, Void, Bitmap> {
        private String mName;
        private RankViewHolder mViewHolder;

        public DownloadRankImage(T target,String name) {
            mName = name;
            mViewHolder = (RankViewHolder) target;
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

    public class RankViewHolder extends RecyclerView.ViewHolder {
        TextView positionTextView;
        ImageView imageView;
        TextView nameTextView;
        TextView scoreTextView;
        ImageView genderImageView;

        public RankViewHolder(@NonNull View itemView) {
            super(itemView);
            positionTextView = itemView.findViewById(R.id.rank_position_text_view);
            imageView = itemView.findViewById(R.id.rank_image_view);
            nameTextView = itemView.findViewById(R.id.rank_name_text_view);
            scoreTextView = itemView.findViewById(R.id.rank_score_text_view);
            genderImageView = itemView.findViewById(R.id.rank_gender_image_view);
        }

        public void bindBitmap(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
