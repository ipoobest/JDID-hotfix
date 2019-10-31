package com.jdid.ekyc.Fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jdid.ekyc.JAppActivity;
import com.jdid.ekyc.R;
import com.jdid.ekyc.repository.RetrofitInstance;
import com.jdid.ekyc.repository.api.VerifyUser;
import com.jdid.ekyc.repository.pojo.Otp;
import com.jdid.ekyc.repository.pojo.ResponseOtp;

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

        Otp request = new Otp();
        request.setPhoneNo(mPhoneNumber);
        request.setOtp(edOTP.getText().toString());
        request.setOtpRef(mRef);
        VerifyUser service = RetrofitInstance.getRetrofitInstance().create(VerifyUser.class);
        Call<ResponseOtp> call = service.verifyUser(id, request);
        call.enqueue(new Callback<ResponseOtp>() {
            @Override
            public void onResponse(Call<ResponseOtp> call, Response<ResponseOtp> response) {
                if (response.isSuccessful()) {
                    ((JAppActivity) getActivity()).successFragment();
                } else {
                    Log.d("result", "ยืนยันไม่สำเร็จ");
                }
            }

            @Override
            public void onFailure(Call<ResponseOtp> call, Throwable t) {
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
//        view.findViewById(R.id.btnNextStep).setOnClickListener(new View.OnClickListener() {
//            VerifyOTP vOTP = new VerifyOTP();
//        });
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
