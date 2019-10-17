package com.jdid.ekyc.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
        initialize(view);
        fillCardInformation();
        return view;
    }

    private void initialize(View view) {
        edCID = view.findViewById(R.id.edCID);
        edThaiName = view.findViewById(R.id.edThaiName);
        edEnglishName = view.findViewById(R.id.edEnglishName);
        edBirthDate = view.findViewById(R.id.edBirthDate);
        edGender = view.findViewById(R.id.edGender);
        edAddress = view.findViewById(R.id.edAddress);

        if (((JAppActivity)getActivity()).isVerifyPerson()==false) {
            ((TextView)view.findViewById(R.id.txtTitle)).setText(R.string.kyc_title);
        } else {
            ((TextView)view.findViewById(R.id.txtTitle)).setText(R.string.verify_person);
        }

        btnNextStep = view.findViewById(R.id.btnNextStep);
        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((JAppActivity) getActivity()).captureFragment();
            }
        });

        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((JAppActivity)getActivity()).acquireCardData(((JAppActivity)getActivity()).isVerifyPerson());
            }
        });

        imageFromCard = view.findViewById(R.id.imageFromCard);
        imageFromCard.setImageResource(R.drawable.ic_credit_card_black_24dp);
        byte[] byteImage = ((JAppActivity)getActivity()).getByteImage();
        Bitmap bm = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) ((JAppActivity)getActivity()).getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        imageFromCard.setMinimumHeight(metrics.heightPixels);
        imageFromCard.setMinimumWidth(metrics.widthPixels);
        imageFromCard.setImageBitmap(bm);
    }

    private void fillCardInformation() {
        String[] generalInformation = ((JAppActivity)getActivity()).getGeneralInformation();
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
        if (strSex.charAt(0)=='1') {
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

}
