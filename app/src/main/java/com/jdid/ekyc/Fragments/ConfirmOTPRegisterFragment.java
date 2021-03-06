package com.jdid.ekyc.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;
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


public class ConfirmOTPRegisterFragment extends Fragment {

    private static final int VERIFY_EKYC = 0;
    private static final int VERIFY_PERSON = 1;
    private static final int VERIFY_DIP_CHIP = 2;

    private ProgressDialog mProgressDialog;
    private EditText edOTP;
    private TextView txtOTPREF;
    private TextView tvResend;
    private Button btnNextStep;
    private String mPhoneNumber;
    private String mPhonePerson;
    private String mRef;
    private int falseOtp = 0;

    private FirebaseAnalytics mFirebaseAnalytics;
//
//    String[] generalInformation = ((JAppActivity) getActivity()).getGeneralInformation();
//    String name = generalInformation[THAIFULLNAME];

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 6) {
                btnNextStep.setEnabled(true);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };

    public ConfirmOTPRegisterFragment() {
    }

    public ConfirmOTPRegisterFragment(String mPhonePerson) {
        this.mPhonePerson = mPhonePerson;
    }

    /* ******************************************************* */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_otp_register, container, false);
        setHasOptionsMenu(true);
        mPhoneNumber = ((JAppActivity) getActivity()).fieldsList[JAppActivity.CONTACT_NUMBER];
        Log.d( "mPhoneNumber xxx ::: ", mPhoneNumber);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        setupUI(view);
        requestOTP();
        return view;
    }

    private void setupUI(View view) {
        // Toolbar
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        switch (((JAppActivity) getActivity()).isVerifyPerson()) {
            case VERIFY_EKYC:
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.kyc_title);
                break;
            case VERIFY_PERSON:
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.verify_person);
                break;
        }

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
        txtOTPREF = view.findViewById(R.id.tvOtpRef);
        tvResend = view.findViewById(R.id.tvResend);
        tvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestOTP();
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
                .setMessage("ต้องการรยกเลิกการทำรายการหรือไม่")
                .setCancelable(false)
                .setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if ((((JAppActivity) getActivity()).isVerifyDipChip() == VERIFY_PERSON)) {
                            ((JAppActivity) getActivity()).showCardInformation();
                        } else {
                            ((JAppActivity) getActivity()).showFormFillFragment();
                        }
                    }
                })
                .setNegativeButton("ยกเลิก", null)
                .show();
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
                    txtOTPREF.setText("OTP Ref : " + otpRef);
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
        Log.d("response", "verifyOTP");
        RequestOTPForVerify request = new RequestOTPForVerify();

        request.setPhoneNo(mPhoneNumber);

        request.setOtp(edOTP.getText().toString());
        request.setOtpRef(mRef);

        Admin service = RetrofitInstance.getRetrofitInstance().create(Admin.class);
        Call<ResponseOTPForVerify> call = service.verifyOTP(request);
        call.enqueue(new Callback<ResponseOTPForVerify>() {
            @Override
            public void onResponse(Call<ResponseOTPForVerify> call, Response<ResponseOTPForVerify> response) {

                if (response.isSuccessful()) {
                    if (response.body().getStatusCode() == 200) {
                        if ((((JAppActivity) getActivity()).isVerifyDipChip() == VERIFY_EKYC)) {
                            mProgressDialog.dismiss();
                            mProgressDialog = null;
                            Log.d("response", "1");

                            ((JAppActivity) getActivity()).SaveInformation();
                        } else if ((((JAppActivity) getActivity()).isVerifyDipChip() == VERIFY_PERSON)) {

                            mProgressDialog.dismiss();
                            mProgressDialog = null;
                            Log.d("response", "2");
                            ((JAppActivity) getActivity()).CheckTypePersonal();
                        } else {
                            mProgressDialog.dismiss();
                            mProgressDialog = null;
                            Log.d("response", "4");
                            ((JAppActivity) getActivity()).PutInformationForPerson(VERIFY_DIP_CHIP);

                        }

                    } else if (response.body().getStatusCode() == 500){
                        mProgressDialog.dismiss();
                        mProgressDialog = null;
                        alertDialog("ระบบ SMS ขัดข้องกรุณาติดต่อเจ้าหน้าที่");

                    } else {
                        mProgressDialog.dismiss();
                        mProgressDialog = null;
                        falseOtp += 1;
                        Toast.makeText(getContext(), "รหัส OTP ผิด กรุณากรอกอีกครั้ง" + falseOtp, Toast.LENGTH_LONG).show();
                        if (falseOtp == 3) {
                            falseOtp = 0;
                            Toast.makeText(getContext(),"กรุณากรอก OTP ที่ท่านได้รับใหม่", Toast.LENGTH_SHORT).show();
                            requestOTP();
                        }
                    }
                } else {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                    falseOtp += 1;
                    if (falseOtp == 3) {
                        falseOtp = 0;
                        Toast.makeText(getContext(),"กรุณากรอก OTP ที่ท่านได้รับใหม่", Toast.LENGTH_SHORT).show();
                        requestOTP();
                    }
                    response.errorBody();
                    Log.d("error : ", response.errorBody().getClass().getName());
                    Toast.makeText(getContext(), "รหัส OTP ผิด กรุณากรอกอีกครั้ง" , Toast.LENGTH_LONG).show();
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
    private void alertDialog(String text) {
        new AlertDialog.Builder(getContext())
                .setMessage(text)
                .setCancelable(false)
                .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((JAppActivity) getActivity()).showHomeFragment();
                    }
                })
                .show();
        }


}
