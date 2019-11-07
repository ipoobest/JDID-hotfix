package com.jdid.ekyc.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.crashlytics.android.Crashlytics;
import com.jdid.ekyc.JAppActivity;
import com.jdid.ekyc.R;

public class CardAcquireFragment extends Fragment implements CardAcquireInterface {

    private static final String TAG = "CardAcquireFragment";

    private static final int VERIFY_EKYC = 0;
    private static final int VERIFY_PERSON = 1;
    private static final int VERIFY_DIP_CHIP = 2;

    private Button btnNextStep;

    private int miCurrentLog;

    private int tvCount = 5;
    private TextView[] tvInfo = new TextView[tvCount];
    private ImageView[] imageInfo = new ImageView[tvCount];

    private boolean mfNextStep = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_card_acquire, container, false);
        setHasOptionsMenu(true);
        initialize(view);

        ((JAppActivity) getActivity()).initializeCardReader();
        return view;
    }

    private void initialize(View view) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (((JAppActivity)getActivity()).isVerifyPerson()==VERIFY_EKYC) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.kyc_title);
        } else if (((JAppActivity)getActivity()).isVerifyPerson()==VERIFY_PERSON){
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.verify_person);
        } else if ((((JAppActivity)getActivity()).isVerifyPerson()==VERIFY_DIP_CHIP)){
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.dip_chip);
        }

        miCurrentLog = 0;
        mfNextStep = false;
        btnNextStep = view.findViewById(R.id.btnNextStep);
        btnNextStep.setText(R.string.get_data_from_card);
        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mfNextStep) {
                    ((JAppActivity) getActivity()).showCardInformation();
                } else {
                    ((JAppActivity) getActivity()).StartGrapInformation();
                }
            }
        });

        tvInfo[0] = view.findViewById(R.id.txtLog1);
        tvInfo[0].setText("");
        tvInfo[1] = view.findViewById(R.id.txtLog2);
        tvInfo[1].setText("");
        tvInfo[2] = view.findViewById(R.id.txtLog3);
        tvInfo[2].setText("");
        tvInfo[3] = view.findViewById(R.id.txtLog4);
        tvInfo[3].setText("");
        tvInfo[4] = view.findViewById(R.id.txtLog5);
        tvInfo[4].setText("");
        imageInfo[0] = view.findViewById(R.id.imgCorrect1);
        imageInfo[0].setImageResource(0);
        imageInfo[1] = view.findViewById(R.id.imgCorrect2);
        imageInfo[1].setImageResource(0);
        imageInfo[2] = view.findViewById(R.id.imgCorrect3);
        imageInfo[2].setImageResource(0);
        imageInfo[3] = view.findViewById(R.id.imgCorrect4);
        imageInfo[3].setImageResource(0);
        imageInfo[4] = view.findViewById(R.id.imgCorrect5);
        imageInfo[4].setImageResource(0);
    }


    @Override
    public void updateEventLog(boolean fPass, boolean fShowIcon, String strInformation) {
        if (fShowIcon) {
            imageInfo[miCurrentLog].setImageResource(R.drawable.collect_icon);
        } else {
            imageInfo[miCurrentLog].setImageResource(0);
        }
        if (fPass) {
            tvInfo[miCurrentLog].setTextColor(Color.parseColor("#00aa00"));
        } else {
            tvInfo[miCurrentLog].setTextColor(Color.parseColor("#aa0000"));
        }
        tvInfo[miCurrentLog].setText(strInformation);
        miCurrentLog++;
        if (miCurrentLog == 5) {
            clearLog();
        }
    }

    private void clearLog() {
        tvInfo[0].setText("");
        tvInfo[1].setText("");
        tvInfo[2].setText("");
        tvInfo[3].setText("");
        tvInfo[4].setText("");
        imageInfo[0].setImageResource(0);
        imageInfo[1].setImageResource(0);
        imageInfo[2].setImageResource(0);
        imageInfo[3].setImageResource(0);
        imageInfo[4].setImageResource(0);
        miCurrentLog = 0;
    }

    @Override
    public void setNextStep() {
        mfNextStep = true;
        btnNextStep.setText(R.string.next_step);
    }

    @Override
    public void startCompareFace() {
        ((JAppActivity) getActivity()).showFaceCompareResult();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            ((JAppActivity) getActivity()).showHomeFragment();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
