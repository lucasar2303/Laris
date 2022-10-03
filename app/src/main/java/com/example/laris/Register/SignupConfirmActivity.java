package com.example.laris.Register;

import static java.lang.System.out;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.laris.R;
import com.example.laris.databinding.ActivitySignupConfirmBinding;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class SignupConfirmActivity extends AppCompatActivity {

    private ActivitySignupConfirmBinding binding;
    private String img, nome, email, celular, cpf;
    private Bitmap bitmap;
    private Uri imgUri;
    Boolean celularSup = true, cpfSup = true;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupConfirmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });


        binding.btnEnviar.setOnClickListener(view -> validaDados());

        binding.imgUser.setOnClickListener(view -> selectPhoto());

        binding.etCelular.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                celularSup = false;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.etCpf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                cpfSup = false;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key_register), Context.MODE_PRIVATE);
        nome = sharedPref.getString("nome", "");
        email = sharedPref.getString("email", "");
        celular = sharedPref.getString("celular", "");
        cpf = sharedPref.getString("cpf", "");

        binding.etNome.setText(nome);
        binding.etEmail.setText(email);
        binding.etCelular.setText(celular);
        binding.etCpf.setText(cpf);
        cpfSup = true;
        celularSup = true;
        img = sharedPref.getString("pathimg", "");
        imgUri = Uri.parse(img);

        if (!img.isEmpty()){
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                carrega(bitmap);
            } catch (IOException e){
                e.printStackTrace();
                Toast.makeText(this, "Erro ao selecionar imagem", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void selectPhoto(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0){
            if (resultCode == RESULT_OK){

                imgUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                    carrega(bitmap);
                } catch (IOException e){
                    e.printStackTrace();
                    Toast.makeText(this, "Erro ao selecionar imagem", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void carrega(Bitmap bitmap){

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(getContentResolver().openInputStream(imgUri));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90: matrix.setRotate(90);break;
            case ExifInterface.ORIENTATION_ROTATE_180: matrix.setRotate(180);break;
            case ExifInterface.ORIENTATION_ROTATE_270: matrix.setRotate(270);break;
            default:
        }
        Bitmap rtbitmap = Bitmap.createBitmap(bitmap, 0,0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        rtbitmap.compress(Bitmap.CompressFormat.JPEG, 10, out);
        binding.imgUser.setImageBitmap(rtbitmap);

    }

    private void validaDados(){
        Boolean confirm = true;
        email = binding.etEmail.getText().toString();
        celular = binding.etCelular.getText().toString();
        nome = binding.etNome.getText().toString();
        cpf = binding.etCpf.getText().toString();


        if (celularSup == false){
            if (!binding.etCelular.isDone()){
                binding.tvErro.setText("Número de celular inválido");
                binding.tvErro.setVisibility(View.VISIBLE);
                confirm = false;
            }
        }

        if (cpfSup == false){
            if (!binding.etCpf.isDone()){
                binding.tvErro.setText("Cpf inválido");
                binding.tvErro.setVisibility(View.VISIBLE);
                confirm = false;
            }
        }


          if (nome.length()<3){
            binding.tvErro.setText("Nome inválido");
            binding.tvErro.setVisibility(View.VISIBLE);
            confirm = false;
        }else if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //Se estiver certo nao façaa nada
        }else{
            binding.tvErro.setText("Email inválido");
            binding.tvErro.setVisibility(View.VISIBLE);
            confirm = false;
        } if (confirm){
                enviaDados();
            }

    }


    private void enviaDados(){

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key_register), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email", email);
        editor.putString("celular", celular);
        editor.putString("nome", nome);
        editor.putString("cpf", cpf);

        editor.commit();

        Intent intent = new Intent(getApplicationContext(), SignupPasswordActivity.class);
        startActivity(intent);
        finish();
    }
}