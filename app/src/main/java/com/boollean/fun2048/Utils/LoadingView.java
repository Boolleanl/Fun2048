package com.boollean.fun2048.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boollean.fun2048.R;

/**
 * 加载框，包含进度圈和提醒文字。
 *
 * @author Boollean
 */
public class LoadingView extends RelativeLayout {
    private RelativeLayout mRelativeLayoutLoading;  //加载时显示的圈
    private RelativeLayout mRelativeLayoutFailed;   //加载失败显示的区域
    private TextView mTextView; //加载失败显示的文字显示区域

    private LoadingView.LoadingViewListener mListener;

    public LoadingView(Context context) {
        super(context);
        initView(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void setListener(LoadingViewListener listener) {
        this.mListener = listener;
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.loading, this);
        mRelativeLayoutLoading = view.findViewById(R.id.progress_bar_layout);
        mRelativeLayoutFailed = view.findViewById(R.id.failed_layout);
        mTextView = view.findViewById(R.id.failed_text_view);
        mRelativeLayoutFailed.setOnClickListener(view1 -> {
            // 加载失败，点击重试
            mRelativeLayoutLoading.setVisibility(View.VISIBLE);
            mRelativeLayoutFailed.setVisibility(View.GONE);
            mListener.onFailedClickListener();
        });
    }

    /**
     * 显示加载过程中的状态
     */
    public void showLoading() {
        mRelativeLayoutLoading.setVisibility(View.VISIBLE);
        mRelativeLayoutFailed.setVisibility(View.GONE);
    }

    /**
     * 显示加载完成的状态
     */
    public void showContentView() {
        mRelativeLayoutLoading.setVisibility(View.GONE);
        mRelativeLayoutFailed.setVisibility(View.GONE);
    }

    /**
     * 显示加载失败的状态
     */
    public void showFailed() {
        mRelativeLayoutLoading.setVisibility(View.GONE);
        mRelativeLayoutFailed.setVisibility(View.VISIBLE);
        mTextView.setText("加载失败，点击重试");
    }

    public void showNetworkUnavailable() {
        mRelativeLayoutLoading.setVisibility(View.GONE);
        mRelativeLayoutFailed.setVisibility(View.VISIBLE);
        mTextView.setText("网络不可用");
    }

    public interface LoadingViewListener {
        void onFailedClickListener();
    }
}
