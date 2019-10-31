package com.jdid.ekyc.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jdid.ekyc.JAppActivity;
import com.jdid.ekyc.R;
import com.jdid.ekyc.RegisterActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
            //This sets a textview to the current length
//            mTextView.setText(String.valueOf(s.length()));
            if (s.length()==6) {
                btnNextStep.setEnabled(true);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };
    /* ******************************************************* */
    private class RequestOTP extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(getActivity(),
                    null, "กำลังทำการร้องขอรหัส OTP", true, false);
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            JSONObject result;
            result = _requestOTP();
            return result;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            mProgressDialog.dismiss();
            mProgressDialog = null;

            if (result!=null) {
                try {
                    JSONObject data = result.getJSONObject("data");
                    mRef = data.getString("otp_ref");
                    txtOTPREF.setText("OTP Ref : "+mRef);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private JSONObject _requestOTP() {
        JSONObject responseJson = null;
        JSONObject requestParams = new JSONObject();
        try {
            requestParams.put("phone_no", mPhoneNumber);
        } catch (JSONException e) {
            Log.e(TAG, "JsonException in requestparams makeup in Device Registration", e);
        }

        try {
            final String USER_AGENT = "Mozilla/5.0";
            URL obj = new URL("https://e-kyc.dome.cloud/admin/login");
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("X-API-KEY", "3Oi6FUtmmf0aLt6LzVS2FhZXMmEguCMb");

            String requestContent = requestParams.toString();
            OutputStream os = conn.getOutputStream();
            os.write(requestContent.getBytes());
            os.close();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
            responseJson = new JSONObject(result);

            in.close();
            conn.disconnect();

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return responseJson;
    }

    /* ******************************************************* */
    private class VerifyOTP extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(getActivity(),
                    null, "กำลังทำการตรวจสอบรหัส OTP", true, false);
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            JSONObject result;
            result = _verifyOTP();
            return result;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            mProgressDialog.dismiss();
            mProgressDialog = null;

            if (result!=null) {
                try {
                    if (result.getString("access_token").length()>0) {
                        ((JAppActivity)getActivity()).SaveInformation();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private JSONObject _verifyOTP() {
        JSONObject responseJson = null;
        JSONObject requestParams = new JSONObject();
        try {
            requestParams.put("phone_no", mPhoneNumber);
            requestParams.put("otp_ref", mRef);
            requestParams.put("otp", edOTP.getText().toString());
        } catch (JSONException e) {
            Log.e(TAG, "JsonException in requestparams makeup in Device Registration", e);
        }

        try {
            final String USER_AGENT = "Mozilla/5.0";
            URL obj = new URL("https://e-kyc.dome.cloud/admin/verify_login");
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("X-API-KEY", "3Oi6FUtmmf0aLt6LzVS2FhZXMmEguCMb");

            String requestContent = requestParams.toString();
            OutputStream os = conn.getOutputStream();
            os.write(requestContent.getBytes());
            os.close();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
            responseJson = new JSONObject(result);

            in.close();
            conn.disconnect();

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return responseJson;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_otp_register, container, false);
        mPhoneNumber = ((JAppActivity)getActivity()).fieldsList[JAppActivity.CONTACT_NUMBER];
        setupUI(view);
        requestOTP();
        return view;
    }

    private void setupUI(View view) {
//        view.findViewById(R.id.btnNextStep).setOnClickListener(new View.OnClickListener() {
//            VerifyOTP vOTP = new VerifyOTP();
//        });
        btnNextStep = view.findViewById(R.id.btnNextStep);
        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VerifyOTP otp = new VerifyOTP();
                otp.execute();
            }
        });
        btnNextStep.setEnabled(false);
        edOTP = view.findViewById(R.id.edOTP);
        edOTP.addTextChangedListener(mTextEditorWatcher);
        txtOTPREF = view.findViewById(R.id.txtOTPREF);
    }

    private void requestOTP() {
        RequestOTP requestOtp = new RequestOTP();
        requestOtp.execute();
    }

    private final View.OnClickListener mOnButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            VerifyOTP verifyOTP = new VerifyOTP();
            verifyOTP.execute();
        }
    };



}
