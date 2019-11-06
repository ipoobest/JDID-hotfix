package com.jdid.ekyc.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import com.jdid.ekyc.JAppActivity;
import com.jdid.ekyc.R;
import com.jdid.ekyc.RegisterActivity;
import com.jdid.ekyc.views.PFCodeView;

public class PinCodeFragment extends Fragment {

    private static final String TAG = "PinCodeFragment";
    public static final int ASSIGN_PIN = 1;
    public static final int CONFIRM_PIN = 2;
    public static final int AUTHEN_PIN = 3;

    private PFCodeView mCodeView;
    private boolean mIsCreateMode = false;
    private int mPinState = 0;

    private TextView tvTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_pincode, container, false);
        initialize(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    private void initialize(View view) {
        tvTitle = view.findViewById(R.id.title_text_view);
        switch (mPinState) {
            case ASSIGN_PIN:
                tvTitle.setText(getResources().getString(R.string.assign_pin));
                break;
            case CONFIRM_PIN:
                tvTitle.setText(getResources().getString(R.string.confirm_assign_pin));
                break;
            case AUTHEN_PIN:
                tvTitle.setText(getResources().getString(R.string.input_pin));
                break;
        }

        view.findViewById(R.id.button_0).setOnClickListener(mOnKeyClickListener);
        view.findViewById(R.id.button_1).setOnClickListener(mOnKeyClickListener);
        view.findViewById(R.id.button_2).setOnClickListener(mOnKeyClickListener);
        view.findViewById(R.id.button_3).setOnClickListener(mOnKeyClickListener);
        view.findViewById(R.id.button_4).setOnClickListener(mOnKeyClickListener);
        view.findViewById(R.id.button_5).setOnClickListener(mOnKeyClickListener);
        view.findViewById(R.id.button_6).setOnClickListener(mOnKeyClickListener);
        view.findViewById(R.id.button_7).setOnClickListener(mOnKeyClickListener);
        view.findViewById(R.id.button_8).setOnClickListener(mOnKeyClickListener);
        view.findViewById(R.id.button_9).setOnClickListener(mOnKeyClickListener);
        view.findViewById(R.id.button_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 mCodeView.delete();
            }
        });

        mCodeView = view.findViewById(R.id.code_view);
        mCodeView.setListener(mCodeListener);
    }

    public void setPinAssign(int iPinState) {
        mPinState = iPinState;
    }

    private final View.OnClickListener mOnKeyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v instanceof TextView) {
                final String string = ((TextView) v).getText().toString();
                if (string.length() != 1) {
                    return;
                }
                final int codeLength = mCodeView.input(string);
                Log.v(TAG, "code length : "+codeLength);
                if (codeLength==PFCodeView.DEFAULT_CODE_LENGTH) {
                    if (mPinState==ASSIGN_PIN) {
                        ((RegisterActivity)getActivity()).showConfirmPinRegisterFragment(mCodeView.getCode());
                    } else if (mPinState==CONFIRM_PIN) {
                        ((RegisterActivity)getActivity()).confirmPinRegister(mCodeView.getCode());
                        getActivity().finish();
                    } else if (mPinState==AUTHEN_PIN) {
                        //TODO hide toolbar pin
                        ((JAppActivity)getActivity()).authenPinCode(mCodeView.getCode());
                    }
                }
            }
        }
    };

    private final PFCodeView.OnPFCodeListener mCodeListener = new PFCodeView.OnPFCodeListener() {
        @Override
        public void onCodeCompleted(String code) {

        }

        @Override
        public void onCodeNotCompleted(String code) {
            if (mIsCreateMode) {
                return;
            }
        }
    };


    // Interface //
    public interface OnPFLockScreenCodeCreateListener {

        /**
         * Callback method for pin code creation.
         *
         * @param encodedCode encoded pin code string.
         */
        void onCodeCreated(String encodedCode);

    }


    /**
     * Login callback interface.
     */
    public interface OnPFLockScreenLoginListener {

        /**
         * Callback method for successful login attempt with pin code.
         */
        void onCodeInputSuccessful();

        /**
         * Callback method for successful login attempt with fingerprint.
         */
        void onFingerprintSuccessful();

        /**
         * Callback method for unsuccessful login attempt with pin code.
         */
        void onPinLoginFailed();

        /**
         * Callback method for unsuccessful login attempt with fingerprint.
         */
        void onFingerprintLoginFailed();

    }
}
