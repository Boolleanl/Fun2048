package com.boollean.fun2048.User;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.boollean.fun2048.Entity.User;
import com.boollean.fun2048.R;
import com.boollean.fun2048.Utils.HttpUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_CANCELED;
import static android.content.Context.MODE_PRIVATE;


/**
 * 用户信息界面的Fragment。
 *
 * @author Boollean
 */
public class UserEditorFragment extends Fragment {

    private static final int CODE_GALLERY_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final String CROP_IMAGE_FILE_NAME = "avatar.jpg";
    private static SaveInformationTask mSaveInformationTask;
    private static boolean ImageIsChanged;
    @BindView(R.id.gender_radio_group)
    RadioGroup genderRadioGroup;
    @BindView(R.id.male_radio_button)
    RadioButton maleRadioButton;
    @BindView(R.id.female_radio_button)
    RadioButton femaleRadioButton;
    @BindView(R.id.secret_radio_button)
    RadioButton secretRadioButton;
    @BindView(R.id.user_name_edit_text)
    EditText userNameEditText;
    @BindView(R.id.user_password_edit_text)
    EditText userPasswordEditText;
    @BindView(R.id.user_avatar_image_view)
    ImageView avatarImageView;
    @BindView(R.id.user_information_complete_button)
    Button completeButton;
    private User mUser = User.getInstance();
    private Uri mUriPath;
    private String oldName;
    private String password;
    private int gender = mUser.getGender();
    private Bitmap bitmap;
    private SharedPreferences mPreferences;

