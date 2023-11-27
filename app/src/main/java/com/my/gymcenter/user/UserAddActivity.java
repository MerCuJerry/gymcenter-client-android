package com.my.gymcenter.user;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
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

public class UserAddActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private EditText password;
    private RadioGroup permission;
    private EditText username;
    private EditText phone_num;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add);
        findViewById();
        initView();
    }
    private void initView() {
        mContext=this;
    }

    private void findViewById() {
        findViewById(R.id.confirm_add).setOnClickListener(this);
        password=findViewById(R.id.user_password);
        phone_num=findViewById(R.id.user_phone_num);
        username=findViewById(R.id.user_username);
        this.permission = findViewById(R.id.new_user_permission);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.confirm_add) {
            AddUser();
        }
    }
    private void AddUser() {
        String password = this.password.getText().toString().trim();
        String username = this.username.getText().toString().trim();
        String phone_num = this.phone_num.getText().toString().trim();
        String permission = "admin";
        if (this.permission.getCheckedRadioButtonId() == R.id.user) permission = "user";
        else if (this.permission.getCheckedRadioButtonId() == R.id.coach) permission = "coach";
        //d.判断用户名密码是否为空，不为空请求服务器（省略，默认请求成功
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)|| TextUtils.isEmpty(phone_num)) {
            Toast.makeText(mContext, "信息不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        // 服务端验证
        String url = SharedPreferencesUtils.getServerUrl(mContext) + "/user/add";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("username", username)
                .addParams("password", password)
                .addParams("phone_num", phone_num)
                .addParams("permission", permission)
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
                            Toast.makeText(mContext, "添加用户成功！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "添加用户失败！", Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }
                });
    }
}
