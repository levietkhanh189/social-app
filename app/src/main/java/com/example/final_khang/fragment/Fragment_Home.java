package com.example.final_khang.fragment;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.final_khang.R;
import com.example.final_khang.adapter.PostAdapter;
import com.example.final_khang.dao.PostDAO;
import com.example.final_khang.dao.UserDAO;
import com.example.final_khang.entity.Post;
import com.example.final_khang.entity.User;

import java.util.ArrayList;

public class Fragment_Home extends Fragment {

    private RecyclerView recycler_view_home;
    private PostAdapter postAdapter;
    ArrayList<Post> posts;
    PostDAO postDAO;

    public Fragment_Home() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__home, container, false);
        //Tạo postDAO.
        postDAO = new PostDAO(getContext());
        //MappingUI()
        MappingUI(view);
        // Set dữ liệu cho mảng
        posts = new ArrayList<>();
        storeDataInArrays();

        postAdapter = new PostAdapter(getContext(), posts);
        recycler_view_home.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler_view_home.setAdapter(postAdapter);

        return view;
    }

    private void MappingUI(View view) {
        recycler_view_home = (RecyclerView) view.findViewById(R.id.recycler_view_home);
    }

    void storeDataInArrays() {
        Cursor cursor = postDAO.getAllPosts();
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


}