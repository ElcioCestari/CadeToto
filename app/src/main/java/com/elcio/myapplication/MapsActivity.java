package com.elcio.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.elcio.myapplication.model.LatitudeLongitude;
import com.elcio.myapplication.model.PetLatLong;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private LatLng latLng;
    private GoogleMap mMap;
    private String petId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();//recuperando o id do pet para procurar as cordenadas do pet que foi repassado pela activity PetDetailActivity
        this.petId = intent.getStringExtra("petId");//atribuindo o valor

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * todo tenho que recuperar os dados do pet
     */
    private void getAllLatLongFromFirebase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();//pegando a instancia do firebase
        DatabaseReference petRef = firebaseDatabase.getReference("animal");//acessando o nó animal
        DatabaseReference thisPetReference = petRef.child(this.petId);
        DatabaseReference coordenadasRef = thisPetReference.child("coordenadas");//acessando o nó coordenadas

        coordenadasRef.addListenerForSingleValueEvent(myListner);

    }

    ValueEventListener myListner = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            DataSnapshot teste = dataSnapshot;
            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                LatitudeLongitude latitudeLongitude = dataSnapshot.getValue(LatitudeLongitude.class);//Recuérando os dados de longitude e atribuindo eles ao objeto
                latLng = new LatLng(latitudeLongitude.getLatitude(), latitudeLongitude.getLongitude());//iniciallizando a variavel com as coordenadas
            }

            if (latLng == null) {//caso não haja os dados da latitude e longitude iniciara ocom os dados default
                latLng = new LatLng(-20.4631965, -54.6101142);//configruando default para lat long de campo grande ms, mais precisamente no obelisco.
                drawPetOnTheMap(latLng,  mMap);
            } else {
                drawPetOnTheMap(latLng, mMap);//caso existam as coordenads desenha o pet comm elas
            }

            /**
             * faz o tratamento dos eventos de click no mapa
             */
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    saveLatLongInFirebase(latLng);//salva as coordenadas que o usuário clicou
                    drawPetOnTheMap(latLng, mMap);//desenha o icone nessa nova coordenada
                }
            });
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getAllLatLongFromFirebase();
    }

    /**
     * Salva as coordenadas de latitude e longitude no firebase relacionando elas ao pet em questão
     * TODO AINDA NAO ESTOU SALVANDO NO FIREBASE APENAS UM ARRAY LOCAL E TEMPORARIO
     *
     * @param latLng - Com coordenadas relacionadas a local.
     */
    private void saveLatLongInFirebase(LatLng latLng) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();//recuperando a instancia do firebase
        DatabaseReference animalReference = firebaseDatabase.getReference("animal");//recuperando uma referencia para o nó animal
        DatabaseReference thisPetReference = animalReference.child(this.petId);

        LatitudeLongitude latitudeLongitude = new LatitudeLongitude(latLng.latitude, latLng.longitude);//instanciando um lobjeto para salvar no firebase
        thisPetReference.child("coordenadas").setValue(latitudeLongitude);//salvando de fato no firebase
    }

    /**
     * Salva no firebase na tabela pet_coordenadas os ids das tabelas 'animais' e da tabela 'coordenadaas'
     *
     * @param latLongId - chave estrangeira para tablea coordenadas
     * @param petId     - chave estrangeirar para tabela animais
     */
    private void savePetLatLongInFirebase(String latLongId, String petId) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();//recuperando a instancia do firebase
        DatabaseReference petCoordenadasReference =
                firebaseDatabase.getReference("pet_coordenadas");//recuperando uma referencia para o nó pet_coordenadas

        String id = UUID.randomUUID().toString();//gera um id randomico para o nó pet_coordenadas
        PetLatLong petLatLong = new PetLatLong(petId, latLongId);// cria um objeto com as chaves estrangeiras de 'animal' e 'coordenadas'
        petCoordenadasReference.child(id).setValue(petLatLong);//atribui esse objeto no banco
    }

    /**
     * Desenha um novo icone do pet no map com as coordenadas repassadas pelo usuário atual
     *
     * @param latLng
     * @param googleMap
     */
    private void drawPetOnTheMap(LatLng latLng, GoogleMap googleMap) {
        mMap = googleMap;


        mMap.addMarker(
                new MarkerOptions()
                        .position(latLng)
                        .title("sua localização")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icons_cachorro_map_48px))
        );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
    }
}
