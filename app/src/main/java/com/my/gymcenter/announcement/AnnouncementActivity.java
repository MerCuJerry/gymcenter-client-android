package com.my.gymcenter.announcement;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.reflect.TypeToken;
import com.my.gymcenter.R;
import com.my.gymcenter.entity.Notice;
import com.my.gymcenter.entity.ResponseData;
import com.my.gymcenter.utils.ResponseCallback;
import com.my.gymcenter.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.lang.reflect.Type;

import okhttp3.Call;

public class AnnouncementActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private EditText content;

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_notice_publish);
        mContext = this;
        this.content = findViewById(R.id.add_news_et_share_content);
        Button release = findViewById(R.id.add_news_btn_release);
        release.setOnClickListener(this);
        found();
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_news_btn_release) {
            checkInfo();
        }
    }
    private void checkInfo() {
        String contentStr = content.getText().toString();
        if (TextUtils.isEmpty(contentStr)) {
            Toast.makeText(mContext, "请输入通知内容", Toast.LENGTH_SHORT).show();
            content.requestFocus();
            return;
        }
        releaseNews();
    }

    private void found() {
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
                        content.setHint(response.get_data().get_notice_content());
                    }
                });
    }
    private void releaseNews() {
        String contentStr = content.getText().toString();
        String url = SharedPreferencesUtils.getServerUrl(mContext) + "/notice/add";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("notice_content", contentStr)
                .build()
                .execute(new ResponseCallback<String>() {
                    @Override
                    public Type getType() {
                        return new TypeToken<ResponseData<String>>(){}.getType();
                    }
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(mContext, "网络连接出错", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(ResponseData<String> response, int id) {
                        if (response.get_result().equals("success")) {
                            Toast.makeText(mContext, "发布成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(mContext, "请稍后再试", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
