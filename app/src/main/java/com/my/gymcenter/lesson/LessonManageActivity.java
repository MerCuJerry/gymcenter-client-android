package com.my.gymcenter.lesson;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.reflect.TypeToken;
import com.my.gymcenter.R;
import com.my.gymcenter.adapter.CourseListAdapter;
import com.my.gymcenter.entity.Course;
import com.my.gymcenter.entity.ResponseData;
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
import java.util.Objects;

import okhttp3.Call;

public class LessonManageActivity extends AppCompatActivity {
    private Context mContext;
    private String introPoint;
    private SwipeRecyclerView mRecyclerView;
    private Button mBtn;
    private List<Course> mList;
    CourseListAdapter adapter = new CourseListAdapter(this, null);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_lesson_with_menu);
        introPoint = getIntent().getStringExtra("introPoint");
        mRecyclerView=findViewById(R.id.listcourse2);
        mRecyclerView.setOnItemMenuClickListener(mItemMenuClickListener);
        mRecyclerView.setSwipeMenuCreator(mSwipeMenuCreator);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);
        mContext = this;
        mBtn=findViewById(R.id.LessonAdd);
        if(Objects.equals(introPoint, "coachManage")){
            mBtn.setVisibility(View.VISIBLE);
        }
        mBtn.setOnClickListener(v -> {
            Intent intent=new Intent(mContext, LessonAddActivity.class);
            intent.putExtra("coachId",getIntent().getIntExtra("coachId",0));
            startActivity(intent);
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
        introPoint = getIntent().getStringExtra("introPoint");
        reLoad();
    }

    private final OnItemMenuClickListener mItemMenuClickListener = new OnItemMenuClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge, int position) {
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection();
            int menuPosition = menuBridge.getPosition();
            if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
                if (menuPosition == 0) {
                    delete(mList.get(position));
                    reLoad();
                } else if (menuPosition == 1) {
                    Intent intent=new Intent(mContext, LessonModifyActivity.class);
                    intent.putExtra("courseId",mList.get(position).get_id());
                    intent.putExtra("courseName",mList.get(position).get_course_name());
                    intent.putExtra("courseDescribe",mList.get(position).get_course_describe());
                    intent.putExtra("coachId",mList.get(position).get_coach_id());
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
            if(Objects.equals(introPoint, "admin")) {
                SwipeMenuItem modifyItem = new SwipeMenuItem(mContext).setBackground(
                                R.drawable.selector_yellow)
                        .setText("修改")
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(modifyItem);
            }
        }
    };

    private void reLoad() {
        if(Objects.equals(introPoint, "admin")){
            String url = SharedPreferencesUtils.getServerUrl(mContext) + "/course/query";
            OkHttpUtils
                    .get()
                    .url(url)
                    .build()
                    .execute(new MyCallback());
        }else if (Objects.equals(introPoint, "coach") || Objects.equals(introPoint, "coachManage")) {
            String url = SharedPreferencesUtils.getServerUrl(mContext) + "/course/query/" + getIntent().getIntExtra("coachId",0);
            OkHttpUtils
                    .get()
                    .url(url)
                    .build()
                    .execute(new MyCallback());
        }
    }

    private void delete(Course course) {
        String url = SharedPreferencesUtils.getServerUrl(mContext) + "/course/delete";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("id", String.valueOf(course.get_id()))
                .build()
                .execute(new ResponseCallback<String>() {
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
                        if(response.get_result().equals("success")){
                            Toast.makeText(mContext, "删除项目成功！", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(mContext, "删除项目失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private class MyCallback extends ResponseCallback<ArrayList<Course>> {
        @Override
        public Type getType() {
            return new TypeToken<ResponseData<ArrayList<Course>>>(){}.getType();
        }
        @Override
        public void onResponse(ResponseData<ArrayList<Course>> response, int id) {
            try {
                mList = response.get_data();
                mRecyclerView.setItemViewCacheSize(mList.size());
                adapter.updateData(mList);
            } catch (Exception e) {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void onError(Call arg0, Exception arg1, int arg2) {
            Toast.makeText(mContext, "网络链接出错！", Toast.LENGTH_SHORT).show();
        }
    }
}
