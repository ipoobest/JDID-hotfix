package com.viva.ekyc.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.viva.ekyc.JAppActivity;
import com.viva.ekyc.R;

public class WaitForAuthoriseFragment extends Fragment implements WaitForAuthoriseInterface {

    public TextView txtBranch;
    public TextView txtDetail;
    public TextView txtDetail2;
    public TextView txtDetail3;
    public TextView txtDetail4;
    public TextView txtDetail5;
    public TextView txtDetail6;
    public TextView txtDetail7;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_wait_for_authorized, container, false);
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
        txtBranch = view.findViewById(R.id.txtBranch);
        txtDetail = view.findViewById(R.id.txtDetail);
        txtDetail2 = view.findViewById(R.id.txtDetail2);
        txtDetail3 = view.findViewById(R.id.txtDetail3);
        txtDetail4 = view.findViewById(R.id.txtDetail4);
        txtDetail5 = view.findViewById(R.id.txtDetail5);
        txtDetail6 = view.findViewById(R.id.txtDetail6);
        txtDetail7 = view.findViewById(R.id.txtDetail7);

        txtBranch.setText("JDID สาขา "+((JAppActivity)getActivity()).mBranch);
        txtDetail2.setText("Device ID : "+((JAppActivity)getActivity()).mIMEI);
        txtDetail3.setText("บริษัท : "+((JAppActivity)getActivity()).mCompanyName);
        txtDetail4.setText("ผู้จัดการสาขา : "+((JAppActivity)getActivity()).mName);
        txtDetail5.setText("ชื่อสาขา : "+((JAppActivity)getActivity()).mBranch);
        txtDetail6.setText("เบอร์โทรศัพท์ : "+((JAppActivity)getActivity()).mPhone);
        txtDetail7.setText("อีเมล์ : "+((JAppActivity)getActivity()).mEMail);
    }

    @Override
    public void setDeviceInfo(String imei, String company, String name, String phone, String branch, String email) {
        txtBranch.setText("JDID สาขา "+branch);
        txtDetail2.setText("Device ID : "+imei);
        txtDetail3.setText("บริษัท : "+company);
        txtDetail4.setText("ผู้จัดการสาขา : "+name);
        txtDetail5.setText("ชื่อสาขา : "+branch);
        txtDetail6.setText("เบอร์โทรศัพท์ : "+phone);
        txtDetail7.setText("อีเมล์ : "+email);
    }

    private String formatImei(String strImei) {
        String strReturn = "";
        if (strImei.length() == 15){
            strReturn = strImei.substring(0,4);
            strReturn += "-";
            strReturn += strImei.substring(4,8);
            strReturn += "-";
            strReturn += strImei.substring(8,12);
            strReturn += "-";
            strReturn += strImei.substring(12,15);
        }else {
            strReturn = strImei.substring(0,4);
            strReturn += "-";
            strReturn += strImei.substring(4,8);
            strReturn += "-";
            strReturn += strImei.substring(8,12);
            strReturn += "-";
            strReturn += strImei.substring(12,16);
        }
        return strReturn;
    }
}
