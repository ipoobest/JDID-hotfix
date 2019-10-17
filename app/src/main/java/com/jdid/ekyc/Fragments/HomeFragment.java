package com.jdid.ekyc.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jdid.ekyc.JAppActivity;
import com.jdid.ekyc.R;
import com.jdid.ekyc.RegisterActivity;
import com.jdid.ekyc.views.PFCodeView;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment : ";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        initialize(view);
        return view;
    }

    private void initialize(View view) {
        view.findViewById(R.id.btnRegisterEKYC).setOnClickListener(mOnKeyClickListener);
        view.findViewById(R.id.btnVerifyPerson).setOnClickListener(mOnKeyClickListener);
        ((TextView)view.findViewById(R.id.txtBranch)).setText("JDID สาขา "+((JAppActivity)getActivity()).mBranch);

    }

    private final View.OnClickListener mOnKeyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v instanceof TextView) {
                int id = v.getId();
                Log.d(TAG, "resource id : "+id);
                final String string = ((TextView) v).getText().toString();
                if (id==R.id.btnRegisterEKYC) {
                    ((JAppActivity)getActivity()).acquireCardData(false);
                } else if (id==R.id.btnVerifyPerson) {
                    ((JAppActivity)getActivity()).acquireCardData(true);
                }
            }
        }
    };

}
