package com.example.final_khang.adapter;

import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_khang.R;
import com.example.final_khang.entity.Post;
import com.example.final_khang.entity.User;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_USER = 0;
    private static final int VIEW_TYPE_POST = 1;

    private List<Object> results = new ArrayList<>();

    public void setResults(List<?> results) {
        this.results.clear();
        if (results != null) {
            this.results.addAll(results);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_USER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
            return new UserViewHolder(view);
        } else if (viewType == VIEW_TYPE_POST) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_post_item, parent, false);
            return new PostViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UserViewHolder) {
            User user = (User) results.get(position);
            ((UserViewHolder) holder).bind(user);
        } else if (holder instanceof PostViewHolder) {
            Post post = (Post) results.get(position);
            ((PostViewHolder) holder).bind(post);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (results.get(position) instanceof User) {
            return VIEW_TYPE_USER;
        } else if (results.get(position) instanceof Post) {
            return VIEW_TYPE_POST;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userName, userEmail;

        UserViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_Name);
            userEmail = itemView.findViewById(R.id.user_email);
        }

        void bind(User user) {
            userName.setText(user.getUserName());
            userEmail.setText(user.getEmail());
        }
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView postID, postContent;

        PostViewHolder(View itemView) {
            super(itemView);
            postID = itemView.findViewById(R.id.post_id);
            postContent = itemView.findViewById(R.id.post_content);
        }

        void bind(Post post) {
            postID.setText(post.getId());
            postContent.setText(post.getCaption());
        }
    }

}