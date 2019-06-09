package com.elcio.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.elcio.myapplication.model.Pet;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PetListActivity extends AppCompatActivity {
    protected static ArrayList<Pet> petList;
    private String[] stringPetList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_list);

        petList = new ArrayList<>();

        /*acessa o firebase e le todos os animais e exibe em uma lista*/
        getPetsFromFirebase();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*
         * Quando o usuário deseja cadastrar um novo pet ele deve clicar nessa view.
         * Ela ira chamar uma activity que faz esse processamento
         */
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //chama activity para cadastrar um novo pet
                Intent intent = new Intent(getApplicationContext(), CadastrarPetActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * esse método é responsavel por acessar o firebase,
     * o nó "animal",
     * percorrer todas as instancias e adicioná-los na variavel petlist.
     */
    private void getPetsFromFirebase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();//peganddo referencia do firebase
        DatabaseReference reference = firebaseDatabase.getReference("animal");//pegando referencia do nó animal

        //utilizado para recuperar os dados em animal
        reference.addValueEventListener(new ValueEventListener() {

            /**
             * O processamento é realizado aqui para ler e exibir um pét
             * @param dataSnapshot - o retorno do firebase da consulta
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Pet pet;// pet temporario

                /*Faz a leitura dos pet e atribui eles na lista*/
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    pet = snapshot.getValue(Pet.class);
                    petList.add(pet);
                    Log.d("LISTA DE ANIMAIS", "PETS: " + petList.get(0));
                }


                /*converte ArrayList<Pet> em String[] para serem utilizados em Myadapter*/
                stringPetList = new String[petList.size()];
                String[] tituloTeste = new String[petList.size()];
                String[] descricaoTeste = new String[petList.size()];
                String[] imageTeste = new String[petList.size()];
                for (int i = 0; i < petList.size(); i++) {
                    stringPetList[i] = petList.get(i).toString();
                    tituloTeste[i] = petList.get(i).toString();
                    descricaoTeste[i] = petList.get(i).toString();
                    imageTeste[i] = petList.get(i).getIdFoto();


                    Log.d("PET ATUAIS", "\n\n pet atuais: " + petList.get(i));
                }

                /*Cria uma listview para ser exibida*/
                MyAdapter myAdapter = new MyAdapter(getApplicationContext(), imageTeste, tituloTeste, descricaoTeste);
                ListView listView = findViewById(R.id.listView_pet_list);
                listView.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /**
     * recupera imagem do firebase
     */
    private void findPetViewsOnFirebase() {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagensRef = storageRef.child("imagens");
        StorageReference fotoRef = imagensRef.child("3b89a0e1-a961-4e1e-ac88-b2a15992d9f4.jpeg");

        ImageView imgTeste = findViewById(R.id.img_foto_pet_list);
        Context context = getApplicationContext();
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(fotoRef)
                .into(imgTeste);
    }

    public ArrayList<Pet> getPetList() {
        return petList;
    }
}


class MyAdapter extends BaseAdapter {

    String[] imageId;
    String[] titulo;
    String[] descricao;
    Context context;

    public MyAdapter(Context applicationContext, String[] imageTeste, String[] tituloTeste, String[] descricaoTeste) {
        this.imageId = imageTeste;
        this.titulo = tituloTeste;
        this.descricao = descricaoTeste;
        this.context = applicationContext;
    }

    @Override
    public int getCount() {
        return titulo.length;
    }//recupera a quantidade de animais na lista

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.customlistview, parent, false);

        //recuperando a referencia das views
        ImageView img = row.findViewById(R.id.img_foto_pet_list);
        TextView textName = row.findViewById(R.id.text_nome_pet_list);
        TextView textDetalhe = row.findViewById(R.id.text_detalhes_pet_list);

        /*referenia a imagem que sera recuperada do firebase*/
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagensRef = storageRef.child("imagens");
        StorageReference fotoRef;

        //caso não haja nenhuma foto de um pet cadastrado ele executara o catch
        try {
            fotoRef = imagensRef.child(imageId[position]);
        } catch (Exception e) {
            fotoRef = imagensRef.child("3b89a0e1-a961-4e1e-ac88-b2a15992d9f4.jpeg");
        }

        //recupera a imagem do firebase e seta ela em um image view
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(fotoRef)
                .asBitmap().into(img);

        textName.setText(titulo[position]);//seta cada textName com base em cada nome recuperado do firebase
        textDetalhe.setText(descricao[position]);// seta cada textDetalhe com base em cada descricao recuperada

        //ao clicar em um item do listView o evento é tratado aqui...
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Um Toast com uma mensagem para melhorar a usabilidade
                Toast.makeText(context, "Caso tenha visto esse bichinho mostre no mapa!", Toast.LENGTH_LONG).show();//estou usando apenas para debug
                Intent intent = new Intent(context, PetDetailActivity.class);

                Bundle bundle = new Bundle();
                intent.putExtra("nome", titulo[position]);
                intent.putExtra("descricao", descricao[position]);
                intent.putExtra("imagem", imageId[position]);
                intent.putExtra("petId", PetListActivity.petList.get(position).getIdPet());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);//sem essa linha de código naõ estava seno chamado o outra activity

                context.startActivity(intent);
                return;
            }
        });

        return row;
    }
}