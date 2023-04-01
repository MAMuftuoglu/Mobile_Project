package edu.marda.cs310news;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;

public class PostCommentActivity extends AppCompatActivity {

    Handler responseHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            String line = message.obj.toString();

            try {
                JSONObject jsonObject = new JSONObject(line);

                if(jsonObject.getInt("serviceMessageCode") == 0) {
                    Toast.makeText(PostCommentActivity.this, "Could not send the comment!", Toast.LENGTH_SHORT).show();
                }
                else {

                    successfulPost();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            prgbar.setVisibility(View.INVISIBLE);
            return true;
        }
    });

    Button postCommentBtn;
    EditText txtName;
    EditText txtText;
    ProgressBar prgbar;
    int news_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comment);

        setTitle("Post Comment");

        postCommentBtn = findViewById(R.id.buttonPost);
        txtName = findViewById(R.id.editTextTextPersonName);
        txtText = findViewById(R.id.editTxtCommentText);
        prgbar = findViewById(R.id.progressBarPost);

        postCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = txtName.getText().toString();
                String text = txtText.getText().toString();

                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(text)) {
                    Toast.makeText(PostCommentActivity.this, "Please fill the empty boxes!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    news_id = getIntent().getIntExtra("news_id", -1);
                    CommentModel commentModel = new CommentModel(name, text, 0, news_id);
                    NewsRepository repository = new NewsRepository();
                    ExecutorService srv = ((NewsApplication)getApplication()).srv;
                    prgbar.setVisibility(View.VISIBLE);
                    repository.postComment(srv, responseHandler, commentModel);
                }
            }
        });
    }

    public void successfulPost() {
        Intent intent = new Intent(this, ShowCommentsActivity.class);
        intent.putExtra("news_id", news_id);
        this.startActivity(intent);
    }
}