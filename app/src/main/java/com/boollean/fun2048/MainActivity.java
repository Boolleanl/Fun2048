package com.boollean.fun2048;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

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
    private NumberItem mNumberItem = NumberItem.getInstance();  //获取单例。
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @OnClick(R.id.new_game_button)
    void newGame() {
        Intent intent = GameActivity.newIntent(this);
        startActivity(intent);
    }

    @OnClick(R.id.continue_game_button)
    void continueGame() {
        int[][] n = getLastNumbers();
        mNumberItem.setNumbers(n);
        Intent intent = GameActivity.newIntent(this, n);
        startActivity(intent);
    }

    /**
     * 获取上次最后一局的数组，解析JSON字段来获得其中的值，放进二维数组中。
     *
     * @return 上次最后一步的二维数组。
     */
    private int[][] getLastNumbers() {
        mPreferences = getApplicationContext().getSharedPreferences("SAVE_LAST_NUMBERS", MODE_PRIVATE);
        int[][] n = new int[4][4];
        try {
            JSONArray jsonArray = new JSONArray(mPreferences.getString("LAST_NUMBERS", ""));
            Log.i(TAG, jsonArray.toString());
            int a = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    n[i][j] = jsonArray.getInt(a);
                    a++;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return n;
    }

    @OnClick(R.id.score_button)
    void score() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("当前最高分:" + String.valueOf(getBestScore()))
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
    private int getBestScore() {
        mPreferences = getApplicationContext().getSharedPreferences("SAVE_BEST_SCORE", MODE_PRIVATE);
        int s = 0;
        try {
            s = mPreferences.getInt("BEST_SCORE", 0);
            Log.i(TAG, String.valueOf(s));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    @OnClick(R.id.quit_button)
    void quit() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("确定退出？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
