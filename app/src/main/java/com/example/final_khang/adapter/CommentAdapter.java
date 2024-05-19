package com.example.final_khang.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_khang.R;
import com.example.final_khang.dao.UserDAO;
import com.example.final_khang.entity.Comment;
import com.example.final_khang.entity.User;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> commentList;
    private Context context;
    private UserDAO userDAO;

    public CommentAdapter(Context contextMain, List<Comment> commentList) {
        this.context = contextMain;
        this.commentList = commentList;
        userDAO = new UserDAO(contextMain);
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        User user = userDAO.getUserByID(comment.getUserId());
        holder.commentBody.setText(comment.getCommentBody()); // Set the comment text
        holder.photoUser.setImageBitmap(BitmapFactory.decodeByteArray
                (user.getImage(), 0, user.getImage().length));
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        private ImageView photoUser;
        private TextView commentBody;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            photoUser = itemView.findViewById(R.id.photoUser);
            commentBody = itemView.findViewById(R.id.commentUser);
        }
    }
}