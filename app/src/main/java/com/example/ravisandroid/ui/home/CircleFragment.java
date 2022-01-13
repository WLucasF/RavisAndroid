package com.example.ravisandroid.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ravisandroid.R;
import com.example.ravisandroid.databinding.FragmentCircleBinding;

public class CircleFragment extends Fragment {

    private View CircleView;
    private RecyclerView myCircle;

    public CircleFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        CircleView = inflater.inflate(R.layout.fragment_circle, container, false);

        myCircle = (RecyclerView) CircleView.findViewById(R.id.circle_list);
        myCircle.setLayoutManager(new LinearLayoutManager(getContext()));
        return CircleView;
    }

    @Override
    public void onStart(){
        super.onStart();
    }


/*    private HomeViewModel homeViewModel;
    private FragmentCircleBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentCircleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textCircle;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }*/
}