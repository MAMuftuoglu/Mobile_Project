package edu.marda.cs310news;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class SwipeFragment extends Fragment {

    List<NewsModel> newsTitlePage = new ArrayList<>();
    Activity activity;
    MainViewModel viewModel;
    RecyclerView recyclerView;
    Handler titleHandler;
    ExecutorService srv;
    NewsRepository repository = new NewsRepository();

    public static SwipeFragment newInstance(Activity activity, String id) {
        Bundle bundle = new Bundle();
        bundle.putString("category_id", id);
        SwipeFragment fragment = new SwipeFragment();
        fragment.setArguments(bundle);
        fragment.activity = activity;
        fragment.viewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(MainViewModel.class);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_swipe, container, false);
        if(this.activity != null) {



            viewModel.getNewsTitleHandler().observe(requireActivity(), handler -> titleHandler = handler);
            viewModel.getSrv().observe(requireActivity(), threadSrv -> srv = threadSrv);
            recyclerView = view.findViewById(R.id.recView_news);
        }


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        if(activity != null) {
            repository.getNewsByCategoryID(srv, titleHandler, Integer.parseInt(getArguments().getString("category_id")));
            viewModel.getNews().observe(requireActivity(), newsModels -> setUpNews(newsModels, recyclerView));
        }
    }

    public void setUpNews(List<NewsModel> newsModels, RecyclerView recyclerView) {
        newsTitlePage = newsModels;

        NewsTitleRecViewAdapter adapter = new NewsTitleRecViewAdapter(requireContext(), newsTitlePage);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

    }
}
