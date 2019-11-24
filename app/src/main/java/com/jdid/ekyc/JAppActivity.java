package com.jdid.ekyc;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import androidx.annotation.Nullable;

import com.acs.smartcard.Features;
import com.acs.smartcard.Reader;
import com.acs.smartcard.ReaderException;
import com.jdid.ekyc.Fragments.CardAcquireFragment;
import com.jdid.ekyc.Fragments.CardInfoFragment;
import com.jdid.ekyc.Fragments.ConfirmOTPRegisterFragment;
import com.jdid.ekyc.Fragments.ConfirmOTPRegisterUserFragment;
import com.jdid.ekyc.Fragments.FaceCompareResultFragment;
import com.jdid.ekyc.Fragments.FormFillFragment;
import com.jdid.ekyc.Fragments.HomeFragment;
import com.jdid.ekyc.Fragments.PinCodeFragment;
import com.jdid.ekyc.Fragments.SuccessFragment;
import com.jdid.ekyc.Fragments.WaitForAuthoriseFragment;
import com.jdid.ekyc.base.JCompatActivity;
import com.jdid.ekyc.repository.RetrofitInstance;
import com.jdid.ekyc.repository.api.User;
import com.jdid.ekyc.repository.api.Device;
import com.jdid.ekyc.repository.pojo.OtpRef;
import com.jdid.ekyc.repository.pojo.RequestPutUser;
import com.jdid.ekyc.repository.pojo.RequestVrifyPin;
import com.jdid.ekyc.repository.pojo.RequestCreateUser;
import com.jdid.ekyc.repository.pojo.ResponVerifyPin;
import com.jdid.ekyc.repository.pojo.ResponseCreateUser;
import com.jdid.ekyc.repository.pojo.ResponseVerifyUser;
import com.jdid.ekyc.repository.pojo.UserInformation;
import com.jdid.ekyc.views.PFCodeView;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JAppActivity extends JCompatActivity {

    private static final String TAG = "JAppActivity";

    public static final String APP_VERSION = "release 1.0.6";
    public static final String APP_DATE_UPDATE = "14/11/62";

    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;

    private static final int VERIFY_PERSON = 1;


    private PFCodeView mCodeView;

    private boolean mfLoginPage = false;
    private boolean mfStartFromRegister = false;
    private int mfVerifyPerson = 0;
    private int mVerifyDipChip = 1;

    public int isVerifyPerson() {
        return mfVerifyPerson;
    }

    public int isVerifyDipChip() {
        return mVerifyDipChip;
    }

    private static Context context;

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    int iActualState = 1;
    private UsbManager mManager;
    private static Reader mReader;
    private PendingIntent mPermissionIntent;
    static int iSlotNum = -1;
    private String deviceName;

    private static final int COMPARE_SIMILARITY = 90;

    static byte[] byteImageCam = null;
    static byte[] byteImage = null;
    private ImageView imageBuffer;

    static String strCLA;
    static String strINS;
    static String strP1;
    static String strP2;
    static String strLc;
    static String strLe;
    static String strDataIn;

    static boolean boolGetImage = false;
    static int iMaxImageChunk = 20;
    static int iCurrentImageChunk = 0;
    static String[] imageRetrieve = {
            "80b0017B0200FF",
            "80b0027A0200FF",
            "80b003790200FF",
            "80b004780200FF",
            "80b005770200FF",
            "80b006760200FF",
            "80b007750200FF",
            "80b008740200FF",
            "80b009730200FF",
            "80b00A720200FF",
            "80b00B710200FF",
            "80b00C700200FF",
            "80b00D6F0200FF",
            "80b00E6E0200FF",
            "80b00F6D0200FF",
            "80b0106C0200FF",
            "80b0116B0200FF",
            "80b0126A0200FF",
            "80b013690200FF",
            "80b014680200FF"
    };
    static boolean boolGetInformation = false;
    static int iMaxInfoChunk = 9;
    static int iCurrentInfoChunk = 0;
    public static final int CID = 0;
    public static final int THAIFULLNAME = 1;
    public static final int ENGLISHFULLNAME = 2;
    public static final int BIRTH = 3;
    public static final int GENDER = 4;
    public static final int ISSUER = 5;
    public static final int ISSUE = 6;
    public static final int EXPIRE = 7;
    public static final int ADDRESS = 8;
    static String[] generalInformation = new String[9];

    public String[] getGeneralInformation() {
        return generalInformation;
    }

    static String[] generalRetrieve = {
            "80b0000402000d",
            "80b00011020064",
            "80b00075020064",
            "80b000D9020008",
            "80b000E1020001",
            "80b000F6020064",
            "80b00167020008",
            "80b0016F020008",
            "80b01579020064"
    };

    private Features mFeatures = new Features();

    static byte[] byteAPDU = null;
    static byte[] respAPDU = null;

    static byte[] byteAPDU2 = null;
    static byte[] respAPDU2 = null;

    private ProgressDialog mProgressDialog;
    static boolean fImageFromCamLoaded = false;
    public Uri imageUri;

    public CardAcquireFragment mcardAcquireFragment;

    public JSONObject resultCompare;

    public static Context getAppContext() {
        return JAppActivity.context;
    }

    public byte[] getByteImage() {
        return JAppActivity.byteImage;
    }

    public final static int PURPOSE = 0;
    public final static int CONTACT_NUMBER = 1;
    public final static int CENSUS_ADDRESS = 2;
    public final static int MARIAGE_STATUS = 3;
    public final static int OCCUPATION = 4;
    public final static int COMPANY = 5;
    public final static int COMPANY_ADDRSS = 6;
    public final static int INCOME = 7;
    public final static int MAX_FORM_FIELDS = 8;
    public String[] fieldsList = new String[MAX_FORM_FIELDS];

    //registration information
    public String mIMEI;
    public String mCompanyName;
    public String mName;
    public String mBranch;
    public String mPhone;
    public String mEMail;

    //registration person mobile phone
    public String mPhonePerson;

    public String getMobilePhone() {
        return mPhone;
    }


    private String mAuthenPinCode;

    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        JAppActivity.context = getApplicationContext();
        setContentView(R.layout.activity_japp);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent().getBooleanExtra("ToFormFill", false)) {
            showFormFillFragment();
            return;
        }

        imageBuffer = new ImageView(this);

        mfStartFromRegister = getIntent().getBooleanExtra("from_register", false);
        boolean fVerifyIMEI = getIntent().getBooleanExtra("IMEI_VALIDATED", false);
        mIMEI = getIntent().getStringExtra("imei");
        mCompanyName = getIntent().getStringExtra("company");
        mName = getIntent().getStringExtra("name_th");
        mBranch = getIntent().getStringExtra("branch");
        mPhone = getIntent().getStringExtra("phone_no");
        mEMail = getIntent().getStringExtra("email");

        if (mfStartFromRegister) {
            showWaitForAuthorise();
        } else {
            if (fVerifyIMEI) {
                _showPinRegisterFragment();
            } else {
                showWaitForAuthorise();
            }
        }
    }

    private void showWaitForAuthorise() {
        final WaitForAuthoriseFragment fragment = new WaitForAuthoriseFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, fragment).addToBackStack(null).commit();
    }

    private void _showPinRegisterFragment() {
        final PinCodeFragment fragment = new PinCodeFragment();
        fragment.setPinAssign(PinCodeFragment.AUTHEN_PIN);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, fragment).addToBackStack(null).commit();
    }

    public void authenPinCode(String strPin) {
        mAuthenPinCode = strPin;
        AuthenPinCode();
    }

    public void showHomeFragment() {
        final HomeFragment fragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, fragment).commit();
    }

    public void showCardInformation() {
        final CardInfoFragment fragment = new CardInfoFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, fragment).commit();
    }

    public void showFaceCompareResult() {
        final FaceCompareResultFragment fragment = new FaceCompareResultFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, fragment).commit();
    }

    public void showPinRegisterFragment() {
        _showPinRegisterFragment();
    }

    public void captureFragment() {
        OpenCameraForCapture();
    }

    public void showFormFillFragment() {
        final FormFillFragment fragment = new FormFillFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, fragment).addToBackStack(null).commit();
    }

    public void showOTPVerifyFragment(int type) {
        mVerifyDipChip = type;
        final ConfirmOTPRegisterFragment fragment = new ConfirmOTPRegisterFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, fragment).addToBackStack(null).commit();
    }

    public void showOTPVerifyFragment(int type, String mPersonPhone) {
        mVerifyDipChip = type;
        mPhonePerson = mPersonPhone;
        final ConfirmOTPRegisterFragment fragment = new ConfirmOTPRegisterFragment(mPersonPhone);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, fragment).addToBackStack(null).commit();
    }

    public void showOTPVerifyUserFragment(String id, String otpRef, String mPhonePerson) {
        final ConfirmOTPRegisterUserFragment fragment = new ConfirmOTPRegisterUserFragment(id, otpRef, mPhonePerson);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, fragment).addToBackStack(null).commit();
    }

    public void successFragment() {
        final SuccessFragment fragment = new SuccessFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, fragment).commit();
    }

    //    public void acquireCardData(boolean fVerify) {
