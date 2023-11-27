package com.my.gymcenter.lesson;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.reflect.TypeToken;
import com.my.gymcenter.R;
import com.my.gymcenter.entity.ResponseData;
import com.my.gymcenter.utils.ResponseCallback;
import com.my.gymcenter.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.lang.reflect.Type;

import okhttp3.Call;

public class LessonAddActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private EditText lessonName;
    private EditText lessonContent;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_add);
        findViewById();
        mContext=this;
    }
    private void findViewById() {
        findViewById(R.id.ConfirmAdd).setOnClickListener(this);
        lessonName =findViewById(R.id.lesson_name);
        lessonContent = findViewById(R.id.lesson_content);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ConfirmAdd) {
            register();
        }
    }
    private void register() {
        String lesson_name = lessonName.getText().toString().trim();
        String lesson_content = lessonContent.getText().toString().trim();
        if (TextUtils.isEmpty(lesson_name) || TextUtils.isEmpty(lesson_content) ) {
            Toast.makeText(mContext, "信息不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = SharedPreferencesUtils.getServerUrl(mContext) + "/course/add";
            OkHttpUtils
                    .post()
                    .url(url)
                    .addParams("course_name", lesson_name)
                    .addParams("course_describe", lesson_content)
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
                                Toast.makeText(mContext, "添加项目成功！", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(mContext, "添加项目失败！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
    }
}
