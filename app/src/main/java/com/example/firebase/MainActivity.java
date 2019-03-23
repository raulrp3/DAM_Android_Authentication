package com.example.firebase;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText textEmail;
    private EditText textPassword;
    private Button buttonRegister;

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        firebaseAuth = FirebaseAuth.getInstance();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void initUI(){
        textEmail = findViewById(R.id.text_email);
        textPassword = findViewById(R.id.text_password);
        buttonRegister = findViewById(R.id.register_button);
    }

    private void registerUser(){
        String email = textEmail.getText().toString().trim();
        String password = textPassword.getText().toString().trim();

        if (validation(email,password)){
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Registro realizado correctamente",Toast.LENGTH_SHORT).show();
                        clean();
                    }else{
                        Toast.makeText(getApplicationContext(),"Registro fallido",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private boolean validation(String email,String password){
        boolean isValid = true;
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(),"Campos obligatorios", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        return isValid;
    }

    private void clean(){
        textEmail.setText("");
        textPassword.setText("");
    }
}
