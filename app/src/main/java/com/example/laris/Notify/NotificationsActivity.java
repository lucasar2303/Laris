package com.example.laris.Notify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.laris.databinding.ActivityNotificationsBinding;

public class NotificationsActivity extends AppCompatActivity {

    private ActivityNotificationsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imgBack.setOnClickListener(view -> finish());
    }
}