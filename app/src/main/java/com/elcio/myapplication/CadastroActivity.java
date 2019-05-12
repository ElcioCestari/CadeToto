package com.elcio.myapplication;

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

public class CadastroActivity extends AppCompatActivity {

    private EditText editEmail, editSenha, editNome;
    private Button btnSalvar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        initComponents();

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

    private void createUser(String email, String senha, String nome) {
        firebaseAuth.createUserWithEmailAndPassword(email, senha).
                addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            alert("Usuário cadastrado com sucesso!");
                            finish();
                        }else {
                            alert("Erro ao salvar o usuário");
                        }
                    }
                });
    }

    private void alert(String msg){
        Toast.makeText(CadastroActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    private void initComponents() {
        this.editEmail = findViewById(R.id.edit_novo_email);
        this.editNome = findViewById(R.id.edit_novo_nome);
        this.editSenha = findViewById(R.id.edit_novo_senha);
        this.btnSalvar = findViewById(R.id.btn_novo_salvar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = Conection.getFirebaseAuth();
    }
}
