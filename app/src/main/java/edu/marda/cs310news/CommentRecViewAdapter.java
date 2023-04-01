package edu.marda.cs310news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommentRecViewAdapter extends RecyclerView.Adapter<CommentRecViewAdapter.CommentViewHolder>{

    Context ctx;
    List<CommentModel> list;

    public CommentRecViewAdapter(Context ctx, List<CommentModel> data) {
        this.ctx = ctx;
        this.list = data;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.comment_row, parent, false);


        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {

        holder.commentName.setText(list.get(position).getName());
        holder.commentText.setText(list.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView commentName;
        TextView commentText;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            commentName = itemView.findViewById(R.id.commentName);
            commentText = itemView.findViewById(R.id.commentTxt);
        }
    }
}
