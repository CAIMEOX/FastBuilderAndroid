package io.bushe.fastbuilder.UI.Activity.ui.cloud;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import io.bushe.fastbuilder.R;


public class CloudFragment extends Fragment {

    private CloudViewModel cloudViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        cloudViewModel =
                ViewModelProviders.of(this).get(CloudViewModel.class);
        View root = inflater.inflate(R.layout.fragment_cloud, container, false);

        return root;
    }
}