package com.my.gymcenter.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.my.gymcenter.R;
import com.my.gymcenter.announcement.AnnouncementActivity;
import com.my.gymcenter.coach.CoachManageActivity;
import com.my.gymcenter.lesson.LessonManageActivity;
import com.my.gymcenter.user.LoginActivity;
import com.my.gymcenter.user.UserAddActivity;
import com.my.gymcenter.user.UserManageActivity;
import com.my.gymcenter.utils.AppManager;
import com.my.gymcenter.utils.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainInterfaceAdminActivity extends AppCompatActivity implements View.OnClickListener {
    private long mExitTime;
    private Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_interface_admin);
        findViewById();
        mContext=this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_logout) {
            AppManager.getInstance().killAllActivity();
            startActivity(new Intent(mContext, LoginActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void findViewById() {
        findViewById(R.id.LessonManage).setOnClickListener(this);
        findViewById(R.id.UserManage).setOnClickListener(this);
        findViewById(R.id.UserAdd).setOnClickListener(this);
        findViewById(R.id.releaseAnnouncement).setOnClickListener(this);
        findViewById(R.id.CoachManage).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Map<Integer,Class<?>> IntroPoint=new HashMap<>();
        IntroPoint.put(R.id.LessonManage, LessonManageActivity.class);
        IntroPoint.put(R.id.UserManage, UserManageActivity.class);
        IntroPoint.put(R.id.UserAdd, UserAddActivity.class);
        IntroPoint.put(R.id.CoachManage, CoachManageActivity.class);
        IntroPoint.put(R.id.releaseAnnouncement, AnnouncementActivity.class);
        Intent intent=new Intent().setClass(mContext, Objects.requireNonNull(IntroPoint.get(v.getId())));
        intent.putExtra("introPoint", SharedPreferencesUtils.getUserInfo(mContext).get_permission());
        startActivity(intent);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 800) {
                Toast.makeText(mContext, "再按一次退出APP", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
                return true;
            } else {
                AppManager.getInstance().killAllActivity();
                AppManager.getInstance().AppExit(this);
            }
        }
        return super.onKeyDown(keyCode, event);
    }


}
