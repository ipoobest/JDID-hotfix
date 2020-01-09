package com.viva.ekyc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.viva.ekyc.base.JCompatActivity;

public class MainActivity extends JCompatActivity {

    Button btnToStartApp;
    Button btnToPinPageLogin;
    Button btnToPinPageRegister;
    Button btnFormFill;
    Button btnToRegisterPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeInterface();
    }

    private void initializeInterface() {
        btnToStartApp = findViewById(R.id.btnStartApp);
        btnToStartApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, JAppActivity.class);
                startActivity(intent);
            }
        });

        btnToRegisterPage = findViewById(R.id.btnToRegisterPage);
        btnToRegisterPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnFormFill = findViewById(R.id.btnFormFill);
        btnFormFill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, JAppActivity.class);
                intent.putExtra("ToFormFill", true);
                startActivity(intent);
            }
        });

    }
}
