package edu.marda.cs310news;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;


public class PageFragment extends Fragment {

    List<Fragment> fragmentList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pager, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainViewModel viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);


        ViewPager2 pager2 = view.findViewById(R.id.news_pager);


        viewModel.getCategories().observe(requireActivity(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                setAllCategories(categories, view, pager2);
            }
        });
    }

    public void setAllCategories(List<Category> value, View view, ViewPager2 pager2) {
        for (Category c :
                value) {
            fragmentList.add(SwipeFragment.newInstance(requireActivity(), String.valueOf(c.getId())));
        }

        NewsAdapter adapter = new NewsAdapter(requireActivity(), fragmentList);
        pager2.setAdapter(adapter);

        TabLayout tabLayout = view.findViewById(R.id.category_names);
        new TabLayoutMediator(tabLayout, pager2, (
                (tab, position) -> tab.setText(value.get(position).getName())
        )).attach();
    }
}
