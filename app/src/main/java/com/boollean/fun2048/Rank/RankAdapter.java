package com.boollean.fun2048.Rank;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boollean.fun2048.Entity.RankUserEntity;
import com.boollean.fun2048.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 排行榜界面每条排行信息的适配器。
 * Created by Boollean on 2019/3/8.
 */
public class RankAdapter extends RecyclerView.Adapter<RankAdapter.RankViewHolder> {
    private List<RankUserEntity> mList;

    public RankAdapter(List<RankUserEntity> list) {
        mList = list;
        Log.i("Rank4: ", mList.get(0).getName());
    }

    @NonNull
    @Override
    public RankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_item, parent, false);
        return new RankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankViewHolder holder, int position) {
        holder.positionTextView.setText(mList.get(position).getPosition() + "");
        holder.nameTextView.setText(mList.get(position).getName());
        holder.scoreTextView.setText(mList.get(position).getScore() + "");
        if (mList.get(position).getGender() == 1) {
            holder.genderImageView.setImageResource(R.mipmap.ic_male);
        } else if (mList.get(position).getGender() == 2) {
            holder.genderImageView.setImageResource(R.mipmap.ic_female);
        } else {
            holder.genderImageView.setImageResource(0);
        }
    }

    @Override
    public long getItemId(int position) {
        return mList.get(position).getPosition();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class RankViewHolder extends RecyclerView.ViewHolder {
        TextView positionTextView;
        //        ImageView imageView;
        TextView nameTextView;
        TextView scoreTextView;
        ImageView genderImageView;

        public RankViewHolder(@NonNull View itemView) {
            super(itemView);
            positionTextView = itemView.findViewById(R.id.rank_position_text_view);
//          imageView = view.findViewById(R.id.rank_image_view);
            nameTextView = itemView.findViewById(R.id.rank_name_text_view);
            scoreTextView = itemView.findViewById(R.id.rank_score_text_view);
            genderImageView = itemView.findViewById(R.id.rank_gender_image_view);
        }
    }
}
