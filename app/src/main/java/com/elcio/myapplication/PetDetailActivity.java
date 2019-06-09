package com.elcio.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PetDetailActivity extends AppCompatActivity {

    private String petId;
    private String imagem;
    private String descricao;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_detail);

        Intent intent = getIntent();// recuperando a intent contendo os dados para exibir nessa activity
        name = intent.getStringExtra("nome");//recuperarndo o nome do pet
        descricao = intent.getStringExtra("descricao");//recuprando a descrição
        imagem = intent.getStringExtra("imagem");//recuperando a id da imagem
        petId = intent.getStringExtra("petId");//recuperando id do pet

        /*Fzendo referencia das views*/
        TextView txtName = findViewById(R.id.text_pet_title_detail);
        TextView txtDescricao = findViewById(R.id.text_pet_detail_description);
        ImageView imgFoto = findViewById(R.id.img_pet_detail);
        Button btnMap = findViewById(R.id.btn_pet_detail_map);

        findPetViewsOnFirebase(imagem);//recuperando a imagem do firebase

        txtName.setText(name);//configurando a view com o nome o pet
        txtDescricao.setText(descricao);//configruando a view com a descrição do pet

        /*Trata o evendo do click do botao para inicar o mapa*/
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("petId", petId);
                startActivity(intent);
            }
        });
    }

    /**
     * TODO: apenas para teste, avaliar posteriormente se ira permancer ou não
     */
    private void findPetViewsOnFirebase(String id) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();//fazendo referncia ao firebase e recuperando uma instancia
        StorageReference imagensRef = storageRef.child("imagens");//fazendo referencia a pasta imagens em storage
        StorageReference fotoRef = imagensRef.child(id);//fazendo referencia a foto com o respectivo id

        ImageView imgTeste = findViewById(R.id.img_pet_detail);//recuperando referenica da view que ira receber a imagem
        Context context = getApplicationContext();//recuperando o contexto

        /*Com o Glide é que esta sendo recuperado de fato a foto do firebase e setando ela na imageview*/
        Glide.with(context).using(new FirebaseImageLoader())
                .load(fotoRef)
                .into(imgTeste);
    }
}
