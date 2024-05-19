package com.example.final_khang.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

        // Đặt nội dung comment
        holder.commentBody.setText(comment.getCommentBody());

        // Lấy thông tin người dùng bằng cách sử dụng callback
        userDAO.getUserByID(comment.getUserId(), new UserDAO.UserCallback() {
            @Override
            public void onSuccess(User user) {
                if (user != null && user.getImage() != null) {
                    holder.photoUser.setImageBitmap(BitmapFactory.decodeByteArray(user.getImage(), 0, user.getImage().length));
                } else {
                    holder.photoUser.setImageResource(R.drawable.profile_icon); // Icon mặc định nếu không có hình ảnh
                }
            }

            @Override
            public void onError(String error) {
                holder.photoUser.setImageResource(R.drawable.profile_icon); // Icon mặc định trong trường hợp có lỗi
                Toast.makeText(holder.itemView.getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
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