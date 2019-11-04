package com.jdid.ekyc.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.jdid.ekyc.JAppActivity;
import com.jdid.ekyc.R;
import com.jdid.ekyc.RegisterActivity;
import com.jdid.ekyc.repository.RetrofitInstance;
import com.jdid.ekyc.repository.api.Admin;
import com.jdid.ekyc.repository.pojo.OtpRef;
import com.jdid.ekyc.repository.pojo.RequestOTPForRegister;
import com.jdid.ekyc.repository.pojo.RequestOTPForVerify;
import com.jdid.ekyc.repository.pojo.ResponseOTPForRegister;
import com.jdid.ekyc.repository.pojo.ResponseOTPForVerify;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ConfirmOTPRegisterFragment extends Fragment {

    private ProgressDialog mProgressDialog;
    private EditText edOTP;
    private TextView txtOTPREF;
    private Button btnNextStep;
    private String mPhoneNumber;
    private String mRef;
    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length()==6) {
                btnNextStep.setEnabled(true);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };
    /* ******************************************************* */

    private void requestOTP(){
        mProgressDialog = ProgressDialog.show(getActivity(),
                null, "กำลังทำการร้องขอรหัส OTP", true, false);
        RequestOTPForRegister request = new RequestOTPForRegister();
        request.setPhoneNo(mPhoneNumber);
        Admin service = RetrofitInstance.getRetrofitInstance().create(Admin.class);
        Call<ResponseOTPForRegister> call = service.sentOTP(request);
        call.enqueue(new Callback<ResponseOTPForRegister>() {
            @Override
            public void onResponse(Call<ResponseOTPForRegister> call, Response<ResponseOTPForRegister> response) {
                if (response.isSuccessful()){
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                    ResponseOTPForRegister result = response.body();
                    String otpRef = result.getOtpRef().getOtpRef();
                    txtOTPREF.setText("OTP Ref : "+ otpRef);
                    mRef = otpRef;
                }else {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                    Toast.makeText(getContext(), "error request", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseOTPForRegister> call, Throwable t) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
                Log.d("requestOTP :" , t.toString());
            }
        });
    }

    /* ******************************************************* */
    private void verifyOTP(){
        mProgressDialog = ProgressDialog.show(getActivity(),
                null, "กำลังทำการรตรวจรหัส OTP", true, false);

        RequestOTPForVerify request = new RequestOTPForVerify();
        request.setPhoneNo(mPhoneNumber);
        request.setOtp(edOTP.getText().toString());
        request.setOtpRef(mRef);

        Admin service = RetrofitInstance.getRetrofitInstance().create(Admin.class);
        Call<ResponseOTPForVerify> call = service.verifyOTP(request);
        call.enqueue(new Callback<ResponseOTPForVerify>() {
            @Override
            public void onResponse(Call<ResponseOTPForVerify> call, Response<ResponseOTPForVerify> response) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
                if (response.isSuccessful()){
                    if (response.body().getAccessToken().length() > 0){
                        ((JAppActivity)getActivity()).SaveInformation();
                    }
                }else {
                    Toast.makeText(getContext(), "รหัส OTP ผิดกรุณากรอกอีกครั้ง", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseOTPForVerify> call, Throwable t) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
                Log.d("requestOTP :" , t.toString());
            }

        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_otp_register, container, false);
        setHasOptionsMenu(true);
        mPhoneNumber = ((JAppActivity)getActivity()).fieldsList[JAppActivity.CONTACT_NUMBER];
        setupUI(view);
        requestOTP();
        return view;
    }

    private void setupUI(View view) {
        // Toolbar
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Confirm OTP");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnNextStep = view.findViewById(R.id.btnNextStep);
        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyOTP();
            }
        });
        btnNextStep.setEnabled(false);
        edOTP = view.findViewById(R.id.edOTP);
        edOTP.addTextChangedListener(mTextEditorWatcher);
        txtOTPREF = view.findViewById(R.id.txtOTPREF);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            confirmBack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirmBack() {
         new AlertDialog.Builder(getContext())
                .setMessage("ต้องการหยุดทำรายการ และกลับไปหน้ากรอกข้อมูลใหม่หรือไม่")
                .setCancelable(false)
                .setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((JAppActivity) getActivity()).showFormFillFragment();
                    }
                })
                .setNegativeButton("ยกเลิก", null)
                .show();
    }
}
