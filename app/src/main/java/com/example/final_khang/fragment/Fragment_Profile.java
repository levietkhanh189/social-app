package com.example.final_khang.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.final_khang.Activity_EditProfile;
import com.example.final_khang.R;
import com.example.final_khang.SharedPreferences.UserPreferences;
import com.example.final_khang.adapter.PostAdapter;
import com.example.final_khang.dao.PostDAO;
import com.example.final_khang.entity.Post;
import com.example.final_khang.entity.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_Profile extends Fragment {

    // Variable
    private Button btnEditProfile;
    private TextView txtUsernameProfile, txtBioProfile;
    private CircleImageView imgViewProfie;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private  User user;
    ArrayList<Post> posts;
    PostDAO postDAO;
    public Fragment_Profile() {
        // No changes here
    }

    public static Fragment_Profile newInstance(String param1, String param2) {
        Fragment_Profile fragment = new Fragment_Profile();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__profile, container, false);

        //Tạo postDAO.
        postDAO = new PostDAO(getContext());

        // Mapping UI
        MappingUI(view);


        // lấy dữ liệu từ share.
         user = UserPreferences.getUserData(getContext());

        //Set dữ liệu lên Fragment
        txtUsernameProfile.setText(user.getUserName());
        txtBioProfile.setText(user.getBio());
        if (user.getImage() != null) {
            Bitmap profileBitmap = BitmapFactory.decodeByteArray(user.getImage(), 0, user.getImage().length);
            imgViewProfie.setImageBitmap(profileBitmap);
        }

        // btnEditProfile click.
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Activity_EditProfile.class);
                startActivity(intent);
            }
        });

        // Set dữ liệu cho mảng
        posts = new ArrayList<>();
        storeDataInArrays();

        postAdapter = new PostAdapter(getContext(), posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(postAdapter);

        return view;
    }

    private void storeDataInArrays() {
        Cursor cursor = postDAO.getAllPostsByUserId(user.getUserID());
        if (cursor.getCount() == 0) {
            Toast.makeText(getContext(), "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                int userID = cursor.getInt(1);
                String content = cursor.getString(2);
                byte[] imgUrl = cursor.getBlob(3);
                Post post = new Post(userID, content, imgUrl);
                posts.add(post);
            }
        }
    }

    private void MappingUI(View view) {
        btnEditProfile = view.findViewById(R.id.edit_account_settings_btn);
        txtUsernameProfile = view.findViewById(R.id.profile_fragment_username);
        txtBioProfile = view.findViewById(R.id.bio_profile_frag);
        imgViewProfie = (CircleImageView) view.findViewById(R.id.pro_img_profile_frag);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_viewPerson);
    }
}