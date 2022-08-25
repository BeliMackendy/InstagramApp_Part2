package com.mypath.instagramapp;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
//import com.parse.ParseFile;

import java.io.File;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private final List<Post> posts;

    public PostAdapter(List<Post> posts) {
        this.posts = posts;
    }

    // Clean all elements of the recycler
    @SuppressLint("NotifyDataSetChanged")
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

// Add a list of items -- change to type used
    @SuppressLint("NotifyDataSetChanged")
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);

        holder.tvUsername.setText(post.getUser().getUsername());
        holder.tvDescription.setText(post.getDescription());

        File photoFile = null;
        try {
            photoFile = post.getImage().getFile();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assert photoFile != null;
        Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

        Glide.with(holder.itemView.getContext())
                .load(takenImage)
                .into(holder.ivPostUser);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername;
        TextView tvDescription;
        ImageView ivPostUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivPostUser = itemView.findViewById(R.id.ivPostUser);
        }
    }
}
