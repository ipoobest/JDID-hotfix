package com.jdid.ekyc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jdid.ekyc.Fragments.ConfirmOTPRegisterFragment;
import com.jdid.ekyc.Fragments.PinCodeFragment;
import com.jdid.ekyc.Fragments.RegisterFragment;
import com.jdid.ekyc.base.JCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeoutException;

import static android.content.ContentValues.TAG;

public class RegisterActivity extends JCompatActivity {

    private String strPinCode;
    private ProgressDialog mProgressDialog;
    private boolean mImeiValidated;
    private String mFinalPinCode;
    private String imei;
    public String mPhone;
    public String mCompanyName;
    public String mName;
    public String mBranch;
    public String mEMail;

    /* ******************************************************* */
    /* ******************************************************* */
    private class RegisterDevice extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(RegisterActivity.this,
                    null, "กำลังทำการตรวจสอบอุปกรณ์ในระบบ", true, false);
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            JSONObject result = new JSONObject();
//            try {
//                result = _registerDevice();
//            } catch (TimeoutException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            return result;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
//            try {
//                if (result != null) {
//
//                }
                mProgressDialog.dismiss();
                mProgressDialog = null;
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }
    }

    private JSONObject _registerDevice() {
        JSONObject result = new JSONObject();
        return result;
    }

    /* ******************************************************* */
    private class RegisterPinCode extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(RegisterActivity.this,
                    null, "กำลังทำการบันทึก PIN Code", true, false);
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            JSONObject result = null;
            result = _registerPincode();
            return result;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
            try {
                if (result != null) {
                    if (result.getString("created_at").length()>0) {
                        Intent intent = new Intent(RegisterActivity.this, JAppActivity.class);
                        intent.putExtra("from_register ", true);
                        intent.putExtra("imei", imei);
                        intent.putExtra("company", mCompanyName);
                        intent.putExtra("name_th", mName);
                        intent.putExtra("branch", mBranch);
                        intent.putExtra("phone_no", mPhone);
                        intent.putExtra("email", mEMail);
                        intent.putExtra("IMEI_VALIDATED", false);
                        startActivity(intent);
                    }
                }
            } catch (JSONException e) {

            }

//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }
    }

    private JSONObject _registerPincode() {
        JSONObject responseJson = null;
        JSONObject requestParams = new JSONObject();
        try {
            String imei = getImeiNumber();
            requestParams.put("imei",imei);
            requestParams.put("pin", mFinalPinCode);

        } catch (JSONException e) {
            Log.e(TAG, "JsonException in requestparams makeup in PinCoee register", e);
        }

        try {
            final String USER_AGENT = "Mozilla/5.0";
            URL obj = new URL("https://e-kyc.dome.cloud/device/set_pin");
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
//            conn.setRequestProperty("Authorization", "Basic ZWt5Y2Rldjpla3ljZGV2");
            conn.setRequestProperty("X-API-KEY", "3Oi6FUtmmf0aLt6LzVS2FhZXMmEguCMb");


            String requestContent = requestParams.toString();
            Log.d("request : " , requestContent);
            OutputStream os = conn.getOutputStream();
            os.write(requestContent.getBytes());
            os.close();

            InputStream in = conn.getInputStream();
            String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
            responseJson = new JSONObject(result);

            in.close();
            conn.disconnect();

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return responseJson;    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        imei = getIntent().getStringExtra("imei");
        showRegisterFragment();
    }

    // Fragment function
    private void showRegisterFragment() {
        final RegisterFragment fragment = new RegisterFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, fragment).commit();
    }

    public void showConfirmOTPFragment() {
        final ConfirmOTPRegisterFragment fragment = new ConfirmOTPRegisterFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, fragment).commit();
    }

    public void showPinRegisterFragment() {
        final PinCodeFragment fragment = new PinCodeFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, fragment).commit();
        fragment.setPinAssign(PinCodeFragment.ASSIGN_PIN);
    }

    public void showConfirmPinRegisterFragment(String strPin) {
        final PinCodeFragment fragment = new PinCodeFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, fragment).commit();
        fragment.setPinAssign(PinCodeFragment.CONFIRM_PIN);
        strPinCode = strPin;
    }

    public void confirmPinRegister(String strPin) {
        if (strPinCode.equals(strPin)) {
            // save pin code then start JAppActivity
//            Intent intent = new Intent(RegisterActivity.this, JAppActivity.class);
//            intent.putExtra("from_register", true);
//            startActivity(intent);
            // register PIN Code
            mFinalPinCode = strPin;
            RegisterPinCode regisPinCode = new RegisterPinCode();
            regisPinCode.execute();
        } else {
            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.missing_confirm_pin), Toast.LENGTH_LONG).show();
            showPinRegisterFragment();
        }
    }

    //get imei
    private String getImeiNumber(){
        String ts = this.TELEPHONY_SERVICE;
        TelephonyManager mTelephonyMgr = (TelephonyManager) this.getSystemService(ts);
        if (Build.VERSION.SDK_INT >= 26) {
            if (mTelephonyMgr.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
                imei = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

            } else if (mTelephonyMgr.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
                imei = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

            } else {
                imei = ""; // default!!!

            }
        } else {
            imei = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        Log.d("imeii : " , imei);
        return imei;
    }
}

