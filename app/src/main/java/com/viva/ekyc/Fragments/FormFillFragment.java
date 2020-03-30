package com.viva.ekyc.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
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

import com.viva.ekyc.JAppActivity;
import com.viva.ekyc.R;

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
import static com.viva.ekyc.JAppActivity.ADDRESS;

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

    private ProgressDialog mProgressDialog;

    /* ******************************************************* */
    private class GetPurpose extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(getContext(),
                    null, "กำลังทำการอ่านข้อมูล", true, false);
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            JSONObject result;
            result = _getPurpose();
            return result;
        }

        @Override
        protected void onPostExecute(JSONObject result) {

            if (result == null) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
                return;
            }

            if (result != null) {
                try {
                    JSONArray ar = result.getJSONArray("purpose");
                    slPurpose = new ArrayList<String>();
                    for (int idx = 0; idx < ar.length(); idx++) {
                        JSONObject obj = (JSONObject) ar.get(idx);
                        slPurpose.add(obj.getString("purpose"));
                    }
                    ArrayAdapter<String> adapterPurpose = new ArrayAdapter<String>(((JAppActivity) getActivity()).getApplicationContext(),
                            android.R.layout.simple_spinner_item, slPurpose);
                    spPurpose.setAdapter(adapterPurpose);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    private JSONObject _getPurpose() {
        JSONObject responseJson = null;
        try {
            URL obj = new URL("https://e-kyc.dome.cloud/purpose");
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
//            conn.setRequestProperty("Authorization", "Basic ZWt5Y2Rldjpla3ljZGV2");
            conn.setRequestProperty("X-API-KEY", "3Oi6FUtmmf0aLt6LzVS2FhZXMmEguCMb");

            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            JSONArray array = new JSONArray(response.toString());
            responseJson = new JSONObject();
            responseJson.put("purpose", array);
            in.close();

            conn.disconnect();

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return responseJson;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_form_fill, container, false);
        setHasOptionsMenu(true);
        initialize(view);
        GetPurpose purpose = new GetPurpose();
        purpose.execute();
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
                    ((JAppActivity) getActivity()).successFragment();
//                    authenOTP();
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
