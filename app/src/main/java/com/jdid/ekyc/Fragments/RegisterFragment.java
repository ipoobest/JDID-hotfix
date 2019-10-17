package com.jdid.ekyc.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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

public class RegisterFragment extends Fragment {

    private ProgressDialog mProgressDialog;

    private String imei;
    private EditText edCompanyName;
    private EditText edName;
    private EditText edBranch;
    private EditText edPhone;
    private EditText edEmail;


    /* ******************************************************* */
    private class ProcessVerifyDevice extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(getContext(),
                    null, "กำลังทำการลงทะเบียนอุปกรณ์", true, false);
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            JSONObject result;
            result = _registerDevice();
            return result;
        }

        @Override
        protected void onPostExecute(JSONObject result) {

            mProgressDialog.dismiss();
            mProgressDialog = null;

            if (result == null) {
                return;
            }

            if (result!=null) {
                try {
                    if (result.getString("created_at").length()>0) {
                        ((RegisterActivity)getActivity()).mCompanyName = edCompanyName.getText().toString();
                        ((RegisterActivity)getActivity()).mName = edName.getText().toString();
                        ((RegisterActivity)getActivity()).mBranch = edBranch.getText().toString();
                        ((RegisterActivity)getActivity()).mPhone = edPhone.getText().toString();
                        ((RegisterActivity)getActivity()).mEMail = edEmail.getText().toString();
                        ((RegisterActivity)getActivity()).showPinRegisterFragment();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
    payload
    {
        "imei": "string",
        "name_th": "string",
        "branch": "string",
        "phone_no": "string",
        "email": "string",
        "name_en": "string"
    }
     */
    private JSONObject _registerDevice() {
        JSONObject responseJson = null;
        JSONObject requestParams = new JSONObject();
        try {
            requestParams.put("imei", this.imei);
            requestParams.put("company", edCompanyName.getText().toString());
            requestParams.put("name_th", edName.getText().toString());
            requestParams.put("branch", edBranch.getText().toString());
            requestParams.put("phone_no", edPhone.getText().toString());
            requestParams.put("email", edEmail.getText().toString());

        } catch (JSONException e) {
            Log.e(TAG, "JsonException in requestparams makeup in Device Registration", e);
        }

        try {
            final String USER_AGENT = "Mozilla/5.0";
            URL obj = new URL("https://e-kyc.dome.cloud/device");
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Basic ZWt5Y2Rldjpla3ljZGV2");

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
        final View view = inflater.inflate(R.layout.fragment_register, container, false);
        view.findViewById(R.id.btnNextStep).setOnClickListener(mOnButtonClickListener);
        setupUI(view);
        return view;
    }

    private final View.OnClickListener mOnButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ProcessVerifyDevice verify = new ProcessVerifyDevice();
            verify.execute();
        }
    };

    @SuppressWarnings("deprecation")
    private void setupUI(View view) {
        String ts = getActivity().TELEPHONY_SERVICE;
        TelephonyManager mTelephonyMgr = (TelephonyManager) getActivity().getSystemService(ts);
        if (Build.VERSION.SDK_INT >= 26) {
            if (mTelephonyMgr.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
                imei = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
            } else if (mTelephonyMgr.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
                imei = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
            } else {
                imei = ""; // default!!!
            }
        } else {
            imei = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        ((EditText)view.findViewById(R.id.edDeviceID)).setText(formatImei(imei));
        edCompanyName = view.findViewById(R.id.edCompanyName);
        edName = view.findViewById(R.id.edName);
        edBranch = view.findViewById(R.id.edBranch);
        edPhone = view.findViewById(R.id.edPhone);
        edEmail = view.findViewById(R.id.edEMail);
    }

    private String formatImei(String strImei) {
        String strReturn = strImei.substring(0,4);
        strReturn += "-";
        strReturn += strImei.substring(4,8);
        strReturn += "-";
        strReturn += strImei.substring(8,12);
        strReturn += "-";
        strReturn += strImei.substring(12,16);
        return strReturn;
    }
}
