package com.jdid.ekyc.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.jdid.ekyc.JAppActivity;
import com.jdid.ekyc.R;

import static com.jdid.ekyc.JAppActivity.ADDRESS;
import static com.jdid.ekyc.JAppActivity.BIRTH;
import static com.jdid.ekyc.JAppActivity.CID;
import static com.jdid.ekyc.JAppActivity.ENGLISHFULLNAME;
import static com.jdid.ekyc.JAppActivity.EXPIRE;
import static com.jdid.ekyc.JAppActivity.GENDER;
import static com.jdid.ekyc.JAppActivity.ISSUE;
import static com.jdid.ekyc.JAppActivity.ISSUER;
import static com.jdid.ekyc.JAppActivity.THAIFULLNAME;

public class CardInfoFragment extends Fragment {

    private static final int VERIFY_EKYC = 0;
    private static final int VERIFY_PERSON = 1;
    private static final int VERIFY_DIP_CHIP = 2;

    private static final String TAG = "CardInforxxx : ";

    private ImageView imageFromCard;
    private EditText edCID;
    private EditText edThaiName;
    private EditText edEnglishName;
    private EditText edBirthDate;
    private EditText edGender;
    private EditText edAddress;
    private Button btnNextStep;
    private Button btnBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_card_information, container, false);
        setHasOptionsMenu(true);
        initialize(view);
        fillCardInformation();
        return view;
    }

    private void initialize(View view) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        switch (((JAppActivity) getActivity()).isVerifyPerson()) {
            case VERIFY_EKYC:
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.kyc_title);
                break;
            case VERIFY_PERSON:
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.verify_person);
                break;
            case VERIFY_DIP_CHIP:
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.dip_chip);
                break;
        }


        edCID = view.findViewById(R.id.edCID);
        edThaiName = view.findViewById(R.id.edThaiName);
        edEnglishName = view.findViewById(R.id.edEnglishName);
        edBirthDate = view.findViewById(R.id.edBirthDate);
        edGender = view.findViewById(R.id.edGender);
        edAddress = view.findViewById(R.id.edAddress);


        btnNextStep = view.findViewById(R.id.btnNextStep);
        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] generalInformation = ((JAppActivity) getActivity()).getGeneralInformation();
                if (((JAppActivity) getActivity()).isVerifyPerson() == VERIFY_DIP_CHIP){
                    ((JAppActivity) getActivity()).getUser(generalInformation[CID]);
                }else {
                    ((JAppActivity) getActivity()).captureFragment();
                }
            }
        });

        imageFromCard = view.findViewById(R.id.imageFromCard);
        imageFromCard.setImageResource(R.drawable.ic_credit_card_black_24dp);
        byte[] byteImage = ((JAppActivity) getActivity()).getByteImage();
        Bitmap bm = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
        // todo:: fixed imageFormCard
        if (bm == null){
            alertDialogImageNull("ไม่สามารถอ่านข้อมูลจากบัตรได้กรุณา ถอดบัตร แล้วทำรายการใหม่อีกครั้ง");
            btnNextStep.setEnabled(false);
        }else {
            Log.d(TAG, bm.toString());
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) ((JAppActivity) getActivity()).getApplicationContext()
                    .getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(metrics);
            imageFromCard.setMinimumHeight(metrics.heightPixels);
            imageFromCard.setMinimumWidth(metrics.widthPixels);
            imageFromCard.setImageBitmap(bm);
        }
    }

    private void fillCardInformation() {
        String[] generalInformation = ((JAppActivity) getActivity()).getGeneralInformation();
        edCID.setText(parsingCID(generalInformation[CID]));
        edThaiName.setText(generalInformation[THAIFULLNAME]);
        edEnglishName.setText(generalInformation[ENGLISHFULLNAME]);
        edBirthDate.setText(parsingDate(generalInformation[BIRTH]));
        edGender.setText(parsingSex(generalInformation[GENDER]));
        edAddress.setText(generalInformation[ADDRESS]);
    }


    private String parsingDate(String strDate) {
        String strReturn = strDate.substring(6, 8);
        strReturn += "/";
        strReturn += strDate.substring(4, 6);
        strReturn += "/";
        strReturn += strDate.substring(0, 4);
        return strReturn;
    }

    private String parsingSex(String strSex) {
        if (strSex.charAt(0) == '1') {
            return "ชาย";
        } else {
            return "หญิง";
        }
    }

    private String parsingCID(String strCID) {
        String strReturn = strCID.substring(0, 1);
        strReturn += "-";
        strReturn += strCID.substring(1, 5);
        strReturn += "-";
        strReturn += strCID.substring(5, 10);
        strReturn += "-";
        strReturn += strCID.substring(10, 12);
        strReturn += "-";
        strReturn += strCID.substring(12, 13);
        return strReturn;
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
                .setMessage("ต้องการกลับไปหน้าเมนูหลักหรือไม่")
                .setCancelable(false)
                .setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((JAppActivity) getActivity()).showHomeFragment();
                    }
                })
                .setNegativeButton("ยกเลิก", null)
                .show();
    }

    private void alertDialogImageNull(String text) {
        new AlertDialog.Builder(getContext())
                .setMessage(text)
                .setCancelable(false)
                .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((JAppActivity) getActivity()).showHomeFragment();
                    }
                })
                .show();

    }

}
