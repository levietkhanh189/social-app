package com.example.final_khang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.final_khang.adapter.CommentAdapter;
import com.example.final_khang.dao.CommentDAO;
import com.example.final_khang.entity.Comment;
import com.example.final_khang.entity.Post;

import java.util.ArrayList;
import java.util.List;

public class ActivityComment extends AppCompatActivity {

    //Variables
    private RecyclerView recyclerViewCom;
    private EditText edtComment;
    private ImageView imgViewSend, imgViewBack;
    private CommentDAO commentDAO;
    private CommentAdapter commentAdapter;
    private List<Comment> commentArrayList;
    private int postId;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        //MappingUI
        MappingUI();

        //creat commentDAO
        commentDAO = new CommentDAO(ActivityComment.this);

        //create Intent
        Intent intent = getIntent();
        postId = intent.getIntExtra("postId", -1); // -1 is a default value if the key is not found
        userId = intent.getIntExtra("userId", -1); // -1 is a default value if the key is not found


        //Set Adapter
        commentArrayList = new ArrayList<>();
        storeDataInArrays(postId+1);
        commentAdapter = new CommentAdapter(ActivityComment.this, commentArrayList);
        recyclerViewCom.setAdapter(commentAdapter);
        recyclerViewCom.setLayoutManager(new LinearLayoutManager(ActivityComment.this));

        imgViewSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentBody = edtComment.getText().toString();
                Comment comment = new Comment(postId+1, userId, commentBody);
                boolean check = commentDAO.insertComment(comment);
                if (check) {
                    commentAdapter.notifyDataSetChanged();
                    recyclerViewCom.setAdapter(commentAdapter);
                    recyclerViewCom.setLayoutManager(new LinearLayoutManager(ActivityComment.this));
                    Toast.makeText(ActivityComment.this, "Comment thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(ActivityComment.this, MainActivity.class);
                    startActivity(intent1);
                } else {
                    Toast.makeText(ActivityComment.this, "Comment thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imgViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void MappingUI() {
        recyclerViewCom = (RecyclerView) findViewById(R.id.recycler_view_comment);
        edtComment = (EditText) findViewById(R.id.edtComment);
        imgViewSend = (ImageView) findViewById(R.id.imgView_Send);
    }

    void storeDataInArrays(int postId) {
        Cursor cursor = commentDAO.getCommentByIDPost(postId);
        if (cursor.getCount() == 0) {
            Toast.makeText(ActivityComment.this, "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                int postID = cursor.getInt(1);
                int userID = cursor.getInt(2);
                String content = cursor.getString(3);
                Comment comment = new Comment(postID, userID,content);
                commentArrayList.add(comment);
            }
        }
    }
}