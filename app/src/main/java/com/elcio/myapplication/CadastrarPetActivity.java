package com.elcio.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.elcio.myapplication.model.Pet;
import com.elcio.myapplication.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <h2>CadastrarPetActivity </h2>
 * <p>Classe responsavel por exibir activity que
 * recebe os dados para criação de um novo Pet e salva ela no Firebase</p>
 *
 * @author elcio
 * @version 0.1
 */
public class CadastrarPetActivity extends AppCompatActivity {

    Button btnSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_pet);

        btnSalvar = findViewById(R.id.btn_pet_salvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Write a message to the database
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Pessoa");
/*
        String email = "elciotaira@email.com";
        String id = UUID.randomUUID().toString();
        String name = "Elcio";
        String senha = "123456";

        User usuario = new User();
        usuario.setEmail(email);
        usuario.setIdUser(id);
        usuario.setName(name);
        usuario.setSenha(senha);

        myRef.child(id).setValue(usuario);
*/

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User dono = null;
                        List<User> list = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            dono = snapshot.getValue(User.class);
                            list.add(dono);
                        }

                        DatabaseReference refPet = database.getReference("animal");
                        String name = "toto";
                        Pet pet = new Pet(UUID.randomUUID().toString(),name,dono.getIdUser().toString());
                        refPet.child(pet.getIdPet()).setValue(pet);



                        //String value = dataSnapshot.getKey().toString();
                        Log.d("FIREBASE", "\n\n\n Value is: " + list.get(0).toString() + "\n\n");
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("MY tag: ", "Failed to read value.", error.toException());
                    }
                });
            }
        });
    }
}
