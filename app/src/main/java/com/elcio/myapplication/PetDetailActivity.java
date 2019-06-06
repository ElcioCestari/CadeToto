package com.elcio.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PetDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_detail);

        Intent intent = getIntent();
        String name = intent.getStringExtra("nome");
        String descricao = intent.getStringExtra("descricao");
        String imagem = intent.getStringExtra("imagem");

        TextView txtName = findViewById(R.id.text_pet_title_detail);
        TextView txtDescricao = findViewById(R.id.text_pet_detail_description);
        ImageView imgFoto = findViewById(R.id.img_pet_detail);

        findPetViewsOnFirebase(imagem);

        txtName.setText(name);
        txtDescricao.setText(descricao);

    }

    /**
     * TODO: apenas para teste, avaliar posteriormente se ira permancer ou não
     */
    private void findPetViewsOnFirebase(String id) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagensRef = storageRef.child("imagens");
        StorageReference fotoRef = imagensRef.child(id);

        ImageView imgTeste = findViewById(R.id.img_pet_detail);
        Context context = getApplicationContext();
        Glide.with(context).using(new FirebaseImageLoader())
                .load(fotoRef)
                .into(imgTeste);
    }
}
