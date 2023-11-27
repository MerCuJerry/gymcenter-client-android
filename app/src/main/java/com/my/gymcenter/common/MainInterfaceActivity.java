package com.my.gymcenter.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.my.gymcenter.R;
import com.my.gymcenter.lesson.ChooseLessonFragment;
import com.my.gymcenter.user.LoginActivity;
import com.my.gymcenter.utils.AppManager;
import com.my.gymcenter.utils.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Objects;

public class MainInterfaceActivity extends AppCompatActivity {
    private BottomNavigationView navView;
    private Context mContext;
    private long mExitTime;
    private ViewPager2 viewPager;
    private static int NUM_PAGES=0;
    private static final HashMap<Integer,Fragment> SwitchFragment = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_interface);
        viewPager=findViewById(R.id.Interface_Fragment);
        mContext=this;
        FragmentStateAdapter pagerAdapter = new ScreenSlidePagerAdapter(this);
        navView = findViewById(R.id.nav_view);
        SwitchFragment.put(0,new MainPageListFragment());
        if(Objects.equals(SharedPreferencesUtils.getUserInfo(mContext).get_permission(), "user")){
            NUM_PAGES=3;
            navView.inflateMenu(R.menu.navi_user_menu);
            SwitchFragment.put(1,new ChooseLessonFragment());
            SwitchFragment.put(2,new MyFragment());
        }else if(Objects.equals(SharedPreferencesUtils.getUserInfo(mContext).get_permission(), "coach")){
            NUM_PAGES=2;
            navView.inflateMenu(R.menu.navi_coach_menu);
            SwitchFragment.put(1,new MyFragment());
        }
        viewPager.setAdapter(pagerAdapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                navView.getMenu().getItem(position).setChecked(true);
            }
        });
        navView.setOnItemSelectedListener(item -> {
            if(Objects.equals(SharedPreferencesUtils.getUserInfo(mContext).get_permission(), "user")){
                if(item.getItemId() == R.id.menu_lesson){
                    viewPager.setCurrentItem(0);
                }else if(item.getItemId() == R.id.menu_my_lesson){
                    viewPager.setCurrentItem(1);
                }else if(item.getItemId() == R.id.menu_my){
                    viewPager.setCurrentItem(2);
                }
            }else if(Objects.equals(SharedPreferencesUtils.getUserInfo(mContext).get_permission(), "coach")){
                if(item.getItemId() == R.id.menu_my_student){
                    viewPager.setCurrentItem(0);
                    return true;
                }else if(item.getItemId() == R.id.menu_coach_my){
                    viewPager.setCurrentItem(1);
                    return true;
                }
            }
            return true;
        });
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
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (viewPager.getCurrentItem() == 0) {
                if ((System.currentTimeMillis() - mExitTime) > 1000) {
                    Toast.makeText(mContext, "再按一次退出APP", Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();
                    return true;
                } else {
                    AppManager.getInstance().killAllActivity();
                    AppManager.getInstance().AppExit(mContext);
                }
            } else {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    private static class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return Objects.requireNonNull(SwitchFragment.get(position));
        }
        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }
}
