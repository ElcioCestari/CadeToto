package com.elcio.myapplication;

import android.app.ListActivity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.elcio.myapplication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

/**
 * <h1>CadastroActivity</h1>
 *
 * <p>Activity utilizada para fazer a criação de um usuário</p>
 * <p>
 * TODO: não ha uma implementaão robusta até o presente mommento para a "senha"
 *
 * @author Elcio
 * @version 0.1
 */
public class CadastroActivity extends AppCompatActivity {

    private EditText editEmail, editSenha, editNome;
    private Button btnSalvar;
    private FirebaseAuth firebaseAuth; //usado para logar o usuário
    private DatabaseReference reference; //faz referencia ao banco firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        initComponents();//inicaliza os componentes das views

        /**
         * Tratamento de evento quando o usuário clica em salvar
         * TODO: ate o presente mommento não foram implementadas funcionalidades para tratativa do tipo: usuário naõ digita a senha.=, entre outras
         */
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, senha, nome;
                email = editEmail.getText().toString();
                senha = editSenha.getText().toString();
                nome = editNome.getText().toString();
                createUser(email, senha, nome);
            }
        });
    }

    /**
     * Cria um usuário no firebase com os seguintes parametros:
     *
     * @param email - uma String contendo um padrão de email valido
     * @param senha - uma String contendo uma senha
     * @param nome  - uma String contendo o nome do usuário
     */
    private void createUser(final String email, final String senha, final String nome) {
        firebaseAuth.createUserWithEmailAndPassword(email, senha).
                addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            /*Chama o metodo para criar uma pessoa no firebase*/
                            createUser(
                                    new User(UUID.randomUUID().toString(),
                                            nome,
                                            email,
                                            senha));

                            alert("Usuário cadastrado com sucesso!");
                            Intent intent = new Intent(getApplication(), PetListActivity.class);
                            startActivity(intent);
                            //todo: implementar algo que chame uma activity de lista, pois esta voltando para tela de llogin.
                            finish();
                        } else {
                            alert("Erro ao salvar o usuário");
                        }
                    }
                }).addOnFailureListener(CadastroActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        alert(e.toString());
                    }
                });
    }

    /**
     * Cria uma Pessoa no firebase com os seguintes parametros:
     *
     * @param pessoa - um objeto do tipo User
     */
    private void createUser(User pessoa) {
        reference = FirebaseDatabase.getInstance().getReference(); //faz referencia ao nó raiz
        reference.child("Pessoa").child(pessoa.getIdUser()).setValue(pessoa);//salava um Usuário no firebase
    }

    /**
     * Exibe uma Toast na tela com uma mensagem que é recebida como parametro
     *
     * @param msg - uma String que deve ser exibida para o usuário
     */
    private void alert(String msg) {
        Toast.makeText(CadastroActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * Inicializa os componentes visuaís da activity,
     * atribuindo-os aos seus resbectivos View
     */
    private void initComponents() {
        this.editEmail = findViewById(R.id.edit_novo_email);
        this.editNome = findViewById(R.id.edit_novo_nome);
        this.editSenha = findViewById(R.id.edit_novo_senha);
        this.btnSalvar = findViewById(R.id.btn_novo_salvar);
    }

    /**
     * Nesse método é inicializado o atributo firebaseAuth,
     * sem sua inicialização não há a autenticação com o firebase.
     */
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = Conection.getFirebaseAuth();
    }

}
