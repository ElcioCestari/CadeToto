package com.elcio.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elcio.myapplication.model.Pet;
import com.elcio.myapplication.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
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

    private static final int IMAGEM_FOR_RESULT = 1;
    private static final int PERMISION_REQUEST = 2;


    Button btnSalvar;//button usado para salvar as alterações de um pet
    Button btnFoto;//button que irá ler a foto do pet
    TextView tViewPetNome; //TextView usado para ler o nome do pet
    ImageView imgViewFoto;//Foto que será salva
    ImageView imgViewFotoAtual;//Imagem que aparece no layout


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_pet);


        //solicitando permissão para acessar a galeria
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){

            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISION_REQUEST);
            }
        }

        /*Fazendo referencia das views*/
        tViewPetNome = findViewById(R.id.edit_pet_nome);
        btnSalvar = findViewById(R.id.btn_pet_salvar);
        imgViewFoto = findViewById(R.id.imgView_foto_pet);
        btnFoto = findViewById(R.id.btn_foto);
        imgViewFotoAtual = findViewById(R.id.imgView_foto_pet);


        /**
         * Carrega a imagem do celular do usuário para ser salva no firebase
         */
        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,IMAGEM_FOR_RESULT);
            }

        });

        /**
         * Salva um pet no firebase
         */
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Pessoa");

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        salvarPet();
                        User dono = null;
                        List<User> list = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            dono = snapshot.getValue(User.class);
                            list.add(dono);
                        }

                        DatabaseReference refPet = database.getReference("animal");/*referencia ao nó 'animal'*/

                        String name = tViewPetNome.getText().toString();//recuperando a entrada do usuário

                        /*Criando  o objeto pet que será salvo no banco*/
                        Pet pet = new Pet(UUID.randomUUID().toString(), name, dono.getIdUser().toString());

                        refPet.child(pet.getIdPet()).setValue(pet);//salvando um pet no firebase

                        //String value = dataSnapshot.getKey().toString();
                        Log.d("FIREBASE", "\n\n\n Value is: " + list.get(0).toString() + "\n\n");

                        salvarImage(pet.getIdPet());//salva a imagem do pet

                        /*Após salvar os salvar os dados chamma uma nova activity*/
                        Intent intent = new Intent(getApplicationContext(), PetListActivity.class);
                        startActivity(intent);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,filePath,null,null,null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePath[0]);
            String picturePath = cursor.getString(columnIndex);;
            cursor.close();
            Bitmap imaBitmap = BitmapFactory.decodeFile(picturePath);
            imgViewFotoAtual.setImageBitmap(imaBitmap);
        }
    }

    /**
     * TODO implementar o salvamento de um pet aqui para melhorar a legibilidade
     */
    private void salvarPet() {
    }


    /**
     * Trecho de códio para salvar uma image
     * TODO implementar melhorias, por exemplo: id do pet, ler a imagem do aparelho do usuario...
     */
    private void salvarImage(String idPet) {

        imgViewFoto.setDrawingCacheEnabled(true);
        imgViewFoto.buildDrawingCache();

        Bitmap bitmap = imgViewFoto.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);

        byte[] dadosImage = baos.toByteArray();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();//pega a referencia do FirebaseStorage
        StorageReference imagensReference = storageReference.child("imagens");//cria uma pasta chamada "imagens"
        StorageReference imageReference = imagensReference.child(idPet + ".jpeg");//salva uma imagem cujo caminho é o id Pet

        //TODO: caso queira tratar eventos deve ser feito implementando os metodos do uploadTask
        UploadTask uploadTask = imageReference.putBytes(dadosImage);
    }
}
