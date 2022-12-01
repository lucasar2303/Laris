package com.example.laris.Policy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.laris.Notify.NotificationsActivity;
import com.example.laris.Profile.ProfileEditActivity;
import com.example.laris.R;
import com.example.laris.databinding.ActivityTermsPolicyBinding;

public class TermsPolicyActivity extends AppCompatActivity {

    private ActivityTermsPolicyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Laris);
        binding = ActivityTermsPolicyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imgBack.setOnClickListener(view -> finish());
        binding.layoutTermos.setOnClickListener(view -> newActivty(TermsActivity.class));
        binding.layoutPrivacidade.setOnClickListener(view -> newActivty(PrivacyActivity.class));
    }

    private void newActivty(Class c ){
        Intent intent = new Intent(getApplicationContext(), c);
        startActivity(intent);
    }
}