package com.boollean.fun2048;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_CANCELED;

public class UserEditorFragment extends Fragment {

    private static final String TAG = "UserEditorFragment";

    private static final int CODE_GALLERY_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final String CROP_IMAGE_FILE_NAME = "avatar.jpg";
    private static int output_X = 160;
    private static int output_Y = 160;
    private static SaveInformationTask mSaveInformationTask;
    @BindView(R.id.gender_radio_group)
    RadioGroup genderRadioGroup;
    @BindView(R.id.user_name_edit_text)
    EditText userNameEditText;
    @BindView(R.id.user_avatar_image_view)
    ImageView avatarImageView;
    @BindView(R.id.user_information_complete_button)
    Button completeButton;
    private String mExtStorDir;
    private Uri mUriPath;
    private String name = null;
    private int gender = 0;
    private Bitmap bitmap = null;
    private User mUser = User.getInstance();

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

    /**
     * 初始化用户信息设置界面
     *
     * @param view
     */
    private void initView(View view) {
        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonId = group.getCheckedRadioButtonId();
                switch (radioButtonId) {
                    case R.id.secret_radio_button:
                        gender = 0;
                        break;

                    case R.id.male_radio_button:
                        gender = 1;
                        break;

                    case R.id.female_radio_button:
                        gender = 2;
                        break;
                }
            }
        });

        if (mUser.getAvatar() != null) {
            avatarImageView.setImageBitmap(mUser.getAvatar());
        }
        avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExtStorDir = Environment.getExternalStorageDirectory().toString();
                Intent intentFromGallery = new Intent(Intent.ACTION_PICK, null);
                intentFromGallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
            }
        });

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = userNameEditText.getText().toString().trim();
                if (isAvailableName(name)) {
                    mSaveInformationTask = new SaveInformationTask(name, gender, bitmap, getActivity());
                    mSaveInformationTask.execute();
                }
            }
        });
    }

    /**
     * 判断用户名是否可用
     *
     * @param s 需要判断的用户名
     * @return
     */
    private boolean isAvailableName(String s) {
        if (s.isEmpty()) {
            userNameEditText.setError("昵称不能为空");
            userNameEditText.setFocusable(true);
            userNameEditText.setFocusableInTouchMode(true);
            userNameEditText.requestFocus();
            return false;
        }
        if (s.length() > 14) {
            userNameEditText.setError("昵称不能大于14个字");
            userNameEditText.setFocusable(true);
            userNameEditText.setFocusableInTouchMode(true);
            userNameEditText.requestFocus();
            return false;
        }
        //TODO 加入用户名已存在的判断
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getActivity(), "取消", Toast.LENGTH_LONG).show();
            return;
        }

        switch (requestCode) {
            case CODE_GALLERY_REQUEST: {
                cropRawPhoto(data.getData());
                break;
            }
            case CODE_RESULT_REQUEST: {
                try {
                    bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(mUriPath));
                    setImageToHeadView(data, bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 选择用户头像
     *
     * @param uri
     */
    private void cropRawPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("return-data", true);

        String mLinshi = System.currentTimeMillis() + CROP_IMAGE_FILE_NAME;
        File mFile = new File(mExtStorDir, mLinshi);
        mUriPath = Uri.parse("file://" + mFile.getAbsolutePath());

        //将裁剪好的图输出到所建文件中
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUriPath);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("return-data", false);
        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    /**
     * 更新首页侧滑菜单栏的用户头像
     *
     * @param intent
     * @param bitmap 头像文件
     */
    private void setImageToHeadView(Intent intent, Bitmap bitmap) {
        try {
            if (intent != null) {
                avatarImageView.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static boolean hasSdcard() {
//        String state = Environment.getExternalStorageState();
//        if (state.equals(Environment.MEDIA_MOUNTED)) {
//            // 有存储的SDCard
//            return true;
//        } else {
//            return false;
//        }
//    }

    /**
     * 保存用户信息的线程
     */
    private static class SaveInformationTask extends AsyncTask<User, Process, Void> {

        private User mUser = User.getInstance();
        private String mName;
        private int mGender;
        private Bitmap mAvatar;
        private Context context;

        public SaveInformationTask(String name, int gender, Bitmap bitmap, FragmentActivity activity) {
            mName = name;
            mGender = gender;
            mAvatar = bitmap;
            context = activity;
        }

        @Override
        protected Void doInBackground(User... users) {
            mUser.setName(mName);
            mUser.setGender(mGender);
            mUser.setAvatar(mAvatar);
            //TODO 网络数据传输
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(context, mUser.getName(), Toast.LENGTH_SHORT).show();
        }
    }
}
