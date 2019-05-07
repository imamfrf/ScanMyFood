package com.scanmyfood.imamf.scanmyfood.Model;

public class Food {
    private String id, name, calorie, fat, carb, protein, img, funFact;

    public Food() {
    }

    public Food(final Builder builder){
        this.id = builder.id;
        this.name = builder.name;
        this.calorie = builder.calorie;
        this.fat = builder.fat;
        this.carb = builder.carb;
        this.protein = builder.protein;
        this.img = builder.img;
        this.funFact = builder.funFact;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCalorie() {
        return calorie;
    }

    public String getFat() {
        return fat;
    }

    public String getCarb() {
        return carb;
    }

    public String getProtein() {
        return protein;
    }

    public String getImg() {
        return img;
    }

    public String getFunFact() {
        return funFact;
    }

    public static class Builder{
        private String id, name, calorie, fat, carb, protein, img, funFact;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;

        }

        public Builder setCalorie(String calorie) {
            this.calorie = calorie;
            return this;

        }

        public Builder setFat(String fat) {
            this.fat = fat;
            return this;

        }

        public Builder setCarb(String carb) {
            this.carb = carb;
            return this;

        }

        public Builder setProtein(String protein) {
            this.protein = protein;
            return this;

        }

        public Builder setImg(String img) {
            this.img = img;
            return this;

        }

        public Builder setFunFact(String funFact) {
            this.funFact = funFact;
            return this;

        }

        public Food create(){
            return new Food(this);
        }
    }
}
