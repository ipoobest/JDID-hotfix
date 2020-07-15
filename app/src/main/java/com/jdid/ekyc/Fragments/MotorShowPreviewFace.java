package com.jdid.ekyc.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.jdid.ekyc.JAppActivity;
import com.jdid.ekyc.R;

public class MotorShowPreviewFace extends Fragment {


    private Button btnSaveAndGo;
    private ImageView imgPreview;
    private TextView tvSuccess;
    private int id;
    private int subjectId;
    private int photoId;
    private String name;
    private String urls;

    private byte[] byteImage;

    public MotorShowPreviewFace() {}
    public MotorShowPreviewFace( int id, int subjectId, int photoId, String name, String urls){
        this.id = id;
        this.subjectId = subjectId;
        this.photoId = photoId;
        this.name = name;
        this.urls = urls;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_motorshow_preview_image, container, false);
        setHasOptionsMenu(true);
        initialize(view);
        return view;
    }


    private void initialize(View view) {
        // ToolBar
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("ลงทะเบียนงาน Motor Show");
        byteImage = ((JAppActivity) getActivity()).getByteImageCam();

        tvSuccess = view.findViewById(R.id.tvSuccess);
        tvSuccess.setVisibility(View.INVISIBLE);

        imgPreview =  view.findViewById(R.id.imgPreview);

        Bitmap bm = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
        DisplayMetrics metrics = new DisplayMetrics();
        imgPreview.setMinimumHeight(metrics.heightPixels);
        imgPreview.setMinimumWidth(metrics.widthPixels);
        imgPreview.setImageBitmap(bm);

        btnSaveAndGo = view.findViewById(R.id.btnNextStep);


        if (id == 1) {
            tvSuccess.setVisibility(View.INVISIBLE);
        } else if (id == 5) {
            tvSuccess.setVisibility(View.VISIBLE);
            btnSaveAndGo.setText("กลับสู่หน้าหลัก");
            tvSuccess.setText("ระบบยังไม่สามารถลงทะเบียนได้ตอนนี้ กรุณาลองใหม่ภายหลัง");
        } else {
            tvSuccess.setVisibility(View.VISIBLE);
            btnSaveAndGo.setText("ลองใหม่อีกครั้ง");
        }

        btnSaveAndGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id == 1 ) {
                    ((JAppActivity) getActivity()).requestDataToSubjectParse(subjectId, photoId, name, urls);
                } else  if (id == 5){
                    ((JAppActivity) getActivity()).showMotorShowFragment();
                } else {
                    ((JAppActivity) getActivity()).OpenCameraForCapture();
                }
            }
        });
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
                        ((JAppActivity) getActivity()).showMotorShowFragment();
                    }
                })
                .setNegativeButton("ยกเลิก", null)
                .show();
    }

}
