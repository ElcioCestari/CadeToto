package com.elcio.myapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.elcio.myapplication.model.Pet;

import java.util.ArrayList;

public class PetListActivity extends AppCompatActivity {

    private ArrayList<Pet> petList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_list);


        petList = new ArrayList<>();
        petList.add(new Pet("1", "toto", "1")); //apenas para teste
        petList.add(new Pet("2", "bato", "2")); //apenas para teste

        /*converte stringPetList ArrayList<Pet> em String[]*/
        String[] stringPetList = new String[petList.size()];
        for(int i = 0; i < petList.size(); i++){
            stringPetList[i] = petList.get(i).toString();
        }

        /*Cria uma simples listview para ser exibida*/
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringPetList);

        ListView listView = findViewById(R.id.listView_pet_list);
        listView.setAdapter(adapter);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(), "ta funcionando", Toast.LENGTH_LONG).show();
            }
        });
    }

}
