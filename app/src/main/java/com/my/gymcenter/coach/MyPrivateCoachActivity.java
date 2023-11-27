package com.my.gymcenter.coach;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import com.my.gymcenter.R;
import com.my.gymcenter.adapter.UserListAdapter;
import com.my.gymcenter.entity.ResponseData;
import com.my.gymcenter.entity.User;
import com.my.gymcenter.utils.ResponseCallback;
import com.my.gymcenter.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class MyPrivateCoachActivity extends AppCompatActivity  {
    private Context mContext;
    private RecyclerView mRecyclerView;
    private final UserListAdapter adapter = new UserListAdapter(this,null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_user);
        mRecyclerView = findViewById(R.id.listuser);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);
        mContext=this;
    }

    @Override
    public void onResume(){
        super.onResume();
        reLoadNews();
    }
    private void reLoadNews() {
        String url = SharedPreferencesUtils.getServerUrl(mContext) + "/depend/query/coach";
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
                        List<User> mList = response.get_data();
                        mRecyclerView.setItemViewCacheSize(mList.size());
                        adapter.updateData(mList);
                    }
                });
    }
}
