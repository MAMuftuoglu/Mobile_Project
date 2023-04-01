package edu.marda.cs310news;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class ShowCommentsActivity extends AppCompatActivity {

    Handler commentHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            String jsonStr = message.obj.toString();

            try {
                JSONObject jsonObject = new JSONObject(jsonStr);

                JSONArray jsonArray = jsonObject.getJSONArray("items");

                for (int i = 0; i < jsonArray.length(); i++) {
                    String name = jsonArray.getJSONObject(i).getString("name");
                    String text = jsonArray.getJSONObject(i).getString("text");
                    int id = jsonArray.getJSONObject(i).getInt("id");
                    int news_id = jsonArray.getJSONObject(i).getInt("news_id");
                    CommentModel comm = new CommentModel(name, text, id, news_id);
                    commentModelList.add(comm);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            CommentRecViewAdapter adapter = new CommentRecViewAdapter(ShowCommentsActivity.this, commentModelList);
            commentRec.setAdapter(adapter);
            commentRec.setLayoutManager(new LinearLayoutManager(ShowCommentsActivity.this));

            prgBar.setVisibility(View.INVISIBLE);
            return true;
        }
    });

    RecyclerView commentRec;
    List<CommentModel> commentModelList = new ArrayList<>();
    ProgressBar prgBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_comments);

        commentRec = findViewById(R.id.commentRecyclerView);
        prgBar = findViewById(R.id.progressBarShow);
        int id;
        if (savedInstanceState != null) {
            id = savedInstanceState.getInt("news_id");
        }
        else {
            id = getIntent().getIntExtra("news_id", -1);
        }
        setUpComments(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_comment, menu);
        MenuItem item = menu.findItem(R.id.post_comments);
        item.setIcon(R.drawable.ic_baseline_edit_24);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.post_comments) {
            Intent i = new Intent(ShowCommentsActivity.this, PostCommentActivity.class);
            i.putExtra("news_id", getIntent().getIntExtra("news_id", -1));
            this.startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        int id = getIntent().getIntExtra("id", -1);
        outState.putInt("news_id", id);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setUpComments(intent.getIntExtra("news_id", -1));
    }

    private void setUpComments(int id) {

        NewsRepository repository = new NewsRepository();
        ExecutorService srv = ((NewsApplication)getApplication()).srv;
        prgBar.setVisibility(View.VISIBLE);
        repository.getCommentsByNewsID(srv, commentHandler, id);
    }
}
