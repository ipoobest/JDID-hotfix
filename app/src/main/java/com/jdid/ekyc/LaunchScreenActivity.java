package com.jdid.ekyc;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jdid.ekyc.models.RetrofitParseApiJfinInstance;
import com.jdid.ekyc.models.api.Device;
import com.jdid.ekyc.models.pojo.ResponseCheckVersion;
import com.jdid.ekyc.models.pojo.ResultCheckVersion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LaunchScreenActivity extends AppCompatActivity {

    private static final int PERMISSION_PHONESTATE_CODE = 101;
    private static final String TAG = "LaunchScreenActivity";
    private ProgressDialog mProgressDialog;
    private String mStrDeviceID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_screen);

        if(!isConnected(this)) buildDialog(this,"No Internet Connection","กรุณาเชื่อมต่อ internet. กด ok เพื่อออกจากแอพ").show();
        else {
            checkVersion();
        }

       //3000 L = 3 detik
    }

    private void checkVersion() {
        Device service =  RetrofitParseApiJfinInstance.getRetrofitInstance().create(Device.class);
        Call<ResponseCheckVersion> call = service.checkVersion();
        call.enqueue(new Callback<ResponseCheckVersion>() {
            @Override
            public void onResponse(Call<ResponseCheckVersion> call, Response<ResponseCheckVersion> response) {
                if (response.isSuccessful()) {
                    ResponseCheckVersion result = response.body();
                    String version = result.getResults().get(0).getValue();
                    Log.d("version aaa", version);
                    //TODO : VERSION APP
                    if (version.equals("1.1.23")) {
//                        Log.d("version xxx", version);
                        verifyDevice();
                    } else {
//                        Log.d("version yyy", version);
                        alertDialog("กรุณาอัพเดทแอปพลิเคชัน","กรุณากดตกลงอัพเดทแอปพลิเคชัน");

                    }
                } else {
                    alertDialog("กรุณาอัพเดทแอปพลิเคชัน","กรุณาอัพเดทแอปพลิเคชัน");
                }
            }

            @Override
            public void onFailure(Call<ResponseCheckVersion> call, Throwable t) {
                alertDialog("กรุณาอัพเดทแอปพลิเคชัน","กรุณาอัพเดทแอปพลิเคชัน");

            }
        });

    }

    private void verifyDevice() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), VerifyDevice.class));
                finish();
            }
        }, 2000L);
    }

    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
        else return false;
        } else
        return false;
    }


    public AlertDialog.Builder buildDialog(Context c, String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);

        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        return builder;
    }

    private void alertDialog( String title, String message) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d(TAG, "onClick: xxxx");
                        String urls = "https://project.in.th/ekyc1.0.1.apk?v=1.0.6";
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(urls));
                        startActivity(Intent.createChooser(intent, "Open with"));
                    }
                })
                .show();

    }


}
