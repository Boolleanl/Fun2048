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

class RankAdapter extends BaseAdapter {

//    private ArrayList<Bitmap> ImageList;
    private ArrayList<Integer> mPositionList;
    private ArrayList<String> mNameList;
    private ArrayList<Integer> mScoreList;

    public RankAdapter(ArrayList<Integer> positionList,ArrayList<String> nameList, ArrayList<Integer> scoreList) {
        mPositionList = positionList;
        mNameList = nameList;
        mScoreList = scoreList;
    }

    @Override
    public int getCount() {
        return mNameList.size();
    }

    @Override
    public Object getItem(int position) {
        return mNameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_item,parent,false);
        TextView positionTextView = view.findViewById(R.id.rank_position_text_view);
//        ImageView imageView = view.findViewById(R.id.rank_image_view);
        TextView nameTextView = view.findViewById(R.id.rank_name_text_view);
        TextView scoreTextView = view.findViewById(R.id.rank_score_text_view);

        Integer p = mPositionList.get(position);
        String name = mNameList.get(position);
        Integer score = mScoreList.get(position);

        positionTextView.setText(p.toString());
        nameTextView.setText(name);
        scoreTextView.setText(score.toString());

        return view;
    }
}
