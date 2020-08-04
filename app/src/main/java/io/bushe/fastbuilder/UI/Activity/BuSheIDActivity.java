package io.bushe.fastbuilder.UI.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Objects;

import io.bushe.bushe_application_security.QVM.QVMProtect;
import io.bushe.fastbuilder.R;
import io.bushe.fastbuilder.User.BuSheID;
import io.bushe.fastbuilder.User.FastBuilder_Pro_User;
import io.bushe.fastbuilder.User.Listener.BuSheIDCallbackListener;
import io.bushe.fastbuilder.User.UserBridge;

@QVMProtect
public class BuSheIDActivity extends AppCompatActivity {
    private Button button_login;
    private String Username;
    private String Password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busheid);
        final TextInputLayout account_username = findViewById(R.id.textInputLayout_busheid_username);
        final TextInputLayout account_password = findViewById(R.id.textInputLayout_busheid_password);
        button_login = findViewById(R.id.button_login);
        button_login.setOnClickListener(v -> new AlertDialog.Builder(BuSheIDActivity.this)
                .setTitle("Account")
                .setMessage("您现在同样可以使用 FastBuiledr Pro 账户 登录了！")
                .setPositiveButton("继续", (dialog, which) -> {
                    Username = Objects.requireNonNull(account_username.getEditText()).getText().toString();
                    Password = Objects.requireNonNull(account_password.getEditText()).getText().toString();
                    button_login.setText("正在登录");
                    button_login.setClickable(false);
                    try {
                        new UserBridge().Login(Username, Password, new BuSheIDCallbackListener() {
                            @Override
                            public void onFinish(BuSheID buSheID) {
                                SharedPreferences.Editor editor = getSharedPreferences("BuSheID", MODE_PRIVATE).edit();
                                editor.putString("Username", buSheID.getUsername());
                                editor.putString("Password", Password);
                                editor.putString("Login status","true");
                                editor.apply();
                                startActivity(new Intent(BuSheIDActivity.this, BaseActivity.class));
                                finish();
                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(BuSheIDActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                button_login.setText("重试");
                                button_login.setClickable(true);
                            }

                            @Override
                            public void onFBProFinish(String UserDataJSON) {

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .setNegativeButton("取消", null)
                .setNeutralButton("FastBuilder Pro 通行证", (dialog, which) -> {
                    Username = Objects.requireNonNull(account_username.getEditText()).getText().toString();
                    Password = Objects.requireNonNull(account_password.getEditText()).getText().toString();
                    button_login.setText("正在登录");
                    button_login.setClickable(false);
                    new UserBridge().LoginByFastBuilder(new UserBridge().FTUserData(Username, Password), new BuSheIDCallbackListener() {
                        @Override
                        public void onFinish(BuSheID buSheID) {

                        }

                        @Override
                        public void onError(Exception e) {
                            runOnUiThread(() -> {
                                Toast.makeText(BuSheIDActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                button_login.setText("重试");
                                button_login.setClickable(true);
                            });
                        }

                        @Override
                        public void onFBProFinish(String UserDataJSON) {
                            runOnUiThread(() -> {
                                String done = null;
                                String Bedrock_Username = null;
                                String FBProUsername = null;
                                String message = null;
                                Gson gson = new Gson();
                                List<FastBuilder_Pro_User> retList = gson.fromJson(Objects.requireNonNull(UserDataJSON), new TypeToken<List<FastBuilder_Pro_User>>() {
                                }.getType());
                                for (FastBuilder_Pro_User fastBuilder_pro_user : retList) {
                                    done = fastBuilder_pro_user.getDone();
                                    Bedrock_Username = fastBuilder_pro_user.getBd_username();
                                    Bedrock_Username = fastBuilder_pro_user.getCn_username();
                                    FBProUsername = fastBuilder_pro_user.getUsername();
                                    message = fastBuilder_pro_user.getMessage();
                                }
                                if (Objects.equals(done, "true")) {
                                    SharedPreferences.Editor editor = getSharedPreferences("BuSheID", MODE_PRIVATE).edit();
                                    editor.putString("Username", FBProUsername);
                                    editor.putString("Password", Password);
                                    editor.putBoolean("isFBProAccount", true);
                                    editor.putString("Login status","true");
                                    editor.apply();
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(BuSheIDActivity.this);
                                    alertDialog.setMessage("您需要重启本应用切换许可。")
                                            .setPositiveButton("好", (dialog1, which1) -> android.os.Process.killProcess(android.os.Process.myPid()))
                                            .setCancelable(false)
                                            .show();
//                                    startActivity(new Intent(BuSheIDActivity.this, MainActivity.class));
//                                    finish();
                                } else {
                                    Toast.makeText(BuSheIDActivity.this, message, Toast.LENGTH_LONG).show();
                                    button_login.setText("重试");
                                    button_login.setClickable(true);
                                }
                            });

                        }
                    });

                })
                .show());

    }
}
