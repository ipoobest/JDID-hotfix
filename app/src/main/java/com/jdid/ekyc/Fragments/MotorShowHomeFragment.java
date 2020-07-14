package com.jdid.ekyc.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.jdid.ekyc.JAppActivity;
import com.jdid.ekyc.R;

public class MotorShowHomeFragment extends Fragment {

    private static final String TAG = "HomeFragment : ";

    private static final int VERIFY_EKYC = 0;
    private static final int VERIFY_PERSON = 1;
    private static final int VERIFY_DIP_CHIP = 2;
    private static final int MOTORSHOW_DIP_CHIP = 3;
    private static final int MOTORSHOW_REGISTER = 4;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_motorshow, container, false);
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
        view.findViewById(R.id.btnVerifyPerson).setOnClickListener(mOnKeyClickListener);
        view.findViewById(R.id.btnRegisterMotorShow).setOnClickListener(mOnKeyClickListener);

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
                    case R.id.btnVerifyPerson:
                        ((JAppActivity)getActivity()).acquireCardData(MOTORSHOW_DIP_CHIP);
                        break;
                    case R.id.btnRegisterMotorShow:
                        ((JAppActivity)getActivity()).FormFillMotorShowRegisterFragment();
                        break;
                }
            }
        }
    };

    public static boolean hasUsbHostFeature(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_USB_HOST);
    }



}
