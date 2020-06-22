package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class Signup extends AppCompatActivity implements View.OnClickListener {

    private EditText edtSignupEmail, edtSignupUsername, edtSignupPassword;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("What's App Sign Up");
        edtSignupEmail = findViewById(R.id.edtSignupEmail);
        edtSignupUsername = findViewById(R.id.edtSignupUsername);
        edtSignupPassword = findViewById(R.id.edtSignupPassword);

        btnSignup = findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(this);

        if (ParseUser.getCurrentUser() != null){
            ParseUser.getCurrentUser().logOut();
         //   transitionToWhatsApp();
        }

    }

    private void transitionToWhatsApp() {
        Intent intent = new Intent(Signup.this, WhatsApp.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        if (edtSignupEmail.getText().toString().equals("") || edtSignupUsername.getText().toString().equals("") ||
        edtSignupPassword.getText().toString().equals("")){
            FancyToast.makeText(Signup.this, "Email, Username, Password fields are required", Toast.LENGTH_LONG,
                    FancyToast.INFO, false).show();
        } else{
            final ParseUser appUser = new ParseUser();
            appUser.setEmail(edtSignupEmail.getText().toString());
            appUser.setUsername(edtSignupUsername.getText().toString());
            appUser.setPassword(edtSignupPassword.getText().toString());

            appUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null){
                        FancyToast.makeText(Signup.this, appUser.getUsername() + " added successfully", Toast.LENGTH_LONG,
                                FancyToast.SUCCESS,false).show();
                        transitionToWhatsApp();
                    } else {
                        FancyToast.makeText(Signup.this, e.getMessage(), Toast.LENGTH_LONG,
                                FancyToast.ERROR, false).show();
                    }
                }
            });

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.signup_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(Signup.this, Login.class);
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }
}