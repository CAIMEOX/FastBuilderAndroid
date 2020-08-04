package io.bushe.fastbuilder.UI.Activity.ui.local;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taobao.sophix.SophixManager;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import cn.bmob.v3.BmobUser;
import io.bushe.fastbuilder.Core.Config;
import io.bushe.fastbuilder.Core.Golang.GoBuilderService;
import io.bushe.fastbuilder.R;
import io.bushe.fastbuilder.User.BuSheID;
import io.bushe.fastbuilder.Util.OKHttp3Util;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.internal.annotations.EverythingIsNonNull;


public class LocalFragment extends Fragment {
    private LocalViewModel localViewModel;
    private Button stop;
    private Button start;
    private String Package;
    private String ProductName;
    private String ProductVersion;
    private String License;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences("BuSheID", Context.MODE_PRIVATE);
    }

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        localViewModel =
                ViewModelProviders.of(this).get(LocalViewModel.class);
        View root = inflater.inflate(R.layout.fragment_local, container, false);
        TextView textView_fastbuilder_go_ver = root.findViewById(R.id.textView_fragment_local_ver);
        start = root.findViewById(R.id.button_fragment_local_Start);
        stop = root.findViewById(R.id.button_fragment_local_Stop);
        SophixManager.getInstance().queryAndLoadNewPatch();
        final AlertDialog check = new AlertDialog.Builder(Objects.requireNonNull(getActivity())).create();
        check.setMessage("Verifying your available licenses.\n" +
                "This may take some time.");
        check.setCancelable(false);
        check.show();
        start.setClickable(false);
        start.setText("...");
        if (BmobUser.isLogin()) {
            BuSheID buSheID = BmobUser.getCurrentUser(BuSheID.class);
            String UserType = buSheID.getUserType();
            if (UserType.equals("Pro")) {
                start.setClickable(true);
                start.setText(R.string.button_start);
                start.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), GoBuilderService.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Objects.requireNonNull(getActivity()).startForegroundService(intent);
                        start.setClickable(false);
                        start.setText("Running");
                        stop.setClickable(true);
                    } else {
                        Objects.requireNonNull(getActivity()).startService(intent);
                        start.setClickable(false);
                        start.setText("Running");
                        stop.setClickable(true);
                    }
                });
            } else {
                start.setClickable(false);
                start.setOnClickListener(null);
                start.setText(R.string.no_permission);
            }
        } else {
            start.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), GoBuilderService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Objects.requireNonNull(getActivity()).startForegroundService(intent);
                    start.setClickable(false);
                    start.setText("Running");
                    stop.setClickable(true);
                } else {
                    Objects.requireNonNull(getActivity()).startService(intent);
                    start.setClickable(false);
                    start.setText("Running");
                    stop.setClickable(true);
                }
            });
        }
        boolean FBPro = sharedPreferences.getBoolean("isFBProAccount", false);
        if (FBPro) {
            start.setClickable(true);
            start.setText(R.string.button_start);
            start.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), GoBuilderService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Objects.requireNonNull(getActivity()).startForegroundService(intent);
                    start.setClickable(false);
                    start.setText("Running");
                    stop.setClickable(true);
                } else {
                    Objects.requireNonNull(getActivity()).startService(intent);
                    start.setClickable(false);
                    start.setText("Running");
                    stop.setClickable(true);
                }
            });
        }

        new Thread(() -> OKHttp3Util.sendOkHttpRequest("https://dl.bushellc.com/config/fastbuilder/catalina/core/fastbuilder-go-0.1.20200109-release.json", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                check.dismiss();
                AlertDialog onFailure = new AlertDialog.Builder(Objects.requireNonNull(getActivity())).create();
                onFailure.setMessage(e.getMessage());
                onFailure.setCancelable(false);
                onFailure.setButton(DialogInterface.BUTTON_POSITIVE, "OK", (dialog, which) -> android.os.Process.killProcess(android.os.Process.myPid()));
            }

            @Override
            @EverythingIsNonNull
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                List<Config> retList = gson.fromJson(Objects.requireNonNull(response.body()).string(), new TypeToken<List<Config>>() {
                }.getType());
                for (Config config1 : retList) {
                    Package = config1.getPackage();
                    ProductName = config1.getProductName();
                    ProductVersion = config1.getProductVersion();
                    License = config1.getLicense();
                }
            }
        })).start();
        check.dismiss();
        stop.setClickable(false);
        textView_fastbuilder_go_ver.setText("轻触此处查看详细信息");
        textView_fastbuilder_go_ver.setOnClickListener(v -> {
            AlertDialog.Builder ProductConfig = new AlertDialog.Builder(getActivity());
            ProductConfig.setMessage("Package: \n" + Package + "\nProduct Name: \n" + ProductName + "\nProduct Version: \n" + ProductVersion + "\nLicensed to " + License);
            ProductConfig.show();
        });

        stop.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), GoBuilderService.class);
            Objects.requireNonNull(getActivity()).stopService(intent);
            stop.setClickable(false);
            start.setClickable(true);
            start.setText(R.string.button_start);
        });
        return root;
    }

}