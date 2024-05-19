package com.example.final_khang;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.final_khang.dao.UserDAO;
import com.example.final_khang.entity.User;

import java.io.ByteArrayOutputStream;
import java.util.regex.Pattern;

public class ActivityRegister extends AppCompatActivity {
    //Variables
    EditText edtUsername, edtPassword, edtRePass, edtEmail;
    Button btnRegister, btnGotoLogin;
    UserDAO userdao;
    ImageView imgViewUser;
    private static final int REQUEST_PICK_IMAGE = 102;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Create UserDAO
        userdao = new UserDAO(this);
        //Mapping UI
        MappingUI();
        //ImageView click
        imgViewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mayChuphinh = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(mayChuphinh, REQUEST_PICK_IMAGE);
            }
        });

        //btnRegister click
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, username, password, repass;
                email = edtEmail.getText().toString();
                username = edtUsername.getText().toString();
                password = edtPassword.getText().toString();
                repass = edtRePass.getText().toString();
                //Check if all fields are filled
                if (email.isEmpty() || username.isEmpty() || password.isEmpty() || repass.isEmpty()) {
                    Toast.makeText(ActivityRegister.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!isValidEmail(email)) {
                    Toast.makeText(ActivityRegister.this, "Vui lòng nhập đúng định dạng email!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (!password.equals(repass)) {
                        Toast.makeText(ActivityRegister.this, "Vui lòng nhập mật khẩu giống nhau!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (userdao.checkEmailExists(email)) {
                        Toast.makeText(ActivityRegister.this, "Email đã tồn tại", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // Register
                    User user = new User(email, password, username, ImageView_ToByte(imgViewUser), "");
                    boolean registerSuccess = userdao.insertUser(user);
                    if (registerSuccess) {
                        Toast.makeText(ActivityRegister.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ActivityRegister.this, "Đăng kí thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnGotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityRegister.this, ActivityLogin.class);
                startActivity(intent);
                finish(); // Close the current activity
            }
        });
    }

    private void MappingUI() {
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtRePass = findViewById(R.id.edtRePassword);
        edtEmail = findViewById(R.id.edtEmail);
        btnRegister = findViewById(R.id.btnRegister);
        btnGotoLogin = findViewById(R.id.btnGotoLogin);
        imgViewUser = findViewById(R.id.imgViewUser);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            // Hiển thị ảnh trong ImageView
            Uri selectedImageUri = data.getData();
            // Hiển thị ảnh trong ImageView
            imgViewUser.setImageURI(selectedImageUri);
        }
    }

    // Hàm đổi Bitmap thành Byte.
    public byte[] ImageView_ToByte(ImageView imageView) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }


    // hàm check định dạng Email.
    public boolean isValidEmail(String email) {
        String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                        "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        return pattern.matcher(email).matches();
    }
}
