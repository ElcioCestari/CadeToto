package com.elcio.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.elcio.myapplication.model.Pet;
import com.elcio.myapplication.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PetListActivity extends AppCompatActivity {

    private ArrayList<Pet> petList;
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
                Intent intent = new Intent(getApplication(), CadastrarPetActivity.class);
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
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("animal");

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

                /*converte stringPetList ArrayList<Pet> em String[]*/
                stringPetList = new String[petList.size()];
                for (int i = 0; i < petList.size(); i++) {
                    stringPetList[i] = petList.get(i).toString();
                    Log.d("PET ATUAIS", "\n\n pet atuais: " + petList.get(i));

                }

                /*Cria uma simples listview para ser exibida*/
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, stringPetList);
                ListView listView = findViewById(R.id.listView_pet_list);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
