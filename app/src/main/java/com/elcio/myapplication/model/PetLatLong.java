package com.elcio.myapplication.model;

/**
 * Essa classe serve mais como base para salvar e recuperar
 * o id de um pet e o id da localização do respectivo pet no firebase.
 */
public class PetLatLong {
    private String idPet;
    private String idLatLong;

    public PetLatLong() {
    }

    public PetLatLong(String idPet, String idLatLong) {
        this.idPet = idPet;
        this.idLatLong = idLatLong;
    }

    public String getIdPet() {
        return idPet;
    }

    public void setIdPet(String idPet) {
        this.idPet = idPet;
    }

    public String getIdLatLong() {
        return idLatLong;
    }

    public void setIdLatLong(String idLatLong) {
        this.idLatLong = idLatLong;
    }
}
