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
import android.view.View;
import android.widget.Toast;

import com.example.laris.R;
import com.example.laris.Register.SignupContactActivity;
import com.example.laris.Register.SignupPersonalActivity;
import com.example.laris.databinding.ActivitySignupPhotoBinding;

import java.io.IOException;

public class SignupPhotoActivity extends AppCompatActivity {

    private ActivitySignupPhotoBinding binding;
    private Uri mSelectedUri;
    private Bitmap bitmap =  null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Laris);
        binding = ActivitySignupPhotoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imgBack.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SignupPersonalActivity.class);
            startActivity(intent);
            finish();
        });

        binding.btnEnviar.setOnClickListener(view -> enviaDados());

        binding.imgPhoto.setOnClickListener(view -> selectPhoto());
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

                mSelectedUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mSelectedUri);
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
            exif = new ExifInterface(getContentResolver().openInputStream(mSelectedUri));
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
        binding.imgPhoto.setImageBitmap(rtbitmap);

    }

    private void enviaDados(){

        if (bitmap != null){

            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key_register), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("pathimg" , mSelectedUri.toString());
            editor.commit();

            Intent intent = new Intent(getApplicationContext(), SignupContactActivity.class);
            startActivity(intent);
            finish();
        }else  {
            Toast.makeText(this, "Selecione sua foto", Toast.LENGTH_SHORT).show();
        }


    }

}

