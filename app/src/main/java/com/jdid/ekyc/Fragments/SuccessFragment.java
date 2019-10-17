package com.jdid.ekyc.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jdid.ekyc.JAppActivity;
import com.jdid.ekyc.R;

public class SuccessFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_success, container, false);
        initialize(view);
        return view;
    }

    private void initialize(View view) {
        view.findViewById(R.id.btnNextStep).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((JAppActivity)getActivity()).showHomeFragment();
            }
        });
    }
}
