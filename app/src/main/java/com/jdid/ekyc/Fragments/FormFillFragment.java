package com.jdid.ekyc.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class FormFillFragment extends Fragment {

    private Spinner spPurpose;
    private EditText edOtherPurpose;
    private EditText edCurrentAddress;
    private EditText edPhone;
    private Spinner spMarriageStatus;
    private EditText edProfession;
    private EditText edWork;
    private EditText edWorkAddress;
    private EditText edIncome;
    private Button btnSaveAndGo;

    private List<String> slPurpose;

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

            if (result!=null) {
                try {
                    JSONArray ar = result.getJSONArray("purpose");
                    slPurpose = new ArrayList<String>();
                    for (int idx=0;idx<ar.length();idx++) {
                        JSONObject obj = (JSONObject)ar.get(idx);
                        slPurpose.add(obj.getString("purpose"));
                    }
                    ArrayAdapter<String> adapterPurpose = new ArrayAdapter<String>(((JAppActivity)getActivity()).getApplicationContext(),
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
        initialize(view);
        GetPurpose purpose = new GetPurpose();
        purpose.execute();
        return view;
    }

    private void initialize(View view) {
        if (((JAppActivity)getActivity()).isVerifyPerson()==false) {
            ((TextView)view.findViewById(R.id.txtTitle)).setText(R.string.kyc_title);
        } else {
            ((TextView)view.findViewById(R.id.txtTitle)).setText(R.string.verify_person);
        }

        spPurpose = view.findViewById(R.id.spPurpose);
        spPurpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Log.e(TAG, "selected item pos : " + spPurpose.getSelectedItemPosition()+"");
                if (spPurpose.getSelectedItemPosition()==0) {
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
        ArrayAdapter<String> adapterMonth = new ArrayAdapter<String>(((JAppActivity)getActivity()).getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line, mariageStatusList);
        spMarriageStatus.setAdapter(adapterMonth);

        edProfession = view.findViewById(R.id.edProfession);
        edWork = view.findViewById(R.id.edWork);
        edWorkAddress = view.findViewById(R.id.edWorkAddress);
        edIncome = view.findViewById(R.id.edIncome);
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

    private void saveAndGo() {
        if (spPurpose.getSelectedItemPosition()==0) {
            ((JAppActivity) getActivity()).fieldsList[JAppActivity.PURPOSE] = edOtherPurpose.getText().toString();
        } else {
            ((JAppActivity) getActivity()).fieldsList[JAppActivity.PURPOSE] = spPurpose.getSelectedItem().toString();
        }
        ((JAppActivity)getActivity()).fieldsList[JAppActivity.CONTACT_NUMBER] = edPhone.getText().toString();
        ((JAppActivity)getActivity()).fieldsList[JAppActivity.CENSUS_ADDRESS] = edCurrentAddress.getText().toString();
        long lMariageStatus = spMarriageStatus.getSelectedItemId();
        ((JAppActivity)getActivity()).fieldsList[JAppActivity.MARIAGE_STATUS] = Long.toString(lMariageStatus+1);
        ((JAppActivity)getActivity()).fieldsList[JAppActivity.OCCUPATION] = edProfession.getText().toString();
        ((JAppActivity)getActivity()).fieldsList[JAppActivity.COMPANY] = edWork.getText().toString();
        ((JAppActivity)getActivity()).fieldsList[JAppActivity.COMPANY_ADDRSS] = edWorkAddress.getText().toString();
        ((JAppActivity)getActivity()).fieldsList[JAppActivity.INCOME] = edIncome.getText().toString();
        hideKeyboard();
        ((JAppActivity)getActivity()).SaveInformation();
    }

    private void authenOTP() {
        // need save data fields first
        if (spPurpose.getSelectedItemPosition()==0) {
            ((JAppActivity) getActivity()).fieldsList[JAppActivity.PURPOSE] = edOtherPurpose.getText().toString();
        } else {
            ((JAppActivity) getActivity()).fieldsList[JAppActivity.PURPOSE] = spPurpose.getSelectedItem().toString();
        }
        ((JAppActivity)getActivity()).fieldsList[JAppActivity.CONTACT_NUMBER] = edPhone.getText().toString();
        ((JAppActivity)getActivity()).fieldsList[JAppActivity.CENSUS_ADDRESS] = edCurrentAddress.getText().toString();
        long lMariageStatus = spMarriageStatus.getSelectedItemId();
        ((JAppActivity)getActivity()).fieldsList[JAppActivity.MARIAGE_STATUS] = Long.toString(lMariageStatus+1);
        ((JAppActivity)getActivity()).fieldsList[JAppActivity.OCCUPATION] = edProfession.getText().toString();
        ((JAppActivity)getActivity()).fieldsList[JAppActivity.COMPANY] = edWork.getText().toString();
        ((JAppActivity)getActivity()).fieldsList[JAppActivity.COMPANY_ADDRSS] = edWorkAddress.getText().toString();
        ((JAppActivity)getActivity()).fieldsList[JAppActivity.INCOME] = edIncome.getText().toString();
        hideKeyboard();
        ((JAppActivity)getActivity()).showOTPVerifyFragment();
    }

    private boolean finishedFormFill() {
        if (edPhone.getText().length()==0) {
            edPhone.requestFocus();
            return false;
        }

        //TODO :: uncomments this
//        if (edCurrentAddress.getText().length()==0) {
//            edCurrentAddress.requestFocus();
//            return false;
//        }
//        if (edPhone.getText().length()==0) {
//            edPhone.requestFocus();
//            return false;
//        }
//        if (edProfession.getText().length()==0) {
//            edProfession.requestFocus();
//            return false;
//        }
//        if (edWork.getText().length()==0) {
//            edWork.requestFocus();
//            return false;
//        }
//        if (edWorkAddress.getText().length()==0) {
//            edWorkAddress.requestFocus();
//            return false;
//        }
//        if (edIncome.getText().length()==0) {
//            edIncome.requestFocus();
//            return false;
//        }
        return true;
    }
    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
