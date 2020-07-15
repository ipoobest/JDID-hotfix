package com.jdid.ekyc.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.jdid.ekyc.JAppActivity;
import com.jdid.ekyc.R;
import com.jdid.ekyc.models.RetrofitInstance;
import com.jdid.ekyc.models.api.Admin;
import com.jdid.ekyc.models.pojo.RequestOTPForRegister;
import com.jdid.ekyc.models.pojo.RequestOTPForVerify;
import com.jdid.ekyc.models.pojo.ResponseOTPForRegister;
import com.jdid.ekyc.models.pojo.ResponseOTPForVerify;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormFillMotorShowRegisterFragment extends Fragment {

    private static final int VERIFY_EKYC = 0;
    private static final int VERIFY_PERSON = 1;
    private static final int VERIFY_DIP_CHIP = 2;

    private Button btnSaveAndGo;
    private Button btnRequestOtp;
    private EditText edPhone;
    private EditText edName;
    private EditText edOtp;
    private TextView txtOTPREF;
    private TextView tvOtpRefText;
    private boolean photo;

    String mRef;
    String mPhoneNumber;

    private ProgressDialog mProgressDialog;

    public FormFillMotorShowRegisterFragment() { }

    public FormFillMotorShowRegisterFragment(boolean takePhoto) {
        this.photo = takePhoto;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_form_fill_motorshow_register, container, false);
        setHasOptionsMenu(true);
        initialize(view);
        return view;
    }


    private void initialize(View view) {
        // ToolBar
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("ลงทะเบียนงาน Motor Show");



        edName =  view.findViewById(R.id.edName);
        edPhone =  view.findViewById(R.id.edPhone);
        edOtp =  view.findViewById(R.id.edOtp);
        txtOTPREF = view.findViewById(R.id.tvOtpRef);
        tvOtpRefText = view.findViewById(R.id.tvOtpRefText);

        edOtp.setVisibility(View.INVISIBLE);
        tvOtpRefText.setVisibility(View.INVISIBLE);
        txtOTPREF.setVisibility(View.INVISIBLE);


        btnSaveAndGo = view.findViewById(R.id.btnNextStep);
        btnRequestOtp = view.findViewById(R.id.btnRequestOtp);

        btnRequestOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhoneNumber = edPhone.getText().toString();
                Log.d("mPhoneNumber", mPhoneNumber);
                edOtp.setVisibility(View.VISIBLE);
                tvOtpRefText.setVisibility(View.VISIBLE);
                txtOTPREF.setVisibility(View.VISIBLE);
                requestOTP();

            }
        });

        if (photo) {
            btnRequestOtp.setVisibility(View.INVISIBLE);
            btnSaveAndGo.setText("ลองอีกครั้ง");
        }

        btnSaveAndGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (photo) {
                    ((JAppActivity) getActivity()).OpenCameraForCapture();
                }
                else {
                    if (finishedFormFill()) {
                        ((JAppActivity) getActivity()).mortorshowRegister = true;
                        ((JAppActivity) getActivity()).mortorName = edName.getText().toString();
                        verifyOTP();

//                    ((JAppActivity) getActivity()).OpenCameraForCapture();
                    } else {
                        Toast.makeText(getActivity(), "กรุณากรอกข้อมูลให้ถูกต้อง", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }


    private boolean finishedFormFill() {

        if (edName.getText().length() == 0) {
            edName.requestFocus();
            return false;
        }
        if (edPhone.getText().length() <= 9 ) {
            edPhone.requestFocus();
            return false;
        }

        return true;
    }

    private void requestOTP() {
        mProgressDialog = ProgressDialog.show(getActivity(),
                null, "กำลังทำการร้องขอรหัส OTP", true, false);
        RequestOTPForRegister request = new RequestOTPForRegister();
        Log.d( "OTP requestOTP: ", mPhoneNumber);
        request.setPhoneNo(mPhoneNumber);


        Admin service = RetrofitInstance.getRetrofitInstance().create(Admin.class);
        Call<ResponseOTPForRegister> call = service.sentOTP(request);
        call.enqueue(new Callback<ResponseOTPForRegister>() {
            @Override
            public void onResponse(Call<ResponseOTPForRegister> call, Response<ResponseOTPForRegister> response) {
                if (response.isSuccessful()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                    ResponseOTPForRegister result = response.body();
                    String otpRef = result.getOtpRef().getOtpRef();
                    txtOTPREF.setText(otpRef);
                    mRef = otpRef;
                } else {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                    Toast.makeText(getContext(), "error request", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseOTPForRegister> call, Throwable t) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
                Log.d("requestOTP :", t.toString());
            }
        });
    }

    /* ******************************************************* */
    private void verifyOTP() {
        mProgressDialog = ProgressDialog.show(getActivity(),
                null, "กำลังตรวจสอบรหัส OTP", true, false);

        RequestOTPForVerify request = new RequestOTPForVerify();

        request.setPhoneNo(mPhoneNumber);

        request.setOtp(edOtp.getText().toString());
        request.setOtpRef(mRef);

        Admin service = RetrofitInstance.getRetrofitInstance().create(Admin.class);
        Call<ResponseOTPForVerify> call = service.verifyOTP(request);
        call.enqueue(new Callback<ResponseOTPForVerify>() {
            @Override
            public void onResponse(Call<ResponseOTPForVerify> call, Response<ResponseOTPForVerify> response) {

                if (response.isSuccessful()) {
                    Log.d("otp ref", "onResponse: " + response.body().getStatusCode());
                    if (response.body().getStatusCode() == 200) {
                        mProgressDialog.dismiss();
                        mProgressDialog = null;
                        ((JAppActivity) getActivity()).OpenCameraForCapture();
                    }else {
                        mProgressDialog.dismiss();
                        mProgressDialog = null;
                        Toast.makeText(getContext(), "รหัส OTP ผิดกรุณากรอกอีกครั้ง", Toast.LENGTH_LONG).show();
                    }
                } else {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;

                    response.errorBody();
                    Log.d("error : ", response.errorBody().getClass().getName());
                    Toast.makeText(getContext(), "OTP หมดอายุ กรุณาขอ OTP ใหม่", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseOTPForVerify> call, Throwable t) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
                Log.d("requestOTP :", t.toString());
            }

        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            confirmBack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirmBack() {
        new AlertDialog.Builder(getContext())
                .setMessage("ต้องการหยุดทำรายการ และกลับไปหน้าเมนูหลัก หรือไม่?")
                .setCancelable(false)
                .setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((JAppActivity) getActivity()).showMotorShowFragment();
                    }
                })
                .setNegativeButton("ยกเลิก", null)
                .show();
    }
}