//        mfVerifyPerson = fVerify;
//        final CardAcquireFragment fragment = new CardAcquireFragment();
//        mcardAcquireFragment = fragment;
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.container_view, fragment).commit();
//    }
    public void acquireCardData(int type) {
        mfVerifyPerson = type;
        final CardAcquireFragment fragment = new CardAcquireFragment();
        mcardAcquireFragment = fragment;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, fragment).commit();
    }

    public void registerNextStep() {
        showPinRegisterFragment();
    }

    public void OpenCameraForCapture() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED) {
                String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permission, PERMISSION_CODE);
            } else {
                openCamera();
            }
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent resultIntent) {
        if ((resultCode == RESULT_OK) && (requestCode == IMAGE_CAPTURE_CODE)) {

            imageBuffer.setImageURI(imageUri);
            Log.d(TAG, imageUri.toString());
            try {
                Bitmap bitmap = convertImageViewToBitmap(imageBuffer);
                bitmap = getResizedBitmap(bitmap, 297, 355);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byteImageCam = stream.toByteArray();

            } catch (Exception e) {
                e.printStackTrace();
            }

            // Start image compare
            CompareImage compare = new CompareImage();
            compare.execute();
        }
    }


    /* ******************************************************* */
    /* Authenticate PIN Code                                   */
    /* ******************************************************* */

    public void AuthenPinCode() {
        mProgressDialog = ProgressDialog.show(JAppActivity.this,
                null, "กำลังทำการขออนุญาตเข้าระบบ", true, false);

        RequestVrifyPin request = new RequestVrifyPin();
        request.setImei(mIMEI);
        request.setPin(mAuthenPinCode);

        Device service = RetrofitInstance.getRetrofitInstance().create(Device.class);
        Call<ResponVerifyPin> call = service.checkPin(request);

        call.enqueue(new Callback<ResponVerifyPin>() {
            @Override
            public void onResponse(Call<ResponVerifyPin> call, Response<ResponVerifyPin> response) {
                if (response.isSuccessful()) {
                    ResponVerifyPin result = response.body();
                    if (result.getVerified()) {
                        showHomeFragment();
                    } else {
                        Toast.makeText(getAppContext(), "รหัสผ่านผิดกรุณากรอกใหม่", Toast.LENGTH_SHORT).show();
                        showPinRegisterFragment();
                    }
                } else {
                    Toast.makeText(getAppContext(), "รหัสผ่านผิดกรุณากรอกใหม่", Toast.LENGTH_SHORT).show();
                    showPinRegisterFragment();
                }
            }

            @Override
            public void onFailure(Call<ResponVerifyPin> call, Throwable t) {
                Log.d("error xx : ", t.toString());
            }
        });
        mProgressDialog.dismiss();
        mProgressDialog = null;

    }

    /* ******************************************************* */
    /* Facial Compare Routine                                  */
    /* ******************************************************* */
    private class CompareImage extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(JAppActivity.this,
                    null, "กำลังทำการเปรียบเทียบข้อมูล กรุณารอสักครู่", true, false);
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            JSONObject result = new JSONObject();
            try {
                result = _compareImage(Base64.encodeToString(byteImage, Base64.NO_WRAP),
                        Base64.encodeToString(byteImageCam, Base64.NO_WRAP));
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            resultCompare = result;
            try {
                if ((result != null) && (result.toString().length() != 0)
                        && (result.getInt("rtn") == 0 || result.getInt("rtn") == -6131)
                        && (result.getInt("pair_verify_similarity") >= 95)) {

                }
                mProgressDialog.dismiss();
                mProgressDialog = null;
                showFaceCompareResult();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private Bitmap convertImageViewToBitmap(ImageView v) {
        Bitmap bm = ((BitmapDrawable) v.getDrawable()).getBitmap();
        return bm;
    }

    private Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }


    public JSONObject _compareImage(String base64Image1, String base64Image2) throws TimeoutException, IOException {
        JSONObject responseJson = null;
        JSONObject requestParams = new JSONObject();
        try {
            requestParams.put("database_image_content", base64Image1);
            requestParams.put("database_image_type", 101);
            requestParams.put("query_image_content", base64Image2);
            requestParams.put("query_image_type", 301);
            requestParams.put("true_negative_rate", "99.9");
        } catch (JSONException e) {
            Log.e(TAG, "JsonException in requestparams makeup in FacialCompareActivity::compareImage", e);
        }

        try {
            final String USER_AGENT = "Mozilla/5.0";
            URL obj = new URL("http://203.150.199.227:9500/face/v1/algorithm/recognition/face_pair_verification");
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            String requestContent;
            requestContent = requestParams.toString();

            // TODO : THIS
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
    /* SAVE Data fields */
    /* ******************************************************* */

    public void SaveInformation() {
        Log.d("save", "SaveInformation");
        RequestCreateUser request = new RequestCreateUser();
        double income;
        String mStrDeviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        try {
            income = Double.parseDouble(fieldsList[INCOME]);
        } catch (NumberFormatException e) {
            income = 0;
        }

        request.setNameTh(generalInformation[THAIFULLNAME]);
        request.setNameEn(generalInformation[ENGLISHFULLNAME]);
        request.setBirthdate(generalInformation[BIRTH]);
        request.setId(generalInformation[CID]);
        request.setGender(generalInformation[GENDER]);
        request.setOfficialAddress(generalInformation[ADDRESS]);
        request.setNationality("Thai");
        request.setContactNumber(fieldsList[CONTACT_NUMBER]);
        request.setPurpose(fieldsList[PURPOSE]);
        request.setCurrentAddress(fieldsList[CENSUS_ADDRESS]);
        request.setMariageStatus(fieldsList[MARIAGE_STATUS]);
        request.setOccupation(fieldsList[OCCUPATION]);
        request.setCompany(fieldsList[COMPANY]);
        request.setCompanyAddress(fieldsList[COMPANY_ADDRSS]);
        request.setIncome(income);
        request.setVerifyBy(mStrDeviceID);
        request.setPhoto(Base64.encodeToString(byteImage, Base64.NO_WRAP));

        createUser(request);

    }

    public void PutInformationForPerson() {
        String mStrDeviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        RequestPutUser request = new RequestPutUser();

        request.setNameTh(generalInformation[THAIFULLNAME]);
        request.setNameEn(generalInformation[ENGLISHFULLNAME]);
        request.setBirthdate(generalInformation[BIRTH]);
        request.setId(generalInformation[CID]);
        request.setGender(generalInformation[GENDER]);
        request.setOfficialAddress(generalInformation[ADDRESS]);
        request.setNationality("Thai");
        request.setVerifyBy(mStrDeviceID);
        request.setPhoto(Base64.encodeToString(byteImage, Base64.NO_WRAP));

        putUser(request);
    }

    private void createUser(RequestCreateUser requestCreateUser) {
        User service = RetrofitInstance.getRetrofitInstance().create(User.class);
        Call<ResponseCreateUser> call = service.createUser(requestCreateUser);
        call.enqueue(new Callback<ResponseCreateUser>() {
            @Override
            public void onResponse(Call<ResponseCreateUser> call, Response<ResponseCreateUser> response) {
                if (response.isSuccessful()) {
                    ResponseCreateUser resutl = response.body();
                    if (resutl.getStatusCode() == 409) {
                        alertDialogPhone();
                    } else if (resutl.getStatusCode() == 200) {
                        Log.d("success xxx : ", resutl.getStatusCode().toString());
                        Log.d("onResponse: xx ", resutl.getMessage());
                        sentConfirmOtp(generalInformation[CID], resutl);
                    }

                } else {
                    Log.d("not success", "response ไม่สำเร็จ");
                    Toast.makeText(getAppContext(), "response ไม่สำเร็จ", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseCreateUser> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }

    public void getUser(String id) {
        User service = RetrofitInstance.getRetrofitInstance().create(User.class);
        Call<UserInformation> call = service.getUser(id);
        call.enqueue(new Callback<UserInformation>() {
            @Override
            public void onResponse(Call<UserInformation> call, Response<UserInformation> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    UserInformation result = response.body();
                    if (result.getPortraitUrl() == null){
                        alertDialogPutUser("ไม่สามารถยินยันได้ กรุณายทำรายการ ekyc ก่อน");
                    }else {
                        String imageUrl = result.getPortraitUrl();
                        Log.d("onResponse: ",imageUrl);
                        byte[] image = new byte[0];

                        try {
                            image = recoverImageFromUrl(imageUrl);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (compareImageUrl(image)) {
                            Toast.makeText(getAppContext(), "สำเร็จ", Toast.LENGTH_SHORT).show();
                            PutInformationForPerson();
                        } else {
                            alertDialogPutUser("ไม่สามารถยินยันได้ กรุณายทำ ekyc ก่อน");
                        }
                    }

                } else {
                    Toast.makeText(getAppContext(), "ไม่สามารถยินยันได้ กรุณายทำ ekyc ก่อน", Toast.LENGTH_SHORT).show();
                    alertDialogPutUser("ไม่สามารถยินยันได้ กรุณายทำ ekyc ก่อน");
                }
            }


            @Override
            public void onFailure(Call<UserInformation> call, Throwable t) {

            }
        });
    }

    public byte[] recoverImageFromUrl(String urlText) throws Exception {
        URL url = new URL(urlText);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (InputStream inputStream = url.openStream()) {
            int n = 0;
            byte [] buffer = new byte[ 1024 ];
            while (-1 != (n = inputStream.read(buffer))) {
                output.write(buffer, 0, n);
            }
        }

        return output.toByteArray();
    }


    private boolean compareImageUrl(byte[] image) {
        mProgressDialog = ProgressDialog.show(JAppActivity.this,
                null, "กำลังส่ง OTP กรุณารอสักครู่", true, false);

        String imageDB = Base64.encodeToString(byteImage, Base64.NO_WRAP);
        String imageURL = Base64.encodeToString(image, Base64.NO_WRAP);
//        String imageURL = image;
        Log.d("ssss: xx ", imageURL + " " + image.getClass().getName());
        Log.d("compareImageUrl: xx", imageDB + " " + imageDB.getClass().getName());
        JSONObject result = new JSONObject();
        try {
            result = _compareImage(imageDB, imageURL);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if ((result != null) && (result.toString().length() != 0)
                    && (result.getInt("rtn") == 0 || result.getInt("rtn") == -6131)
                    && (result.getInt("pair_verify_similarity") >= 95)) {

            }
            mProgressDialog.dismiss();
            mProgressDialog = null;
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mProgressDialog.dismiss();
        mProgressDialog = null;
        return false;
    }

    private void putUser(RequestPutUser requestPutUser) {
        User service = RetrofitInstance.getRetrofitInstance().create(User.class);
        Call<ResponseVerifyUser> call = service.editteUser(requestPutUser.getId(), requestPutUser);
        call.enqueue(new Callback<ResponseVerifyUser>() {
            @Override
            public void onResponse(Call<ResponseVerifyUser> call, Response<ResponseVerifyUser> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 404) {
                        Toast.makeText(getAppContext(), "กรุณาทำ ekyc มาก่อน", Toast.LENGTH_SHORT).show();
                        alertDialogPutUser("กรุณาทำ ekyc มาก่อน");
                    } else {
                        Toast.makeText(getAppContext(), "ยืนยันสำเร็จ", Toast.LENGTH_SHORT).show();
                        successFragment();
                    }
                } else {
                    Toast.makeText(getAppContext(), "กรุณาทำ ekyc มาก่อน", Toast.LENGTH_SHORT).show();
                    alertDialogPutUser("กรุณาทำ ekyc มาก่อน");
                }
            }

            @Override
            public void onFailure(Call<ResponseVerifyUser> call, Throwable t) {
                Toast.makeText(getAppContext(), "ปปปหหหหหหปป", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sentConfirmOtp(String id, ResponseCreateUser result) {
        OtpRef otpRef = result.getOtpRef();
        String otp = otpRef.getOtpRef();
        mProgressDialog = ProgressDialog.show(JAppActivity.this,
                null, "กำลังส่ง OTP กรุณารอสักครู่", true, false);
        mProgressDialog.dismiss();
        mProgressDialog = null;
        // if save success then show success fragment
        if (otp != null) {
            showOTPVerifyUserFragment(id, otp, mPhonePerson);
        }
    }
    /* ******************************************************* */
    /* Citizen Card Reader Routine
    /* ******************************************************* */

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {

                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            new OpenTask().execute(device);
                        }
                    } else {
                        mcardAcquireFragment.updateEventLog(false, false, "ไม่ได้รัรบอนุญาตให้เข้าถึง USB");
                    }
                }
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                synchronized (this) {
                    deviceName = "";
                    for (UsbDevice device : mManager.getDeviceList().values()) {
                        if (mReader.isSupported(device)) {
                            deviceName = device.getDeviceName();
                            break;
                        }
                    }
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (device != null && device.equals(mReader.getDevice())) {
//                        icoUSB.setImageResource(R.drawable.ic_usb_off);
//                        TextUsb.setText("NO READER DETECTED");
//                        TextCard.setText("PLEASE INSERT CARD");
//                        icoCard.setImageResource(R.drawable.ic_icc_off);
//                        clearlog();
//                        mSendAPDUButton.setEnabled(false);

                        mReader.close();
                    }
                }
            }
        }
    };

    private class OpenTask extends AsyncTask<UsbDevice, Void, Exception> {
        @Override
        protected Exception doInBackground(UsbDevice... params) {
            Exception result = null;
            try {
                mReader.open(params[0]);
            } catch (Exception e) {
                result = e;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Exception result) {
            if (result != null) {
//                clearlog();
//                print("Please, disconnect and reconnect the reader again.");
                Toast.makeText(JAppActivity.this, "Please, disconnect and reconnect the reader again", Toast.LENGTH_SHORT).show();
                mcardAcquireFragment.updateEventLog(false, false, "กรุณาติดตั้งเครื่องอ่านบัตรใหม่");
            } else {
//                icoUSB.setImageResource(R.drawable.ic_usb_on);
//                TextUsb.setText(mReader.getReaderName());
                int numSlots = mReader.getNumSlots();
                if (numSlots > 0) {
                    iSlotNum = 0;
                }
                mFeatures.clear();
//                clearlog();
//                print("This program is distributed in the hope that it will be useful for educational purposes.  Enjoy! ");
                //Toast.makeText(JAppActivity.this, "Enjoy!", Toast.LENGTH_SHORT). show();
                mcardAcquireFragment.updateEventLog(true, false, "ติดตั้งเครื่องอ่านบัตรแล้ว");
            }
        }
    }

    public void initializeCardReader() {

        clearCardInformation();

        mManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        mReader = new Reader(mManager);
        mReader.setOnStateChangeListener(new Reader.OnStateChangeListener() {
            @Override
            public void onStateChange(int slotNum, int prevState, int currState) {
                if (prevState < Reader.CARD_UNKNOWN || prevState > Reader.CARD_SPECIFIC) {
                    prevState = Reader.CARD_UNKNOWN;
                }
                if (currState < Reader.CARD_UNKNOWN || currState > Reader.CARD_SPECIFIC) {
                    currState = Reader.CARD_UNKNOWN;
                }
                final int iActualState = currState;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (iActualState == 1) {
                            strCLA = "";
                            strINS = "";
                            strP1 = "";
                            strP2 = "";
                            strLc = "";
                            strLe = "";
                            Toast.makeText(JAppActivity.this, "Please insert card", Toast.LENGTH_SHORT).show();
                            clearCardInformation();
                        }
                        if (iActualState == 2) {
                            vPowerOnCard();
                        }
                    }
                });
            }
        });

        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(mReceiver, filter);

        deviceName = "";
        for (UsbDevice device : mManager.getDeviceList().values()) {
            if (mReader.isSupported(device)) {
                deviceName = device.getDeviceName();
                Toast.makeText(JAppActivity.this, deviceName, Toast.LENGTH_SHORT).show();
                break;
            }
        }

        byteAPDU = null;
        byteAPDU2 = null;
        respAPDU = null;
        respAPDU2 = null;

        if (deviceName != "") {
            for (UsbDevice device : mManager.getDeviceList().values()) {
                if (deviceName.equals(device.getDeviceName())) {
//                    clearlog();
                    mManager.requestPermission(device, mPermissionIntent);
                    break;
                }
            }
        }

    }

    private void vPowerOnCard() {
        byte[] atr = null;
        int actionNum = 1;
        int preferredProtocols = (Reader.PROTOCOL_UNDEFINED | Reader.PROTOCOL_T0 | Reader.PROTOCOL_T1);
        int activeProtocol = 0;

        if (iSlotNum >= 0) {
            if (actionNum < Reader.CARD_POWER_DOWN || actionNum > Reader.CARD_WARM_RESET) {
                actionNum = Reader.CARD_WARM_RESET;
            }
            try {
                //TODO : 1 ERROR this
                atr = mReader.power(iSlotNum, actionNum);
            } catch (ReaderException e1) {
                e1.printStackTrace();
            }

            if (atr != null) {
                try {
//                    TextCard.setText(getHexString(atr, atr.length));
//                    icoCard.setImageResource(R.drawable.ic_icc_on);
//                    mSendAPDUButton.setEnabled(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                activeProtocol = mReader.setProtocol(iSlotNum, preferredProtocols);
            } catch (ReaderException e) {
                e.printStackTrace();
            }
            String activeProtocolString = "Transmission Protocol ";
            switch (activeProtocol) {
                case Reader.PROTOCOL_T0:
                    activeProtocolString += "T=0";
                    break;
                case Reader.PROTOCOL_T1:
                    activeProtocolString += "T=1";
                    break;
                default:
                    activeProtocolString += "Unknown";
                    break;
            }
            mcardAcquireFragment.updateEventLog(true, false, "การ์ดถูกเสียบเข้าเครื่องอ่านแล้ว");
            vShowCardProtocol(activeProtocolString);

            SelectApplet();

        }
    }

    public void StartGrapInformation() {
//        mProgressDialog = ProgressDialog.show(JAppActivity.this,
//                null, "กำลังทำการอ่านข้อมูลจากบัตร", true, false);
        boolGetImage = true;
        iCurrentImageChunk = 0;
        byteImage = null;
        strDataIn = imageRetrieve[iCurrentImageChunk];
        bSendAPDU();
    }

    private void SelectApplet() {
        strDataIn = "00a4040008a000000054480001";
        bSendAPDU();
    }

    private boolean bSendAPDU() {
//        HideKbd();

        byteAPDU = null;
        byteAPDU2 = null;
        respAPDU = null;
        respAPDU2 = null;

        String StringAPDU = null;

        String StringCLA = strCLA;
        String StringINS = strINS;
        String StringP1 = strP1;
        String StringP2 = strP2;
        String StringLc = strLc;
        String StringDataIn = strDataIn;
        String StringLe = strLe;

//        if (!mCheckRaw.isChecked())
        if (false) {
            if ((StringCLA.length() == 0) || (StringINS.length() == 0) || (StringP1.length() == 0) || (StringP2.length() == 0) || ((StringDataIn.length() % 2) != 0)) {
                return false;
            }
            if (!StringLc.contentEquals("")) {
                if (StringDataIn.length() != (((int) Long.parseLong(StringLc, 16)) * 2)) {
                    return false;
                }
            }
            if (StringLe.length() == 1) {
                StringLe = "0" + StringLe;
                //editLe.setText(StringLe);
                strLe = StringLe;
            }
            if (StringLc.length() == 1) {
                StringLc = "0" + StringLc;
//                editLc.setText(StringLc);
                strLc = StringLc;
            }
            if (StringP2.length() == 1) {
                StringP2 = "0" + StringP2;
//                editP2.setText(StringP2);
                strP2 = StringP2;
            }
            if (StringP1.length() == 1) {
                StringP1 = "0" + StringP1;
//                editP1.setText(StringP1);
                strP1 = StringP1;
            }
            if (StringINS.length() == 1) {
                StringINS = "0" + StringINS;
//                editINS.setText(StringINS);
                strINS = StringINS;
            }
            if (StringCLA.length() == 1) {
                StringCLA = "0" + StringCLA;
//                editCLA.setText(StringCLA);
                strCLA = StringCLA;
            }
        }

        if (true) {
//            StringAPDU = editDataIn.getText().toString();
            StringAPDU = strDataIn;
            if (((StringAPDU.length() % 2) != 0) || (StringAPDU.length() < 1)) {
                return false;
            }
        }
//        else
//        {
//            StringAPDU = StringCLA + StringINS + StringP1 + StringP2 + StringLc + StringDataIn + StringLe;
//        }

        if (StringAPDU.length() < 8) {
            return false;
        }

        byteAPDU = atohex(StringAPDU);
        respAPDU = transceives(byteAPDU);

        if (true) {
            try {
                vShowResponseInterpretation(respAPDU);
            } catch (Exception e) {
//                clearlog();
//                print("Response is not TLV format !!!");
            }
        }

        return true;
    }

    private byte[] transceives(byte[] data) {
        byte[] response = new byte[512];
        int responseLength = 0;
        try {
//            print("***COMMAND APDU***");
//            print("");
//            print("IFD - " + getHexString(data,data.length))
            Log.i("FacialCompareActivity", "transceives: " + getHexString(data, data.length));
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        if (iSlotNum >= 0) {
            try {
                responseLength = mReader.transmit(iSlotNum, data, data.length, response, response.length);
            } catch (Exception e) {
//                print("****************************************");
//                print("       ERROR transmit: Review APDU  ");
//                print("****************************************");
//                Toast.makeText(FacialCompareActivity.this, "Error transmit: Review APDU", Toast.LENGTH_SHORT).show();
                responseLength = 0;
                byte[] ra = Arrays.copyOf(response, responseLength);
                response = null;
                return (ra);
            }
            try {
                byte[] ra2 = Arrays.copyOf(response, responseLength);
                respAPDU2 = ra2;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        if(mCheckAutoSend.isChecked())
        {
            if ((response[0] == 0x61) || (response[0] == 0x6C)) {
                byte[] GetResponse = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
                if (response[0] == 0x6C) {
                    GetResponse[0] = data[0];
                    GetResponse[1] = data[1];
                    GetResponse[2] = data[2];
                    GetResponse[3] = data[3];
                    GetResponse[4] = response[1];
                } else {
                    GetResponse[0] = (byte) 0x00;
                    GetResponse[1] = (byte) 0xC0;
                    GetResponse[2] = (byte) 0x00;
                    GetResponse[3] = (byte) 0x00;
                    GetResponse[4] = response[1];
                }
                try {
//                    print("IFD - " + getHexString(GetResponse,GetResponse.length));
                    byteAPDU2 = GetResponse;
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                if (iSlotNum >= 0) {
                    try {
                        responseLength = mReader.transmit(iSlotNum, GetResponse, GetResponse.length, response, response.length);
                    } catch (Exception e) {
                        responseLength = 0;
                        byte[] ra = Arrays.copyOf(response, responseLength);
                        response = null;
                        return (ra);
                    }
                    try {

//                        String strResponse = getHexString(response, responseLength);
//                        String strApplet = strResponse.substring(6, 29);
//                        if (strApplet.equals("A0 00 00 00 54 48 00 01")) {
//                            iCurrentInfoChunk = 0;
//                            iCurrentImageChunk = 0;
//                            boolGetImage = true;
//                            byteImage = null;
//                            strDataIn = imageRetrieve[iCurrentImageChunk];
//                            bSendAPDU();
//                        } else {

                        if (boolGetImage) {
                            // save image chunk into byteImage
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            if (iCurrentImageChunk != 0)
                                outputStream.write(byteImage);
                            outputStream.write(response, 0, responseLength - 2);
                            byteImage = outputStream.toByteArray();

                            if (iCurrentImageChunk == 19) {
                                boolGetImage = false;
                                finishImageRetrieval();

                                boolGetInformation = true;
                                iCurrentInfoChunk = 0;
                                strDataIn = generalRetrieve[iCurrentInfoChunk];
                                bSendAPDU();
                            } else {
                                iCurrentImageChunk++;
                                strDataIn = imageRetrieve[iCurrentImageChunk];
                                bSendAPDU();
                            }
                        } else if (boolGetInformation) {
                            // convert data from card to string and put into
                            // generalInformation[]
                            if (iCurrentInfoChunk == 8) {
                                generalInformation[iCurrentInfoChunk] = TrimData(new String(Arrays.copyOfRange(response, 0, response.length - 2), "TIS620"));
                            } else {
                                generalInformation[iCurrentInfoChunk] = TrimData(new String(Arrays.copyOfRange(response, 0, response.length - 2), "TIS620"));
                            }
                            if (iCurrentInfoChunk == iMaxInfoChunk - 1) {
                                boolGetInformation = false;
                                iCurrentImageChunk = 0;
                                iCurrentInfoChunk = 0;
                                //finishGeneralInformation();
                                // ^^^ just store
//                                    mProgressDialog.dismiss();
//                                    mProgressDialog = null;
                                JAppActivity.this.mcardAcquireFragment.updateEventLog(true, true, "อ่านข้อมูลจากบัตรแล้ว");
                                JAppActivity.this.mcardAcquireFragment.setNextStep();
                            } else {
                                iCurrentInfoChunk++;
                                strDataIn = generalRetrieve[iCurrentInfoChunk];
                                bSendAPDU();
                            }
                        }
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                respAPDU2 = null;
                byteAPDU2 = null;
            }
        }
        byte[] ra = Arrays.copyOf(response, responseLength);
        response = null;
        return (ra);
    }

    static void finishImageRetrieval() {

        File photo = new File(Environment.getExternalStorageDirectory(), "scrooge.jpg");
        if (photo.exists()) {
            photo.delete();
        }

        try {
//            Toast.makeText(this, photo.getPath(), Toast.LENGTH_LONG).show();
            FileOutputStream fos = new FileOutputStream(photo.getPath());
            fos.write(byteImage);
            fos.close();
        } catch (java.io.IOException e) {
            Log.e("PictureDemo", "Exception in photoCallback", e);
        }

        Bitmap bm = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
//        DisplayMetrics dm = new DisplayMetrics();
//        ((AppCompatActivity)FacialCompareActivity.getAppContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);

//        imageFromCard.setMinimumHeight(metrics.heightPixels);
//        imageFromCard.setMinimumWidth(metrics.widthPixels);
//        imageFromCard.setImageBitmap(bm);

    }


    private static void vShowResponseInterpretation(byte[] data) {
//        print("");
//        print("====================================");
//        print("RESPONSE INTERPRETATION:");

        if (data.length > 2) {
            byte[] sw12 = new byte[2];
            System.arraycopy(data, data.length - 2, sw12, 0, 2);
            byte[] payload = Arrays.copyOf(data, (data.length) - 2);
            try {
//                print("SW1-SW2 " + getHexString(sw12,sw12.length) + RetStatusWord.getSWDescription(Util.szByteHex2String(sw12[0]) + Util.szByteHex2String(sw12[1])));
                //textInformation.setText("SW1-SW2 " + getHexString(sw12,sw12.length) + RetStatusWord.getSWDescription(Util.szByteHex2String(sw12[0]) + Util.szByteHex2String(sw12[1])));
            } catch (Exception e) {
//                print("Error Processing Response");
//                textInformation.setText("Error Processing Response");
            }
//            print(EmvInterpreter.ShowEMV_Interpretation(payload));


        } else if (data.length == 2) {
            byte[] sw12 = new byte[2];
            System.arraycopy(data, data.length - 2, sw12, 0, 2);
            try {
//                print("SW1-SW2 " + getHexString(sw12,sw12.length) );
//                print(RetStatusWord.getSWDescription(Util.szByteHex2String(sw12[0]) + Util.szByteHex2String(sw12[1])));
//                textInformation.setText("SW1-SW2 " + getHexString(sw12,sw12.length));
            } catch (Exception e) {
//                print("Error Processing Response");
//                textInformation.setText("Error Processing Response");
            }
        }
//        print("====================================");
        return;
    }

    private static String getHexString(byte[] data, int slen) throws Exception {
        String szDataStr = "";
        for (int ii = 0; ii < slen; ii++) {
            szDataStr += String.format("%02X ", data[ii] & 0xFF);
        }
        return szDataStr;
    }

    private static byte[] atohex(String data) {
        String hexchars = "0123456789abcdef";

        data = data.replaceAll(" ", "").toLowerCase();
        if (data == null) {
            return null;
        }
        byte[] hex = new byte[data.length() / 2];

        for (int ii = 0; ii < data.length(); ii += 2) {
            int i1 = hexchars.indexOf(data.charAt(ii));
            int i2 = hexchars.indexOf(data.charAt(ii + 1));
            hex[ii / 2] = (byte) ((i1 << 4) | i2);
        }
        return hex;
    }

    private void vShowCardProtocol(String activeProtocolString) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, activeProtocolString, duration);
        toast.show();
    }

    private void checkDiskPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "no disk access", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            Toast.makeText(this, "disk access - OK", Toast.LENGTH_LONG).show();
        }
    }

    static void clearCardInformation() {
        fImageFromCamLoaded = false;
        generalInformation = new String[9];
    }

    private static String TrimData(String strData) {
        String strReturn = new String();
        boolean fHash = false;
        for (int idx = 0; idx < strData.length(); idx++) {
            int ch = strData.charAt(idx);
            if (ch == '#') {
                if (fHash == false) {
                    fHash = true;
                    strReturn += " ";
                }
//            } else if ((strData.charAt(idx)==0x20) ||
//                    (strData.charAt(idx)==0) ||
//                    (strData.charAt(idx)==0x90)) {
            } else if ((ch == 0) ||
                    (ch == 0x90)) {
                fHash = false;
                break;
            } else {
                fHash = false;
                strReturn += strData.charAt(idx);
            }
            int ixx = ch;
            Log.d(TAG, Integer.toString(ixx));
        }
        return strReturn;
    }

    public String toHex(String arg) throws UnsupportedEncodingException {
        return String.format("%040x__", new BigInteger(1, arg.getBytes("TIS620")));
    }


    /* ******************************************************* */
    /* ******************************************************* */

    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        View focusedView = getCurrentFocus();

        if (focusedView != null) {
            inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void alertDialogPhone() {
        new AlertDialog.Builder(this)
                .setMessage("เบอร์โทรศัพท์นี้ถูกใช้งานแล้ว กรุณาเปลี่ยนใหม่")
                .setCancelable(false)
                .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        showFormFillFragment();
                    }
                })
                .show();

    }

    private void alertDialogPutUser(String text) {
        new AlertDialog.Builder(this)
                .setMessage(text)
                .setCancelable(false)
                .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        showHomeFragment();
                    }
                })
                .show();

    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("ต้องการหยุดทำรายการ และออกจาก application หรือไม่?")
                .setCancelable(false)
                .setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("ยกเลิก", null)
                .show();
    }


}
