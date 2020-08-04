package io.bushe.fastbuilder.UI.Activity.ui.about;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;

import io.bushe.fastbuilder.FastBuilder;
import io.bushe.fastbuilder.R;

public class AboutFragment extends Fragment {
    private String PMVersion;
    private AboutViewModel aboutViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about, container, false);
        aboutViewModel =
                ViewModelProviders.of(this).get(AboutViewModel.class);
        LinearLayout linearLayout = root.findViewById(R.id.linearLayout_about_license);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OssLicensesMenuActivity.setActivityTitle(getString(R.string.license_title));
                startActivity(new Intent(getActivity(), OssLicensesMenuActivity.class));
            }
        });
        PackageManager manager = FastBuilder.getContext().getPackageManager();
        try {
            PMVersion = manager.getPackageInfo(FastBuilder.getContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        TextView textView = root.findViewById(R.id.text_about_version);
        textView.setText(PMVersion);
        return root;
    }

}
