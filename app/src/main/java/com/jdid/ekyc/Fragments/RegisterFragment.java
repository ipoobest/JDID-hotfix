package com.jdid.ekyc.Fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
    private Spinner spCompanyName;
    private EditText edName;
    private EditText edBranch;
    private EditText edPhone;
    private EditText edEmail;
    String company;


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

            if (result != null) {
                try {
                    if (result.getString("created_at").length() > 0) {
                        ((RegisterActivity) getActivity()).mCompanyName = company;
                        ((RegisterActivity) getActivity()).mName = edName.getText().toString();
                        ((RegisterActivity) getActivity()).mBranch = edBranch.getText().toString();
                        ((RegisterActivity) getActivity()).mPhone = edPhone.getText().toString();
                        ((RegisterActivity) getActivity()).mEMail = edEmail.getText().toString();
                        ((RegisterActivity) getActivity()).showPinRegisterFragment();
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
            requestParams.put("company", company);
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
//            conn.setRequestProperty("Authorization", "Basic ZWt5Y2Rldjpla3ljZGV2");
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
        final View view = inflater.inflate(R.layout.fragment_register, container, false);
        view.findViewById(R.id.btnNextStep).setOnClickListener(mOnButtonClickListener);
        setupUI(view);
        return view;
    }

    private final View.OnClickListener mOnButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ((RegisterActivity) getActivity()).hideKeyboard();
            if (finishedFormFill()) {
                ProcessVerifyDevice verify = new ProcessVerifyDevice();
                verify.execute();
            } else {
                Toast.makeText(getContext(), "กรุณากรอกข้อมูลให้ครบทุกช่อง", Toast.LENGTH_LONG).show();
            }
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
        ((EditText) view.findViewById(R.id.edDeviceID)).setText(formatImei(imei));
        spCompanyName = view.findViewById(R.id.spCompanyName);
        edName = view.findViewById(R.id.edName);
        edBranch = view.findViewById(R.id.edBranch);
        edPhone = view.findViewById(R.id.edPhone);
        edEmail = view.findViewById(R.id.edEMail);


        //spCompanyName
        spCompanyName.setFocusable(true);
        spCompanyName.setFocusableInTouchMode(true);

        final String[] companyName = getResources().getStringArray(R.array.companyList);
        ArrayAdapter<String> spComAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, companyName);
        spCompanyName.setAdapter(spComAdapter);

        spCompanyName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("กรุณาเลือก ชื่อบริษัท")) {
                    company = null;
                } else {
                    company = companyName[position];
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                company = "กรุณาเลือก ชื่อบริษัท";
            }
        });

    }

    private String formatImei(String strImei) {
        String strReturn = "";
        if (strImei.length() == 15) {
            strReturn = strImei.substring(0, 4);
            strReturn += "-";
            strReturn += strImei.substring(4, 8);
            strReturn += "-";
            strReturn += strImei.substring(8, 12);
            strReturn += "-";
            strReturn += strImei.substring(12, 15);
        } else {
            strReturn = strImei.substring(0, 4);
            strReturn += "-";
            strReturn += strImei.substring(4, 8);
            strReturn += "-";
            strReturn += strImei.substring(8, 12);
            strReturn += "-";
            strReturn += strImei.substring(12, 16);
        }
        return strReturn;
    }

    private boolean finishedFormFill() {
        if (edPhone.getText().length() == 0) {
            edPhone.requestFocus();
            return false;
        }

        if (company == null) {
            spCompanyName.isFocusableInTouchMode();
            return false;
        }
        if (edName.getText().length() == 0) {
            edName.requestFocus();
            return false;
        }
        if (edBranch.getText().length() == 0) {
            edBranch.requestFocus();
            return false;
        }
        if (edPhone.getText().length() != 10) {
            edPhone.requestFocus();
            return false;
        }
        if (edEmail.getText().length() == 0) {
            edEmail.requestFocus();
            return false;
        }
        return true;
    }
}
