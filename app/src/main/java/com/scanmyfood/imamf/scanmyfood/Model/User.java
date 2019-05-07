package com.scanmyfood.imamf.scanmyfood.Model;

public class User {

    private String userId;
    private String nama;
    private String email;
    private String tanggalLahir;
    private String jenisKelamin;
    private double tinggiBadan;
    private double beratBadan;
    private double kebutuhanKalori;
    private int usia;
    private String photoUrl;

    public User() {
    }

    public User(String userId, String nama, String email, String tanggalLahir, String jenisKelamin, double tinggiBadan, double beratBadan, double kebutuhanKalori, int usia, String photoUrl) {
        this.userId = userId;
        this.nama = nama;
        this.email = email;
        this.tanggalLahir = tanggalLahir;
        this.jenisKelamin = jenisKelamin;
        this.tinggiBadan = tinggiBadan;
        this.beratBadan = beratBadan;
        this.kebutuhanKalori = kebutuhanKalori;
        this.usia = usia;
        this.photoUrl = photoUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(String tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public double getTinggiBadan() {
        return tinggiBadan;
    }

    public void setTinggiBadan(double tinggiBadan) {
        this.tinggiBadan = tinggiBadan;
    }

    public double getBeratBadan() {
        return beratBadan;
    }

    public void setBeratBadan(double beratBadan) {
        this.beratBadan = beratBadan;
    }

    public double getKebutuhanKalori() {
        return kebutuhanKalori;
    }

    public void setKebutuhanKalori(double kebutuhanKalori) {
        this.kebutuhanKalori = kebutuhanKalori;
    }

    public int getUsia() {
        return usia;
    }

    public void setUsia(int usia) {
        this.usia = usia;
    }
}
