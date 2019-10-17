package com.jdid.ekyc.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jdid.ekyc.JAppActivity;
import com.jdid.ekyc.R;

import org.json.JSONException;
import org.json.JSONObject;

public class FaceCompareResultFragment extends Fragment {

    private ImageView imageFromCard;
    private ImageView imageFromCam;
    private TextView txtResult;
    private TextView txtResultDescription;
    private Button btnNextStep;
    private Button btnBack;
    private byte[] byteImage;
    private boolean mfNextStep = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_face_compare_result, container, false);
        initialize(view);
        return view;
    }

    private void initialize(View view) {
        if (((JAppActivity)getActivity()).isVerifyPerson()==false) {
            ((TextView)view.findViewById(R.id.txtTitle)).setText(R.string.kyc_title);
        } else {
            ((TextView)view.findViewById(R.id.txtTitle)).setText(R.string.verify_person);
        }
        btnNextStep = view.findViewById(R.id.btnNextStep);
        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((JAppActivity)getActivity()).showCardInformation();
            }
        });
        txtResult = view.findViewById(R.id.txtResult);
        txtResultDescription = view.findViewById(R.id.txtResultDescription);
        imageFromCard = view.findViewById(R.id.imageFromCard);
        imageFromCam = view.findViewById(R.id.imageFromCam);

        imageFromCam.setImageURI(((JAppActivity)getActivity()).imageUri);
        byteImage = ((JAppActivity)getActivity()).getByteImage();
        Bitmap bm = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) ((JAppActivity)getActivity()).getAppContext()
                .getSystemService(((JAppActivity)getActivity()).getAppContext().WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);

        imageFromCard.setMinimumHeight(metrics.heightPixels);
        imageFromCard.setMinimumWidth(metrics.widthPixels);
        imageFromCard.setImageBitmap(bm);

        checkResult();

        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mfNextStep) {
                    if (((JAppActivity)getActivity()).isVerifyPerson()) {
                        ((JAppActivity)getActivity()).successFragment();
                    } else {
                        ((JAppActivity)getActivity()).showFormFillFragment();
                    }
                } else {
                    ((JAppActivity)getActivity()).OpenCameraForCapture();
                }
            }
        });

    }

    private void checkResult() {
        JSONObject result = ((JAppActivity)getActivity()).resultCompare;
        try {
            if ((result != null) && (result.toString().length() != 0)
                    && (result.getInt("rtn") == 0 || result.getInt("rtn") == -6131)
                    && (result.getInt("pair_verify_similarity")>=95)) {
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
}
