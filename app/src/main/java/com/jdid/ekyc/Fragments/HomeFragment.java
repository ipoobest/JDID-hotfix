package com.jdid.ekyc.Fragments;

import android.content.Context;
import android.content.pm.PackageManager;
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

    private static final int VERIFY_EKYC = 0;
    private static final int VERIFY_PERSON = 1;
    private static final int VERIFY_DIP_CHIP = 2;

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

        ((TextView)view.findViewById(R.id.tvAppVersion)).setText("อัพเดทเมื่อ " + ((JAppActivity)getActivity()).APP_DATE_UPDATE + " VERSION : " +((JAppActivity)getActivity()).APP_VERSION);
        ((TextView)view.findViewById(R.id.txtBranch)).setText(" JDID สาขา "+((JAppActivity)getActivity()).mBranch);

        Log.d("hasUsbHostFeature: ", hasUsbHostFeature(getContext()) + "");


    }

    private final View.OnClickListener mOnKeyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v instanceof TextView) {
                int id = v.getId();
                Log.d(TAG, "resource id : "+id);
                final String string = ((TextView) v).getText().toString();

                switch (v.getId()){
                    case R.id.btnRegisterEKYC:
                        ((JAppActivity)getActivity()).acquireCardData(VERIFY_EKYC);
                        break;
                    case R.id.btnVerifyPerson:
                        ((JAppActivity)getActivity()).acquireCardData(VERIFY_PERSON);
                        break;
                    case R.id.btnVerifyDipChip:
                        ((JAppActivity)getActivity()).acquireCardData(VERIFY_DIP_CHIP);
                        break;

                }

            }
        }
    };

    public static boolean hasUsbHostFeature(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_USB_HOST);
    }

}
