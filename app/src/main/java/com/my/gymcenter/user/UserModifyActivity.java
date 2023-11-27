package com.my.gymcenter.user;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.reflect.TypeToken;
import com.my.gymcenter.R;
import com.my.gymcenter.entity.ResponseData;
import com.my.gymcenter.entity.User;
import com.my.gymcenter.utils.ResponseCallback;
import com.my.gymcenter.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.lang.reflect.Type;

import okhttp3.Call;

public class UserModifyActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private TextView phone_num;
    private EditText new_password;
    private TextView now_permission;
    private EditText new_username;
    private EditText new_self_sign;
    private int user_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_modify);
        user_id = getIntent().getIntExtra("user_id", 0);
        found(user_id);
        findViewById();
        initView();
    }
    private void initView() {
        mContext=this;
    }

    private void findViewById() {
        findViewById(R.id.ConfirmModify).setOnClickListener(this);
        phone_num = findViewById(R.id.phone_num);
        new_password = findViewById(R.id.new_password);
        now_permission = findViewById(R.id.permission);
        new_username = findViewById(R.id.new_username);
        new_self_sign = findViewById(R.id.new_self_sign);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ConfirmModify) {
            update(user_id);
        }
    }

    private void found(int id) {
        String url = SharedPreferencesUtils.getServerUrl(mContext) + "/user/query/"+ id;
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new ResponseCallback<User>() {
                    @Override
                    public Type getType() {
                        return new TypeToken<ResponseData<User>>(){}.getType();
                    }
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(mContext, "网络链接出错！", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(ResponseData<User> response, int id) {
                        User user = response.get_data();
                        phone_num.setText(user.get_phone_num());
                        new_password.setText(user.get_password());
                        switch (user.get_permission()){
                            case "admin":
                                now_permission.setText("管理员"); break;
                            case "user":
                                now_permission.setText("会员");break;
                            case "coach":
                                now_permission.setText("教练");break;
                        }
                        new_username.setText(user.get_username());
                        new_self_sign.setText(user.get_self_sign());
                    }
                });
    }
    private void update(int id) {
        String password = new_password.getText().toString().trim();
        String username = new_username.getText().toString().trim();
        String self_sign = new_self_sign.getText().toString().trim();
        String permission = now_permission.getText().toString().trim();
        if ( TextUtils.isEmpty(password)||TextUtils.isEmpty(username)||TextUtils.isEmpty(self_sign)) {
            Toast.makeText(mContext, "新的信息不能留空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (id == 1) {
            Toast.makeText(mContext, "不可以修改admin管理员", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = SharedPreferencesUtils.getServerUrl(mContext) + "User?method=update22";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("id", String.valueOf(id))
                .addParams("username", username)
                .addParams("password", password)
                .addParams("permission", permission)
                .addParams("self_sign", self_sign)
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
                            Toast.makeText(mContext, "修改用户成功！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "修改用户失败！", Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }
                });

    }
}
