package com.example.laris.Profile;

import static java.lang.System.out;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laris.Address.AddressActivity;
import com.example.laris.Login.LoginActivity;
import com.example.laris.Login.WelcomeActivity;
import com.example.laris.MainActivity;
import com.example.laris.Notify.NotificationsActivity;
import com.example.laris.Policy.TermsPolicyActivity;
import com.example.laris.R;
import com.example.laris.Task.TaskActivity;
import com.example.laris.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userId, imgUrlSup;
    private Bitmap bitmap;
    private Uri imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imgUser.setOnClickListener(view -> selectPhoto());
        binding.layoutSair.setOnClickListener(view -> showDialogDelete());
        binding.imgBack.setOnClickListener(view -> finish());
        binding.layoutNotify.setOnClickListener(view -> newActivty(NotificationsActivity.class));
        binding.layoutEditDados.setOnClickListener(view -> newActivty(ProfileEditActivity.class));
        binding.layoutTermos.setOnClickListener(view -> newActivty(TermsPolicyActivity.class));
        binding.layoutEndereco.setOnClickListener(view -> newActivty(AddressActivity.class));
        binding.layoutAtividades.setOnClickListener(view -> newActivty(TaskActivity.class));
    }

    private void showDialogDelete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(ProfileActivity.this).inflate(R.layout.dialog_logout, (LinearLayout)findViewById(R.id.dialogLinearLayout));
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.btnSim).setOnClickListener(view12 -> {

                    FirebaseAuth.getInstance().signOut();
                    newActivty(WelcomeActivity.class);
                    finish();
                    alertDialog.dismiss();


        });

        view.findViewById(R.id.btnNao).setOnClickListener(view1 -> {
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    private void newActivty(Class c ){
        Intent intent = new Intent(getApplicationContext(), c);
        startActivity(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (userId != null){
            DocumentReference documentReference = db.collection("Usuarios").document(userId);
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value != null){
                        binding.tvNome.setText(value.getString("nome")+" "+value.getString("sobrenome"));
                        Float avaliacao = Float.valueOf(value.getString("avaliacao"));
                        DecimalFormat df = new DecimalFormat("0.00");
                        binding.tvAvaliacao.setText(df.format(avaliacao));
                        if (value.getString("urlImagem").equals("") ||value.getString("urlImagem") == null){
                            return;
                        }else{
                            Picasso.get().load(value.getString("urlImagem"))
                                    .error(R.drawable.icon_profile)
                                    .placeholder(R.drawable.icon_contact)
                                    .into(binding.imgUser);
                        }
                    }
                }
            });

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
                atualizaImagem();
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

    private void atualizaImagem(){

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

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        DocumentReference documentReference = db.collection("Usuarios").document(userId);
                        documentReference.update("urlImagem", imgUrlSup).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(ProfileActivity.this, "Imagem de perfil atualizada", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });


    }

}