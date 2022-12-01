package com.example.laris.Register;

import static java.lang.System.err;
import static java.lang.System.in;
import static java.lang.System.out;

import androidx.annotation.NonNull;
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
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.laris.Login.LoginActivity;
import com.example.laris.Login.WelcomeActivity;
import com.example.laris.MainActivity;
import com.example.laris.Model.Endereco;
import com.example.laris.R;
import com.example.laris.databinding.ActivitySignupConfirmBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SignupConfirmActivity extends AppCompatActivity {

    private ActivitySignupConfirmBinding binding;
    private String img, nome, sobrenome, data, genero,  email, celular, cpf, senha, userId;
    private Bitmap bitmap;
    private Uri imgUri;
    public String imgUrlSup;
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
        sobrenome = sharedPref.getString("sobrenome", "");
        data = sharedPref.getString("data", "");
        genero = sharedPref.getString("genero", "");
        email = sharedPref.getString("email", "");
        celular = sharedPref.getString("celular", "");
        cpf = sharedPref.getString("cpf", "");
        senha = sharedPref.getString("senha", "");

        binding.etNome.setText(nome);
        binding.etEmail.setText(email);
        binding.etCelular.setText(celular);
        binding.etCpf.setText(cpf);
        cpfSup = true;
        celularSup = true;
//        img = sharedPref.getString("pathimg", "");
//        imgUri = Uri.parse(img);

//        if (!img.isEmpty()){
//            try {
//
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
//                carrega(bitmap);
//            } catch (IOException e){
//                e.printStackTrace();
//                Toast.makeText(this, "Erro ao selecionar imagem", Toast.LENGTH_SHORT).show();
//            }
//        }
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
        }
        if (imgUri==null){
            binding.tvErro.setText("Selecione uma foto");
            binding.tvErro.setVisibility(View.VISIBLE);
            confirm = false;
        }

          if (confirm){
                cadastrarUser();
            }

    }


    private void cadastrarUser(){

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    salvarImagem();
                    Toast.makeText(SignupConfirmActivity.this, "Cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                    startActivity(intent);
                    finish();
//                    finish();
                }else{
                    String erro;
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthUserCollisionException e){
                        erro = "O email ja esta sendo usado";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erro = "E-mail inválido";
                    }catch (Exception e){
                        erro = "Erro ao cadastrar usuário";
                    }

                    binding.tvErro.setText(erro);
                    binding.tvErro.setVisibility(View.VISIBLE);
                }
            }
        });





    }

    private void salvarImagem(){

        //salvar imagem
        String filename = UUID.randomUUID().toString();
        final StorageReference reference = FirebaseStorage.getInstance().getReference("/images/"+filename);
        reference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imgUrlSup = uri.toString();
                        salvarDadosUsuario(imgUrlSup);
                    }
                });
            }
        });
    }

    private void salvarDadosUsuario(String urlImg){



        //criar usuario
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Map<String, Object> usuario = new HashMap<>();


        usuario.put("nome", nome);
        usuario.put("apelido", nome);
        usuario.put("sobrenome", sobrenome);
        usuario.put("data", data);
        usuario.put("genero", genero);
        usuario.put("celular", celular);
        usuario.put("cpf", cpf);
        usuario.put("avaliacao" , "5");
        usuario.put("email" , email);
        usuario.put("urlImagem" , urlImg);


        DocumentReference documentReference = db.collection("Usuarios").document(userId);
        documentReference.set(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("db", "Sucesso ao salvar dados");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("db", "Erro ao salvar dados" + e.toString());
                    }
                });
    }
}