package com.boollean.fun2048;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UserEditorFragment extends Fragment {

    private static final String TAG = "UserEditorFragment";

    @BindView(R.id.gender_radio_group)
    RadioGroup genderRadioGroup;
    @BindView(R.id.user_name_edit_text)
    EditText userNameEditText;
    @BindView(R.id.user_avatar_image_view)
    ImageView avatarImageView;
    @BindView(R.id.user_information_complete_button)
    Button completeButton;

    private String name = null;
    private int gender = 0;
    private ImageView imageView = null;

    private static SaveInformationTask mSaveInformationTask;

    public static UserEditorFragment newInstance() {
        return new UserEditorFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_editor, container, false);
        ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    private void initView(View view) {
        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonId = group.getCheckedRadioButtonId();
                switch (radioButtonId) {
                    case R.id.secret_radio_button:
                        gender = 0;
                        Snackbar.make(view, "0", Snackbar.LENGTH_SHORT).show();
                        break;

                    case R.id.male_radio_button:
                        gender = 1;
                        Snackbar.make(view, "1", Snackbar.LENGTH_SHORT).show();
                        break;

                    case R.id.female_radio_button:
                        gender = 2;
                        Snackbar.make(view, "2", Snackbar.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = userNameEditText.getText().toString();
                imageView = avatarImageView;
                mSaveInformationTask = new SaveInformationTask(name, gender, imageView, getActivity());
                mSaveInformationTask.execute();
                //Snackbar.make(v,"完成",Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private static class SaveInformationTask extends AsyncTask<User, Process, Void> {

        private User mUser = User.getInstance();
        private String mName;
        private int mGender;
        private ImageView mImageView;
        private Context context;

        public SaveInformationTask(String name, int gender, ImageView imageView, FragmentActivity v) {
            mName = name;
            mGender = gender;
            mImageView = imageView;
            context = v;
        }

        @Override
        protected Void doInBackground(User... users) {
            mUser.setName(mName);
            mUser.setGender(mGender);
            mUser.setAvatar(mImageView);
            //TODO 网络数据传输
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(context, mUser.getName(), Toast.LENGTH_SHORT).show();
        }
    }
}
