package com.elcio.myapplication.model;

/**
 * <h2>User</h2>
 * <p>Usuário padrão do sistema ele possui nome, email, id e senha</p>
 * <h3>ATENÇÃO!</h3>
 * <p>A implementação de senha é apenas para teste, caso for realizado a implantação
 * desse app em um ambiente real a <code>senha</code> deve se melhorarada</p>
 * @author Elcio
 * @version 0.1
 */
public class User {
    private String idUser;
    private String name;
    private String email;
    private String senha;//campo com implementação pobre, pois o objetivo desse app é para por em pratica outros conhecimentos

    /**
     * Construtor padrão que é utilizado pelo firebase
     */
    public User(){}

    public User(String idUser, String name, String email, String senha) {
        this.idUser = idUser;
        this.name = name;
        this.email = email;
        this.senha = senha;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
