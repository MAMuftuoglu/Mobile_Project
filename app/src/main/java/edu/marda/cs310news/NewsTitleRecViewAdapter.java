package edu.marda.cs310news;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class NewsTitleRecViewAdapter extends RecyclerView.Adapter<NewsTitleRecViewAdapter.MyViewHolder> {

    Context ctx;
    List<NewsModel> newsModelList;

    public NewsTitleRecViewAdapter(Context ctx, List<NewsModel> newsModelList) {
        this.ctx = ctx;
        this.newsModelList = newsModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.news_rec_view_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        holder.setIsRecyclable(false);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.txtTitle.setText(newsModelList.get(holder.getAdapterPosition()).getTitle());
        String date = newsModelList.get(holder.getAdapterPosition()).getDate();

        date = new StringBuilder().append(date.substring(8,10))
                .append("/").append(date.substring(5,7))
                .append("/").append(date.substring(0,4)).toString();
        holder.dateTxt.setText(date);


        NewsApplication application = (NewsApplication) ((AppCompatActivity) ctx).getApplication();
        holder.downloadImage(application.srv, newsModelList.get(holder.getAdapterPosition()).getImg());


        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO add intent and open news
                Intent i = new Intent(ctx, NewsBodyActivity.class);
                i.putExtra("id", newsModelList.get(holder.getAdapterPosition()).getId());
                ctx.startActivity(i);
            }
        });
    }


    @Override
    public int getItemCount() {
        return newsModelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        Handler imgHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {

                Bitmap img = (Bitmap)message.obj;
                newsImg.setImageBitmap(img);
                imageDownloaded = true;
                prgBar.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        ImageView newsImg;
        TextView txtTitle;
        TextView dateTxt;
        ConstraintLayout row;
        ProgressBar prgBar;
        boolean imageDownloaded;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            newsImg = itemView.findViewById(R.id.newsImage);
            txtTitle = itemView.findViewById(R.id.newsTitle);
            dateTxt = itemView.findViewById(R.id.dateRow);
            row = itemView.findViewById(R.id.news_row_constraint);
            prgBar = itemView.findViewById(R.id.progressBarMain);
        }

        public void downloadImage(ExecutorService srv, String path) {
            if(!imageDownloaded) {
                NewsRepository repository = new NewsRepository();
                prgBar.setVisibility(View.VISIBLE);
                repository.downloadImage(srv, imgHandler, path);
            }
        }
    }
}
