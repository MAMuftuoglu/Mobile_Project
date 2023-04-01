package edu.marda.cs310news;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class MainActivity extends AppCompatActivity {

    Handler categoryHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            String jsonString = message.obj.toString();

            try {
                JSONObject jsonObject = new JSONObject(jsonString);

                if(jsonObject.getInt("serviceMessageCode") == 1) {
                    JSONArray jsonArray = jsonObject.getJSONArray("items");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        String name = jsonArray.getJSONObject(i).getString("name");
                        int id = jsonArray.getJSONObject(i).getInt("id");
                        Category category = new Category(name, id);
                        categoryList.add(category);
                    }

                    Collections.sort(categoryList, new Comparator<Category>() {
                        @Override
                        public int compare(Category category, Category t1) {
                            return category.getId() - t1.getId();
                        }
                    });

                    viewModel.setCategories(categoryList);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
    });

    Handler newsTitleHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            String jsonString = message.obj.toString();
            newsModelList.clear();

            try {
                JSONObject jsonObject = new JSONObject(jsonString);

                if(jsonObject.getInt("serviceMessageCode") == 1) {
                    JSONArray jsonArray = jsonObject.getJSONArray("items");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        String title = jsonArray.getJSONObject(i).getString("title");
                        String text = jsonArray.getJSONObject(i).getString("text");
                        String date = jsonArray.getJSONObject(i).getString("date");
                        String categoryName = jsonArray.getJSONObject(i).getString("categoryName");
                        int id = jsonArray.getJSONObject(i).getInt("id");
                        String imgPath = jsonArray.getJSONObject(i).getString("image");
                        NewsModel newsModel = new NewsModel(id, title, text, date, categoryName, imgPath);
                        newsModelList.add(newsModel);
                    }

                    viewModel.setNews(newsModelList);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
    });

    MainViewModel viewModel;
    List<Category> categoryList = new ArrayList<>();
    List<NewsModel> newsModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_news);
        actionBar.setDisplayHomeAsUpEnabled(true);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        NewsRepository repository = new NewsRepository();
        ExecutorService srv = ((NewsApplication)getApplication()).srv;
        repository.getAllCategories(srv, categoryHandler);

        viewModel.setSrv(srv);
        viewModel.setNewsTitleHandler(newsTitleHandler);


    }
}