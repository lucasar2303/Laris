package com.example.laris;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.laris.databinding.ActivityFilterHomeBinding;

public class FilterHome extends AppCompatActivity {

    private ActivityFilterHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFilterHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}