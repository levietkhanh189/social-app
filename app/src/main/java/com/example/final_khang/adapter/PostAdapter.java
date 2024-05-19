package com.example.final_khang.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

        // Đặt các thông tin của bài viết trước
        holder.post_image.setImageBitmap(BitmapFactory.decodeByteArray(post.getImageUri(), 0, post.getImageUri().length));
        holder.caption_text.setText(post.getCaption());
        holder.updateLikeStatus(post);

        // Lấy thông tin người dùng bằng cách sử dụng callback
        userDAO.getUserByID(post.getUserId(), new UserDAO.UserCallback() {
            @Override
            public void onSuccess(User user) {
                if (user != null) {
                    holder.username.setText(user.getUserName());
                    holder.imgViewUser.setImageBitmap(BitmapFactory.decodeByteArray(user.getImage(), 0, user.getImage().length));
                } else {
                    holder.username.setText("Unknown User");
                    holder.imgViewUser.setImageResource(R.drawable.profile_icon);
                }
            }

            @Override
            public void onError(String error) {
                holder.username.setText("Unknown User");
                holder.imgViewUser.setImageResource(R.drawable.profile_icon);
                Toast.makeText(holder.itemView.getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
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
        private ImageView imgComment;
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

            if (imgComment != null) {
                imgComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Post post = myListPost.get(position);
                            Intent intent = new Intent(context, ActivityComment.class);
                            User user = UserPreferences.getUserData(context);
                            intent.putExtra("postId", post.getId());
                            intent.putExtra("userId", user.getUserID());
                            context.startActivity(intent);
                        }
                    }
                });
            } else {
                Log.e("PostAdapter", "imgComment is null, unable to set OnClickListener");
            }

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
            int userId = UserPreferences.getUserData(context).getUserID();

            likeDAO.getLikeCount(post.getId(), new LikeDAO.LikeCountCallback() {
                @Override
                public void onCount(int count) {
                    likes_text.setText(String.valueOf(count) + " likes");
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(context, "Error: " + error, Toast.LENGTH_SHORT).show();
                }
            });

            likeDAO.isPostLikedByUser(userId, post.getId(), new LikeDAO.LikeStatusCallback() {
                @Override
                public void onStatus(boolean isLiked) {
                    if (isLiked) {
                        like_image.setImageResource(R.drawable.heart_clicked); // Đổi icon nếu đã like
                    } else {
                        like_image.setImageResource(R.drawable.heart); // Icon mặc định
                    }
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(context, "Error: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        }


        private void handleLikeButtonClick(Post post) {
            int userId = UserPreferences.getUserData(context).getUserID();

            likeDAO.isPostLikedByUser(userId, post.getId(), new LikeDAO.LikeStatusCallback() {
                @Override
                public void onStatus(boolean isLikedByUser) {
                    if (isLikedByUser) {
                        likeDAO.deleteLike(userId, post.getId());
                    } else {
                        likeDAO.insertLike(new Like(userId, post.getId()));
                    }
                    updateLikeStatus(post); // Cập nhật trạng thái like sau khi thực hiện thao tác
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(context, "Error: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
