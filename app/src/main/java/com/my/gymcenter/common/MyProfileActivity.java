package com.my.gymcenter.common;

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
import com.my.gymcenter.entity.User;
import com.my.gymcenter.utils.ResponseCallback;
import com.my.gymcenter.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.lang.reflect.Type;

import okhttp3.Call;

public class MyProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private int id;
    private EditText password;
    private EditText name;
    private EditText phonenum;
    private EditText data;
    private EditText weight2;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        mContext=this;
        findViewById();
        setContent();
    }

    private void findViewById() {
        findViewById(R.id.ConfirmModify).setOnClickListener(this);
        data=findViewById(R.id.data);
        password =findViewById(R.id.new_my_password);
        name = findViewById(R.id.new_my_name);
        phonenum = findViewById(R.id.new_my_tele);
        weight2=findViewById(R.id.weight);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ConfirmModify) {
            update();
        }
    }
    private void setContent() {
        User user = SharedPreferencesUtils.getUserInfo(mContext);
        id = user.get_id();
        password.setText(user.get_password());
        phonenum.setText(user.get_phone_num());
        name.setText(user.get_username());
        weight2.setText("42");
        data.setText(user.get_self_sign());
    }
    private void update() {
        String password = this.password.getText().toString().trim();
        String phone = phonenum.getText().toString().trim();
        String name = this.name.getText().toString().trim();
        String weight=weight2.getText().toString().trim();
        String data2=data.getText().toString();
        if ( TextUtils.isEmpty(password)||TextUtils.isEmpty(phone)||TextUtils.isEmpty(name) ||TextUtils.isEmpty(weight)||TextUtils.isEmpty(data2) ) {
            Toast.makeText(mContext, "新的信息不能留空", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = SharedPreferencesUtils.getServerUrl(mContext) + "/user/change";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("id", String.valueOf(id))
                .addParams("permission", "user")
                .addParams("password", password)
                .addParams("username", name)
                .addParams("self_sign", data2)
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
                            Toast.makeText(mContext, "修改成功！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "修改失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
