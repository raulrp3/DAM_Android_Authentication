package com.example.firebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    private EditText textEmail;
    private EditText textPassword;
    private Button buttonRegister;
    private Button buttonLogin;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUser();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void initUI(){
        textEmail = findViewById(R.id.text_email);
        textPassword = findViewById(R.id.text_password);
        buttonRegister = findViewById(R.id.register_button);
        buttonLogin = findViewById(R.id.login_button);
    }

    private void checkUser(){
        final String email = textEmail.getText().toString().trim();
        final String password = textPassword.getText().toString().trim();

        if (validation(email, password)) {
            firebaseFirestore.collection("users").whereEqualTo("email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        int existUser = task.getResult().getDocuments().size();
                        if (existUser > 0) {
                            register(email, password);
                        } else {
                            Toast.makeText(MainActivity.this, "¡No puedes registrate!", Toast.LENGTH_SHORT).show();
                        }
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

    private void loginUser(){
        String email = textEmail.getText().toString().trim();
        String password = textPassword.getText().toString().trim();

        if (validation(email,password)){
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Intent intent = new Intent(MainActivity.this,WellcomeActivity.class);
                        intent.putExtra("Email",textEmail.getText().toString().trim());
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(),"Inicio de sesión fallido",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void register(String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Registro realizado correctamente",Toast.LENGTH_SHORT).show();
                    clean();

                    FirebaseUser user = task.getResult().getUser();
                    if (!user.isEmailVerified()){
                        user.sendEmailVerification();
                    }

                }else{
                    if (task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(),"El usuario ya está registrado",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Registro fallido",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
