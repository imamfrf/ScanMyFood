package com.scanmyfood.imamf.scanmyfood.Model;

public class User {

    public User() {
    }

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


    private User(final Builder builder) {
        this.userId = builder.userId;
        this.nama = builder.nama;
        this.email = builder.email;
        this.tanggalLahir = builder.tanggalLahir;
        this.jenisKelamin = builder.jenisKelamin;
        this.tinggiBadan = builder.tinggiBadan;
        this.beratBadan = builder.beratBadan;
        this.kebutuhanKalori = builder.kebutuhanKalori;
        this.usia = builder.usia;
        this.photoUrl = builder.photoUrl;
    }


    public static class Builder{
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


        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder setNama(String nama) {
            this.nama = nama;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setTanggalLahir(String tanggalLahir) {
            this.tanggalLahir = tanggalLahir;
            return this;
        }

        public Builder setJenisKelamin(String jenisKelamin) {
            this.jenisKelamin = jenisKelamin;
            return this;
        }

        public Builder setTinggiBadan(double tinggiBadan) {
            this.tinggiBadan = tinggiBadan;
            return this;
        }

        public Builder setBeratBadan(double beratBadan) {
            this.beratBadan = beratBadan;
            return this;
        }

        public Builder setKebutuhanKalori(double kebutuhanKalori) {
            this.kebutuhanKalori = kebutuhanKalori;
            return this;
        }

        public Builder setUsia(int usia) {
            this.usia = usia;
            return this;
        }

        public Builder setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
            return this;
        }

        public User create() {
            return new User(this);
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getNama() {
        return nama;
    }

    public String getEmail() {
        return email;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public double getTinggiBadan() {
        return tinggiBadan;
    }

    public double getBeratBadan() {
        return beratBadan;
    }

    public double getKebutuhanKalori() {
        return kebutuhanKalori;
    }

    public int getUsia() {
        return usia;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}
