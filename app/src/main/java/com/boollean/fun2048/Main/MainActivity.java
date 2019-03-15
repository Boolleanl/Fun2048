package com.boollean.fun2048.Main;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.boollean.fun2048.FeedBack.FeedBackActivity;
import com.boollean.fun2048.Game.GameActivity;
import com.boollean.fun2048.Message.MessageActivity;
import com.boollean.fun2048.R;
import com.boollean.fun2048.Rank.RankActivity;
import com.boollean.fun2048.User.MyPhotoFactory;
import com.boollean.fun2048.User.User;
import com.boollean.fun2048.User.UserEditorActivity;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileNotFoundException;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 主界面的Activity。
 * Created by Boollean on 2019/2/27.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    public static boolean volumeSwitch = true;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.new_game_button)
    Button newGameButton;
    @BindView(R.id.continue_game_button)
    Button continueButton;
    @BindView(R.id.score_button)
    Button scoreButton;
    @BindView(R.id.quit_button)
    Button quitButton;
    private MenuItem volumeOnItem;
    private MenuItem volumeOffItem;

    private User mUser = User.getInstance();    //获取用户的单一实例
    private View headerView;    //侧滑菜单头部
    private Button signInButton;
    private ImageView avatarImageView;      //用以显示用户头像
    private TextView nameTextView;      //用以显示用户名
    private ImageView genderView;

    private SharedPreferences mPreferences;
    private int whichGame;  //游戏模式的全局变量
    private int bestScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        initUser();
        initView();
    }

    private void initView() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        headerView = navigationView.getHeaderView(0);
        signInButton = headerView.findViewById(R.id.sign_in_button);
        avatarImageView = headerView.findViewById(R.id.round_avatar_image_view);
        nameTextView = headerView.findViewById(R.id.name_text_view);
        genderView = headerView.findViewById(R.id.main_gender_image_view);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = UserEditorActivity.newIntent(getApplicationContext());
                startActivity(intent);
            }
        });

        if (mUser.getAvatar() == null && mUser.getName() == null) {
            avatarImageView.setVisibility(View.GONE);
            nameTextView.setVisibility(View.GONE);
            signInButton.setVisibility(View.VISIBLE);
        }
        if (mUser.getAvatar() != null) {
            signInButton.setVisibility(View.GONE);
            nameTextView.setVisibility(View.VISIBLE);
            avatarImageView.setVisibility(View.VISIBLE);
            avatarImageView.setImageBitmap(MyPhotoFactory.toRoundBitmap(mUser.getAvatar()));
        }
        if (mUser.getName() != null) {
            signInButton.setVisibility(View.GONE);
            avatarImageView.setVisibility(View.VISIBLE);
            nameTextView.setVisibility(View.VISIBLE);
            nameTextView.setText(mUser.getName());
        }
        if (mUser.getGender() == 1) {
            genderView.setImageResource(R.mipmap.ic_male);
        } else if (mUser.getGender() == 2) {
            genderView.setImageResource(R.mipmap.ic_female);
        } else {
            genderView.setImageResource(0);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mUser.getAvatar() == null && mUser.getName() == null) {
            avatarImageView.setVisibility(View.GONE);
            nameTextView.setVisibility(View.GONE);
            signInButton.setVisibility(View.VISIBLE);
        }
        if (mUser.getAvatar() != null) {
            signInButton.setVisibility(View.GONE);
            avatarImageView.setVisibility(View.VISIBLE);
            avatarImageView.setImageBitmap(MyPhotoFactory.toRoundBitmap(mUser.getAvatar()));
        }
        if (mUser.getName() != null) {
            signInButton.setVisibility(View.GONE);
            nameTextView.setVisibility(View.VISIBLE);
            nameTextView.setText(mUser.getName());
        }
        if (mUser.getGender() == 1) {
            genderView.setImageResource(R.mipmap.ic_male);
        } else if (mUser.getGender() == 2) {
            genderView.setImageResource(R.mipmap.ic_female);
        } else {
            genderView.setImageResource(0);
        }
    }

    @OnClick(R.id.new_game_button)
    void newGame() {
        String[] items = new String[]{"4X4", "5X5", "6X6"};
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("选择游戏模式")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        switch (i) {
                            case 0:
                                whichGame = 4;
                                bestScore = getBestScore(whichGame);
                                Intent intent4 = GameActivity.newIntent(MainActivity.this, whichGame, bestScore);
                                startActivity(intent4);
                                break;
                            case 1:
                                whichGame = 5;
                                bestScore = getBestScore(whichGame);
                                Intent intent5 = GameActivity.newIntent(MainActivity.this, whichGame, bestScore);
                                startActivity(intent5);
                                break;
                            case 2:
                                whichGame = 6;
                                bestScore = getBestScore(whichGame);
                                Intent intent6 = GameActivity.newIntent(MainActivity.this, whichGame, bestScore);
                                startActivity(intent6);
                                break;
                        }
                    }
                }).create();
        dialog.show();
    }

    @OnClick(R.id.continue_game_button)
    void continueGame() {
        whichGame = getLastMode();
        int[][] n = getLastNumbers();
        Intent intent = GameActivity.newIntent(this, whichGame, n);
        startActivity(intent);
    }

    /**
     * 获取上次退出时游玩的游戏模式
     */
    private int getLastMode() {
        mPreferences = getApplicationContext().getSharedPreferences("SAVE_DATA", MODE_PRIVATE);
        int which = mPreferences.getInt("LAST_MODE", 0);
        return which;
    }

    /**
     * 获取上次最后一局的数组，解析JSON字段来获得其中的值，放进二维数组中。
     *
     * @return 上次最后一步的二维数组。
     */
    private int[][] getLastNumbers() {
        mPreferences = getApplicationContext().getSharedPreferences("SAVE_DATA", MODE_PRIVATE);
        int[][] n = new int[whichGame][whichGame];
        try {
            JSONArray jsonArray = new JSONArray(mPreferences.getString("LAST_NUMBERS", ""));
            Log.i(TAG, jsonArray.toString());
            int a = 0;
            for (int i = 0; i < whichGame; i++) {
                for (int j = 0; j < whichGame; j++) {
                    n[i][j] = jsonArray.getInt(a);
                    a++;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return n;
    }

    private void initUser() {
        mPreferences = getApplicationContext().getSharedPreferences("SAVE_DATA", MODE_PRIVATE);
        mUser.setName(mPreferences.getString("USER_NAME", null));
        mUser.setPassword(mPreferences.getString("USER_PASSWORD", null));
        mUser.setGender(mPreferences.getInt("USER_GENDER", 0));
        Uri uri = Uri.parse(mPreferences.getString("USER_BITMAP_PATH", ""));
        mUser.setBitmapPath(uri);
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            mUser.setAvatar(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Log.i(TAG, mUser.getName() + mUser.getPassword());
    }

    @OnClick(R.id.score_button)
    void score() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(
                        "4X4模式最高分:" + String.valueOf(getBestScore(4))
                                + "\n" + "5X5模式最高分:" + String.valueOf(getBestScore(5))
                                + "\n" + "6X6模式最高分:" + String.valueOf(getBestScore(6)))
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .create();
        dialog.show();
    }

    /**
     * 获取最高分的记录。
     *
     * @return 目前所有游戏的最高分数。
     */
    private int getBestScore(int which) {
        mPreferences = getApplicationContext().getSharedPreferences("SAVE_DATA", MODE_PRIVATE);
        int s = 0;
        try {
            if (which == 4) {
                s = mPreferences.getInt("BEST_SCORE_FOR_FOUR", 0);
            } else if (which == 5) {
                s = mPreferences.getInt("BEST_SCORE_FOR_FIVE", 0);
            } else if (which == 6) {
                s = mPreferences.getInt("BEST_SCORE_FOR_SIX", 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    @OnClick(R.id.quit_button)
    void quit() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("确定退出？")
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.exit(0);
                    }
                })
                .create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        volumeOnItem = menu.findItem(R.id.action_volume_on);
        volumeOffItem = menu.findItem(R.id.action_volume_off);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_volume_on) {
            volumeSwitch = false;
            volumeOnItem.setVisible(false);
            volumeOffItem.setVisible(true);
            return true;
        }
        if (id == R.id.action_volume_off) {
            volumeSwitch = true;
            volumeOffItem.setVisible(false);
            volumeOnItem.setVisible(true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_user) {
            Intent intent = UserEditorActivity.newIntent(this);
            startActivity(intent);
        } else if (id == R.id.nav_message) {
            Intent intent = MessageActivity.newIntent(this);
            startActivity(intent);
        } else if (id == R.id.nav_rank) {
            Intent intent = RankActivity.newIntent(this);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_feedback) {
            Intent intent = FeedBackActivity.newIntent(this);
            startActivity(intent);
        } else if (id == R.id.nav_send) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
