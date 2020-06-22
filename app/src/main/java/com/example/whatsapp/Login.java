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

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private EditText edtLoginUsername, edtLoginPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("What's App Log In");

        edtLoginUsername = findViewById(R.id.edtLoginUsername);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        if (ParseUser.getCurrentUser() != null){
           transitionToWhatsApp();
           //ParseUser.getCurrentUser().logOut();
        }
    }

    private void transitionToWhatsApp() {
        Intent intent = new Intent(Login.this, WhatsApp.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        if (edtLoginUsername.getText().toString().equals("") || edtLoginPassword.getText().toString().equals("")){
            FancyToast.makeText(Login.this, "Username and Password are required", Toast.LENGTH_LONG,
                    FancyToast.INFO, false).show();
        } else {
            ParseUser.logInInBackground(edtLoginUsername.getText().toString(), edtLoginPassword.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (user != null && e == null){
                        FancyToast.makeText(Login.this, user.getUsername() + " logged in successfully", Toast.LENGTH_LONG,
                                FancyToast.SUCCESS,false).show();
                        transitionToWhatsApp();
                    } else {
                        FancyToast.makeText(Login.this, e.getMessage(), Toast.LENGTH_LONG,
                                FancyToast.ERROR, false).show();
                    }
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(Login.this, Signup.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}