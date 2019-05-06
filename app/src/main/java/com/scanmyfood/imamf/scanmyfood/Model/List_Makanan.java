package com.scanmyfood.imamf.scanmyfood.Model;

public class List_Makanan {

    private String idMakanan, namaMakanan, namaCatering, kaloriMakanan, harga, nomorHp, deskripsi, photo;
    private Double latitude, longitude;

    public List_Makanan() {
    }

    public List_Makanan(String idMakanan, String namaMakanan, String namaCatering, String kaloriMakanan, String harga, String nomorHp, String deskripsi, Double latitude, Double longitude, String photo) {
        this.idMakanan = idMakanan;
        this.namaMakanan = namaMakanan;
        this.namaCatering = namaCatering;
        this.kaloriMakanan = kaloriMakanan;
        this.harga = harga;
        this.nomorHp = nomorHp;
        this.deskripsi = deskripsi;
        this.latitude = latitude;
        this.longitude = longitude;
        this.photo = photo;
    }

    public String getNamaMakanan() {
        return namaMakanan;
    }

    public void setNamaMakanan(String namaMakanan) {
        this.namaMakanan = namaMakanan;
    }

    public String getNamaCatering() {
        return namaCatering;
    }

    public void setNamaCatering(String namaCatering) {
        this.namaCatering = namaCatering;
    }

    public String getKaloriMakanan() {
        return kaloriMakanan;
    }

    public void setKaloriMakanan(String kaloriMakanan) {
        this.kaloriMakanan = kaloriMakanan;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getNomorHp() {
        return nomorHp;
    }

    public void setNomorHp(String nomorHp) {
        this.nomorHp = nomorHp;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getIdMakanan() {
        return idMakanan;
    }

    public void setIdMakanan(String idMakanan) {
        this.idMakanan = idMakanan;
    }
}
