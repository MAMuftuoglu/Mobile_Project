package edu.marda.cs310news;

import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    private final MutableLiveData<List<NewsModel>> news = new MutableLiveData<>();
    private final MutableLiveData<Handler> newsTitleHandler = new MutableLiveData<>();
    private final MutableLiveData<ExecutorService> srv = new MutableLiveData<>();

    public void setCategories(List<Category> value) {
        categories.setValue(value);
    }
    public void setNews(List<NewsModel> value)
    {
        news.setValue(value);
    }
    public void setNewsTitleHandler(Handler value) {newsTitleHandler.setValue(value);}
    public void setSrv(ExecutorService value) {
        srv.setValue(value);
    }

    public LiveData<List<Category>> getCategories() {
        return this.categories;
    }
    public LiveData<List<NewsModel>> getNews() {
        return this.news;
    }
    public LiveData<Handler> getNewsTitleHandler() {
        return this.newsTitleHandler;
    }
    public LiveData<ExecutorService> getSrv() {
        return this.srv;
    }
}
