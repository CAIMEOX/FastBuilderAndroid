package io.bushe.fastbuilder.UI.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import io.bushe.fastbuilder.R;
import io.bushe.fastbuilder.User.Listener.BuSheIDCallbackListener;
import io.bushe.fastbuilder.User.UserBridge;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class BaseActivity extends AppCompatActivity {
    public static String TAG = "FastBuilder";
    private SharedPreferences.Editor editor;
    private SharedPreferences BuSheID;
    private TextView textView;
    private long VersionCode;
    private String username;
    // 启动标识
    private int Start = 0;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BaseActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        textView = findViewById(R.id.textView_base_title);
        Log.i(TAG, "Starting FastBuilder...");
        BaseActivityPermissionsDispatcher.getPermissionWithPermissionCheck(this);
        // BuShe ID Smart Lock
        BuSheID = getSharedPreferences("BuSheID", MODE_PRIVATE);
        if (!Objects.equals(BuSheID.getString("Login status", null), "true")) {
            AlertDialog.Builder NeedLogin = new AlertDialog.Builder(BaseActivity.this);
            NeedLogin.setMessage(R.string.need_login);
            NeedLogin.setPositiveButton("Login", (dialog, which) -> {
                Intent intent = new Intent(BaseActivity.this, BuSheIDActivity.class);
                startActivity(intent);
                finish();
            }).setCancelable(false).show();
        } else if (BuSheID.getBoolean("isFBProAccount", false)) {
            String Username = BuSheID.getString("Username", null);
            String Password = BuSheID.getString("Password", null);
            UserBridge userBridge = new UserBridge();
            userBridge.LoginByFastBuilder(userBridge.FTUserData(Username, Password), new BuSheIDCallbackListener() {
                @Override
                public void onFinish(io.bushe.fastbuilder.User.BuSheID buSheID) {

                }

                @Override
                public void onError(Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(BaseActivity.this, "An error occurred while we tried to log in automatically.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(BaseActivity.this, BuSheIDActivity.class));
                        BuSheID.edit().clear().apply();
                        finish();
                    });
                }

                @Override
                public void onFBProFinish(String UserDataJSON) {
                    startMainActivity(1, Username);
                }
            });
        } else {
            String Username = BuSheID.getString("Username", null);
            String Password = BuSheID.getString("Password", null);
            UserBridge userBridge = new UserBridge();
            userBridge.Login(Username, Password, new BuSheIDCallbackListener() {
                @Override
                public void onFinish(io.bushe.fastbuilder.User.BuSheID buSheID) {
                    startMainActivity(1, Username);
                }

                @Override
                public void onError(Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(BaseActivity.this, "An error occurred while we tried to log in automatically.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(BaseActivity.this, BuSheIDActivity.class));
                        BuSheID.edit().clear().apply();
                        finish();
                    });
                }

                @Override
                public void onFBProFinish(String UserDataJSON) {

                }
            });
        }
        // An update has occurred 
        PackageManager manager = this.getPackageManager();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                VersionCode = manager.getPackageInfo(this.getPackageName(), 0).getLongVersionCode();
            } else {
                VersionCode = manager.getPackageInfo(this.getPackageName(), 0).versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        SharedPreferences FastBuilder = getSharedPreferences("FastBuilder", MODE_PRIVATE);
        long Version = FastBuilder.getLong("Version", 0);
        if (Version == 0) {
            editor = FastBuilder.edit();
            editor.putLong("Version", VersionCode);
            editor.apply();
            FromUpdate();
        } else if (VersionCode > Version) {
            FromUpdate();
        } else {
            startMainActivity(1, null);
        }
//        if (BuSheID.getString("Username", null) == null) {
//            new AlertDialog.Builder(BaseActivity.this)
//                    .setMessage(R.string.need_login)
//                    .setPositiveButton("Log in", (dialog, which) -> {
//                        Intent intent = new Intent(BaseActivity.this, BuSheIDActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }).setCancelable(false).show();
//        } else {
//            if (isFBPro = true) {
//                String Username = BuSheID.getString("Username", null);
//                String Password = BuSheID.getString("Password", null);
//                if (Username != null && Password != null) {
//                    textView.setText("正在登录...");
//                    new UserBridge().LoginByFastBuilder(new UserBridge().FTUserData(Username, Password), new BuSheIDCallbackListener() {
//                        @Override
//                        public void onFinish(io.bushe.fastbuilder.User.BuSheID buSheID) {
//
//                        }
//
//                        @Override
//                        public void onError(Exception e) {
//                            new AlertDialog.Builder(BaseActivity.this)
//                                    .setMessage("我们试图登录到 BuShe ID 但是出现了错误：\n" + e.getMessage())
//                                    .setPositiveButton("继续", (dialog, which) -> {
//                                        SharedPreferences.Editor BuSheID_Editor = BuSheID.edit();
//                                        BuSheID_Editor.clear();
//                                        BuSheID_Editor.apply();
//                                        Intent intent = new Intent(BaseActivity.this, BuSheIDActivity.class);
//                                        startActivity(intent);
//                                        finish();
//                                    })
//                                    .setCancelable(false)
//                                    .show();
//                        }
//
//                        @Override
//                        public void onFBProFinish(String UserDataJSON) {
//                            username = Username;
//                            StartCondition(1);
//                        }
//                    });
//                }
//            } else {
//                String Username = BuSheID.getString("Username", null);
//                String Password = BuSheID.getString("Password", null);
//                if (Username != null && Password != null) {
//                    textView.setText("正在登录...");
//                    new UserBridge().Login(Username, Password, new BuSheIDCallbackListener() {
//                        @Override
//                        public void onFinish(BuSheID buSheID) {
//                            username = Username;
//                            StartCondition(1);
//                        }
//
//                        @Override
//                        public void onError(Exception e) {
//                            new AlertDialog.Builder(BaseActivity.this)
//                                    .setMessage("我们试图登录到 BuShe ID 但是出现了错误：\n" + e.getMessage())
//                                    .setPositiveButton("继续", (dialog, which) -> {
//                                        SharedPreferences.Editor BuSheID_Editor = BuSheID.edit();
//                                        BuSheID_Editor.clear();
//                                        BuSheID_Editor.apply();
//                                        Intent intent = new Intent(BaseActivity.this, BuSheIDActivity.class);
//                                        startActivity(intent);
//                                        finish();
//                                    })
//                                    .setCancelable(false)
//                                    .show();
//
//                        }
//
//                        @Override
//                        public void onFBProFinish(String UserDataJSON) {
//
//                        }
//                    });
//                }
//            }
//        }

    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void getPermission() {
        Log.d(TAG, "getPermission: getPermission");
        startMainActivity(1, null);
    }

    @OnShowRationale({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void ShowRationale(final PermissionRequest permissionRequest) {
        Log.d(TAG, "ShowRationale: ShowRationale");
        new AlertDialog.Builder(BaseActivity.this)
                .setTitle("权限请求")
                .setMessage("使用 FastBuilder 需要 STORAGE 组权限，用于执行 FastBuilder Bin 以及 FastBuilder 相关文件存储，点按 继续 将请求权限。")
                .setPositiveButton("继续", (dialog, which) -> permissionRequest.proceed())
                .setNegativeButton("取消", (dialog, which) -> {
                    permissionRequest.cancel();
                    finish();
                })
                .setCancelable(false)
                .show();
    }

    @OnPermissionDenied({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void OnPermissionDenied() {
        new AlertDialog.Builder(BaseActivity.this)
                .setTitle("权限请求")
                .setMessage("使用 FastBuilder 需要 STORAGE 组权限，用于执行 FastBuilder Bin 以及 FastBuilder 相关文件存储，但是我们无法获取以上权限。您将无法使用 FastBuilder。")
                .setPositiveButton("好", (dialog, which) -> android.os.Process.killProcess(android.os.Process.myPid()))
                .setCancelable(false)
                .show();
    }

    @OnNeverAskAgain({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void OnNeverAskAgain() {
        new AlertDialog.Builder(BaseActivity.this)
                .setTitle("权限请求")
                .setMessage("使用 FastBuilder 需要 STORAGE 组权限，用于执行 FastBuilder Bin 以及 FastBuilder 相关文件存储，但是我们无法获取以上权限。您将无法使用 FastBuilder。")
                .setPositiveButton("好", (dialog, which) -> android.os.Process.killProcess(android.os.Process.myPid()))
                .setCancelable(false)
                .show();
    }

    public void startMainActivity(int Plus, String Username) {
        Start = Plus + Start;
        if (Start == 3) {
            Intent intent = new Intent(BaseActivity.this, MainActivity.class);
            intent.putExtra("bushe-id-username", Username);
            startActivity(intent);
            finish();
            Log.i(TAG, "FastBuilder started successfully.");
        }
    }

    public void FromUpdate() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(BaseActivity.this);
        alertDialog.setTitle("新特性");
        alertDialog.setMessage("FastBuilder Catalina 的本领，突飞猛进。\n- 在此次更新中，包含许多安全性更新，为您的使用保驾护航。");
        alertDialog.setPositiveButton("好", (dialog, which) -> startMainActivity(1, null));
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}