    /**
     * 获取一个新的UserEditorFragment对象
     * @return 新的UserEditorFragment对象
     */
    public static UserEditorFragment newInstance() {
        return new UserEditorFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_editor, container, false);
        ButterKnife.bind(this, view);
        initUser();
        initView();
        return view;
    }

    /**
     * 初始化用户信息设置界面
     */
    private void initView() {
        switch (mUser.getGender()) {
            case 0:
                secretRadioButton.setChecked(true);
                maleRadioButton.setChecked(false);
                femaleRadioButton.setChecked(false);
                break;
            case 1:
                maleRadioButton.setChecked(true);
                femaleRadioButton.setChecked(false);
                secretRadioButton.setChecked(false);
                break;
            case 2:
                femaleRadioButton.setChecked(true);
                maleRadioButton.setChecked(false);
                secretRadioButton.setChecked(false);
                break;
        }
        genderRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
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
        });

        userNameEditText.setText(mUser.getName());

        if (mUser.getPassword() != null) {
            userPasswordEditText.setText(mUser.getPassword());
        }

        if (mUser.getAvatar() != null) {
            avatarImageView.setImageBitmap(mUser.getAvatar());
        }
        avatarImageView.setOnClickListener(v -> {
            Intent intentFromGallery = new Intent(Intent.ACTION_PICK, null);
            intentFromGallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
        });
    }

    @OnClick(R.id.user_information_complete_button)
    void Complete() {
        if(!HttpUtils.isNetworkAvailable(Objects.requireNonNull(getActivity()))){
            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setMessage("当前网络不可用,无法创建账户")
                    .setPositiveButton(R.string.ok, (dialogInterface, i) -> Objects.requireNonNull(getActivity()).finish())
                    .create();
            dialog.show();
        }

        String name = userNameEditText.getText().toString().trim();
        if (userPasswordEditText.getVisibility() == View.VISIBLE) {
            password = userPasswordEditText.getText().toString().trim();
        }
        if (isAvailableName(name) && isAvailablePassword(password)) {
            mSaveInformationTask = new SaveInformationTask(oldName, name, password, gender, bitmap, mUriPath, mPreferences, getActivity());
            mSaveInformationTask.execute();
            Objects.requireNonNull(getActivity()).finish();
        }
    }

    /**
     * 判断用户名是否可用
     *
     * @param s 需要判断的用户名
     * @return 用户名可用与否
     */
    private boolean isAvailableName(String s) {
        if (s.isEmpty()) {
            userNameEditText.setError("昵称不能为空");
            userNameEditText.setFocusable(true);
            userNameEditText.setFocusableInTouchMode(true);
            userNameEditText.requestFocus();
            return false;
        }
        if (s.length() > 12) {
            userNameEditText.setError("昵称不能大于12个字");
            userNameEditText.setFocusable(true);
            userNameEditText.setFocusableInTouchMode(true);
            userNameEditText.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * 判断密码是否可用
     *
     * @param s 需要判断的密码
     * @return 密码可用与否
     */
    private boolean isAvailablePassword(String s) {
        if (s.isEmpty()) {
            userPasswordEditText.setError("密码不能为空");
            userPasswordEditText.setFocusable(true);
            userPasswordEditText.setFocusableInTouchMode(true);
            userPasswordEditText.requestFocus();
            return false;
        }
        if (s.length() > 18) {
            userPasswordEditText.setError("密码不能大于18个字");
            userPasswordEditText.setFocusable(true);
            userPasswordEditText.setFocusableInTouchMode(true);
            userPasswordEditText.requestFocus();
            return false;
        }
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
                Intent intent = cropRawPhoto(data.getData());
                startActivityForResult(intent, CODE_RESULT_REQUEST);
                break;
            }
            case CODE_RESULT_REQUEST: {
                try {
                    bitmap = BitmapFactory.decodeStream(Objects.requireNonNull(getContext()).getContentResolver().openInputStream(mUriPath));
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
     * @param uri 原图片文件所在的位置
     * @return intent 包含裁剪后头像的一个新的Intent
     */
    private Intent cropRawPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        //输出的图片的宽度
        int output_X = 160;
        intent.putExtra("outputX", output_X);
        //输出的图片的高度
        int output_Y = 160;
        intent.putExtra("outputY", output_Y);
        intent.putExtra("return-data", true);

        String s = "Fun2048_" + System.currentTimeMillis() + CROP_IMAGE_FILE_NAME;
        String mExtStorDir = Environment.getExternalStorageDirectory().toString();
        File mFile = new File(mExtStorDir, s);

        mUriPath = Uri.parse("file://" + mFile.getAbsolutePath());

        //将裁剪好的图输出到所建文件中
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUriPath);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("return-data", false);
        ImageIsChanged = true;
        return intent;
    }

    /**
     * 更新首页侧滑菜单栏的用户头像
     *
     * @param intent 初始的Intent
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

    private void initUser() {
        mPreferences = Objects.requireNonNull(getActivity()).getApplicationContext().getSharedPreferences("SAVE_DATA", MODE_PRIVATE);
        mUser.setName(mPreferences.getString("USER_NAME", null));
        oldName = mUser.getName();
        mUser.setPassword(mPreferences.getString("USER_PASSWORD", null));
        mUser.setGender(mPreferences.getInt("USER_GENDER", 0));
        Uri uri = Uri.parse(mPreferences.getString("USER_BITMAP_PATH", ""));

        mUser.setBitmapPath(uri);
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(Objects.requireNonNull(getContext()).getContentResolver().openInputStream(uri));
            mUser.setAvatar(bitmap);
            ImageIsChanged = false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存用户信息的线程
     */
    private static class SaveInformationTask extends AsyncTask<User, Process, String> {

        private User mUser = User.getInstance();
        private String mOldName;
        private String mName;
        private String mPassword;
        private int mGender;
        private Bitmap mAvatar;
        private Uri mBitmapPath;
        private Context context;
        private SharedPreferences mPreferences;

        SaveInformationTask(String oldName, String name, String password, int gender, Bitmap bitmap, Uri bitmapPath, SharedPreferences preferences, FragmentActivity activity) {
            mOldName = oldName;
            mName = name;
            mPassword = password;
            mGender = gender;
            mAvatar = bitmap;
            mBitmapPath = bitmapPath;
            mPreferences = preferences;
            context = activity;
        }

        @Override
        protected String doInBackground(User... users) {
            String result = null;
            mUser.setName(mName);
            mUser.setPassword(mPassword);
            mUser.setGender(mGender);
            mUser.setAvatar(mAvatar);
            mUser.setBitmapPath(mBitmapPath);

            saveLastMode();

            try {
                //新建账号
                if (mOldName == null) {
                    result = HttpUtils.addUser(mUser);
                    //修改了头像
                    if (ImageIsChanged) {
                        String filePath = Uri.decode(mBitmapPath.getEncodedPath());
                        HttpUtils.upLoadImage(mName, filePath);
                        ImageIsChanged = false;
                    }
                }
                //没有修改用户名
                else if (mOldName.equals(mName)) {
                    result = HttpUtils.updateUserData(mUser);
                    //修改了头像
                    if (ImageIsChanged) {
                        String filePath = Uri.decode(mBitmapPath.getEncodedPath());
                        HttpUtils.upLoadImage(mName, filePath);
                        ImageIsChanged = false;
                    }
                }
                //修改了用户名
                else {
                    result = HttpUtils.updateUser(mOldName, mUser);
                    //修改了头像
                    if (ImageIsChanged) {
                        String filePath = Uri.decode(mBitmapPath.getEncodedPath());
                        HttpUtils.upLoadImage(mName, filePath);
                        ImageIsChanged = false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("fail")) {
                Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show();
            } else {
                //获得解析者
                JsonParser jsonParser = new JsonParser();
                //获得根节点元素
                JsonElement root = jsonParser.parse(s);
                //根据文档判断根节点属于什么类型的Gson节点对象
                JsonObject object = root.getAsJsonObject();
                String msg = object.get("msg").getAsString();
                if (msg.equals("success")) {
                    Toast.makeText(context, "成功", Toast.LENGTH_SHORT).show();
                }
            }
        }

        /**
         * 保存最近修改的用户数据
         */
        private void saveLastMode() {
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString("USER_NAME", mUser.getName()); //保存进SharedPreferences。
            editor.putString("USER_PASSWORD", mUser.getPassword());
            editor.putInt("USER_GENDER", mUser.getGender());
            if (mUser.getBitmapPath() != null && mUser.getAvatar() != null) {
                editor.putString("USER_BITMAP_PATH", mUser.getBitmapPath().toString());
            }
            editor.apply();
        }
    }
}
