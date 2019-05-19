package com.elcio.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.elcio.myapplication.model.Pet;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * <h2>CadastrarPetActivity </h2>
 * <p>Classe responsavel por exibir activity que
 * recebe os dados para criação de um novo Pet e salva ela no Firebase</p>
 *
 * @author elcio
 * @version 0.1
 */
public class CadastrarPetActivity extends AppCompatActivity {

    private DatabaseReference reference;// referencia para o banco do firebase
    private EditText editNome; //recebe o nome do pet
    private Button btnSalvar; //quando o usuario clica em salvar
    private Pet pet; // utilizado para criar um novo pet no firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_pet);

        reference = FirebaseDatabase.getInstance().getReference();// referencia o nó raiz do banco

        referingViews();

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: funcao que ira salvar um pet no firebase
                String nome = editNome.getText().toString().trim();
                pet = new Pet();
                pet.setName(nome);
                reference.child("pet").setValue(pet);
            }
        });
    }

    /**
     * Faz as referencias das views
     */
    private void referingViews() {
        this.editNome = findViewById(R.id.edit_pet_nome);
        this.btnSalvar = findViewById(R.id.btn_pet_salvar);
    }
}
