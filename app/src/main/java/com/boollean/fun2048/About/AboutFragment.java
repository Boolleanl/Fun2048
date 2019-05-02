package com.boollean.fun2048.About;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.boollean.fun2048.Entity.User;
import com.boollean.fun2048.R;
import com.boollean.fun2048.Utils.HttpUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author Boollean
 */
public class AboutFragment extends Fragment {
    private User mUser = User.getInstance();
    @BindView(R.id.about_cancellation_button)
    Button mButton;

    public static AboutFragment newInstance() {
        AboutFragment fragment = new AboutFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        if(mUser.getName()==null){
            mButton.setVisibility(View.GONE);
        }else {
            mButton.setVisibility(View.VISIBLE);
        }

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setMessage(R.string.delete_user_warn)
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {}
                        })
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DeleteUser deleteUser = new DeleteUser();
                                deleteUser.execute();
                            }
                        })
                        .create();
                dialog.show();
            }
        });
    }

    /**
     * 向服务器请求删除该账号数据的线程类
     */
    private class DeleteUser extends AsyncTask<Void, Void, String> {
        private User user = User.getInstance();
        private SharedPreferences mPreferences;

        public DeleteUser() {}

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected String doInBackground(Void... voids) {
            String compeletedURL = HttpUtils.getURLWithParams(user.getName(),user.getPassword());
            String result = HttpUtils.sendHttpRequest(compeletedURL, new HttpUtils.HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    Log.i("aboutF", "成功:  "+response);
                    User.deleteThisUser();
                    deleteUserMode();
                }

                @Override
                public void onError(Exception e) {
                    Log.i("aboutF", "失败");
                }
            });
            return result;
        }

        /**
         * 将本地所持久化存储的账号信息清除
         */
        private void deleteUserMode(){
            mPreferences = getActivity().getApplicationContext().getSharedPreferences("SAVE_DATA", MODE_PRIVATE);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString("USER_NAME", null); //保存进SharedPreferences。
            editor.putString("USER_PASSWORD",null);
            editor.putInt("USER_GENDER", 0);
            editor.putString("USER_BITMAP_PATH", "");
            editor.putInt("BEST_SCORE_FOR_FOUR", 0);
            editor.putInt("BEST_SCORE_FOR_FIVE", 0);
            editor.putInt("BEST_SCORE_FOR_SIX", 0);
            editor.commit();
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("fail")){
                Toast.makeText(getActivity(), "失败", Toast.LENGTH_SHORT).show();
            }
            else {
                //获得解析者
                JsonParser jsonParser = new JsonParser();
                //获得根节点元素
                JsonElement root = jsonParser.parse(s);
                //根据文档判断根节点属于什么类型的Gson节点对象
                JsonObject object = root.getAsJsonObject();
                String msg = object.get("msg").getAsString();
                if(msg.equals("success")){
                    Toast.makeText(getActivity(), "成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
