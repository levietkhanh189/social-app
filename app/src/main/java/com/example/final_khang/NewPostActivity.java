package com.example.final_khang;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.Manifest;

import com.example.final_khang.SharedPreferences.UserPreferences;
import com.example.final_khang.dao.PostDAO;
import com.example.final_khang.entity.Post;
import com.example.final_khang.entity.User;

public class NewPostActivity extends Activity {

    private ImageView ivImagePreview;
    private EditText etCaption;
    private Button btnSelectImage, btnTagPeople, btnPost, btnCancel;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_PICK_IMAGE = 102;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_post);

        // Ánh xạ các thành phần từ layout XML
        ivImagePreview = findViewById(R.id.ivImagePreview);
        etCaption = findViewById(R.id.etCaption);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnTagPeople = findViewById(R.id.btnTagPeople);
        btnPost = findViewById(R.id.btnPost);
        btnCancel = findViewById(R.id.btnCancel);

        // Bắt sự kiện khi click vào nút Select Image
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerDialog();
            }
        });

        // Bắt sự kiện khi click vào nút Post
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String caption = etCaption.getText().toString();

                // Lấy userId từ SharedPreferences
                User user = UserPreferences.getUserData(NewPostActivity.this);
                int userid= user.getUserID();
                // Kiểm tra xem userId có giá trị hợp lệ không
                if (userid != -1) {
                    // Thêm bài viết vào cơ sở dữ liệu
                    byte[] img = ImageView_ToByte(ivImagePreview);
                    boolean isPostInserted = insertPostIntoDatabase(userid, caption, img);

                    // Kiểm tra kết quả và chuyển sang màn hình khác nếu cần
                    if (isPostInserted) {
                        startActivity(new Intent(NewPostActivity.this, MainActivity.class));
                        Toast.makeText(NewPostActivity.this, "Post added successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(NewPostActivity.this, "Failed to add post", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(NewPostActivity.this, "Invalid userId", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Bắt sự kiện khi click vào nút Cancel
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý logic khi người dùng muốn hủy bỏ việc đăng bài
                // Ví dụ: Đóng hoạt động hiện tại, vv.
                finish();
            }
        });
    }

    private void showImagePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image").setItems(new CharSequence[]{"Gallery", "Camera"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        pickImageFromGallery();
                        break;
                    case 1:
                        checkCameraPermission();
                        break;
                }
            }
        }).show();
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    private void checkCameraPermission() {

        captureImageFromCamera();

    }

    private void captureImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                Toast.makeText(this, "Could not create image file. Please try again later.", Toast.LENGTH_SHORT).show();
                return; // Stop the process if an error occurs
            }
            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } else {
                // Handle the case when photoFile is null (unable to create image file)
                Toast.makeText(this, "Could not create image file. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle the case when there is no camera app available
            Toast.makeText(this, "No camera app available.", Toast.LENGTH_SHORT).show();
        }
    }


    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                captureImageFromCamera();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            // Hiển thị ảnh trong ImageView
            Uri selectedImageUri = data.getData();
            // Hiển thị ảnh trong ImageView
            ivImagePreview.setImageURI(selectedImageUri);
        }
    }

    private boolean insertPostIntoDatabase(int userid, String caption, byte[] imageUri) {
        // Khởi tạo một đối tượng Post với các thông tin được truyền vào
        Post post = new Post(userid, caption, imageUri); // userId là ID của người dùng đăng nhập hiện tại

        // Khởi tạo một PostDAO để thao tác với cơ sở dữ liệu
        PostDAO postDAO = new PostDAO(this); // Chú ý: "this" là Context của Activity hiện tại

        // Thực hiện thêm bài viết vào cơ sở dữ liệu bằng PostDAO
        boolean isInserted = postDAO.insertPost(post);
        // Trả về kết quả thực hiện thêm bài viết (true nếu thành công, ngược lại là false)
        return isInserted;
    }

    public byte[] ImageView_ToByte(ImageView imageView) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}

