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

public class LessonModifyActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private EditText newLessonName;
    private EditText newLessonContent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_modify);
        findViewById();
        fillData();
        mContext=this;
    }

    private void findViewById() {
        findViewById(R.id.ConfirmModify).setOnClickListener(this);
        newLessonName =findViewById(R.id.new_lesson_name);
        newLessonContent = findViewById(R.id.new_lesson_content);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ConfirmModify) {
            update();
        }
    }
    private void fillData(){
        newLessonName.setText(getIntent().getStringExtra("courseName"));
        newLessonContent.setText(getIntent().getStringExtra("courseDescribe"));
    }
    private void update() {
        if (TextUtils.isEmpty(newLessonName.getText().toString().trim()) ||
                TextUtils.isEmpty(newLessonContent.getText().toString().trim()) ) {
            Toast.makeText(mContext, "新的信息不能留空", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = SharedPreferencesUtils.getServerUrl(mContext) + "/course/change";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("id", String.valueOf(getIntent().getIntExtra("courseId",0)))
                .addParams("course_name", newLessonName.getText().toString().trim())
                .addParams("course_describe", newLessonContent.getText().toString().trim())
                .addParams("coach_id", String.valueOf(getIntent().getIntExtra("coachId",0)))
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
                        if(response.get_result().equals("success")) {
                            Toast.makeText(mContext, "成功！", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(mContext, "失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
