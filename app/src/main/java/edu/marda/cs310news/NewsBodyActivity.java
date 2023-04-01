package edu.marda.cs310news;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class NewsBodyActivity extends AppCompatActivity {

    Handler newsHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            try {
                JSONObject jsonObject = new JSONObject(message.obj.toString());

                if(jsonObject.getInt("serviceMessageCode") == 1) {
                    String title = jsonObject.getJSONArray("items").getJSONObject(0).getString("title");
                    String text = jsonObject.getJSONArray("items").getJSONObject(0).getString("text");
                    String date = jsonObject.getJSONArray("items").getJSONObject(0).getString("date");
                    String category = jsonObject.getJSONArray("items").getJSONObject(0).getString("categoryName");
                    String imgPath = jsonObject.getJSONArray("items").getJSONObject(0).getString("image");

                    date = new StringBuilder().append(date.substring(8,10))
                            .append("/").append(date.substring(5,7))
                            .append("/").append(date.substring(0,4)).toString();

                    titleView.setText(title);
                    dateView.setText(date);
                    txtView.setText(text);

                    setTitle(category);

                    NewsRepository repository = new NewsRepository();
                    repository.downloadImage(((NewsApplication)getApplication()).srv, imageHandler, imgPath);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return true;
        }
    });

    Handler imageHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            Bitmap img = (Bitmap) message.obj;
            imageView.setImageBitmap(img);

            prgBar.setVisibility(View.INVISIBLE);
            return true;
        }
    });

    ImageView imageView;
    TextView txtView;
    TextView dateView;
    TextView titleView;
    ProgressBar prgBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_body);

        int id = getIntent().getIntExtra("id", -1);

        imageView = findViewById(R.id.imageView);
        txtView = findViewById(R.id.textView);
        dateView = findViewById(R.id.textViewDateNewsBody);
        titleView = findViewById(R.id.textViewTitleNewsBody);
        prgBar = findViewById(R.id.progressBarBody);

        NewsRepository repository = new NewsRepository();
        prgBar.setVisibility(View.VISIBLE);
        repository.getNewsByID(((NewsApplication)getApplication()).srv, newsHandler, id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.show_comment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.show_comments) {
            Intent i = new Intent(NewsBodyActivity.this, ShowCommentsActivity.class);
            i.putExtra("news_id", getIntent().getIntExtra("id", -1));
            this.startActivity(i);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}