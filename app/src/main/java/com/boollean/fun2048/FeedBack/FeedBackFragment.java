package com.boollean.fun2048.FeedBack;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.boollean.fun2048.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 意见反馈界面的Fragment。
 *
 * @author Boollean
 */
public class FeedBackFragment extends Fragment {
    private EditText mEditText;
    @BindView(R.id.feedback_submit_button)
    Button mButton;


    public static FeedBackFragment newInstance() {
        return new FeedBackFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);
        ButterKnife.bind(this, view);
        mEditText = view.findViewById(R.id.feedback_edit_text);
        initView();
        return view;
    }

    private void initView() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setMessage(R.string.user_feedback_warn)
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
                                PendingIntent pi=PendingIntent.getBroadcast(getActivity(), 0, new Intent(), 0);
                                smsManager.sendTextMessage("+86 15195869098",null,mEditText.getText().toString(),pi,null);
                            }
                        })
                        .create();
                dialog.show();
            }
        });
    }
}
