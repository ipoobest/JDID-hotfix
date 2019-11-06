package com.jdid.ekyc.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.jdid.ekyc.JAppActivity;
import com.jdid.ekyc.R;

public class PhoneNumberDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "PhoneNumberDialogFragment";
    private static final int VERIFY_PERSON = 1;

    TextView tvPhone;
    EditText edPhone;
    Button btnPhone;

    String regisPhone;

    public PhoneNumberDialogFragment() {
    }

    public PhoneNumberDialogFragment(String regisPhone) {
        this.regisPhone = regisPhone;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_phone_number, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        tvPhone = view.findViewById(R.id.tvPhoneDialog);
        edPhone = view.findViewById(R.id.edPhoneDialog);
        btnPhone = view.findViewById(R.id.btnPhoneDialog);
        btnPhone.setOnClickListener(this);

        if (regisPhone != null){
            tvPhone.setText(regisPhone);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btnPhone) {
            String mPhone = edPhone.getText().toString();
            Toast.makeText(getContext(), mPhone, Toast.LENGTH_SHORT).show();
            ((JAppActivity) getActivity()).showOTPVerifyFragment(VERIFY_PERSON, mPhone);

            getDialog().dismiss();
        }
    }
}
