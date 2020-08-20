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
import com.jdid.ekyc.models.RetrofitParseApiJfinInstance;
import com.jdid.ekyc.models.api.Admin;
import com.jdid.ekyc.models.pojo.Photo;
import com.jdid.ekyc.models.pojo.RequestOTPForRegister;
import com.jdid.ekyc.models.pojo.RequestOTPForVerify;
import com.jdid.ekyc.models.pojo.RequestUserMotorShow;
import com.jdid.ekyc.models.pojo.ResponseImageMegvii;
import com.jdid.ekyc.models.pojo.ResponseOTPForRegister;
import com.jdid.ekyc.models.pojo.ResponseOTPForVerify;
import com.jdid.ekyc.models.pojo.ResponseParse;
import com.jdid.ekyc.models.RetrofitMotorImageInstance;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jdid.ekyc.JAppActivity.THAIFULLNAME;

public class ConfirmOTPRegisterFragment extends Fragment {

    private static final int VERIFY_EKYC = 0;
    private static final int VERIFY_PERSON = 1;
    private static final int VERIFY_DIP_CHIP = 2;
    private static final int VERIFY_DIP_CHIP_MOTORSHOW = 3;

    private ProgressDialog mProgressDialog;
    private EditText edOTP;
    private TextView txtOTPREF;
    private TextView tvResend;
    private Button btnNextStep;
    private String mPhoneNumber;
    private String mPhonePerson;
    private String mRef;

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
            case VERIFY_DIP_CHIP_MOTORSHOW:
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.dip_chip_motorshow);
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
                        } else if ((((JAppActivity) getActivity()).isVerifyDipChip() == VERIFY_DIP_CHIP_MOTORSHOW)) {
//                            mProgressDialog.dismiss();
//                            mProgressDialog = null;

                            Log.d("response", "3");
                            Log.d("onResponse: ",((JAppActivity) getActivity()).photoFile.getName() );
                            requestImageToMegvii(((JAppActivity) getActivity()).photoFile);

                        } else {
                            mProgressDialog.dismiss();
                            mProgressDialog = null;
                            Log.d("response", "4");
                            ((JAppActivity) getActivity()).PutInformationForPerson(VERIFY_DIP_CHIP);

                        }
                    } else {
                        mProgressDialog.dismiss();
                        mProgressDialog = null;
                        Toast.makeText(getContext(), "รหัส OTP ผิดกรุณากรอกอีกครั้ง", Toast.LENGTH_LONG).show();
                    }
                } else {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;

                    response.errorBody();
                    Log.d("error : ", response.errorBody().getClass().getName());
                    Toast.makeText(getContext(), "รหัส OTP ผิดกรุณากรอกอีกครั้ง", Toast.LENGTH_LONG).show();
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


    private void requestImageToMegvii(File image) {
//        mProgressDialog = ProgressDialog.show(getContext(),
//                null, "ระบบกำลังจัดเก็บข้อมูล", true, false);
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("image/*"),
                        image
                );
//
    String[] generalInformation = ((JAppActivity) getActivity()).getGeneralInformation();
    String name = generalInformation[THAIFULLNAME];
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("photo", image.getName(), requestFile);
        RequestBody requestName = RequestBody.create(name, MediaType.parse("text/plain"));

        com.jdid.ekyc.models.api.MotorShow service = RetrofitMotorImageInstance.getRetrofitInstance().create(com.jdid.ekyc.models.api.MotorShow.class);
        Call<ResponseImageMegvii> call = service.postImage(body, requestName);
        call.enqueue(new Callback<ResponseImageMegvii>() {
            @Override
            public void onResponse(Call<ResponseImageMegvii> call, Response<ResponseImageMegvii> response) {
                if (response.isSuccessful()) {
                    ResponseImageMegvii result = response.body();
//                    Log.d(TAG , "onResponse: " + result.getData().getId());
                    if (result.getStatus() ==  1) {
//                        int photoId = result.getData();
//                        mProgressDialog.dismiss();
//                        mProgressDialog = null;
                        ((JAppActivity) getActivity()).photoFile = null;
                        Photo photos = result.getData();
                        int photoId = photos.getId();
                        int subjectId = photos.getSubjectId();
                        String url = photos.getUrl();
                        String urls = "http://megvii-manage.ap.ngrok.io" + url;
                        String[] generalInformation = ((JAppActivity) getActivity()).getGeneralInformation();
                        String name = generalInformation[THAIFULLNAME];
                       requestDataToSubjectParse(subjectId, photoId, name, urls);
//                        requestDataToSubjectMegvii(name, photoId);

                    } else {
//                        mProgressDialog.dismiss();
//                        mProgressDialog = null;
                        ((JAppActivity) getActivity()).photoFile = null;
//                        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                        alertDialogPhone("ไม่สามารถทำรายการใหม่ได้ กรุณาลองใหม่อีกครั้ง");
                    }

                } else {
//                    mProgressDialog.dismiss();
//                    mProgressDialog = null;
                    ((JAppActivity) getActivity()).photoFile = null;
                    alertDialogPhone("ไม่สามารถทำรายการใหม่ได้ กรุณาลองใหม่อีกครั้ง");
                }
            }

            @Override
            public void onFailure(Call<ResponseImageMegvii> call, Throwable t) {
//                mProgressDialog.dismiss();
//                mProgressDialog = null;
                ((JAppActivity) getActivity()).photoFile = null;
            }
        });

    }


    private void requestDataToSubjectParse(int subjectId, int photoId, String name ,String photoUrl) {
//        mProgressDialog = ProgressDialog.show(getContext(),
//                null, "ระบบกำลังดำเนินการจัดเก็บข้อมูล", true, false);

        Log.d("response", "requestDataToSubjectParse: ");
        RequestUserMotorShow request = new RequestUserMotorShow();
        request.setSubjectId(subjectId);
        request.setPhotoId(photoId);
        request.setFullname(name);
        request.setPhotoUrl(photoUrl);
        request.setPhonNumber(mPhoneNumber);
        request.setCoffee("");
        request.setStatus("created");
        request.setDipchip(true);

        Log.d("request xxx", request.toString());

        com.jdid.ekyc.models.api.MotorShow service = RetrofitParseApiJfinInstance.getRetrofitInstance().create(com.jdid.ekyc.models.api.MotorShow.class);
        Call<ResponseParse> call = service.postDataToParse(request);
        call.enqueue(new Callback<ResponseParse>() {
            @Override
            public void onResponse(Call<ResponseParse> call, Response<ResponseParse> response) {
                if (response.isSuccessful()){
                    Log.d("response", "onResponse: if");

                    mProgressDialog.dismiss();
                    mProgressDialog = null;
//                    successFragment();
                    ((JAppActivity) getActivity()).CheckTypePersonal();
                } else {
                    Log.d("response", "onResponse: else");
//                    mProgressDialog.dismiss();
//                    mProgressDialog = null;
                }
            }

            @Override
            public void onFailure(Call<ResponseParse> call, Throwable t) {
                Log.d("onFailure", "onFailure: " + t.toString());
//                mProgressDialog.dismiss();
//                mProgressDialog = null;
            }
        });

    }
    private void alertDialogPhone(String message) {
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((JAppActivity) getActivity()).showMotorShowFragment();
                    }
                })
                .show();

    }

}
