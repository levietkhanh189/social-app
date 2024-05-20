package com.example.final_khang.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_khang.ActivityComment;
import com.example.final_khang.R;
import com.example.final_khang.SharedPreferences.UserPreferences;
import com.example.final_khang.dao.LikeDAO;
import com.example.final_khang.dao.UserDAO;
import com.example.final_khang.entity.Like;
import com.example.final_khang.entity.Post;
import com.example.final_khang.entity.User;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private Context context;
    private List<Post> myListPost;
    private UserDAO userDAO;
    private LikeDAO likeDAO;

    public PostAdapter(Context context, List<Post> myListPost) {
        this.context = context;
        this.myListPost = myListPost;
        userDAO = new UserDAO(context);
        likeDAO = new LikeDAO(context);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = myListPost.get(position);
        User user1 = userDAO.getUserByID(post.getUserId());
        if (user1 != null) {
            holder.username.setText(user1.getUserName());
            holder.imgViewUser.setImageBitmap(BitmapFactory.decodeByteArray(user1.getImage(), 0, user1.getImage().length));
        } else {
            holder.username.setText("Unknown User");
            holder.imgViewUser.setImageResource(R.drawable.profile_icon);
        }
        holder.post_image.setImageBitmap(BitmapFactory.decodeByteArray(post.getImageUri(), 0, post.getImageUri().length));
        holder.caption_text.setText(post.getCaption());

        holder.imgComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(context, ActivityComment.class);
                    User user = UserPreferences.getUserData(context);
                    intent.putExtra("postId", myListPost.get(adapterPosition).getId());
                    intent.putExtra("userId", user.getUserID());
                    context.startActivity(intent);
                }
            }
        });

        holder.updateLikeStatus(post);
    }

    @Override
    public int getItemCount() {
        return myListPost.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView username;
        private ImageView post_image;
        private TextView caption_text;
        private ImageView imgViewUser;
        private TextView likes_text;
        private ImageButton imgComment;
        private ImageView like_image;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username_text);
            post_image = itemView.findViewById(R.id.post_image);
            caption_text = itemView.findViewById(R.id.caption_text);
            likes_text = itemView.findViewById(R.id.likes_text);
            imgViewUser = itemView.findViewById(R.id.user_photo_image);
            imgComment = itemView.findViewById(R.id.comment_image);
            like_image = itemView.findViewById(R.id.like_image);

            like_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Post post = myListPost.get(position);
                        handleLikeButtonClick(post);
                    }
                }
            });
        }

        private void updateLikeStatus(Post post) {
            int likeCount = likeDAO.getLikeCount(post.getId());
            likes_text.setText(String.valueOf(likeCount) + " likes");

            int userId = UserPreferences.getUserData(context).getUserID();
            boolean isLikedByUser = likeDAO.isPostLikedByUser(userId, post.getId());

            if (isLikedByUser) {
                like_image.setImageResource(R.drawable.heart_clicked); // Đổi icon nếu đã like
            } else {
                like_image.setImageResource(R.drawable.heart); // Icon mặc định
            }
        }

        private void handleLikeButtonClick(Post post) {
            int userId = UserPreferences.getUserData(context).getUserID();
            boolean isLikedByUser = likeDAO.isPostLikedByUser(userId, post.getId());

            if (isLikedByUser) {
                likeDAO.deleteLike(userId, post.getId());
            } else {
                likeDAO.insertLike(new Like(userId, post.getId()));
            }

            updateLikeStatus(post);
        }
    }
}
