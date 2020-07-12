package com.jdid.ekyc.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.jdid.ekyc.JAppActivity;
import com.jdid.ekyc.R;

import java.util.List;

import static android.content.ContentValues.TAG;
import static com.jdid.ekyc.JAppActivity.ADDRESS;

public class FormFillPersonRegisterFragment extends Fragment {

    private static final int VERIFY_EKYC = 0;
    private static final int VERIFY_PERSON = 1;
    private static final int VERIFY_DIP_CHIP = 2;
    private static final int VERIFY_DIP_CHIP_MOTORSHOW = 3;

    private Button btnSaveAndGo;
    private EditText edPhone;

    private ProgressDialog mProgressDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_form_fill_person_register, container, false);
        setHasOptionsMenu(true);
        initialize(view);
        return view;
    }


    private void initialize(View view) {
        // ToolBar
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        switch (((JAppActivity) getActivity()).isVerifyPerson()) {
            case VERIFY_PERSON:
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.verify_person);
                break;
            case VERIFY_DIP_CHIP:
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.dip_chip);
                break;
            case VERIFY_DIP_CHIP_MOTORSHOW:
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.dip_chip_motorshow);
                break;
        }


        edPhone =  view.findViewById(R.id.edPhone);
        btnSaveAndGo = view.findViewById(R.id.btnNextStep);
        btnSaveAndGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finishedFormFill()) {
                    authenOTP();
                } else {
                    Toast.makeText(getActivity(), "กรุณากรอกหมายเลขโทรศัพท์", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void authenOTP() {
        // need save data fields first

        ((JAppActivity) getActivity()).fieldsList[JAppActivity.CONTACT_NUMBER] = edPhone.getText().toString();
        ((JAppActivity) getActivity()).hideKeyboard();
        if (((JAppActivity) getActivity()).isVerifyPerson() == VERIFY_PERSON){
            ((JAppActivity) getActivity()).showOTPVerifyFragment(VERIFY_PERSON);
        } else if (((JAppActivity) getActivity()).isVerifyPerson() == VERIFY_DIP_CHIP_MOTORSHOW){
            ((JAppActivity) getActivity()).showOTPVerifyFragment(VERIFY_DIP_CHIP_MOTORSHOW);
        } else {
            ((JAppActivity) getActivity()).showOTPVerifyFragment(VERIFY_DIP_CHIP);
        }
    }

    private boolean finishedFormFill() {
        if (edPhone.getText().length() <= 9 ) {
            edPhone.requestFocus();
            return false;
        }

        return true;
    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            confirmBack();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void confirmBack() {
//        new AlertDialog.Builder(getContext())
//                .setMessage("ต้องการหยุดทำรายการ และกลับไปหน้าเมนูหลัก หรือไม่?")
//                .setCancelable(false)
//                .setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        ((JAppActivity) getActivity()).showHomeFragment();
//                    }
//                })
//                .setNegativeButton("ยกเลิก", null)
//                .show();
//    }
}
