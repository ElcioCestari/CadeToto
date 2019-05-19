package com.elcio.myapplication.model;

/**
 * @author Elcio
 * @version 0.1
 * <h2>Pet</h2>
 * <p>faz a implementação de um animal.</p>
 * <p>cada <bold>Pet</bold> esta relacionado à um <bold>User</bold> através
 * de seu id</p>
 */
public class Pet {
    private String idPet; // String que sera utilizada como identificador do pet
    private String name; // String que representa o nome do pet
    private String idDono; // String que serve como chave strangeira para idUser (dono do pet)
    //todo: implementar a foto que irá aparecer no app


    /**
     * Construtor padrão que é utilizado pelo firebase
     */
    public Pet() {
    }

    /**
     * Constrrutor da classe
     * @param idPet - identificar unico que será salvo no banco
     * @param name - nome do pet
     * @param idDono - chave strangeira para idUser
     */
    public Pet(String idPet, String name, String idDono) {
        this.idPet = idPet;
        this.name = name;
        this.idDono = idDono;
    }

    /**
     *
     * @return - String contendo o nome do animal
     */
    @Override
    public String toString() {
        return this.getName();
    }

    public String getIdPet() {
        return idPet;
    }

    public void setIdPet(String idPet) {
        this.idPet = idPet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdDono() {
        return idDono;
    }

    public void setIdDono(String idDono) {
        this.idDono = idDono;
    }
}
