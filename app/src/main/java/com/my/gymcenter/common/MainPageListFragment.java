package com.my.gymcenter.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import com.my.gymcenter.R;
import com.my.gymcenter.adapter.CourseListAdapter;
import com.my.gymcenter.entity.Course;
import com.my.gymcenter.entity.Notice;
import com.my.gymcenter.entity.ResponseData;
import com.my.gymcenter.entity.User;
import com.my.gymcenter.lesson.LessonsStudentActivity;
import com.my.gymcenter.utils.ResponseCallback;
import com.my.gymcenter.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;

public class MainPageListFragment extends Fragment {
    private Context mContext;
    private CourseListAdapter adapter;
    private List<Course> mList;
    private RecyclerView mRecyclerView;
    private TextView content;
    Intent intent=new Intent();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.list_lesson_with_notice,container,false);
        mContext=getActivity();
        content=v.findViewById(R.id.add_news_et_share_content);
        mRecyclerView= v.findViewById(R.id.listcourse);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        adapter = new CourseListAdapter(getActivity(), null);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((parent,position) -> {
            if(Objects.equals(SharedPreferencesUtils.getUserInfo(mContext).get_permission(), "user")){
                addCourse(mList.get(position).get_id());
            }else if(Objects.equals(SharedPreferencesUtils.getUserInfo(mContext).get_permission(), "coach")){
                intent.setClass(mContext, LessonsStudentActivity.class);
                intent.putExtra("CourseId",mList.get(position).get_id());
                startActivity(intent);
            }
        });
        return v;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getNotice();
        reLoadNews();
    }
    private void getNotice() {
        String url;
        url = SharedPreferencesUtils.getServerUrl(mContext) + "/notice/query";
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new ResponseCallback<Notice>() {
                    @Override
                    public Type getType() {
                        return new TypeToken<ResponseData<Notice>>(){}.getType();
                    }
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(mContext, "网络连接出错", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(ResponseData<Notice> response, int id) {
                        content.setText(response.get_data().get_notice_content());
                    }
                });
    }
    private void reLoadNews() {
        if(Objects.equals(SharedPreferencesUtils.getUserInfo(mContext).get_permission(), "user")){
            String url = SharedPreferencesUtils.getServerUrl(mContext) + "/course/query";
            OkHttpUtils
                    .get()
                    .url(url)
                    .build()
                    .execute(new MyStringCallback());
        }else if(Objects.equals(SharedPreferencesUtils.getUserInfo(mContext).get_permission(), "coach")){
            String url = SharedPreferencesUtils.getServerUrl(mContext) + "/course/query/"+SharedPreferencesUtils.getUserInfo(mContext).get_id();
            OkHttpUtils
                    .get()
                    .url(url)
                    .build()
                    .execute(new MyStringCallback());
        }
    }
    private class MyStringCallback extends ResponseCallback<ArrayList<Course>> {
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
            Toast.makeText(mContext, "网络连接出错", Toast.LENGTH_SHORT).show();
        }
    }

    private void addCourse(int id){
        String url = SharedPreferencesUtils.getServerUrl(mContext) + "/depend/add";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("user_id", String.valueOf(SharedPreferencesUtils.getUserInfo(mContext).get_id()))
                .addParams("course_id", String.valueOf(id))
                .build()
                .execute(new ResponseCallback<String>() {
                    @Override
                    public Type getType() {
                        return new TypeToken<ResponseData<User>>(){}.getType();
                    }
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(mContext, "网络连接出错", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(ResponseData<String> response, int id) {
                        if(response.get_result().equals("success")){
                            Toast.makeText(mContext, "添加课程成功！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "添加课程失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
