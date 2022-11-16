package com.example.laris.Task;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.laris.databinding.ActivityTaskBinding;

public class TaskActivity extends AppCompatActivity {

    private ActivityTaskBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}