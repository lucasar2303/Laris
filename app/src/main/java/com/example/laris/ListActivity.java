package com.example.laris;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.laris.databinding.ActivityListBinding;

public class ListActivity extends AppCompatActivity {

    ActivityListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imgBack.setOnClickListener(view -> finish());
    }
}