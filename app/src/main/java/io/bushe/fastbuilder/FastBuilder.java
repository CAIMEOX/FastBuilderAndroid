package io.bushe.fastbuilder;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.taobao.sophix.SophixManager;

import java.io.IOException;
import java.io.InputStream;

import cn.bmob.v3.Bmob;
import io.bushe.bushe_application_security.Entrance;
import io.bushe.bushe_application_security.PublishProfile;

/**
  开源版本为提高门槛对项目结构做了较大幅度混淆，其中 FastBuilder Catalina 的安全机制已被部分剥离。
**/
public class FastBuilder extends Application {
    public static String TAG = "FastBuilder";
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Log.i(TAG, PublishProfile.Version);
        new Entrance().onPower(this);
        readHttpsCer();
        SophixManager.getInstance().queryAndLoadNewPatch();
        Bmob.resetDomain("http://fastbuilder.api.bushellc.com/8/");
        Bmob.initialize(this, "ad73f250459b93578f455ce274d6e3ac");
    }

    private void readHttpsCer() {
        try {
            InputStream inputStream = getAssets().open("BuShe LLC/Cer/bushellc-sign");
            OkhttpManager.getInstance().setTrustrCertificates(inputStream);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
