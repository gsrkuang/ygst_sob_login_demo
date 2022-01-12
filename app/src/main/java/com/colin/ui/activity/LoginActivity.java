package com.colin.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.colin.request.RequestInterceptor;
import com.colin.util.Constants;
import com.colin.util.MD5Util;
import com.colin.ygst.R;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private Button mLogin;
    private EditText mVerificationCode;
    private EditText mUserName;
    private EditText mPassWord;
    private AppCompatImageView mVerificationImg;

    private String loginResponseText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initEvent();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Toast.makeText(LoginActivity.this,loginResponseText,Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        refreshCaptcha();
    }

    private void initView() {
        mLogin = findViewById(R.id.login);
        mVerificationCode = findViewById(R.id.verificationCode);
        mPassWord = findViewById(R.id.password);
        mUserName = findViewById(R.id.username);
        mVerificationImg = findViewById(R.id.iv_login_verify_code);
    }
    private void initEvent(){

        mVerificationImg.setOnClickListener(view -> refreshCaptcha());

        mLogin.setOnClickListener(view -> {
            String username = mUserName.getText().toString();
            String password = mPassWord.getText().toString();
            String verificationCode = mVerificationCode.getText().toString();

            try {
                password = MD5Util.getMD5String(password);
                Log.e("++++password",password);
                //点击登录
                submitLogin(username,password,verificationCode);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        });

    }
    private void submitLogin(String username ,String password ,String verifyCode){

        String url = Constants.SUNNY_BEACH_API_BASE_URL+"uc/user/login/";

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"phoneNum\": \""+username +"\",\r\n    \"password\": \""+password +"\"\r\n}");
        Request request = new Request.Builder()
                //请求地址
                .url(url+verifyCode)
                //POST方式
                .method("POST", body)
                //增加请求头
                .addHeader("l_c_i", RequestInterceptor.sobCaptchaKey.toString())
                .addHeader("Content-Type", "application/json")
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("Login", "onFailure: ");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                loginResponseText = response.body().string();
                Log.d("Login", "onResponse: " + loginResponseText);
                handler.sendEmptyMessage(1);
            }
        });

    }

    private void refreshCaptcha(){
        Glide.with(this)
                .load(String.format(Constants.SUNNY_BEACH_API_BASE_URL + "uc/ut/captcha?code=%s", System.currentTimeMillis()))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(mVerificationImg);
    }
}