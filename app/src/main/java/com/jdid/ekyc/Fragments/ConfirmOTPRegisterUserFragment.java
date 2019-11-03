package com.jdid.ekyc.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jdid.ekyc.JAppActivity;
import com.jdid.ekyc.R;
import com.jdid.ekyc.repository.RetrofitInstance;
import com.jdid.ekyc.repository.api.User;
import com.jdid.ekyc.repository.pojo.RequestOTPForVerify;
import com.jdid.ekyc.repository.pojo.ResponseVerifyUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmOTPRegisterUserFragment extends Fragment {

    private ProgressDialog mProgressDialog;
    private EditText edOTP;
    private TextView txtOTPREF;
    private Button btnNextStep;
    private String mPhoneNumber;
    private String mRef;
    private String mId;
    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
//            mTextView.setText(String.valueOf(s.length()));
            if (s.length() == 6) {
                btnNextStep.setEnabled(true);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };

    public ConfirmOTPRegisterUserFragment(String id, String otpRef) {
        mId = id;
        mRef = otpRef;
    }

    /* ******************************************************* */
    private void verifyUser(String id) {
        mProgressDialog = ProgressDialog.show(getActivity(),
                null, "กำลังทำการรตรวจรหัส OTP", true, false);

        RequestOTPForVerify request = new RequestOTPForVerify();
        request.setPhoneNo(mPhoneNumber);
        request.setOtp(edOTP.getText().toString());
        request.setOtpRef(mRef);
        User service = RetrofitInstance.getRetrofitInstance().create(User.class);
        Call<ResponseVerifyUser> call = service.verifyUser(id, request);
        call.enqueue(new Callback<ResponseVerifyUser>() {
            @Override
            public void onResponse(Call<ResponseVerifyUser> call, Response<ResponseVerifyUser> response) {
                if (response.isSuccessful()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                    ((JAppActivity) getActivity()).successFragment();
                } else {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                    Toast.makeText(getContext(), "รหัส OTP ผิดกรุณากรอกอีกครั้ง", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseVerifyUser> call, Throwable t) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
                Log.e("error : ", t.toString());
            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_otp_verify_user, container, false);
        mPhoneNumber = ((JAppActivity) getActivity()).fieldsList[JAppActivity.CONTACT_NUMBER];
        setupUI(view);
        return view;
    }

    private void setupUI(View view) {
        edOTP = view.findViewById(R.id.edOTP);
        edOTP.addTextChangedListener(mTextEditorWatcher);
        txtOTPREF = view.findViewById(R.id.txtOTPREF);
        btnNextStep = view.findViewById(R.id.btnNextStep);
        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyUser(mId);
            }


        });
        btnNextStep.setEnabled(false);

    }

}
