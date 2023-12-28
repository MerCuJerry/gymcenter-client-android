package com.my.gymcenter.user;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.my.gymcenter.R;
import com.my.gymcenter.common.MainInterfaceActivity;
import com.my.gymcenter.entity.ResponseData;
import com.my.gymcenter.entity.User;
import com.my.gymcenter.utils.AppManager;
import com.my.gymcenter.utils.ResponseCallback;
import com.my.gymcenter.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.lang.reflect.Type;

import okhttp3.Call;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private EditText et_password;
    private EditText et_re_password;
    private EditText et_phone_num;
    Intent intent=new Intent();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViewById();
        initView();
    }

    private void initView() {
        mContext=this;
    }

    private void findViewById() {
        findViewById(R.id.reg_btn_register).setOnClickListener(this);
        et_phone_num = findViewById(R.id.reg_et_phone_num);
        et_password=findViewById(R.id.reg_et_password);
        et_re_password = findViewById(R.id.reg_et_re_password);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.reg_btn_register) {
            register();
        }
    }
    private void register() {
        String phone_num = et_phone_num.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String re_password = et_re_password.getText().toString().trim();
        if (TextUtils.isEmpty(phone_num) || TextUtils.isEmpty(password) || TextUtils.isEmpty(re_password)) {
            Toast.makeText(mContext, "信息不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        // 判断两次密码
        if (!password.equals(re_password)) {
            Toast.makeText(mContext, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        // 服务端验证
        String url = SharedPreferencesUtils.getServerUrl(mContext) + "/register";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("phone_num", phone_num)
                .addParams("password", password)
                .build()
                .execute(new ResponseCallback<String>(){
                    @Override
                    public Type getType() {
                        return new TypeToken<ResponseData<String>>(){}.getType();
                    }
                    @Override
                    public void onResponse(ResponseData<String> response, int id) {
                        try {
                            if (response.get_result().equals("success")) {
                                Toast.makeText(mContext, "注册成功，请登录！", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "注册失败！", Toast.LENGTH_SHORT).show();
                            }
                            intent.setClass(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            AppManager.getInstance().killAllActivity();
                        } catch (JsonSyntaxException e) {
                            Toast.makeText(mContext, response.get_result()+"but sth went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(Call arg0, Exception arg1, int arg2) {
                        Toast.makeText(mContext, "网络链接出错！", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
