package com.example.firebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class WellcomeActivity extends AppCompatActivity {

    private TextView textUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome);

        initUI();

        Intent intent = getIntent();
        String email = getIntent().getStringExtra("Email");
        String message = "!Bienvenido " + email + "!";
        textUser.setText(message);
    }

    private void initUI(){
        textUser = findViewById(R.id.text_user);
    }
}
