package com.elcio.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText editEmail, editSenha;
    Button btnLogin, btnNovo;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inicializaComponentes();
        myListner();

    }

    /**
     * trata os eventos dessa activity
     */
    private void myListner() {

        /**
         * chama a activity para a criação de um novo usuario
         */
        btnNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CadastroActivity.class);
                startActivity(intent);
            }
        });

        /**
         * listner para fazer o login no sistema
         */
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString().trim();
                String senha = editSenha.getText().toString().trim();
                login(email, senha);
            }
        });
    }

    /**
     * Faz o login no sistema
     *
     * @param email - uma String contendo um email
     * @param senha - uma string contendo a senha
     */
    private void login(String email, String senha) {
        firebaseAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //TODO: ao entrar nesse if o usário estara logado, portando devo implementar o que fazer apos o login
                if (task.isSuccessful()) {
                    myToast("Usuário Logado");
                } else {
                    myToast("Nao foi possível realizar o login!");
                }
            }
        });
    }

    /**
     * Exibe um toast com uma mensagem recebida como parametro
     *
     * @param msg - a mensagem que deseja ser exiba em um toast
     */
    private void myToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * Todos os compenentes dessa activity são referenciados neste metodo.
     */
    private void inicializaComponentes() {
        editEmail = findViewById(R.id.edit_email);
        editSenha = findViewById(R.id.edit_senha);
        btnLogin = findViewById(R.id.btn_login);
        btnNovo = findViewById(R.id.btn_novo);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = Conection.getFirebaseAuth();
    }
}
