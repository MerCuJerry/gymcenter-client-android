package com.my.gymcenter.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.reflect.TypeToken;
import com.my.gymcenter.R;
import com.my.gymcenter.common.MainInterfaceActivity;
import com.my.gymcenter.common.MainInterfaceAdminActivity;
import com.my.gymcenter.entity.ResponseData;
import com.my.gymcenter.entity.User;
import com.my.gymcenter.utils.ResponseCallback;
import com.my.gymcenter.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.HttpUrl;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private EditText et_phone_num;
    private EditText et_password;
    Intent intent=new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext=this;
        findViewById(R.id.login_bt_register).setOnClickListener(this);
        findViewById(R.id.login_bt_login).setOnClickListener(this);
        et_phone_num=findViewById(R.id.login_et_phone_num);
        et_password=findViewById(R.id.login_et_password);
        User user = SharedPreferencesUtils.getUserInfo(mContext);//获取用户名密码
        et_phone_num.setText(String.valueOf(user.get_phone_num()));
        et_password.setText(user.get_password());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.login_bt_login) {
            login();
        } else if (id == R.id.login_bt_register) {
            intent.setClass(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        }
    }

    private void login() {
        String phone_num = et_phone_num.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(phone_num) || TextUtils.isEmpty(password)) {
            Toast.makeText(mContext, "不可留空", Toast.LENGTH_SHORT).show();
            return;
        }
        checkUser();
    }

    private void checkUser() {

        String url = SharedPreferencesUtils.getServerUrl(mContext) + "/login";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("phone_num", et_phone_num.getText().toString().trim())
                .addParams("password", et_password.getText().toString().trim())
                .build()
                .execute(new ResponseCallback<User>(){
                    @Override
                    public void onResponse(ResponseData<User> response, int id) {
                        User user = response.get_data();

                        Map<String,Class<?>> IntroPoint=new HashMap<>();
                        IntroPoint.put("admin", MainInterfaceAdminActivity.class);
                        IntroPoint.put("user", MainInterfaceActivity.class);
                        IntroPoint.put("coach", MainInterfaceActivity.class);
                        if (user.get_id() == 0) {
                            Toast.makeText(mContext, "账号或密码错误", Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            user.set_password(et_password.getText().toString().trim());
                            boolean result = SharedPreferencesUtils.saveUserInfo(mContext, user);
                            if (result) {
                                Toast.makeText(mContext, "登录成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "用户存储失败", Toast.LENGTH_SHORT).show();
                            }
                            intent.setClass(getApplicationContext(), Objects.requireNonNull(IntroPoint.get(user.get_permission())));
                            startActivity(intent);
                        }
                        finish();
                    }
                    @Override
                    public void onError(Call arg0, Exception arg1, int arg2) {
                        Toast.makeText(mContext, "网络连接出错"+ arg0 + arg1, Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public Type getType() {
                        return new TypeToken<ResponseData<User>>(){}.getType();
                    }
                });
    }
}
