package com.jdid.ekyc.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.jdid.ekyc.JAppActivity;
import com.jdid.ekyc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.jdid.ekyc.JAppActivity.ADDRESS;

public class FormFillFragment extends Fragment {

    private static final int VERIFY_EKYC = 0;
    private static final int VERIFY_PERSON = 1;
    private static final int VERIFY_DIP_CHIP = 2;

    private Spinner spPurpose;
    private EditText edOtherPurpose;
    private CheckBox cbCurrentAddress;
    private EditText edCurrentAddress;
    private EditText edPhone;
    private Spinner spMarriageStatus;
    private EditText edProfession;
    private EditText edWork;
    private EditText edWorkAddress;
    private EditText edIncome;
    private Button btnSaveAndGo;
    private Spinner spReferByCompany;

    private List<String> slPurpose;
    String refCompany;
    String purposeCompany;

    private ProgressDialog mProgressDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_form_fill, container, false);
        setHasOptionsMenu(true);
        initialize(view);
        return view;
    }


    private void initialize(View view) {
        // ToolBar
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);


        switch (((JAppActivity) getActivity()).isVerifyPerson()) {
            case VERIFY_EKYC:
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.kyc_title);
                break;
            case VERIFY_PERSON:
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.verify_person);
                break;
        }


        spPurpose = view.findViewById(R.id.spPurpose);
        spPurpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Log.e(TAG, "selected item pos : " + spPurpose.getSelectedItemPosition() + "");
                if (spPurpose.getSelectedItemPosition() == 0) {
                    edOtherPurpose.setVisibility(View.VISIBLE);
                } else {
                    edOtherPurpose.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        edOtherPurpose = view.findViewById(R.id.edOtherPurpose);
        edCurrentAddress = view.findViewById(R.id.edCurrentAddress);
        edPhone = view.findViewById(R.id.edPhone);

        spMarriageStatus = view.findViewById(R.id.spMariageStatus);
        String[] mariageStatusList = getResources().getStringArray(R.array.mariageStatusList);
        ArrayAdapter<String> adapterMonth = new ArrayAdapter<String>(((JAppActivity) getActivity()).getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line, mariageStatusList);
        spMarriageStatus.setAdapter(adapterMonth);

        edProfession = view.findViewById(R.id.edProfession);
        edWork = view.findViewById(R.id.edWork);
        edWorkAddress = view.findViewById(R.id.edWorkAddress);
        edIncome = view.findViewById(R.id.edIncome);
        //checkbox current address
        cbCurrentAddress = view.findViewById(R.id.checkBoxAddress);
        cbCurrentAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] generalInformation = ((JAppActivity) getActivity()).getGeneralInformation();
                if (cbCurrentAddress.isChecked()){
                    edCurrentAddress.setText(generalInformation[ADDRESS]);
//                    Toast.makeText(getContext(),"ที่อยู่ปัจจุบัน", Toast.LENGTH_SHORT).show();
                } else {
                    edCurrentAddress.setText("");
//                    Toast.makeText(getContext(),"uncheck", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final String[] purposeList = getResources().getStringArray(R.array.purposeList);
        ArrayAdapter<String> spPurposeList = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, purposeList);
        spPurpose.setAdapter(spPurposeList);

        spPurpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (spPurpose.getSelectedItemPosition() == 0) {
                    edOtherPurpose.setVisibility(View.VISIBLE);
                } else {
                    edOtherPurpose.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //spReferByCompany
        spReferByCompany = view.findViewById(R.id.spReferByCompany);
        final String[] companyName = getResources().getStringArray(R.array.companyList);
        ArrayAdapter<String> spComAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, companyName);
        spReferByCompany.setAdapter(spComAdapter);

        spReferByCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getItemAtPosition(position).equals("กรุณาเลือก ชื่อบริษัท")) {
                    refCompany = null;
                } else {
                    refCompany = companyName[position];
                    ((JAppActivity) getActivity()).fieldsList[JAppActivity.REF_COMPANY] = refCompany;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                refCompany = "กรุณาเลือก ชื่อบริษัท";
            }
        });

        btnSaveAndGo = view.findViewById(R.id.btnNextStep);
        btnSaveAndGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finishedFormFill()) {
                    authenOTP();
                } else {
                    Toast.makeText(getActivity(), "กรุณาป้อนข้อมูลให้ครบทุกช่อง", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void authenOTP() {
        // need save data fields first
        if (spPurpose.getSelectedItemPosition() == 0) {
            ((JAppActivity) getActivity()).fieldsList[JAppActivity.PURPOSE] = edOtherPurpose.getText().toString();
        } else {
            ((JAppActivity) getActivity()).fieldsList[JAppActivity.PURPOSE] = spPurpose.getSelectedItem().toString();
        }
        ((JAppActivity) getActivity()).fieldsList[JAppActivity.CONTACT_NUMBER] = edPhone.getText().toString();
        ((JAppActivity) getActivity()).fieldsList[JAppActivity.CENSUS_ADDRESS] = edCurrentAddress.getText().toString();
        long lMariageStatus = spMarriageStatus.getSelectedItemId();
        ((JAppActivity) getActivity()).fieldsList[JAppActivity.MARIAGE_STATUS] = Long.toString(lMariageStatus + 1);
        ((JAppActivity) getActivity()).fieldsList[JAppActivity.OCCUPATION] = edProfession.getText().toString();
        ((JAppActivity) getActivity()).fieldsList[JAppActivity.COMPANY] = edWork.getText().toString();
        ((JAppActivity) getActivity()).fieldsList[JAppActivity.COMPANY_ADDRSS] = edWorkAddress.getText().toString();
        ((JAppActivity) getActivity()).fieldsList[JAppActivity.INCOME] = edIncome.getText().toString();
        ((JAppActivity) getActivity()).fieldsList[JAppActivity.REF_COMPANY] = refCompany;
        ((JAppActivity) getActivity()).hideKeyboard();
        ((JAppActivity) getActivity()).showOTPVerifyFragment(VERIFY_EKYC);
    }

    private boolean finishedFormFill() {
        if (edPhone.getText().length() == 0) {
            edPhone.requestFocus();
            return false;
        }

        if (refCompany == null) {
            spReferByCompany.isFocusableInTouchMode();
            return false;
        }

        if (edCurrentAddress.getText().length() == 0) {
            edCurrentAddress.requestFocus();
            return false;
        }
        if (edPhone.getText().length() == 0) {
            edPhone.requestFocus();
            return false;
        }
        if (edProfession.getText().length() == 0) {
            edProfession.requestFocus();
            return false;
        }
        if (edWork.getText().length() == 0) {
            edWork.requestFocus();
            return false;
        }
        if (edWorkAddress.getText().length() == 0) {
            edWorkAddress.requestFocus();
            return false;
        }
        if (edIncome.getText().length() == 0) {
            edIncome.requestFocus();
            return false;
        }
        return true;
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
                .setMessage("ต้องการหยุดทำรายการ และกลับไปหน้าเมนูหลัก หรือไม่?")
                .setCancelable(false)
                .setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((JAppActivity) getActivity()).showHomeFragment();
                    }
                })
                .setNegativeButton("ยกเลิก", null)
                .show();
    }
}
