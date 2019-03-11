package com.boollean.fun2048;

import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class RankAdapter extends RecyclerView.Adapter<RankAdapter.RankViewHolder> {

//    private ArrayList<Bitmap> ImageList;
    private ArrayList<Integer> mPositionList;
    private ArrayList<String> mNameList;
    private ArrayList<Integer> mScoreList;

    public RankAdapter(ArrayList<Integer> positionList,ArrayList<String> nameList, ArrayList<Integer> scoreList) {
        mPositionList = positionList;
        mNameList = nameList;
        mScoreList = scoreList;
    }

    @NonNull
    @Override
    public RankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_item,parent,false);
        return new RankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankViewHolder holder, int position) {
        Integer p = mPositionList.get(position);
        String name = mNameList.get(position);
        Integer score = mScoreList.get(position);
        holder.positionTextView.setText(p+"");
        holder.nameTextView.setText(name);
        holder.scoreTextView.setText(score+"");
    }

    @Override
    public long getItemId(int position) {
        return mPositionList.get(position);
    }

    @Override
    public int getItemCount() {
        return mPositionList.size();
    }


    public class RankViewHolder extends RecyclerView.ViewHolder {
        TextView positionTextView;
        //        ImageView imageView;
        TextView nameTextView;
        TextView scoreTextView;

        public RankViewHolder(@NonNull View itemView) {
            super(itemView);
            positionTextView = itemView.findViewById(R.id.rank_position_text_view);
//          imageView = view.findViewById(R.id.rank_image_view);
            nameTextView = itemView.findViewById(R.id.rank_name_text_view);
            scoreTextView = itemView.findViewById(R.id.rank_score_text_view);
        }
    }
}
