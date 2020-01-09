package com.viva.ekyc.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.viva.ekyc.JAppActivity;
import com.viva.ekyc.R;

public class SuccessFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_success, container, false);
        initialize(view);
        return view;
    }

    private void initialize(View view) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("ตรวจสอบบุคคล");
        view.findViewById(R.id.btnNextStep).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((JAppActivity)getActivity()).showHomeFragment();
            }
        });
    }
}
