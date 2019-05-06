package com.scanmyfood.imamf.scanmyfood.Model

import com.google.gson.annotations.SerializedName

data class Food(val id: String, val name: String, val calorie: String, val fat: String,
                val carb: String, val protein: String, val img: String, val funFact: String)
