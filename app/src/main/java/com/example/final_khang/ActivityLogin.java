package com.example.final_khang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.final_khang.SharedPreferences.UserPreferences;
import com.example.final_khang.dao.UserDAO;
import com.example.final_khang.data.DBHelper;
import com.example.final_khang.entity.User;

import org.w3c.dom.Text;

import java.util.regex.Pattern;

public class ActivityLogin extends AppCompatActivity {


    EditText edtEmail, edtPassword;
    TextView txtGotoRegister;
    Button btnLogin;
    UserDAO userDAO;
    UserPreferences userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //create dbHelper
        userDAO = new UserDAO(this);

        //Mapping UI
        MappingUI();
        //btnLogin click
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(ActivityLogin.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
                if (!isValidEmail(email)) {
                    Toast.makeText(ActivityLogin.this, "Vui lòng nhập đúng định dạng Email!", Toast.LENGTH_SHORT).show();
                    return;
                }
                User user = userDAO.getUserByEmailAndPassword(email, password);
                if (user != null) {
                    Toast.makeText(ActivityLogin.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    userPreferences.saveUserData(ActivityLogin.this, user);
                    Intent intent = new Intent(ActivityLogin.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ActivityLogin.this, "Tài khoản hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //txtGotoRegister click
        txtGotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLogin.this, ActivityRegister.class);
                startActivity(intent);
            }
        });
    }

    private void MappingUI() {
        edtEmail = (EditText) findViewById(R.id.edtEmailLo);
        edtPassword = (EditText) findViewById(R.id.edtPasswordLo);
        txtGotoRegister = (TextView) findViewById(R.id.txtGotoRegister);
        btnLogin = (Button) findViewById(R.id.btnLogin);
    }

    public boolean isValidEmail(String email) {
        String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                        "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        return pattern.matcher(email).matches();
    }
}