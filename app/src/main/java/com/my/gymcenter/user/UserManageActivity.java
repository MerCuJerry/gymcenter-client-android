package com.my.gymcenter.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.reflect.TypeToken;
import com.my.gymcenter.R;
import com.my.gymcenter.adapter.UserListAdapter;
import com.my.gymcenter.entity.ResponseData;
import com.my.gymcenter.entity.User;
import com.my.gymcenter.utils.ResponseCallback;
import com.my.gymcenter.utils.SharedPreferencesUtils;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class UserManageActivity extends AppCompatActivity{
    private Context mContext;
    private List<User> mList;
    private SwipeRecyclerView mRecyclerView;
    UserListAdapter adapter = new UserListAdapter(this, null);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_user_with_menu);
        mRecyclerView = findViewById(R.id.listuser2);
        mRecyclerView.setOnItemMenuClickListener(mItemMenuClickListener);
        mRecyclerView.setSwipeMenuCreator(mSwipeMenuCreator);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);
        mContext = this;

    }

    @Override
    public void onResume()
    {
        super.onResume();
        reLoad();
    }

    private final OnItemMenuClickListener mItemMenuClickListener = new OnItemMenuClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge, int position) {
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
                if (menuPosition == 0) {
                    delete(mList.get(position).get_id());
                } else if (menuPosition == 1) {
                    Intent intent=new Intent(mContext, UserModifyActivity.class);
                    intent.putExtra("user_id",mList.get(position).get_id());
                    startActivity(intent);
                }
            }
        }
    };

    private final SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int position) {
            int width = getResources().getDimensionPixelSize((R.dimen.dp_70));
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            SwipeMenuItem deleteItem = new SwipeMenuItem(mContext).setBackground(
                            R.drawable.selector_red)
                    .setText("删除")
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(deleteItem);

            SwipeMenuItem modifyItem = new SwipeMenuItem(mContext).setBackground(
                            R.drawable.selector_yellow)
                    .setText("修改")
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(modifyItem);
        }
    };

    private void reLoad() {
        String url = SharedPreferencesUtils.getServerUrl(mContext) + "/user/query";
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new ResponseCallback<ArrayList<User>>() {
                    @Override
                    public Type getType() {
                        return new TypeToken<ResponseData<ArrayList<User>>>(){}.getType();
                    }
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(mContext, "网络链接出错！", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(ResponseData<ArrayList<User>> response, int id) {
                        try {
                            mList = response.get_data();
                            mRecyclerView.setItemViewCacheSize(mList.size());
                            adapter.updateData(mList);
                        } catch (Exception e) {
                            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void delete(int user_id) {
        if (user_id == 1) {
            Toast.makeText(mContext, "不可以删除admin管理员", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = SharedPreferencesUtils.getServerUrl(mContext) + "/user/delete";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("id", String.valueOf(user_id))
                .build()
                .execute(new ResponseCallback<String>(){
                    @Override
                    public Type getType() {
                        return new TypeToken<ResponseData<String>>(){}.getType();
                    }
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(mContext, "网络链接出错！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(ResponseData<String> response, int id) {
                        if (response.get_result().equals("success")) {
                            Toast.makeText(mContext, "删除用户成功！", Toast.LENGTH_SHORT).show();
                            reLoad();
                        } else {
                            Toast.makeText(mContext, response.get_result(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
