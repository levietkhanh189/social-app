package com.example.final_khang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.final_khang.SharedPreferences.UserPreferences;
import com.example.final_khang.dao.UserDAO;
import com.example.final_khang.entity.User;
import com.example.final_khang.fragment.Fragment_Profile;

public class Activity_EditProfile extends AppCompatActivity {

    //Variables
    private ImageView imgClose, imgAgree;
    private Button btnDeleteAccount, btnLogout;
    private EditText edtUsernamePro;
    private EditText edtPasswordPro;
    private EditText edtBioPro;
    UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        //MappingUi
        MappingUI();
        // btnLogout click
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        User user = UserPreferences.getUserData(this);
        // Set dữ liệu lên Edit text
        edtUsernamePro.setText(user.getUserName());
        edtPasswordPro.setText(user.getPassword());
        edtBioPro.setText(user.getBio());

        //btnDeleteAccount click
        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = UserPreferences.getUserData(Activity_EditProfile.this);
                userDAO.deleteUser(user.getUserID());
                Toast.makeText(Activity_EditProfile.this, "Xóa tài khoản thành công!", Toast.LENGTH_SHORT).show();
                logout();
            }
        });

        //imgClose click
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Đóng intent
                finish();
            }
        });

        //imgAgree click
        imgAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update(user);
            }
        });
    }

    private void update(User user){
        String nameUpdate = edtUsernamePro.getText().toString();
        String passUpdate = edtPasswordPro.getText().toString();
        String bioUpdate = edtBioPro.getText().toString();

        user.setUserName(nameUpdate);
        user.setPassword(passUpdate);
        user.setBio(bioUpdate);
        //Update lại dữ liệu trong shared
        UserPreferences.saveUserData(Activity_EditProfile.this, user);

        userDAO.updateUser(user);
        Toast.makeText(Activity_EditProfile.this, "Chỉnh sửa thông tin thành công!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Activity_EditProfile.this, MainActivity.class);
        startActivity(intent);
    }

    private void logout() {
        // Lấy dữ liệu từ SharedPreferences
        UserPreferences.clearUserData(this);
        // Tạo Intent, chuyển sang Login.
        Intent loginIntent = new Intent(Activity_EditProfile.this, ActivityLogin.class);
        startActivity(loginIntent);
        finish();
    }

    private void MappingUI() {
        imgClose = (ImageView) findViewById(R.id.close_profile_btn);
        imgAgree = (ImageView) findViewById(R.id.save_infor_profile_btn);
        btnLogout = (Button) findViewById(R.id.logout_btn_profile_frag);
        btnDeleteAccount = (Button) findViewById(R.id.delete_account_btn);
        edtUsernamePro = (EditText)findViewById(R.id.username_Profile_Edit);
        edtPasswordPro = (EditText) findViewById(R.id.password_Profile_Edit);
        edtBioPro = (EditText) findViewById(R.id.bio_profile_frag);

        userDAO = new UserDAO(this);
    }
}