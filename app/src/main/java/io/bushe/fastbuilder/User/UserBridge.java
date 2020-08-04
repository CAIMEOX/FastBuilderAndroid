package io.bushe.fastbuilder.User;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Objects;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import io.bushe.bushe_application_security.CryptoFactory;
import io.bushe.bushe_application_security.QVM.QVMProtect;
import io.bushe.fastbuilder.User.Listener.BuSheIDCallbackListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.annotations.EverythingIsNonNull;


@QVMProtect
public class UserBridge {


    public void Login(String Username, String Password, final BuSheIDCallbackListener listener) {
        BmobUser.loginByAccount(Username, Password, new LogInListener<BuSheID>() {
            @Override
            public void done(BuSheID user, BmobException e) {
                if (e == null) {
                    listener.onFinish(user);
                } else {
                    listener.onError(e);
                }
            }
        });

    }

    public String FTUserData(String Username, String Password) {
        String SHA256_Password = CryptoFactory.shaEncrypt(Password);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("qq", Username);
        jsonObject.addProperty("password", SHA256_Password);
        return jsonObject.toString();
    }

    @EverythingIsNonNull
    public void LoginByFastBuilder(String UserData, final BuSheIDCallbackListener listener) {
        new Thread(() -> {
            //OkHttpClient client = OkhttpManager.getInstance().build();
            OkHttpClient client = new OkHttpClient.Builder().build();
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), UserData);
            Request request = new Request.Builder()
                    .url("https://uc.fastbuilder.pro/android_login.web")
                    .post(requestBody)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    listener.onError(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    listener.onFBProFinish(Objects.requireNonNull(response.body()).string());
                }
            });

        }).start();

    }
}
