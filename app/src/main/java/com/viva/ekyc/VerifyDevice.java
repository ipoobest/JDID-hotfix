package com.viva.ekyc;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import android.provider.Settings.Secure;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class VerifyDevice extends AppCompatActivity {
    private static final int PERMISSION_PHONESTATE_CODE = 101;
    private static final String TAG = "LaunchScreenActivity";
    private ProgressDialog mProgressDialog;
    private String mStrDeviceID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_screen);

        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            String ts = this.TELEPHONY_SERVICE;
            TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(ts);
            mStrDeviceID = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
            //TODO THIS
            Intent intent = new Intent(getApplicationContext(), JAppActivity.class);
            startActivity(intent);
//            ProcessVerifyDevice verifyDevice = new ProcessVerifyDevice();
//            verifyDevice.execute();
        } else {
            String[] permission = {Manifest.permission.READ_PHONE_STATE};
            requestPermissions(permission, PERMISSION_PHONESTATE_CODE);
        }

    }

    /* ******************************************************* */
    /* ******************************************************* */
    private class ProcessVerifyDevice extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(com.viva.ekyc.VerifyDevice.this,
                    null, "กำลังทำการตรวจสอบอุปกรณ์ในระบบ", true, false);
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            JSONObject result;
            result = _verifyDevice();
            return result;
        }

        @Override
        protected void onPostExecute(JSONObject result) {


            if (result == null) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
                finish();
                return;
            }

            Intent intent;
            Log.d("mStrDeviceID0", mStrDeviceID);
            try {
                /*
                {
                   "imei":"358059080055704",
                   "name_th":"yyy",
                   "branch":"tgff",
                   "phone_no":"588",
                   "email":"588",
                   "verified":false,
                   "verified_at":null,
                   "created_at":"2019-09-21T12:07:14",
                   "updated_at":"2019-09-22T00:46:23"
                }
                 */
                Log.d("imei0", result.getString("imei"));
                if (result.getString("imei").equals(mStrDeviceID)) {
                    intent = new Intent(getApplicationContext(), JAppActivity.class);
                    intent.putExtra("imei", result.getString("imei"));
                    intent.putExtra("name_th", result.getString("name_th"));
                    intent.putExtra("company", result.getString("company"));
                    intent.putExtra("branch", result.getString("branch"));
                    intent.putExtra("phone_no", result.getString("phone_no"));
                    intent.putExtra("email", result.getString("email"));
                    intent.putExtra("from_register", false);
                    intent.putExtra("IMEI_VALIDATED", result.getBoolean("verified"));
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                    startActivity(intent);
                } else {
                    intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    intent.putExtra("imei", mStrDeviceID);
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                    startActivity(intent);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            finish();
        }
    }

    private JSONObject _verifyDevice() {
        JSONObject responseJson = null;

        try {
            URL obj = new URL("https://e-kyc.dome.cloud/device/" + mStrDeviceID);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setRequestProperty("X-API-KEY", "3Oi6FUtmmf0aLt6LzVS2FhZXMmEguCMb");
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                responseJson = new JSONObject(response.toString());
                in.close();
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) { // device not found
                responseJson = new JSONObject();
                responseJson.put("imei", "");
            }

            conn.disconnect();

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return responseJson;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_PHONESTATE_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String ts = this.TELEPHONY_SERVICE;
                    TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(ts);
                    mStrDeviceID = mTelephonyMgr.getImei();
                    ProcessVerifyDevice verifyDevice = new ProcessVerifyDevice();
                    verifyDevice.execute();
                } else {
                    Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}

