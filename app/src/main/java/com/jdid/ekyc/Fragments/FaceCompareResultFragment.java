package com.jdid.ekyc.Fragments;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.jdid.ekyc.JAppActivity;
import com.jdid.ekyc.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class FaceCompareResultFragment extends Fragment {

    private static final int VERIFY_EKYC = 0;
    private static final int VERIFY_PERSON = 1;
    private static final int VERIFY_DIP_CHIP = 2;

    private ImageView imageFromCard;
    private ImageView imageFromCam;
    private TextView txtResult;
    private TextView txtResultDescription;
    private TextView txtScore;
    private Button btnNextStep;
    private byte[] byteImage;
    private String byteImageUrl;
    private boolean mfNextStep = false;
    private Spinner spCompanyRef;
    private TextView tvRefBy;

    double result;
    String companyRef;

    public FaceCompareResultFragment() {
    }

    public FaceCompareResultFragment(double result, String image) {
        this.result = result;
        this.byteImageUrl = image;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_face_compare_result, container, false);
        setHasOptionsMenu(true);
        initialize(view);
        return view;
    }


    private void initialize(View view) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        spCompanyRef = view.findViewById(R.id.spCompanyRef);
        tvRefBy = view.findViewById(R.id.tvRefBy);
        switch (((JAppActivity) getActivity()).isVerifyPerson()) {
            case VERIFY_EKYC:
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.kyc_title);
                spCompanyRef.setVisibility(View.GONE);
                tvRefBy.setVisibility(View.GONE);
                break;
            case VERIFY_PERSON:
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.verify_person);
                break;
            case VERIFY_DIP_CHIP:
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.dip_chip);
                break;
        }

        final String[] companyName = getResources().getStringArray(R.array.companyList);
        ArrayAdapter<String> spComAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, companyName);
        spCompanyRef.setAdapter(spComAdapter);

        spCompanyRef.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(),
//                        "Select : " + companyName[position],
//                        Toast.LENGTH_SHORT).show();

                companyRef = companyName[position];
                ((JAppActivity) getActivity()).fieldsList[JAppActivity.REF_COMPANY] = companyRef;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnNextStep = view.findViewById(R.id.btnNextStep);

        txtResult = view.findViewById(R.id.tvResult);
        txtResultDescription = view.findViewById(R.id.txtResultDescription);
        txtScore = view.findViewById(R.id.tvScore);
        imageFromCard = view.findViewById(R.id.imageFromCard);
        imageFromCam = view.findViewById(R.id.imageFromCam);
        byteImage = ((JAppActivity) getActivity()).getByteImage();
        Bitmap bm = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
        DisplayMetrics metrics = new DisplayMetrics();


        if (byteImageUrl != null) {
            byte[] decodedString = Base64.decode(byteImageUrl, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageFromCam.setImageBitmap(decodedByte);
            imageFromCam.setMinimumHeight(metrics.heightPixels);
            imageFromCam.setMinimumWidth(metrics.widthPixels);
        } else {
            imageFromCam = view.findViewById(R.id.imageFromCam);
            imageFromCam.setImageURI(((JAppActivity) getActivity()).imageUri);
            imageFromCam.setMinimumHeight(metrics.heightPixels);
            imageFromCam.setMinimumWidth(metrics.widthPixels);
        }
        imageFromCard.setMinimumHeight(metrics.heightPixels);
        imageFromCard.setMinimumWidth(metrics.widthPixels);
        imageFromCard.setImageBitmap(bm);

        WindowManager windowManager = (WindowManager) ((JAppActivity) getActivity()).getAppContext()
                .getSystemService(((JAppActivity) getActivity()).getAppContext().WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);

        checkResultBuidu(result);

        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mfNextStep) {
                    switch (((JAppActivity) getActivity()).isVerifyPerson()) {
                        case VERIFY_EKYC:
                            ((JAppActivity) getActivity()).showFormFillFragment();
                            break;
                        case VERIFY_PERSON:
                            ((JAppActivity) getActivity()).PutInformationForPerson(VERIFY_PERSON);
                            break;
                        case VERIFY_DIP_CHIP:
                            ((JAppActivity) getActivity()).PutInformationForPerson(VERIFY_DIP_CHIP);
                            break;
                    }
                } else {
                    ((JAppActivity) getActivity()).OpenCameraForCapture();
                }
            }
        });

    }

    private void checkResultBuidu(double result) {
        String number = new DecimalFormat("0.00").format(result);
//        String re = String.valueOf(result);
        if (result >= 80) {
            txtResult.setText(R.string.compare_success);
            txtResultDescription.setText(R.string.compare_success_description);
            txtScore.setText(number);
            txtResultDescription.setTextColor(getResources().getColor(R.color.success_color));
            btnNextStep.setText(R.string.next_step);
            mfNextStep = true;
        } else {
            Log.d("image score", "checkResultBuidu: " + result);
            Toast.makeText(getContext(), result+"", Toast.LENGTH_SHORT).show();
            txtResult.setText(R.string.compare_fail);
            txtResultDescription.setText(R.string.compare_fail_description);
            txtResultDescription.setTextColor(getResources().getColor(R.color.error_color));
            txtScore.setText(number);
            btnNextStep.setText(R.string.try_again);
            mfNextStep = false;
        }
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
                .setMessage("ต้องการหยุดทำรายการ และกลับไปหน้าแสดงข้อมูลหรือไม่")
                .setCancelable(false)
                .setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((JAppActivity) getActivity()).showCardInformation();
                    }
                })
                .setNegativeButton("ยกเลิก", null)
                .show();
    }
}
