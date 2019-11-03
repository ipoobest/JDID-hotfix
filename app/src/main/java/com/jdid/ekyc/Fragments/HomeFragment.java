package com.jdid.ekyc.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.jdid.ekyc.JAppActivity;
import com.jdid.ekyc.R;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment : ";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        initialize(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    private void initialize(View view) {
        view.findViewById(R.id.btnRegisterEKYC).setOnClickListener(mOnKeyClickListener);
        view.findViewById(R.id.btnVerifyPerson).setOnClickListener(mOnKeyClickListener);
        view.findViewById(R.id.btnVerifyDipChip).setOnClickListener(mOnKeyClickListener);
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
                } else if (id == R.id.btnVerifyDipChip){
                    Toast.makeText(getContext(), "DIPCHIP", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

}
