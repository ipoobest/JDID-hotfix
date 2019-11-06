package com.jdid.ekyc.Fragments;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

public class FaceCompareResultFragment extends Fragment {

    private static final int VERIFY_EKYC = 0;
    private static final int VERIFY_PERSON = 1;
    private static final int VERIFY_DIP_CHIP = 2;

    private ImageView imageFromCard;
    private ImageView imageFromCam;
    private TextView txtResult;
    private TextView txtResultDescription;
    private Button btnNextStep;
    private byte[] byteImage;
    private boolean mfNextStep = false;


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

//        if (((JAppActivity) getActivity()).isVerifyPerson() == VERIFY_EKYC) {
//            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.kyc_title);
//        } else if (((JAppActivity) getActivity()).isVerifyPerson() == VERIFY_PERSON) {
//            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.verify_person);
//        } else if ((((JAppActivity) getActivity()).isVerifyPerson() == VERIFY_DIP_CHIP)) {
//            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.dip_chip);
//        }
        btnNextStep = view.findViewById(R.id.btnNextStep);

        txtResult = view.findViewById(R.id.txtResult);
        txtResultDescription = view.findViewById(R.id.txtResultDescription);
        imageFromCard = view.findViewById(R.id.imageFromCard);
        imageFromCam = view.findViewById(R.id.imageFromCam);

        imageFromCam.setImageURI(((JAppActivity) getActivity()).imageUri);
        byteImage = ((JAppActivity) getActivity()).getByteImage();
        Bitmap bm = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) ((JAppActivity) getActivity()).getAppContext()
                .getSystemService(((JAppActivity) getActivity()).getAppContext().WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);

        imageFromCard.setMinimumHeight(metrics.heightPixels);
        imageFromCard.setMinimumWidth(metrics.widthPixels);
        imageFromCard.setImageBitmap(bm);

        checkResult();

        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mfNextStep) {
                    switch (((JAppActivity) getActivity()).isVerifyPerson()) {
                        case VERIFY_EKYC:
                            ((JAppActivity) getActivity()).showFormFillFragment();
                            break;
                        case VERIFY_PERSON:
                            ((JAppActivity) getActivity()).successFragment();
                            break;
                        case VERIFY_DIP_CHIP:
                            ((JAppActivity) getActivity()).showOTPVerifyFragment(VERIFY_DIP_CHIP);
                            break;
                    }
                } else {
                    ((JAppActivity) getActivity()).OpenCameraForCapture();
                }
            }
        });

    }

    private void checkResult() {
        JSONObject result = ((JAppActivity) getActivity()).resultCompare;
        try {
            if ((result != null) && (result.toString().length() != 0)
                    && (result.getInt("rtn") == 0 || result.getInt("rtn") == -6131)
                    && (result.getInt("pair_verify_similarity") >= 95)) {
                txtResult.setText(R.string.compare_success);
                txtResultDescription.setText(R.string.compare_success_description);
                txtResultDescription.setTextColor(getResources().getColor(R.color.success_color));
                btnNextStep.setText(R.string.next_step);
                mfNextStep = true;
            } else {
                txtResult.setText(R.string.compare_fail);
                txtResultDescription.setText(R.string.compare_fail_description);
                txtResultDescription.setTextColor(getResources().getColor(R.color.error_color));
                btnNextStep.setText(R.string.try_again);
                mfNextStep = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
